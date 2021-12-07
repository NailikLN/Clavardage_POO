package Communication;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class DisconnectMessage extends TypeOfMessage{

    public DisconnectMessage(int port, InetAddress adress) {
        super(port, adress, TypeMessage.Disconnect);
    }

    public DisconnectMessage(DatagramPacket packet) {
        super(packet);
    }
}
