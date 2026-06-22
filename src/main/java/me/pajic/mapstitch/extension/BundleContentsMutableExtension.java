package me.pajic.mapstitch.extension;

import net.minecraft.world.item.ItemStack;

public interface BundleContentsMutableExtension {
	void mapstitch$removeOneAtIndex(int index);
	ItemStack mapstitch$removeOneOrdered();
}
