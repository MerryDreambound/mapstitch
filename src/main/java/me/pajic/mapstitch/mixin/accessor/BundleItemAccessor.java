package me.pajic.mapstitch.mixin.accessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {

	@Accessor("FULL_BAR_COLOR")
	static int mapstitch$getFullBarColor() {
		throw new AssertionError();
	}

	@Accessor("BAR_COLOR")
	static int mapstitch$getBarColor() {
		throw new AssertionError();
	}

	@Invoker("removeOneItemFromBundle")
	static Optional<ItemStack> mapstitch$callRemoveOneItemFromBundle(final ItemStack self, final Player player, final BundleContents initialContents) {
		throw new AssertionError();
	}

	@Invoker("playDropContentsSound")
	static void mapstitch$callPlayDropContentsSound(final Level level, final Entity entity) {}

	@Invoker("playInsertFailSound")
	static void mapstitch$callPlayInsertFailSound(final Entity entity) {}

	@Invoker("playInsertSound")
	static void mapstitch$callPlayInsertSound(final Entity entity) {}

	@Invoker("playRemoveOneSound")
	static void mapstitch$callPlayRemoveOneSound(final Entity entity) {}
}
