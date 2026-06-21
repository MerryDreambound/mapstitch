package me.pajic.mapstitch.config;

import me.pajic.mapstitch.MapStitch;
import me.pajic.mapstitch.minimap.MinimapBackground;
import me.pajic.mapstitch.minimap.MinimapDisplayCondition;
import me.pajic.mapstitch.minimap.MinimapPosition;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//? neoforge {
/*import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;

@ConfigEntryPointForge(MapStitch.MOD_ID)
*///?}
public class ModSodiumConfig implements ConfigEntryPoint {

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName("MapStitch")
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0x60d394))
                .setNonTintedIcon(Identifier.parse("mapstitch:textures/config_icon.png"))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("config.mapstitch.minimap"))
						.addOption(builder.createEnumOption(MapStitch.id("minimap_display_condition"), MinimapDisplayCondition.class)
								.setName(Component.translatable("config.mapstitch.minimap.display_condition"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.display_condition.desc"))
								.setDefaultValue(MinimapDisplayCondition.HOTBAR)
								.setElementNameProvider(MinimapDisplayCondition::getName)
								.setBinding(e -> ModConfigHolder.options().minimapDisplayCondition = e, () -> ModConfigHolder.options().minimapDisplayCondition)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createEnumOption(MapStitch.id("minimap_position"), MinimapPosition.class)
								.setName(Component.translatable("config.mapstitch.minimap.position"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.position.desc"))
								.setDefaultValue(MinimapPosition.TOP_RIGHT)
								.setElementNameProvider(MinimapPosition::getName)
								.setBinding(e -> ModConfigHolder.options().minimapPosition = e, () -> ModConfigHolder.options().minimapPosition)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createEnumOption(MapStitch.id("minimap_background"), MinimapBackground.class)
								.setName(Component.translatable("config.mapstitch.minimap.background"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.background.desc"))
								.setDefaultValue(MinimapBackground.CLEAR)
								.setElementNameProvider(MinimapBackground::getName)
								.setBinding(e -> ModConfigHolder.options().minimapBackground = e, () -> ModConfigHolder.options().minimapBackground)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createIntegerOption(MapStitch.id("minimap_background_opacity"))
								.setName(Component.translatable("config.mapstitch.minimap.background_opacity"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.background_opacity.desc"))
								.setDefaultValue(35)
								.setRange(0, 100, 1)
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setBinding(i -> ModConfigHolder.options().minimapBackgroundOpacity = i, () -> ModConfigHolder.options().minimapBackgroundOpacity)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createIntegerOption(MapStitch.id("minimap_x_offset"))
								.setName(Component.translatable("config.mapstitch.minimap.x_offset"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.x_offset.desc"))
								.setDefaultValue(0)
								.setValueFormatter(value -> Component.literal(String.valueOf(value)))
								.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE, 1)
								.setBinding(i -> ModConfigHolder.options().minimapXOffset = i, () -> ModConfigHolder.options().minimapXOffset)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createIntegerOption(MapStitch.id("minimap_y_offset"))
								.setName(Component.translatable("config.mapstitch.minimap.y_offset"))
								.setTooltip(Component.translatable("config.mapstitch.minimap.y_offset.desc"))
								.setDefaultValue(0)
								.setValueFormatter(value -> Component.literal(String.valueOf(value)))
								.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE, 1)
								.setBinding(i -> ModConfigHolder.options().minimapYOffset = i, () -> ModConfigHolder.options().minimapYOffset)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
                )
				.addPage(builder.createOptionPage()
						.setName(Component.translatable("config.mapstitch.worldmap"))
						.addOption(builder.createIntegerOption(MapStitch.id("worldmap_text_background_opacity"))
								.setName(Component.translatable("config.mapstitch.worldmap.text_background_opacity"))
								.setTooltip(Component.translatable("config.mapstitch.worldmap.text_background_opacity.desc"))
								.setDefaultValue(35)
								.setRange(0, 100, 1)
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setBinding(i -> ModConfigHolder.options().worldMapTextBackgroundOpacity = i, () -> ModConfigHolder.options().worldMapTextBackgroundOpacity)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
						.addOption(builder.createBooleanOption(MapStitch.id("show_help"))
								.setName(Component.translatable("config.mapstitch.show_help"))
								.setTooltip(Component.translatable("config.mapstitch.show_help.desc"))
								.setDefaultValue(true)
								.setBinding(bl -> ModConfigHolder.options().worldMapHelp = bl, () -> ModConfigHolder.options().worldMapHelp)
								.setStorageHandler(() -> ModConfigHolder.options().writeChanges()))
				);
    }
}
