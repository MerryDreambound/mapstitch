package me.pajic.mapstitch.networking;

import me.pajic.mapstitch.MapStitch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record S2CCompassGameRulePayload(boolean required) implements CustomPacketPayload {
	public static final Type<S2CCompassGameRulePayload> TYPE = new Type<>(MapStitch.id("compass_gamerule"));
	public static final StreamCodec<RegistryFriendlyByteBuf, S2CCompassGameRulePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, S2CCompassGameRulePayload::required,
			S2CCompassGameRulePayload::new
	);

	@Override @NotNull
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
