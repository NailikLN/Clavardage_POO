package TCP_Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerSideMessage extends Thread {
    private ServerSocket servSocket;
    private boolean run = true;
    private HashMap<InetAddress, ClientSide> listClientAddr;

    public ServerSideMessage(int port) throws IOException {
        this.servSocket = new ServerSocket(port,2, InetAddress.getByAddress(new byte[] {0,0,0,0}));
    }


    @Override
    public void run()
    {
        Socket clientSocket;
        while(run)
        {
            ClientSide acceptedClient = null;
            try {
                clientSocket = this.servSocket.accept();

            }
            catch (IOException e)
            {
                e.printStackTrace();
                continue;
            }


            try{
                acceptedClient = new ClientSide(clientSocket);
                this.listClientAddr.put(clientSocket.getInetAddress(), acceptedClient);

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

        }
    }

    private ClientSide GetClientByAdress(InetAddress addressClient)
    {
        if(this.listClientAddr.containsKey(addressClient))
        {
            return listClientAddr.get(addressClient);
        }
        return null;
    }

    private InetAddress GetAdressByClient(ClientSide client)
    {
        InetAddress key = null;
        for(Map.Entry<InetAddress, ClientSide> entry : listClientAddr.entrySet())
        {
            if(entry.getValue().equals(client))
            {
                key = entry.getKey();
            }
        }
        return key;
    }

    private void SendToClient(String message, InetAddress AdressClient) throws IOException {
        ClientSide client = GetClientByAdress(AdressClient);
        client.SendMessage(message);
    }
}
