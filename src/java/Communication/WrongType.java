package Communication;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class WrongType extends TypeOfMessage{
    public WrongType(int port, InetAddress adress) {
        super(port, adress, TypeMessage.WrongType);
    }

    public WrongType(DatagramPacket packet) {
        super(packet);
    }
}
