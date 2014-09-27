package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.util.io.netty.buffer.ByteBuf;

public class Var {

    public static void writeVarInt(int paramInt, ByteBuf ser) {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0L) {
                ser.writeByte(paramInt);
                return;
            }

            ser.writeByte((paramInt & 0x7F) | 0x80);
            paramInt >>>= 7;
        }
    }

    public static int readVarInt(ByteBuf ser) {
        int l = 0;
        int i = 0;
        while (true) {
            int j = ser.readByte();

            l |= (j & 0x7F) << i++ * 7;

            if (i > 10) {
                throw new RuntimeException("VarLong too big");
            }

            if ((j & 0x80) != 128) {
                break;
            }
        }

        return l;
    }

    public static void writeVarLong(long paramLong, ByteBuf ser) {
        while (true) {
            if ((paramLong & 0xFFFFFF80) == 0L) {
                ser.writeByte((int) paramLong);
                return;
            }

            ser.writeByte((int) (paramLong & 0x7F) | 0x80);
            paramLong >>>= 7;
        }
    }

    public static long readVarLong(ByteBuf ser) {
        long l = 0L;
        int i = 0;
        while (true)
        {
            int j = ser.readByte();

            l |= (j & 0x7F) << i++ * 7;

            if (i > 10) {
                throw new RuntimeException("VarLong too big");
            }

            if ((j & 0x80) != 128) {
                break;
            }
        }

        return l;
    }
}
