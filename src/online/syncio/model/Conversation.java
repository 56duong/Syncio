package online.syncio.model;

import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Conversation {

    @BsonId
    private ObjectId id;

    @BsonProperty("participants")
    private List<String> participants;

    @BsonProperty("messagesHistory")
    private List<Message> messagesHistory;

    public Conversation() {
    }

    public Conversation(List<String> participants, List<Message> messagesHistory) {
        this.participants = participants;
        this.messagesHistory = messagesHistory;
    }

    public Conversation(ObjectId id, List<String> participants, List<Message> messagesHistory) {
        this.id = id;
        this.participants = participants;
        this.messagesHistory = messagesHistory;
    }

    public Conversation(String id, List<String> participants, List<Message> messagesHistory) {
        this.id = new ObjectId(id);
        this.participants = participants;
        this.messagesHistory = messagesHistory;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessagesHistory() {
        return messagesHistory;
    }

    public void setMessagesHistory(List<Message> messagesHistory) {
        this.messagesHistory = messagesHistory;
    }

    public void addNewMessage(Message msg) {
        this.messagesHistory.add(msg);
    }

    public Message getNewestMessage() {
        if (messagesHistory.isEmpty()) {
            return null;
        }
        return messagesHistory.get(messagesHistory.size() - 1);
    }

    @Override
    public String toString() {
        return "Conversation{" + "id=" + id + "\n\nparticipants=" + participants + "\n\nmessagesHistory=" + messagesHistory + '}';
    }
}
