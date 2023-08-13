package online.syncio.dao;

import com.mongodb.client.ChangeStreamIterable;
import java.util.List;
import online.syncio.model.Conversation;
import online.syncio.model.Message;
import org.bson.types.ObjectId;

public interface ConversationDAO extends DAO<Conversation> {

    public Conversation findByParticipants(List<String> participants);

    public List<Object> findAllMessageHistory(String currentUser);

    public List<String> findMessageHistoryWithUser(String currentUser);

    public ChangeStreamIterable<Conversation> getChangeStream();

    public boolean addMessage(ObjectId id, Message message);
}
