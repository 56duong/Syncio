package online.syncio.controller.user;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.Box;
import online.syncio.component.SearchedCard;
import online.syncio.component.message.ChatArea;
import online.syncio.dao.ConversationDAO;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
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

        historyList = conversationDAO.findAllMessageHistory(LoggedInUser.getCurrentUser().getUsername());

        for (Object o : historyList) {
            if (o instanceof ObjectId groupChat) {
                createCardForGroup(groupChat.toString());
            } else if (o instanceof String name) {
                createCardForUser(name);
            }
        }

        pnlMsg.getPnlUserList().revalidate();
        pnlMsg.getPnlUserList().repaint();
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

    public void createCardForGroup(String conversationID) {
        createCard(conversationID, null);  // Replace with actual user data
    }

    public void createCardForUser(String username) {
        User user = userDAO.getByUsername(username);

        if (user != null) {
            createCard(username, user);
        }
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
        String username = messagingUser.getUsername();

        if (!historyList.contains(username)) {
            createCardForUser(username);
        }

        if (!chatAreas.containsKey(username)) {
            ChatArea ca = new ChatArea();
            ca.findConversation(messagingUser);
            ca.setName(username);
            pnlMsg.getChatArea().add(ca, username);
            chatAreas.put(username, ca);
        }

        cardLayout.show(pnlMsg.getChatArea(), username);
    }

    public void createGroupChatMessagePanel(String conversationID) {
        if (!historyList.contains(new ObjectId(conversationID))) {
            createCardForGroup(conversationID);
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
}
