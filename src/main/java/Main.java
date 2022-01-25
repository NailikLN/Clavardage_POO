import Communication.CommunicationManager;
import TCP_Message.ServerSideMessage;
import BDD.BDD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) throws Exception{
        BDD database = new BDD();
        database.initDatabase();


        CommunicationManager comm = new CommunicationManager(1250, database);
        ServerSideMessage serverSideMessage = new ServerSideMessage(1250, database);
        comm.start();
        serverSideMessage.start();

        boolean running = true ;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;
        String[] cli_args;
        while(running){
            System.out.print(">> ");System.out.flush();
            command = reader.readLine();
            cli_args = command.split(" ");
            switch (cli_args[0]){
                case "/connect" -> comm.Connect();
                case "/nickname" -> comm.Change_Name(cli_args[1]);
                case "/disconnect" -> {
                    comm.Disconnect();
                    serverSideMessage.disconnect();
                    running = false;
                }
                case "/send" -> {if (cli_args.length >= 3)serverSideMessage.SendToClient(cli_args[1], InetAddress.getByName(cli_args[2]));}
                case "/test" -> System.out.println(database.toString());
            }
        }
    }


}
