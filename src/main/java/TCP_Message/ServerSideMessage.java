package TCP_Message;

import BDD.BDD;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;


public class ServerSideMessage extends Thread {
    private final ServerSocket servSocket;
    private boolean run = true;
    private final HashMap<InetAddress, ClientSide> listClientAddr;
    private final BDD database;

    public ServerSideMessage(int port, BDD database) throws IOException {
        super("ServMessage");
        this.servSocket = new ServerSocket(port,2, InetAddress.getByAddress(new byte[] {0,0,0,0}));
        this.listClientAddr = new HashMap<>();
        this.database = database;
    }


    @Override
    public void run()
    {
        Socket clientSocket;
        while(run)
        {
            ClientSide acceptedClient;
            try {
                clientSocket = this.servSocket.accept();
                System.out.println("connected \n");

            }
            catch (IOException e)
            {
                e.printStackTrace();
                continue;
            }


            acceptedClient = new ClientSide(clientSocket, database);
            this.listClientAddr.put(clientSocket.getInetAddress(), acceptedClient);

            acceptedClient.start();

        }
        try {
            this.servSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClientSide GetClientByAdress(InetAddress addressClient) throws IOException {

        if(listClientAddr.containsKey(addressClient))
        {
            return listClientAddr.get(addressClient);
        }
        else if(this.database.getAdressByName().containsValue(addressClient) && !listClientAddr.containsKey(addressClient))
        {
            Socket newClientSocket = new Socket(addressClient, this.servSocket.getLocalPort());
            ClientSide newClient = new ClientSide(newClientSocket, this.database);
            this.listClientAddr.put(addressClient, newClient);
            newClient.start();
            return newClient;
        }

        return null;
    }


    public void SendToClient(String message, InetAddress adressClient) throws IOException, SQLException {

        ClientSide client = GetClientByAdress(adressClient);
        if(client != null)
        {
            if(client.isAlive())
            {
                client.SendMessage(message);
            }
            else {
                this.listClientAddr.remove(adressClient);
                ClientSide client2 = GetClientByAdress(adressClient);
                if(client2 != null)
                {
                    client2.SendMessage(message);
                }
            }

        }
    }

    public void disconnect()
    {
        this.run = false;
        for(ClientSide client: this.listClientAddr.values())
        {
            client.disconnect();
        }
        listClientAddr.clear();
    }
}
