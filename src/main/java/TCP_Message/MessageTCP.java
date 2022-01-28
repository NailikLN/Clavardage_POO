package TCP_Message;

import java.net.InetAddress;
import java.util.Date;


public class MessageTCP extends TypeTCPMessage{
    private final String message;
    private final Date date;
    private final InetAddress AdressDest;

    public MessageTCP(String message, InetAddress inetAddress) {
        super(TypeMessage.MESSAGE);
        this.message = message;
        this.date = new Date();
        this.AdressDest = inetAddress;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public InetAddress getAdressDest() {
        return AdressDest;
    }
}
