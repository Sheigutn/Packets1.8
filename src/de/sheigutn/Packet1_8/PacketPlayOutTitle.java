package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;
import net.minecraft.server.v1_7_R4.PacketPlayOutListener;

import java.io.IOException;

public class PacketPlayOutTitle extends Packet {
    public Action action;

    public String text;

    public int fadeIn;
    public int stay;
    public int fadeOut;

    private PacketPlayOutTitle(int fadeIn, int stay, int fadeOut)
    {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public PacketPlayOutTitle(Action action, String title)
    {
        this(-1,-1,-1);
        this.action = action;
        this.text = title;
    }

    public PacketPlayOutTitle(Action action,  String title, int fadeIn, int stay, int fadeOut)
    {
        this(fadeIn, stay, fadeOut);
        this.action = action;
        this.text = title;
    }

    @Override
    public void a(PacketDataSerializer packetDataSerializer) throws IOException {
        action = Action.values()[packetDataSerializer.a()];
        if(action == Action.TITLE || action == Action.SUBTITLE)
        {
            text = packetDataSerializer.c(256);
        }
        else if(action == Action.TIMES)
        {
            fadeIn = packetDataSerializer.readInt();
            stay = packetDataSerializer.readInt();
            fadeOut = packetDataSerializer.readInt();
        }
    }

    @Override
    public void b(PacketDataSerializer packetDataSerializer) throws IOException {
        packetDataSerializer.b(action.ordinal());
        if(action == Action.TITLE || action == Action.SUBTITLE)
        {
            packetDataSerializer.a(text);
        }
        else if(action == Action.TIMES)
        {
            packetDataSerializer.writeInt(fadeIn);
            packetDataSerializer.writeInt(stay);
            packetDataSerializer.writeInt(fadeOut);
        }
    }

    @Override
    public void handle(PacketListener packetListener) {

    }

    enum Action{
        TITLE, SUBTITLE, TIMES, CLEAR, RESET;
    }
}
