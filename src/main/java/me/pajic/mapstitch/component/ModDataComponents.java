package me.pajic.mapstitch.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class ModDataComponents {
	public static final DataComponentType<Integer> ATLAS_SCALE = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.intRange(0, 4)).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> ATLAS_FULLNESS = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.intRange(0, 4)).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> ATLAS_ACTIVE_MAP_ID = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.intRange(-1, Integer.MAX_VALUE)).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Vec2> MAP_ORIGIN = DataComponentType.<Vec2>builder()
			.persistent(Vec2.CODEC).networkSynchronized(new StreamCodec<>() {
				@Override
				public void encode(@NotNull RegistryFriendlyByteBuf output, @NotNull Vec2 value) {
					output.writeFloat(value.x);
					output.writeFloat(value.y);
				}
				@Override @NotNull
				public Vec2 decode(@NotNull RegistryFriendlyByteBuf input) {
					return new Vec2(input.readFloat(), input.readFloat());
				}
			}).cacheEncoding().build();

	public static void init() {}
}
