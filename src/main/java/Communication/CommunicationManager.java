package Communication;

import BDD.BDD;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class CommunicationManager extends Thread{
    private InetAddress BroadcastAddr = Inet4Address.getByAddress(new byte[] {-1,-1,-1,-1});
    private DatagramSocket socket;
    private boolean disconnected = false;
    private BDD database;

    public CommunicationManager(int port, BDD database) throws Exception {
        this.socket = new DatagramSocket(port, Inet4Address.getByAddress(new byte[] {0,0,0,0}));
        this.socket.setBroadcast(true);

        this.database = database;
    }

    public void Connect() throws Exception{

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback())
                continue;    // Do not want to use the loopback interface.
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
            {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null)
                    continue;

                this.BroadcastAddr = broadcast;
                System.out.println(broadcast + "\n");
            }
        }

        ConnectMessage connectMessage = new ConnectMessage(this.socket.getLocalPort(), BroadcastAddr);
        this.socket.send(connectMessage.to_packet());
        System.out.println(this.socket.getBroadcast() + "\n");


    }

    public void Disconnect() throws Exception{
        DisconnectMessage disconnectMessage = new DisconnectMessage(this.socket.getLocalPort(), BroadcastAddr);
        this.socket.send(disconnectMessage.to_packet());
        this.disconnected = true;
    }

    public void Change_Name(String name) throws Exception{
        this.database.setName(name);
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
            System.out.println(message.toString());

            this.database.updateDatabase(message);
            if(this.database.getName() != null && message.getType() == TypeOfMessage.TypeMessage.Connect)
            {
                ChangeName changeNameMessage = new ChangeName(message.getPort(), message.getAdress(), this.database.getName());
                try {
                    this.socket.send(changeNameMessage.to_packet());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        this.socket.close();
    }
}
