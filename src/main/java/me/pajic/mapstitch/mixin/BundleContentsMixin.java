package me.pajic.mapstitch.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.mapstitch.extension.BundleContentsExtension;
import me.pajic.mapstitch.extension.BundleContentsMutableExtension;
import me.pajic.mapstitch.item.AtlasItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.class)
public class BundleContentsMixin implements BundleContentsExtension {

	@Unique private boolean mapstitch$isAtlas = false;

	@Override
	public void mapstitch$setIsAtlas() {
		mapstitch$isAtlas = true;
	}

	@Override
	public boolean mapstitch$isAtlas() {
		return mapstitch$isAtlas;
	}

	@Mixin(BundleContents.Mutable.class)
	private static abstract class MutableMixin implements BundleContentsMutableExtension {

		@Shadow public abstract int tryInsert(ItemStack itemsToAdd);
		@Shadow @Final private List<ItemStack> items;

		@Unique private boolean mapstitch$isAtlas = false;

		@Override
		public void mapstitch$removeOneAtIndex(int index) {
			if (!items.isEmpty()) {
				ItemStack stack = items.get(index).copy();
				stack.shrink(1);
				if (stack.isEmpty()) items.remove(index);
				else items.set(index, stack);
			}
		}

		@Inject(
				method = "<init>",
				at = @At("TAIL")
		)
		private void passAtlasFlag(BundleContents contents, CallbackInfo ci) {
			mapstitch$isAtlas = ((BundleContentsExtension) (Object) contents).mapstitch$isAtlas();
		}

		@ModifyReturnValue(
				method = "toImmutable",
				at = @At("RETURN")
		)
		private BundleContents setAtlasFlag(BundleContents original) {
			if (mapstitch$isAtlas) ((BundleContentsExtension) (Object) original).mapstitch$setIsAtlas();
			return original;
		}

		@ModifyExpressionValue(
				method = "getMaxAmountToAdd",
				at = @At(
						value = "FIELD",
						target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
						opcode = Opcodes.GETSTATIC
				)
		)
		private Fraction increaseCapacity(Fraction original) {
			return mapstitch$isAtlas ? AtlasItem.MAX_SIZE : original;
		}

		@ModifyExpressionValue(
				method = "tryInsert",
				at = @At(
						value = "INVOKE",
						target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;"
				)
		)
		private ItemStack limitStackSize(
				ItemStack original,
				@Share("remainder") LocalRef<ItemStack> remainderRef
		) {
			int maxStackSize = original.getMaxStackSize();
			if (mapstitch$isAtlas && original.count() > maxStackSize) {
				ItemStack remainder = original.split(maxStackSize);
				remainderRef.set(remainder);
			} else remainderRef.set(ItemStack.EMPTY);
			return original;
		}

		@Inject(
				method = "tryInsert",
				at = @At("TAIL")
		)
		private void tryInsertRemainder(
				CallbackInfoReturnable<Integer> cir,
				@Share("remainder") LocalRef<ItemStack> remainderRef
		) {
			ItemStack remainder = remainderRef.get();
			if (remainder != null && !remainder.isEmpty()) tryInsert(remainder);
		}

		@WrapOperation(
				method = "findStackIndex",
				at = @At(
						value = "INVOKE",
						target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
				)
		)
		private boolean checkStackSizeLimit(ItemStack a, ItemStack b, Operation<Boolean> original) {
			boolean result = original.call(a, b);
			if (mapstitch$isAtlas) return result && a.count() + b.count() <= a.getMaxStackSize();
			return result;
		}
	}
}
