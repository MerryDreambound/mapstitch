package me.pajic.mapstitch.recipe;

import com.mojang.serialization.MapCodec;
import me.pajic.mapstitch.component.ModDataComponents;
import me.pajic.mapstitch.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

public class AtlasRecipe extends CustomRecipe {
	private static final AtlasRecipe INSTANCE = new AtlasRecipe();
	public static final MapCodec<AtlasRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, AtlasRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private ItemStack map = ItemStack.EMPTY;
	private int scale = -1;

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		if (input.size() == 2) {
			boolean hasBook = false;
			boolean hasMap = false;
			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getItem(i);
				if (!itemStack.isEmpty()) {
					if (itemStack.is(Items.BOOK)) {
						if (hasBook) return false;
						hasBook = true;
					} else if (itemStack.is(Items.FILLED_MAP)) {
						if (hasMap) return false;
						hasMap = true;
						map = itemStack.copy();
						MapItemSavedData data = MapItem.getSavedData(itemStack, level);
						if (data != null) scale = data.scale;
					}
				}
			}
			return hasBook && hasMap;
		}
		return false;
	}

	@Override @NotNull
	public ItemStack assemble(@NotNull CraftingInput input) {
		ItemStack itemStack = ItemStack.EMPTY;
		if (scale != -1) {
			itemStack = new ItemStack(ModItems.ATLAS);
			itemStack.set(ModDataComponents.ATLAS_SCALE, scale);
			itemStack.set(ModDataComponents.ATLAS_FULLNESS, 1);
			BundleContents.Mutable contents = new BundleContents.Mutable(itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
			contents.tryInsert(map);
			itemStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
		}
		return itemStack;
	}

	@Override @NotNull
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return ModRecipes.ATLAS;
	}
}
