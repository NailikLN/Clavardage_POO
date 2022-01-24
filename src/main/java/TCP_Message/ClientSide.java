package TCP_Message;

import BDD.BDD;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientSide extends Thread{

    private Socket socketClient;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean run = true;
    private BDD database;

    public ClientSide(Socket socketClient, BDD database) throws IOException {
        this.socketClient = socketClient;
        this.inputStream = new ObjectInputStream(this.socketClient.getInputStream());
        this.outputStream = new ObjectOutputStream(this.socketClient.getOutputStream());
        this.database = database;
    }

    public void run()
    {
        Object messageReceive;
        while(run)
        {
            try
            {
                messageReceive = inputStream.readObject();
                System.out.println("clic");
                MessageType((TypeTCPMessage) messageReceive, messageReceive);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void MessageType(TypeTCPMessage message, Object messageReceive) throws SQLException {
        TypeTCPMessage.TypeMessage typeMessageReceive = message.getTypeMessage();
        switch (typeMessageReceive)
        {
            case MESSAGE -> GetMessage((MessageTCP)messageReceive);
            case WRONG_TYPE -> {
                System.out.println("Wrong Type");
            }
        }
    }

    public String GetMessage(MessageTCP messageReceive) throws SQLException {
        this.database.putReceivedMessage(this.socketClient.getInetAddress(), messageReceive);
        return messageReceive.getMessage();
    }

    public void SendMessage(String message) throws IOException, SQLException {
        MessageTCP messageTCP = new MessageTCP(message, socketClient.getInetAddress());
        this.database.putSentMessage(this.socketClient.getInetAddress(), messageTCP);
        this.outputStream.writeObject(messageTCP);
    }



}
