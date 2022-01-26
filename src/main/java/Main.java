import Communication.CommunicationManager;
import TCP_Message.ServerSideMessage;
import BDD.BDD;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Main {

    private static boolean isLogged = false ;
    private static String nickname = "";

    public static void main(String[] args) throws Exception{

        App app = new App();
        app.run();




        /* boolean running = true ;
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
                case "/send" -> {
                    if (cli_args.length >= 3)
                    {
                        System.out.println("trying to send : " + cli_args[1] + "  to : " + cli_args[2]);
                        serverSideMessage.SendToClient(cli_args[1], InetAddress.getByName(cli_args[2]));

                    }
                    else
                    {
                        System.out.println("wrong parameter");
                    }

                }
                case "/test" -> System.out.println(database);
            }
        }*/
    }
}
