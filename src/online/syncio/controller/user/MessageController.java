package online.syncio.controller.user;

import com.mongodb.client.ChangeStreamIterable;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import online.syncio.component.SearchedCard;
import online.syncio.component.message.ChatArea;
import online.syncio.dao.ConversationDAO;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.Conversation;
import online.syncio.model.LoggedInUser;
import online.syncio.model.User;
import online.syncio.view.user.MessagePanel;
import org.bson.types.ObjectId;

public class MessageController {

    private MessagePanel pnlMsg;
    private CardLayout cardLayout;

    private UserDAO userDAO = MongoDBConnect.getUserDAO();
    private ConversationDAO conversationDAO = MongoDBConnect.getConversationDAO();
    private List<Object> historyList;

    private HashMap<String, Component> chatAreas = new HashMap<>();

    public MessageController(MessagePanel pnlMsg) {
        this.pnlMsg = pnlMsg;
        cardLayout = (CardLayout) pnlMsg.getChatArea().getLayout();
    }

    public void recheckLoggedInUser() {
        if (LoggedInUser.isUser()) {
            addUserToHistoryPanel();
        } else {
            System.out.println("chưa đăng nhập");
        }
    }

    public void addUserToHistoryPanel() {
        pnlMsg.getPnlUserList().removeAll();

        historyList = conversationDAO.getAllMessageHistory(LoggedInUser.getCurrentUserame());

        Thread thread = new Thread(() -> {
            for (Object o : historyList) {
                SwingUtilities.invokeLater(() -> {
                    if (o instanceof ObjectId groupChat) {
                        createCard(groupChat.toString(), null);
                    } else if (o instanceof String name) {
                        User user = userDAO.getByUsername(name);
                        createCard(name, user);
                    }
                });
            }

            pnlMsg.getPnlUserList().revalidate();
            pnlMsg.getPnlUserList().repaint();
        });
        thread.start();

        getConversationChangeStream();
    }

    private void createCard(String identifier, User user) {
        SearchedCard card = new SearchedCard();

        if (user != null) {
            card.setUser(user);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    openMessage(card.getUser().getUsername());
                }
            });
        } else {
            card.setConversationID(identifier);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    openMessage(card.getConversationID());
                }
            });
        }

        card.setName(identifier);

        pnlMsg.getPnlUserList().add(card);
        pnlMsg.getPnlUserList().add(Box.createVerticalStrut(20));
    }

    public void openMessage(String textString) {
        Component chatArea = chatAreas.get(textString);

        if (chatArea == null) {
            User messagingUser = userDAO.getByUsername(textString);
            if (messagingUser != null) {
                createUserMessagePanel(messagingUser);
            } else {
                createGroupChatMessagePanel(textString);
            }
        } else {
            cardLayout.show(pnlMsg.getChatArea(), textString);
        }
    }

    public void createUserMessagePanel(User messagingUser) {
        String messagingUsername = messagingUser.getUsername();

        if (!historyList.contains(messagingUsername)) {
            User user = userDAO.getByUsername(messagingUsername);
            createCard(messagingUsername, user);
            historyList.add(messagingUsername);
        }

        if (!chatAreas.containsKey(messagingUsername)) {
            ChatArea ca = new ChatArea();
            try {
                ca.findConversationWithOneUser(messagingUser);
            } catch (NullPointerException e) {
                String[] participants = new String[]{LoggedInUser.getCurrentUserame(), messagingUsername};
                Conversation con = new Conversation(Arrays.asList(participants), new ArrayList<>());

                conversationDAO.add(con);
                ca.findConversationWithOneUser(messagingUser);
            }

            ca.setName(messagingUsername);

            pnlMsg.getChatArea().add(ca, messagingUsername);
            chatAreas.put(messagingUsername, ca);
        }

        cardLayout.show(pnlMsg.getChatArea(), messagingUsername);
    }

    public void createGroupChatMessagePanel(String conversationID) {
        if (!historyList.contains(new ObjectId(conversationID))) {
            createCard(conversationID, null);
            historyList.add(new ObjectId(conversationID));
        }

        if (!chatAreas.containsKey(conversationID)) {
            ChatArea ca = new ChatArea();
            ca.setConversationID(conversationID);
            ca.setName(conversationID);
            pnlMsg.getChatArea().add(ca, conversationID);

            chatAreas.put(conversationID, ca);
        }

        cardLayout.show(pnlMsg.getChatArea(), conversationID);
    }

    // Dùng để update giao diện, kiểm tra người dùng này có được người mới nhăn tin
    // hoặc được mời vào group chat
    public void getConversationChangeStream() {
        ChangeStreamIterable<Conversation> changeStream = conversationDAO.getChangeStream();

        Thread changeStreamThread = new Thread(() -> {
            changeStream.forEach(changeDocument -> {
                Conversation conversation = changeDocument.getFullDocument();

                List<String> participants = conversation.getParticipants();

                if (participants.contains(LoggedInUser.getCurrentUserame())) {
                    participants.remove(LoggedInUser.getCurrentUserame());

                    if (participants.size() == 1
                            && !historyList.contains(participants.get(0))) {
                        String messeagingUsernam = participants.get(0);
                        User user = userDAO.getByUsername(messeagingUsernam);
                        createUserMessagePanel(user);
                    }

                    if (participants.size() >= 2
                            && !historyList.contains(conversation.getId())) {
                        createGroupChatMessagePanel(conversation.getId().toString());
                    }
                }

                pnlMsg.getPnlUserList().revalidate();
                pnlMsg.getPnlUserList().repaint();
            });
        });

        changeStreamThread.start();
    }
}
