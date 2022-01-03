package Communication;

import java.io.IOException;
import java.net.*;

public class CommunicationManager extends Thread{
    private InetAddress BroadcastAddr = Inet4Address.getByAddress(new byte[] {-1,-1,-1,-1});
    private DatagramSocket socket;
    private boolean disconnected = false;

    public CommunicationManager(int port) throws Exception {
        this.socket = new DatagramSocket(port, Inet4Address.getByAddress(new byte[] {0,0,0,0}));
        this.socket.setBroadcast(true);
    }

    public void Connect() throws Exception{
        ConnectMessage connectMessage = new ConnectMessage(this.socket.getLocalPort(), BroadcastAddr);
        this.socket.send(connectMessage.to_packet());
    }

    public void Disconnect() throws Exception{
        DisconnectMessage disconnectMessage = new DisconnectMessage(this.socket.getLocalPort(), BroadcastAddr);
        this.socket.send(disconnectMessage.to_packet());
        this.disconnected = false;
    }

    public void Change_Name(String name) throws Exception{
        ChangeName changeName = new ChangeName(this.socket.getLocalPort(), BroadcastAddr, name);
        this.socket.send(changeName.to_packet());
    }

    public void run(){
        byte[] buffer = new byte[256];
        while(!this.disconnected){
            DatagramPacket receivedPckt = new DatagramPacket(buffer, buffer.length);
            try{
                socket.receive(receivedPckt);
            }catch (IOException exception){
                exception.printStackTrace();
            }

            TypeOfMessage message = TypeOfMessage.from_packet(receivedPckt);
            System.out.print(message.toString());System.out.flush();
        }
    }
}
