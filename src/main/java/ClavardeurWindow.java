import BDD.BDD;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
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
    private JTextPane historyChat;
    private JTextField enterNicknameTextField;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton sendButton;
    private String selected = "";
    private Boolean Changed = false;
    private ArrayList<String> Temptext;
    private String previousUsername = null;

    public ClavardeurWindow(BDD database, App app) {
        super("LoginScreen");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(MainClavardeur);

        listUser = new JList<>();
        listUser.setModel(database.displayList);



        this.database = database;
        this.Temptext = new ArrayList<>();



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
            }

        });

        this.pack();

        listUser.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selected = listUser.getSelectedValue();
                Changed = true;
                System.out.println("changed");
            }
        });
    }

    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    public void updateMessage()
    {
        if(database.getNameByAdress().containsValue(selected))
        {
            if(Changed) {
                historyChat.setText("");
                Temptext = new ArrayList<>();
                Changed = false;
                previousUsername = null;
            }
            int i = 0;
            for(String text : database.MessageHistToString(selected))
            {
                Boolean show = true;
                Boolean Colored = false;


                if(i >= Temptext.size() || !Temptext.get(i).equals(text) )
                {
                    if(i%2 == 0)
                    {
                        Colored = true;
                        String currentUsername = "";
                        currentUsername = text.split(" ")[0];
                        System.out.println(currentUsername + " / " + previousUsername);
                        if(currentUsername.equals(previousUsername))
                        {

                            show = false;

                        }
                        else
                        {
                            previousUsername = currentUsername;
                        }
                    }

                    if(show)
                        if(Colored)
                        {
                            appendToPane(historyChat,"\n",Color.red);
                            appendToPane(historyChat,text,Color.red);
                            appendToPane(historyChat,"\n",Color.red);
                        }
                        else
                            appendToPane(historyChat,text,Color.black);
                    else
                        System.out.println("nop");

                    Temptext.add(text);
                }
                i++;
            }
        }
    }


}
