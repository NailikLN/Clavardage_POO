package BDD;

import java.util.Comparator;

public class MessageHist implements Comparable<MessageHist>
{



    private String message;
    private String userFrom;
    private String date;


    public MessageHist(String message, String userFrom, String date) {
        this.message = message;
        this.userFrom = userFrom;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public String getDate() {
        return date;
    }

    /*@Override
    public int compare(MessageHist o,MessageHist o2) {
        return CharSequence.compare(o.getDate(),o2.getDate());
    }*/

    @Override
    public int compareTo(MessageHist o) {
        return this.getDate().compareTo(o.getDate());
    }
}
