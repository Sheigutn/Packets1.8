package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.PacketDataSerializer;

public class VarLong {

    public static void writeVarLong(long paramLong, PacketDataSerializer ser) {
        while (true) {
            if ((paramLong & 0xFFFFFF80) == 0L) {
                ser.writeByte((int) paramLong);
                return;
            }

            ser.writeByte((int) (paramLong & 0x7F) | 0x80);
            paramLong >>>= 7;
        }
    }

    public static long readVarLong(PacketDataSerializer ser) {
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
