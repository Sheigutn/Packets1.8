package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketListener;

import java.io.IOException;
public class PacketPlayOutWorldBorder extends Packet {
    public Action action;
    public double x;
    public double z;
    public double oldRadius;
    public double newRadius;
    public long speed;
    public int portalTeleportBoundary = 29999984;
    public int warningTime = 15;
    public int warningBlocks = 5;
    public PacketPlayOutWorldBorder(double x, double z, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks)
    {
        this.x = x;
        this.z = z;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
        if(portalTeleportBoundary != -1) this.portalTeleportBoundary = portalTeleportBoundary;
        if(warningTime != -1) this.warningTime = warningTime;
        if(warningBlocks != -1) this.warningBlocks = warningBlocks;
    }

    public PacketPlayOutWorldBorder(double x, double z)
    {
        this.action = Action.SET_CENTER;
        this.x = x;
        this.z = z;
    }


    public PacketPlayOutWorldBorder(double newRadius)
    {
        this.action = Action.SET_SIZE;
        this.newRadius = newRadius;
    }

    public PacketPlayOutWorldBorder(Double oldRadius, Double newRadius)
    {
        this.action = Action.LERP_SIZE;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
    }


    public PacketPlayOutWorldBorder(Action action, int warningTime, int warningBlocks)
    {
        this.action = action;
        if(warningTime != -1) this.warningTime = warningTime;
        if(warningBlocks != -1) this.warningBlocks = warningBlocks;
    }
                                    @Override
    public void a(PacketDataSerializer packetDataSerializer) throws IOException {
        action = Action.values()[packetDataSerializer.a()];
        if(action.ordinal() == Action.SET_SIZE.ordinal())
        {
            newRadius = packetDataSerializer.readDouble();
        }
        if(action.ordinal() == Action.LERP_SIZE.ordinal())
        {
            oldRadius = packetDataSerializer.readDouble();
            newRadius = packetDataSerializer.readDouble();
            speed = VarLong.readVarLong(packetDataSerializer);
        }
        if(action.ordinal() == Action.SET_CENTER.ordinal()) {
            x = packetDataSerializer.readDouble();
            z = packetDataSerializer.readDouble();
        }
        if(action.ordinal() == Action.INITIALIZE.ordinal())
        {
            x = packetDataSerializer.readDouble();
            z = packetDataSerializer.readDouble();
            oldRadius = packetDataSerializer.readDouble();
            newRadius = packetDataSerializer.readDouble();
            speed = VarLong.readVarLong(packetDataSerializer);
            portalTeleportBoundary = packetDataSerializer.a();
            warningTime = packetDataSerializer.a();
            warningBlocks = packetDataSerializer.a();
        }

        if(action.ordinal() == Action.SET_WARNING_TIME.ordinal())
        {
            warningTime = packetDataSerializer.a();
        }
        if(action.ordinal() == Action.SET_WARNING_BLOCKS.ordinal())
        {
            warningBlocks = packetDataSerializer.a();
        }

    }

    @Override
    public void b(PacketDataSerializer packetDataSerializer) throws IOException {
        packetDataSerializer.b(action.ordinal());
        if(action.ordinal() == Action.SET_SIZE.ordinal())
        {
            packetDataSerializer.writeDouble(newRadius);
        }
        if(action.ordinal() == Action.LERP_SIZE.ordinal())
        {
            packetDataSerializer.writeDouble(oldRadius);
            packetDataSerializer.writeDouble(newRadius);
            VarLong.writeVarLong(speed, packetDataSerializer);
        }
        if(action.ordinal() == Action.SET_CENTER.ordinal()) {
            packetDataSerializer.writeDouble(x);
            packetDataSerializer.writeDouble(z);
        }
        if(action.ordinal() == Action.INITIALIZE.ordinal())
        {
            packetDataSerializer.writeDouble(x);
            packetDataSerializer.writeDouble(z);
            packetDataSerializer.writeDouble(oldRadius);
            packetDataSerializer.writeDouble(newRadius);
            VarLong.writeVarLong(speed, packetDataSerializer);
            packetDataSerializer.b(portalTeleportBoundary);
            packetDataSerializer.b(warningTime);
            packetDataSerializer.b(warningBlocks);
        }

        if(action.ordinal() == Action.SET_WARNING_TIME.ordinal())
        {
            packetDataSerializer.b(warningTime);
        }
        if(action.ordinal() == Action.SET_WARNING_BLOCKS.ordinal())
        {
            packetDataSerializer.b(warningBlocks);
        }
    }

    @Override
    public void handle(PacketListener packetListener) {

    }

    enum Action{
        SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS
    }
}
