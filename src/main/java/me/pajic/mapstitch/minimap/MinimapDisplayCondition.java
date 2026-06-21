package me.pajic.mapstitch.minimap;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.NotNull;

public enum MinimapDisplayCondition implements Nameable {
	HANDS, HOTBAR, INVENTORY;

	@Override @NotNull
	public Component getName() {
		return Component.translatable("config.mapstitch.minimap.display_condition." + name());
	}
}
