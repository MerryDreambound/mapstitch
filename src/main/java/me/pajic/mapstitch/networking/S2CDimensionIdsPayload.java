package me.pajic.mapstitch.networking;

import me.pajic.mapstitch.MapStitch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record S2CDimensionIdsPayload(List<Identifier> dimensionIds) implements CustomPacketPayload {
	public static final Type<S2CDimensionIdsPayload> TYPE = new Type<>(MapStitch.id("dimension_ids"));
	public static final StreamCodec<RegistryFriendlyByteBuf, S2CDimensionIdsPayload> CODEC = CustomPacketPayload.codec(
			S2CDimensionIdsPayload::write,
			S2CDimensionIdsPayload::new
	);

	public S2CDimensionIdsPayload(RegistryFriendlyByteBuf buf) {
		this(buf.readList(FriendlyByteBuf::readIdentifier));
	}

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeCollection(dimensionIds, FriendlyByteBuf::writeIdentifier);
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
