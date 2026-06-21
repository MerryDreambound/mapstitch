package me.pajic.mapstitch.worldmap;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import me.pajic.mapstitch.MapStitch;
import me.pajic.mapstitch.component.ModDataComponents;
import me.pajic.mapstitch.config.ModConfigHolder;
import me.pajic.mapstitch.extension.MapDecorationRenderStateExtension;
import me.pajic.mapstitch.item.ModItems;
import me.pajic.mapstitch.keybind.ModKeybinds;
import me.pajic.mapstitch.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMapScreen extends Screen {
	public static List<Identifier> dimensionIds = List.of();
	public static final Map<Integer, MapRenderState> RENDER_STATES = new HashMap<>();
	private static final Map<Vec2, MapRenderState> RENDER_LIST = new HashMap<>();

	private static Identifier dimensionId = Identifier.withDefaultNamespace("overworld");
	private static int scale = 0;
	private static int zoom = 0;
	private static int posX = 0;
	private static int posY = 0;
	private static int posZ = 0;
	private static boolean help = false;
	private static boolean grid = false;

	private final Minecraft MC = Minecraft.getInstance();
	private double mouseDragX;
	private double mouseDragY;

	public WorldMapScreen() {
		super(Component.translatable("mapstitch.gui.worldmap.title"));
	}

	@Override
	public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		RENDER_LIST.clear();
		if (!ModConfigHolder.options().worldMapHelp) help = false;
		if (MC.level == null || MC.player == null) return;
		int screenX = MC.getWindow().getGuiScaledWidth();
		int screenY = MC.getWindow().getGuiScaledHeight();
		float z = (float) Math.pow(2, zoom);
		int s = Math.powExact(2, scale);
		if (ModUtil.hasCompass(MC)) {
			posX = MC.player.blockPosition().getX();
			posY = MC.player.blockPosition().getY();
			posZ = MC.player.blockPosition().getZ();
		}
		// prepare maps
		MC.player.getInventory().forEach(stack -> {
			if (stack.is(ModItems.ATLAS)) {
				BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
				for (ItemStackTemplate map : contents.items()) {
					if (map.is(Items.FILLED_MAP)) prepareMap(map, z, screenX, screenY);
				}
			} else if (stack.is(Items.FILLED_MAP)) {
				prepareMap(ItemStackTemplate.fromNonEmptyStack(stack), z, screenX, screenY);
			}
		});
		// render prepared maps
		Matrix3x2fStack pose = graphics.pose();
		RENDER_LIST.forEach((pos, state) -> {
			pose.pushMatrix();
			pose.translate(screenX / 2F, screenY / 2F);
			pose.scale(z);
			pose.translate(
					pos.x - screenX / 2F + (float) mouseDragX,
					pos.y - screenY / 2F + (float) mouseDragY
			);
			graphics.map(state);
			pose.popMatrix();
		});
		// render grid
		if (grid) for (int i = -screenY; i < screenY; i += screenY / (64 * MC.getWindow().getGuiScale())) {
			graphics.horizontalLine(0, screenX, Math.round((screenY / 2F) + ((float) (i * 128) / (scale + 1)) - ((float) 64 / (scale + 1)) - (((float) posZ / (scale + 1)) % 128) + (float) mouseDragY * z), 0xffffffff);
			graphics.verticalLine(Math.round((screenX / 2F) + ((float) (i * 128) / (scale + 1)) - ((float) 64 / (scale + 1)) - (((float) posX / (scale + 1)) % 128) + (float) mouseDragX * z), 0, screenY, 0xffffffff);
		}
		// render text lines
		textStack(4, false, graphics, List.of(
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.position", posX, posY, posZ), ModUtil.hasCompass(MC)),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.dimension", getDimensionDisplayName()), true),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.scale", s), true),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.zoom", z), true),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.debug.rendered_maps", RENDER_LIST.size()), MapStitch.xplat().isDebug())
		));
		textStack(screenY - 12, true, graphics, List.of(
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.help"), ModConfigHolder.options().worldMapHelp && !help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.help_control"), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.exit_control", Component.keybind(ModKeybinds.OPEN_WORLD_MAP.getName()).withColor(0xffffff55)), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.scale_control"), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.dimension_control"), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.center_control"), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.zoom_control"), help),
				new ObjectBooleanImmutablePair<>(Component.translatable("mapstitch.gui.worldmap.move_control"), help)
		));
	}

	@SuppressWarnings("DataFlowIssue")
	private void prepareMap(ItemStackTemplate map, float zoomLevel, int screenX, int screenY) {
		MapId mapId = map.get(DataComponents.MAP_ID);
		MapItemSavedData data = MC.level.getMapData(mapId);
		int i = mapId.id();
		if (!data.isExplorationMap() && !data.locked && dimensionId.equals(data.dimension.identifier())) {
			Vec2 mapCenter = map.get(ModDataComponents.MAP_ORIGIN);
			int mapCenterX = (int) mapCenter.x;
			int mapCenterY = (int) mapCenter.y;
			float distX = (float) (Math.abs(mapCenterX - posX + mouseDragX) * zoomLevel);
			float distY = (float) (Math.abs(mapCenterY - posZ + mouseDragY) * zoomLevel);
			if (distX >= 0 && distX < screenX && distY >= 0 && distY < screenY) {
				GridPos mapGridPos = GridPos.offset(GridPos.fromBlockPos(mapCenterX, mapCenterY), GridPos.fromBlockPos(posX, posZ));
				MapRenderState state = RENDER_STATES.getOrDefault(i, new MapRenderState());
				MC.getMapRenderer().extractRenderState(new MapId(i), data, state);
				if (data.scale == scale) {
					state.decorations.forEach(decor -> {
						Holder<MapDecorationType> type = ((MapDecorationRenderStateExtension) decor).mapstitch$getDecorationType();
						if (type != MapDecorationTypes.PLAYER_OFF_LIMITS && type != MapDecorationTypes.PLAYER_OFF_MAP) decor.renderOnFrame = true;
					});
					RENDER_LIST.put(mapGridPos.getPosOnScreen(screenX, screenY, posX, posZ), state);
				}
				if (!ModUtil.hasCompass(MC)) state.decorations.forEach(decor -> {
					Holder<MapDecorationType> type = ((MapDecorationRenderStateExtension) decor).mapstitch$getDecorationType();
					if (type == MapDecorationTypes.PLAYER) decor.renderOnFrame = false;
				});
				if (!RENDER_STATES.containsKey(i)) RENDER_STATES.put(i, state);
			}
		}
	}

	private record GridPos(int x, int y) {
		private static GridPos fromBlockPos(int blockX, int blockY) {
			return new GridPos(blockX / (128 * (scale + 1)), blockY / (128 * (scale + 1)));
		}
		private static GridPos offset(GridPos center, GridPos other) {
			return new GridPos(center.x - other.x, center.y - other.y);
		}
		private Vec2 getPosOnScreen(int screenX, int screenY, int playerX, int playerY) {
			return new Vec2(
					(screenX / 2F) + (x * 128) - ((float) 64 / (scale + 1)) - (((float) playerX / (scale + 1)) % 128),
					(screenY / 2F) + (y * 128) - ((float) 64 / (scale + 1)) - (((float) playerY / (scale + 1)) % 128)
			);
		}
	}

	private void textStack(int startY, boolean flipped, GuiGraphicsExtractor graphics, List<Pair<Component, Boolean>> lines) {
		int y = startY;
		for (Pair<Component, Boolean> line : lines) {
			if (line.right()) {
				graphics.fill(2, y - 2, font.width(line.left()) + 5, y + 9, ARGB.color(
						ARGB.as8BitChannel(ModConfigHolder.options().worldMapTextBackgroundOpacity / 100F),
						0, 0, 0
				));
				graphics.text(MC.font, line.left(), 4, y, 0xffffffff);
				y += flipped ? -12 : 12;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private String getDimensionDisplayName() {
		return WordUtils.capitalize(dimensionId.getPath().replace("_", " "));
	}

	@Override
	public boolean mouseDragged(@NotNull MouseButtonEvent event, double dx, double dy) {
		if (event.button() == 0) {
			mouseDragX += dx / Math.pow(2, zoom);
			mouseDragY += dy / Math.pow(2, zoom);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 2) {
			mouseDragX = 0;
			mouseDragY = 0;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		zoom = Mth.clamp(zoom + (int) Math.signum(scrollY), -2, 1);
		return true;
	}

	@Override
	public boolean keyPressed(@NotNull KeyEvent event) {
		if (event.isUp() && scale < 4) {
			scale++;
			return true;
		}
		if (event.isDown() && scale > 0) {
			scale--;
			return true;
		}
		if (event.isRight()) {
			int i = dimensionIds.indexOf(dimensionId);
			dimensionId = i + 1 > dimensionIds.size() - 1 ? dimensionIds.getFirst() : dimensionIds.get(i + 1);
			return true;
		}
		if (event.isLeft()) {
			int i = dimensionIds.indexOf(dimensionId);
			dimensionId = i - 1 < 0 ? dimensionIds.getLast() : dimensionIds.get(i - 1);
			return true;
		}
		if (ModConfigHolder.options().worldMapHelp && event.key() == InputConstants.KEY_H) {
			help = !help;
			return true;
		}
		if (event.key() == InputConstants.KEY_G) {
			grid = !grid;
			return true;
		}
		if (ModKeybinds.OPEN_WORLD_MAP.matches(event)) {
			onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean isInGameUi() {
		return true;
	}

	@Override
	public void resize(int width, int height) {
		RENDER_STATES.clear();
		super.resize(width, height);
	}

	@Override
	public void onClose() {
		RENDER_STATES.clear();
		super.onClose();
	}
}
