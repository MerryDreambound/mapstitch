package me.pajic.mapstitch.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.mapstitch.extension.MapDecorationRenderStateExtension;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(MapRenderer.class)
public class MapRendererMixin {

	@Inject(
			method = "extractDecorationRenderState",
			at = @At("TAIL")
	)
	private void extractDecorationType(MapDecoration decoration, CallbackInfoReturnable<MapRenderState.MapDecorationRenderState> cir) {
		((MapDecorationRenderStateExtension) cir.getReturnValue()).mapstitch$setDecorationType(decoration.type());
	}
}
