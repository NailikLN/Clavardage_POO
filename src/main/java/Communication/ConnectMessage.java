package Communication;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ConnectMessage extends TypeOfMessage {
    public ConnectMessage(int port, InetAddress adress) {
        super(port, adress, TypeOfMessage.TypeMessage.Connect);
    }

    public ConnectMessage(DatagramPacket packet) {
        super(packet);
    }
}
