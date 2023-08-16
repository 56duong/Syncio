package online.syncio.model;

import online.syncio.utils.TimeHelper;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Message {

    @BsonProperty("senderID")
    private String senderID;

    @BsonProperty("text")
    private String text;

    @BsonProperty("dateSent")
    private String dateSent = TimeHelper.getCurrentDateTime();

    public Message() {
    }

    public Message(String dateSent, String text, String senderID) {
        this.dateSent = dateSent;
        this.text = text;
        this.senderID = senderID;
    }

    public Message(String senderID, String text) {
        this.senderID = senderID;
        this.text = text;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
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
               \t\tsenderID=""" + senderID + "\n\t\t, text=" + text + "\n\t\t, dateSent=" + dateSent + "}\n\n";
    }

}
