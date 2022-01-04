package TCP_Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSide extends Thread{

    private Socket socketClient;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean run = true;

    public ClientSide (Socket socketClient) throws IOException {
        this.socketClient = socketClient;
        this.inputStream = new ObjectInputStream(this.socketClient.getInputStream());
        this.outputStream = new ObjectOutputStream(this.socketClient.getOutputStream());
    }

    public void run()
    {
        Object messageReceive;
        while(run)
        {
            try
            {
                messageReceive = inputStream.readObject();
                MessageType((TypeTCPMessage) messageReceive, messageReceive);
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

        }
    }

    private void MessageType(TypeTCPMessage message, Object messageReceive) {
        TypeTCPMessage.TypeMessage typeMessageReceive = message.getTypeMessage();
        switch (typeMessageReceive)
        {
            case MESSAGE -> System.out.println(GetMessage((MessageTCP) messageReceive));
            case WRONG_TYPE -> {
                System.out.println("Wrong Type");
            }
        }
    }

    private String GetMessage(MessageTCP messageReceive) {
        //String Message = null;
        //System.arraycopy(messageReceive,2,Message,0, messageReceive.length());
        return messageReceive.getMessage();
    }

    private void SendMessage(String message) throws IOException
    {
        this.outputStream.writeObject(new MessageTCP(message));
    }



}
