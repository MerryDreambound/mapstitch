package me.pajic.mapstitch.minimap;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.NotNull;

public enum MinimapPosition implements Nameable {
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

	@Override @NotNull
	public Component getName() {
		return Component.translatable("config.mapstitch.minimap.position." + name());
	}
}
