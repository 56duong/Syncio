package online.syncio.controller;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import online.syncio.component.SearchedCard;
import online.syncio.component.message.ChatBox;
import online.syncio.dao.ConversationDAO;
import online.syncio.dao.MongoDBConnect;
import online.syncio.model.Conversation;
import online.syncio.model.Message;
import online.syncio.view.login.Login;
import org.assertj.swing.edt.GuiActionRunner;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import static org.assertj.swing.timing.Pause.pause;
import org.bson.types.ObjectId;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

    FrameFixture window;
    ConversationDAO conversationDAO = MongoDBConnect.getConversationDAO();

    final String username = "thuanID";
    final String password = "1";

    @Before
    public void setUp() {
        Login frame = GuiActionRunner.execute(() -> new Login());
        window = new FrameFixture(frame);
        window.show();
        window.textBox("txtUser").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();

        // Wait until the main frame becomes visible
        FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());

        pause(1000);
        mainWindow.button("message").click();
        pause(1000);
    }

    @Test
    public void shouldShowMessagedUsersList() {
        FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());

        JPanel pnlHistoryUserList = mainWindow.panel("userList").target();

        List<Object> historyList = conversationDAO.getAllMessageHistory(username);

        int userCount = historyList.size();

        int compCount = 0;

        for (Component comp : pnlHistoryUserList.getComponents()) {
            if (comp instanceof SearchedCard) {
                compCount++;
            }
        }

        assertEquals(userCount, compCount);

        for (Object stuff : historyList) {
            String text = "";

            if (stuff instanceof ObjectId id) {
                text = id.toString();
            }

            if (stuff instanceof String username) {
                text = username;
            }
            assertEquals(text, mainWindow.panel(text).target().getName());
        }
    }

    @Test
    public void shouldShowMessagedHistoryWithAUser() {
        List<Object> historyList = conversationDAO.getAllMessageHistory(username);
        Collections.shuffle(Arrays.asList(historyList));

        int userCount = historyList.toArray().length;

        if (userCount != 0) {
            FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());
            JPanelFixture pnlChatArea = mainWindow.panel("chatArea");

            Object obj = historyList.iterator().next();

            boolean isUser = true;
            String messagingUsername = "";

            if (obj instanceof ObjectId id) {
                messagingUsername = id.toString();
                isUser = false;
            }

            if (obj instanceof String username) {
                messagingUsername = username;
                isUser = true;
            }

            mainWindow.panel("userList").panel(messagingUsername).click();

            pause(1000);

            pnlChatArea.requireVisible();
            pnlChatArea.panel(messagingUsername).requireVisible();

            Conversation converstaion;

            if (isUser) {
                String[] particpants = new String[]{username, messagingUsername};
                converstaion = conversationDAO.getByParticipants(Arrays.asList(particpants));
            } else {
                converstaion = conversationDAO.getByID(messagingUsername);
            }

            int messageCount = converstaion.getMessagesHistory().size();

            int chatBoxCount = 0;

            Component[] compList = pnlChatArea.panel(messagingUsername).panel("body").target().getComponents();

            for (Component cmp : compList) {
                if (cmp instanceof ChatBox) {
                    chatBoxCount++;
                }
            }

            assertEquals(messageCount, chatBoxCount);
        }
    }

    @Test
    public void shouldMessageBeSent() {
        FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());

        List<Object> historyList = conversationDAO.getAllMessageHistory(username);

        Object obj = historyList.iterator().next();

        boolean isUser = true;

        String messagingUsername = "";

        if (obj instanceof ObjectId id) {
            messagingUsername = id.toString();
            isUser = false;
        }

        if (obj instanceof String username) {
            messagingUsername = username;
            isUser = true;
        }

        mainWindow.panel("userList").panel(messagingUsername).click();
        pause(1000);

        Conversation converstaion;
        List<String> participants = new ArrayList<>();

        if (isUser) {
            String[] particpants = new String[]{username, messagingUsername};
            converstaion = conversationDAO.getByParticipants(Arrays.asList(particpants));
        } else {
            converstaion = conversationDAO.getByID(messagingUsername);
            participants = converstaion.getParticipants();
            participants.remove(username);
        }

        String messageContent = "This is a message to see if it can be sent to MongoDB!!!";
        Message m;

        if (isUser) {
            m = new Message(messagingUsername, messageContent);
        } else {
            m = new Message(participants.get(0), messageContent);
        }

        boolean messageSent = conversationDAO.addMessage(converstaion.getId(), m);

        assertEquals(true, messageSent);
    }

    @Test
    public void shouldShowMessagesAfterSending() {
        FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());
        JPanelFixture pnlChatArea = mainWindow.panel("chatArea");

        List<Object> historyList = conversationDAO.getAllMessageHistory(username);

        Object obj = historyList.iterator().next();

        boolean isUser = true;

        String messagingUsername = "";

        if (obj instanceof ObjectId id) {
            messagingUsername = id.toString();
            isUser = false;
        }

        if (obj instanceof String username) {
            messagingUsername = username;
            isUser = true;
        }

        mainWindow.panel("userList").panel(messagingUsername).click();
        pause(1000);

        Conversation converstaion;

        if (isUser) {
            String[] particpants = new String[]{username, messagingUsername};
            converstaion = conversationDAO.getByParticipants(Arrays.asList(particpants));
        } else {
            converstaion = conversationDAO.getByID(messagingUsername);
        }

        String messageContent = "This is a message to see if it can be sent to MongoDB!!!";

        pnlChatArea.textBox("textMessage").enterText(messageContent);
        pnlChatArea.textBox("textMessage").pressAndReleaseKeys(KeyEvent.VK_ENTER);

        pause(3000);

        Component[] compList = pnlChatArea.panel(messagingUsername).panel("body").target().getComponents();
        Collections.reverse(Arrays.asList(compList));

        Component lastestMessage = compList[0];

        if (lastestMessage instanceof ChatBox chatBox) {
            Message msg = chatBox.getMessage();

            // Người gửi có phải là người đăng nhập
            assertEquals(username, msg.getSender());

            // Tin nhắn có trùng nội dùng
            assertEquals(messageContent, msg.getText());

            // Vị trí hiển thị tin nhắn có đúng
            assertEquals(ChatBox.BoxType.RIGHT, chatBox.getBoxType());
        }
    }

    @Test
    public void shouldShowMessagesAfterRecieving() {
        FrameFixture mainWindow = findFrame("Main").withTimeout(10000).using(window.robot());
        JPanelFixture pnlChatArea = mainWindow.panel("chatArea");

        List<Object> historyList = conversationDAO.getAllMessageHistory(username);

        Object obj = historyList.iterator().next();

        boolean isUser = true;

        String messagingUsername = "";

        if (obj instanceof ObjectId id) {
            messagingUsername = id.toString();
            isUser = false;
        }

        if (obj instanceof String username) {
            messagingUsername = username;
            isUser = true;
        }

        mainWindow.panel("userList").panel(messagingUsername).click();
        pause(1000);

        Conversation converstaion;
        List<String> participants = new ArrayList<>();

        if (isUser) {
            String[] particpants = new String[]{username, messagingUsername};
            converstaion = conversationDAO.getByParticipants(Arrays.asList(particpants));
        } else {
            converstaion = conversationDAO.getByID(messagingUsername);
            participants = converstaion.getParticipants();
            participants.remove(username);
        }

        String messageContent = "This is a message to see if the logged in user has recieved this message!!!";
        Message testMessage = new Message();

        if (isUser) {
            testMessage = new Message(messagingUsername, messageContent);
        } else {
            testMessage = new Message(participants.get(0), messageContent);
        }

        boolean messageSent = conversationDAO.addMessage(converstaion.getId(), testMessage);

        assertEquals(true, messageSent);
        pause(3000);

        Component[] compList = pnlChatArea.panel(messagingUsername).panel("body").target().getComponents();
        Collections.reverse(Arrays.asList(compList));

        Component lastestMessage = compList[0];

        if (lastestMessage instanceof ChatBox chatBox) {
            Message msg = chatBox.getMessage();

            // Người gửi có phải là người đang nhắn tin
            if (isUser) {
                assertEquals(messagingUsername, msg.getSender());
            } else {
                assertEquals(participants.get(0), msg.getSender());
            }

            // Tin nhắn có trùng nội dung
            assertEquals(messageContent, msg.getText());

            // Vị trí hiển thị tin nhắn có đúng
            assertEquals(ChatBox.BoxType.LEFT, chatBox.getBoxType());
        }
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }
}
