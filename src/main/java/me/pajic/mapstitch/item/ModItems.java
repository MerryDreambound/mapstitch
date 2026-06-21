package me.pajic.mapstitch.item;

import me.pajic.mapstitch.MapStitch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
	public static final ResourceKey<Item> ATLAS_KEY = ResourceKey.create(Registries.ITEM, MapStitch.id("atlas"));
	public static final Item ATLAS = new AtlasItem();

	public static void init() {}
}
