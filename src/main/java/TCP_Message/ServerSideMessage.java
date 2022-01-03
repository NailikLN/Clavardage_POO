package TCP_Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSideMessage extends Thread {
    private ServerSocket servSocket;
    private boolean run = true;

    public ServerSideMessage(int port) throws IOException {
        this.servSocket = new ServerSocket(port,2, InetAddress.getByAddress(new byte[] {0,0,0,0}));
    }

    @Override
    public void run()
    {
        Socket clientSocket;
        while(run)
        {
            try {
                clientSocket = this.servSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                continue;
            }
        }
    }
}
