package online.syncio.model;

import online.syncio.utils.TimeHelper;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Message {

    @BsonProperty("sender")
    private String sender;

    @BsonProperty("text")
    private String text;

    @BsonProperty("dateSent")
    private String dateSent = TimeHelper.getCurrentDateTime();

    public Message() {
    }

    public Message(String dateSent, String text, String sender) {
        this.dateSent = dateSent;
        this.text = text;
        this.sender = sender;
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    @Override
    public String toString() {
        return """
               \tMessage{
               \t\tsender=""" + sender + "\n\t\t, text=" + text + "\n\t\t, dateSent=" + dateSent + "}\n\n";
    }

}
