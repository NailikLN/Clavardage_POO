package BDD;

import Communication.ChangeName;
import Communication.TypeOfMessage;
import TCP_Message.MessageTCP;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.InetAddress;
import java.sql.*;
import java.util.*;

public class BDD {

    private String name = "test";

    private final Connection database;


    public List<InetAddress> getListUsersConnected() {
        return ListUsersConnected;
    }

    private final List<InetAddress> ListUsersConnected;

    public Map<InetAddress, String> getNameByAdress() {
        return nameByAdress;
    }

    private final Map<InetAddress, String> nameByAdress;
    private final Map<String, InetAddress> adressByName;

    public DefaultListModel displayList;



    @Override
    public String toString() {
        System.out.println(ListUsersConnected.toString());

        for (Map.Entry entry : nameByAdress.entrySet())
        {
            System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }

        for (Map.Entry entry : adressByName.entrySet())
        {
            System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }
        return "";
    }

    public BDD() throws SQLException
    {
        this.database = DriverManager.getConnection("jdbc:sqlite:clavardageDB.db");
        this.ListUsersConnected = new ArrayList<>();
        this.nameByAdress = new HashMap<>();
        this.adressByName = new HashMap<>();
        this.displayList = new DefaultListModel<>();


    }

    public void initDatabase() throws SQLException {

        Statement statement = this.database.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS clavardageLog ("
                + " 'id' INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " 'to' TEXT NOT NULL,"
                + " 'from' BOOLEAN,"
                + " 'content' TEXT NOT NULL,"
                + " 'date' TEXT NOT NULL);";

        statement.executeUpdate(query);

    }

    public void updateDatabase(TypeOfMessage messageUDP ){
        switch (messageUDP.getType()){

            case Connect -> {
                if(!ListUsersConnected.contains(messageUDP.getAdress())){
                    this.ListUsersConnected.add(messageUDP.getAdress());
                    String names = this.nameByAdress.get(messageUDP.getAdress());
                    if( names != null);
                    displayList.addElement(names);
                }

            }
            case Disconnect -> {
                if(ListUsersConnected.contains(messageUDP.getAdress())){
                    this.ListUsersConnected.remove(messageUDP.getAdress());
                    String names = this.nameByAdress.get(messageUDP.getAdress());
                    if( names != null);
                    displayList.removeElement(names);
                }
            }
            case ChangeName -> {
                if(!this.ListUsersConnected.contains(messageUDP.getAdress()))
                {
                    this.ListUsersConnected.add(messageUDP.getAdress());
                    String names = this.nameByAdress.get(messageUDP.getAdress());
                    if( names != null);
                    displayList.addElement(names);
                }

                if(!this.adressByName.containsKey(((ChangeName) messageUDP).getName()))
                {
                    String tempName = this.nameByAdress.get(messageUDP.getAdress());
                    this.nameByAdress.remove(messageUDP.getAdress());
                    this.adressByName.remove(tempName);

                    System.out.println("IP : " + messageUDP.getAdress() + " prends le pseudo : " + ((ChangeName) messageUDP).getName());
                    this.adressByName.put(((ChangeName) messageUDP).getName(), messageUDP.getAdress());
                    this.nameByAdress.put(messageUDP.getAdress(), ((ChangeName) messageUDP).getName());
                }
                else
                {
                    System.out.println("pseudo deja pris");
                }

            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void putReceivedMessage(InetAddress inetAddress, MessageTCP messageReceive) throws SQLException {
        String query = "INSERT INTO clavardageLog('to','from','content','date') VALUES(?,?,?,?)";

        PreparedStatement prepState =  database.prepareStatement(query);
        prepState.setString(1, inetAddress.toString());
        prepState.setString(2, this.name);
        prepState.setString(3, messageReceive.getMessage());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        prepState.setString(4, strDate);
        prepState.executeUpdate();

    }
    public void putSentMessage(InetAddress inetAddress, MessageTCP messageReceive) throws SQLException {
        String query = "INSERT INTO clavardageLog('to','from','content','date') VALUES(?,?,?,?)";

        PreparedStatement prepState =  database.prepareStatement(query);
        prepState.setString(2, inetAddress.toString());
        prepState.setString(1, this.name);
        prepState.setString(3, messageReceive.getMessage());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        prepState.setString(4, strDate);
        prepState.executeUpdate();

    }

    public Map<String, InetAddress> getAdressByName() {
        return adressByName;
    }
}
