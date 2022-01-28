import BDD.BDD;
import Communication.CommunicationManager;
import TCP_Message.ServerSideMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;


public class App {
    private static ClavardeurWindow loginFrame;
    private final CommunicationManager comm;
    private final ServerSideMessage serverSideMessage;
    private boolean isConnected = false;
    private boolean run = true;

    public App() throws Exception {

        BDD database = new BDD();
        database.initDatabase();
        loginFrame = new ClavardeurWindow(database, this);
        comm = new CommunicationManager(1250, database);
        serverSideMessage = new ServerSideMessage(1250, database);
    }

    public void run() {


        loginFrame.setVisible(true);

        comm.start();
        serverSideMessage.start();
        while (run)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loginFrame.updateMessage();
        }
    }

    public void sendMessage(String message, InetAddress adress) throws SQLException, IOException {
        serverSideMessage.SendToClient(message, adress);
    }

    public void connect(String nickname) throws Exception {

        if(!isConnected)
        {

            comm.Connect();
            comm.Change_Name(nickname);
            isConnected = true;
            comm.isConnected = true;
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
            run = false;
        }

    }
}
