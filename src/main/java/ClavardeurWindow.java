import BDD.BDD;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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


        listUser.setModel(database.displayList);


        this.database = database;
        this.Temptext = new ArrayList<>();


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nickname = enterNicknameTextField.getText();
                enterNicknameTextField.setText("");
                connectButton.setText("Change Nickname");
                if (!database.getAdressByName().containsValue(nickname)) {
                    try {
                        app.connect(nickname);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
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
                if (adress != null) {
                    try {
                        app.sendMessage(message, adress);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
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

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    public void updateMessage() {
        if (database.getNameByAdress().containsValue(selected)) {
            if (Changed) {
                historyChat.setText("");
                Temptext = new ArrayList<>();
                Changed = false;
                previousUsername = null;
            }
            int i = 0;
            for (String text : database.MessageHistToString(selected)) {
                Boolean show = true;
                Boolean Colored = false;


                if (i >= Temptext.size() || !Temptext.get(i).equals(text)) {
                    if (i % 2 == 0) {
                        Colored = true;
                        String currentUsername = "";
                        currentUsername = text.split(" ")[0];
                        System.out.println(currentUsername + " / " + previousUsername);
                        if (currentUsername.equals(previousUsername)) {

                            show = false;

                        } else {
                            previousUsername = currentUsername;
                        }
                    }

                    if (show)
                        if (Colored) {
                            appendToPane(historyChat, "\n", Color.red);
                            appendToPane(historyChat, text, Color.red);
                            appendToPane(historyChat, "\n", Color.red);
                        } else
                            appendToPane(historyChat, text, Color.black);
                    else
                        System.out.println("nop");

                    Temptext.add(text);
                }
                i++;
            }
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        MainClavardeur = new JPanel();
        MainClavardeur.setLayout(new GridLayoutManager(8, 7, new Insets(0, 0, 0, 0), -1, -1));
        MainClavardeur.setBorder(BorderFactory.createTitledBorder(null, "Clavardeur", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Enter User Name");
        MainClavardeur.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        MainClavardeur.add(spacer1, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 20), null, new Dimension(-1, 20), 0, false));
        final Spacer spacer2 = new Spacer();
        MainClavardeur.add(spacer2, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Users");
        MainClavardeur.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("History");
        MainClavardeur.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        MainClavardeur.add(sendButton, new GridConstraints(6, 6, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        messageField = new JTextField();
        messageField.setText("");
        MainClavardeur.add(messageField, new GridConstraints(6, 1, 2, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(150, -1), null, 0, false));
        enterNicknameTextField = new JTextField();
        enterNicknameTextField.setText("");
        MainClavardeur.add(enterNicknameTextField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        connectButton = new JButton();
        connectButton.setText("Connect");
        MainClavardeur.add(connectButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        disconnectButton = new JButton();
        disconnectButton.setText("Disconnect");
        MainClavardeur.add(disconnectButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        MainClavardeur.add(spacer3, new GridConstraints(0, 5, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(50, -1), null, new Dimension(50, -1), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        MainClavardeur.add(scrollPane1, new GridConstraints(3, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listUser = new JList();
        scrollPane1.setViewportView(listUser);
        final JScrollPane scrollPane2 = new JScrollPane();
        MainClavardeur.add(scrollPane2, new GridConstraints(3, 1, 3, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        historyChat = new JTextPane();
        scrollPane2.setViewportView(historyChat);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainClavardeur;
    }
}
