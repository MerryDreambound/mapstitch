package me.pajic.mapstitch.minimap;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.NotNull;

public enum MinimapBackground implements Nameable {
	CLEAR, TEXTURE, NONE;

	@Override @NotNull
	public Component getName() {
		return Component.translatable("config.mapstitch.minimap.background." + name());
	}
}
