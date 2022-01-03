package TCP_Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSideMessage extends Thread{

    private Socket socketClient;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean run = true;

    public ClientSideMessage(Socket socketClient) throws IOException {
        this.socketClient = socketClient;
        this.inputStream = new ObjectInputStream(this.socketClient.getInputStream());
        this.outputStream = new ObjectOutputStream(this.socketClient.getOutputStream());
    }

    public void run()
    {
        while(run)
        {

        }
    }
}
