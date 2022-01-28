package BDD;


public class MessageHist implements Comparable<MessageHist>
{



    private final String message;
    private final String userFrom;
    private final String date;


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

    @Override
    public int compareTo(MessageHist o) {
        return this.getDate().compareTo(o.getDate());
    }
}
