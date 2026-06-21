package me.pajic.mapstitch.platform;

import java.nio.file.Path;

public interface Platform {

	boolean isModLoaded(String modId);

	boolean isDevelopmentEnvironment();

	default boolean isDebug() {
		return isDevelopmentEnvironment();
	}

	Path configDir();
}
