package me.pajic.mapstitch.extension;

import net.minecraft.core.Holder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public interface MapDecorationRenderStateExtension {
	void mapstitch$setDecorationType(Holder<MapDecorationType> type);
	Holder<MapDecorationType> mapstitch$getDecorationType();
}
