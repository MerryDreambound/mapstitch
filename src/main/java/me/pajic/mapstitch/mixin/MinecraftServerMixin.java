package me.pajic.mapstitch.mixin;

import me.pajic.mapstitch.gamerule.ModGameRules;
import me.pajic.mapstitch.networking.NetworkingUtil;
import me.pajic.mapstitch.networking.S2CCompassGameRulePayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.gamerules.GameRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

	@Shadow public abstract PlayerList getPlayerList();

	@Inject(
			method = "onGameRuleChanged",
			at = @At("TAIL")
	)
	private <T> void sendModGameRuleUpdatesToPlayers(GameRule<T> rule, T value, CallbackInfo ci) {
		if (rule == ModGameRules.REQUIRE_COMPASS_FOR_POS) getPlayerList().getPlayers().forEach(player ->
				NetworkingUtil.s2c(player, new S2CCompassGameRulePayload((boolean) value))
		);
	}
}
