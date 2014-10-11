package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

import java.io.IOException;

public class PacketPlayOutCamera extends Packet {

	public int entityID;

	public PacketPlayOutCamera(int entityID) {
		this.entityID = entityID;
	}

	@Override
	public void a(PacketDataSerializer packetDataSerializer) throws IOException {
		entityID = packetDataSerializer.a();
	}

	@Override
	public void b(PacketDataSerializer packetDataSerializer) throws IOException {

		packetDataSerializer.b(entityID);
	}

	@Override
	public void handle(PacketListener packetListener) {

	}
}
