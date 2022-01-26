import BDD.BDD;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClavardeurWindow extends JFrame {

    private BDD database;
    private JPanel MainClavardeur;
    private JTextField messageField;
    private JList<String> listUser;
    private JTextArea historyChat;
    private JTextField enterNicknameTextField;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton sendButton;
    private String selected = "";

    public ClavardeurWindow(BDD database, App app) {
        super("LoginScreen");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listUser.setModel(database.displayList);

        this.setContentPane(MainClavardeur);

        this.database = database;



        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nickname = enterNicknameTextField.getText();
                enterNicknameTextField.setText("");
                connectButton.setText("Change Nickname");
                if(!database.getAdressByName().containsValue(nickname))
                {
                    try {
                        app.connect(nickname);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    System.out.println("nickname already used");
                }
            }
        });


        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    app.disconnect();
                    connectButton.setText("Connect");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String message = messageField.getText();
                messageField.setText("");
                InetAddress adress = database.getAdressByName().get(selected);
                if(adress != null) {
                    try {
                        app.sendMessage(message, adress);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("wrong destination");
                }
                updateMessage();
            }

        });

        this.pack();

        listUser.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selected = listUser.getSelectedValue();
                updateMessage();
            }
        });
    }

    public void updateMessage()
    {
        if(database.getNameByAdress().containsValue(selected))
        {
            historyChat.setText("");
            System.out.print("trying to print");
            for(String text :database.MessageHistToString(selected))
            {
                System.out.print(text);
                historyChat.append(text);
            }
        }
    }


}
