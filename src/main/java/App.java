import BDD.BDD;
import Communication.CommunicationManager;
import TCP_Message.ServerSideMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

public class App {
    private BDD database;
    private static ClavardeurWindow loginFrame;
    private CommunicationManager comm;
    private ServerSideMessage serverSideMessage;
    private boolean isConnected = false;

    public App() throws Exception {
        database = new BDD();
        database.initDatabase();
        loginFrame = new ClavardeurWindow(database, this);
        comm = new CommunicationManager(1250, database);
        serverSideMessage = new ServerSideMessage(1250, database);
    }

    public void run() {


        loginFrame.setVisible(true);

        comm.start();
        serverSideMessage.start();
    }

    public void sendMessage(String message, InetAddress adress) throws SQLException, IOException {
        serverSideMessage.SendToClient(message, adress);
    }

    public void connect(String nickname) throws Exception {

        database.setName(nickname);
        if(!isConnected)
        {
            comm.Connect();
            comm.Change_Name(nickname);
            isConnected = true;
        }
        else
        {
            comm.Change_Name(nickname);
        }

    }

    public void disconnect() throws Exception {
        if(isConnected)
        {
            comm.Disconnect();
            serverSideMessage.disconnect();
            isConnected = false;
        }

    }
}
