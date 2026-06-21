package me.pajic.mapstitch.minimap;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import me.pajic.mapstitch.component.ModDataComponents;
import me.pajic.mapstitch.config.ModConfigHolder;
import me.pajic.mapstitch.item.ModItems;
import me.pajic.mapstitch.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MinimapOverlay {
	private static final Minecraft MC = Minecraft.getInstance();
	private static final MapRenderState STATE = new MapRenderState();
	private static final Identifier BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("container/cartography_table/map");

	public static void render(GuiGraphicsExtractor guiGraphics) {
		if (MC.player != null && MC.level != null && !MC.options.hideGui && !MC.gui.getDebugOverlay().showDebugScreen() && ModUtil.hasCompass(MC)) {
			MapId mapId = getMapId();
			if (mapId != null) {
				MapItemSavedData mapData = MapItem.getSavedData(mapId, MC.level);
				if (mapData != null) {
					int width = MC.getWindow().getGuiScaledWidth();
					int height = MC.getWindow().getGuiScaledHeight();
					int offsetX = ModConfigHolder.options().minimapXOffset;
					int offsetY = ModConfigHolder.options().minimapYOffset;
					int offset = switch (ModConfigHolder.options().minimapBackground) {
						case TEXTURE -> 6;
						case CLEAR -> 4;
						case NONE -> 2;
					};

					IntIntImmutablePair position;
					switch (ModConfigHolder.options().minimapPosition) {
						case TOP_RIGHT -> position = new IntIntImmutablePair(
								width - offset - 64 - offsetX,
								offset + offsetY + (MC.player.getActiveEffects().isEmpty() ? 0 : 52)
						);
						case BOTTOM_LEFT -> position = new IntIntImmutablePair(
								offset + offsetX,
								height - offset - 64 - offsetY
						);
						case BOTTOM_RIGHT -> position = new IntIntImmutablePair(
								width - offset - 64 - offsetX,
								height - offset - 64 - offsetY
						);
						default -> position = new IntIntImmutablePair(
								offset + offsetX,
								offset + offsetY
						);
					}
					int x = position.leftInt();
					int y = position.rightInt();

					guiGraphics.pose().pushMatrix();
					guiGraphics.pose().translate(x, y);
					switch (ModConfigHolder.options().minimapBackground) {
						case TEXTURE -> guiGraphics.blitSprite(
								RenderPipelines.GUI_TEXTURED,
								BACKGROUND_TEXTURE,
								-4, -4, 72, 72
						);
						case CLEAR -> guiGraphics.fill(-2, -2, 66, 66, ARGB.color(
								ARGB.as8BitChannel(ModConfigHolder.options().minimapBackgroundOpacity / 100F),
								0, 0, 0
						));
					}
					guiGraphics.pose().scale(0.5F, 0.5F);
					MC.getMapRenderer().extractRenderState(mapId, mapData, STATE);
					STATE.decorations.forEach(decor -> decor.renderOnFrame = true);
					guiGraphics.map(STATE);
					guiGraphics.pose().popMatrix();
				}
			}
		}
	}

	@SuppressWarnings("DataFlowIssue")
	private static MapId getMapId() {
		return switch (ModConfigHolder.options().minimapDisplayCondition) {
			case HANDS -> {
				ItemStack mainhand = MC.player.getMainHandItem();
				ItemStack offhand = MC.player.getOffhandItem();
				if (mainhand.is(ModItems.ATLAS)) {
					int id = mainhand.getOrDefault(ModDataComponents.ATLAS_ACTIVE_MAP_ID, -1);
					if (id != -1) yield new MapId(id);
				}
				if (offhand.is(ModItems.ATLAS)) {
					int id = offhand.getOrDefault(ModDataComponents.ATLAS_ACTIVE_MAP_ID, -1);
					if (id != -1) yield new MapId(id);
				}
				yield null;
			}
			case HOTBAR -> {
				for (int i = 0; i < 9; i++) {
					ItemStack stack = MC.player.getInventory().getItem(i);
					if (stack.is(ModItems.ATLAS)) {
						int id = stack.getOrDefault(ModDataComponents.ATLAS_ACTIVE_MAP_ID, -1);
						if (id != -1) yield new MapId(id);
					}
				}
				yield null;
			}
			case INVENTORY -> {
				for (ItemStack stack : MC.player.getInventory().getNonEquipmentItems()) {
					if (stack.is(ModItems.ATLAS)) {
						int id = stack.getOrDefault(ModDataComponents.ATLAS_ACTIVE_MAP_ID, -1);
						if (id != -1) yield new MapId(id);
					}
				}
				yield null;
			}
		};
	}
}
