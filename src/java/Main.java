import Communication.CommunicationManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception{
        CommunicationManager comm = new CommunicationManager(1250);
        comm.start();

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
                    running = false;
                }
            }
        }
    }


}
