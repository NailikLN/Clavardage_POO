package Communication;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import static java.lang.Math.min;

public class ChangeName extends TypeOfMessage {
    private String name;

    public ChangeName(int port, InetAddress adress, String name) {
        super(port, adress, TypeOfMessage.TypeMessage.ChangeName);
        this.name = name;
    }

    public ChangeName(DatagramPacket packet) {
        super(packet);
        byte nameLength = packet.getData()[1];
        this.name = new String(packet.getData(),2,(int)nameLength, StandardCharsets.UTF_8);

    }

    public String getName()
    {
        return this.name;
    }

    public DatagramPacket to_packet(){
        byte buffer[] = new byte[LENGTH_BUFFER];
        buffer[0] = this.type.to_byte();
        byte[] nameBuffer = this.name.getBytes(StandardCharsets.UTF_8);
        buffer[1] = (byte) (min(nameBuffer.length, 254)-128);
        System.arraycopy(nameBuffer, 0, buffer,2, nameBuffer.length);
        DatagramPacket packet = new DatagramPacket(buffer,LENGTH_BUFFER);
        packet.setAddress(this.adress);
        packet.setPort(this.port);
        return packet;
    }
}
