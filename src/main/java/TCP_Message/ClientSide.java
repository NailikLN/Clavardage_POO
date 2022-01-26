package TCP_Message;

import BDD.BDD;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientSide extends Thread{

    private final Socket socketClient;
    private boolean run = true;
    private final BDD database;

    public ClientSide(Socket socketClient, BDD database) {
        super("Com_IP : "+ socketClient.getInetAddress());
        this.socketClient = socketClient;
        this.database = database;
    }

    public void run()
    {
        Object messageReceive;
        while(run)
        {
            try
            {
                ObjectInputStream inputStream = new ObjectInputStream(this.socketClient.getInputStream());
                messageReceive = inputStream.readObject();
                MessageType((TypeTCPMessage) messageReceive, messageReceive);
            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();

                this.run = false;
                continue;

            }
        }

        try {
            this.socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void MessageType(TypeTCPMessage message, Object messageReceive) throws SQLException {
        TypeTCPMessage.TypeMessage typeMessageReceive = message.getTypeMessage();
        switch (typeMessageReceive)
        {
            case MESSAGE -> System.out.println(GetMessage((MessageTCP)messageReceive));
            case WRONG_TYPE -> System.out.println("Wrong Type");
        }
    }

    public String GetMessage(MessageTCP messageReceive) throws SQLException {
        this.database.putReceivedMessage(this.socketClient.getInetAddress(), messageReceive);
        return messageReceive.getMessage();
    }

    public void SendMessage(String message) throws IOException, SQLException {
        MessageTCP messageTCP = new MessageTCP(message, socketClient.getInetAddress());
        ObjectOutputStream outputStream = new ObjectOutputStream(this.socketClient.getOutputStream());
        outputStream.writeObject(messageTCP);
        this.database.putSentMessage(this.socketClient.getInetAddress(), messageTCP);
    }


    public void disconnect() {
        this.run = false;
    }
}
