package me.pajic.mapstitch.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

//? fabric {
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//?} neoforge {
/*import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
*///?}

public class NetworkingUtil {

	public static void s2c(ServerPlayer player, CustomPacketPayload payload) {
		//? fabric {
		ServerPlayNetworking.send(player, payload);
		//?} neoforge {
		/*PacketDistributor.sendToPlayer(player, payload);
		*///?}
	}

	public static void c2s(CustomPacketPayload payload) {
		//? fabric {
		ClientPlayNetworking.send(payload);
		//?} neoforge {
		/*ClientPacketDistributor.sendToServer(payload);
		 *///?}
	}
}
