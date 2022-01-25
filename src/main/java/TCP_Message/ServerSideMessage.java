package TCP_Message;

import BDD.BDD;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSideMessage extends Thread {
    private ServerSocket servSocket;
    private boolean run = true;
    private final HashMap<InetAddress, ClientSide> listClientAddr;
    private BDD database;

    public ServerSideMessage(int port, BDD database) throws IOException {
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
            ClientSide acceptedClient = null;
            try {
                clientSocket = this.servSocket.accept();
                System.out.println("connected \n");

            }
            catch (IOException e)
            {
                e.printStackTrace();
                continue;
            }


            try{
                acceptedClient = new ClientSide(clientSocket, database);
                this.listClientAddr.put(clientSocket.getInetAddress(), acceptedClient);

                acceptedClient.start();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

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
        System.out.println("rrr");
        if(client != null)
        {
            System.out.println("sent \n");
            client.SendMessage(message);
        }
    }
    /*  public void SendToClient(String message, String name) throws IOException, SQLException {
        Map<String, InetAddress> adressByName = this.database.getAdressByName();
        InetAddress adressClient = adressByName.get(name);
        List<InetAddress> listUser = this.database.getListUsersConnected();
        if(name != null && listUser.contains(adressClient))
        {
            ClientSide client = GetClientByAdress(adressClient);
            System.out.println("sent");
            client.SendMessage(message);
        }
    }  */

    public void disconnect()
    {
        this.run = false;
        for(ClientSide Client: this.listClientAddr.values())
        {
            Client.disconnect();
        }
        listClientAddr.clear();
    }
}
