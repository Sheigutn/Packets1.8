package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

import java.io.IOException;

public class PacketPlayOutTabListHeaderFooter extends Packet {

	public String header;
	public String footer;

	public PacketPlayOutTabListHeaderFooter(String header, String footer) {
		this.header = header;
		this.footer = footer;
	}

	@Override
	public void a(PacketDataSerializer packetDataSerializer) throws IOException {
		header = packetDataSerializer.c(256);
		footer = packetDataSerializer.c(256);
	}

	@Override
	public void b(PacketDataSerializer packetDataSerializer) throws IOException {
		packetDataSerializer.a(header);
		packetDataSerializer.a(footer);
	}

	@Override
	public void handle(PacketListener packetListener) {

	}
}
