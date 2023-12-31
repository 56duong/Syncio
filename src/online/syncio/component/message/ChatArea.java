package online.syncio.component.message;

import com.mongodb.client.ChangeStreamIterable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javaswingdev.FontAwesome;
import javaswingdev.FontAwesomeIcon;
import javaswingdev.GoogleMaterialDesignIcon;
import javaswingdev.GoogleMaterialIcon;
import javaswingdev.GradientType;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import online.syncio.component.MyLabel;
import online.syncio.dao.ConversationDAO;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.Conversation;
import online.syncio.model.LoggedInUser;
import online.syncio.model.Message;
import online.syncio.model.User;
import online.syncio.utils.ImageHelper;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

public class ChatArea extends JPanel {

    private AnimationScroll animationScroll;
    private AnimationFloatingButton animationFloatingButton;
    private List<ChatEvent> events = new ArrayList<>();

    private UserDAO userDAO = MongoDBConnect.getUserDAO();
    private ConversationDAO conversationDAO = MongoDBConnect.getConversationDAO();
    private Conversation chatAreaConversation;
    private ObjectId conversationID;
    private User currentUser = LoggedInUser.getCurrentUser();

    public void addChatEvent(ChatEvent event) {
        events.add(event);
    }

    public ChatArea() {
        init();
        initAnimator();

        addEventsAndRunnable();
    }

    private void init() {
        setOpaque(false);
        layout = new MigLayout("fill, wrap, inset 0", "[fill]", "[fill,40!][fill, 100%][shrink 0,::30%]");
        header = createHeader();
        body = createBody();
        body.setName("body");

        bottom = createBottom();
        layeredPane = createLayeredPane();
        scrollBody = createScroll();
        scrollBody.setViewportView(body);
        scrollBody.setVerticalScrollBar(new ScrollBar());
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBody.getViewport().setOpaque(false);
        scrollBody.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private int oldValues;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int value = scrollBody.getVerticalScrollBar().getValue();
                int extent = scrollBody.getVerticalScrollBar().getModel().getExtent();
                if ((value + extent) >= scrollBody.getVerticalScrollBar().getMaximum() - 300) {
                    animationFloatingButton.hide();
                } else if (oldValues <= e.getValue()) {
                    if (!animationScroll.isRunning()) {
                        animationFloatingButton.show();
                    }
                }

            }
        });
        floatingButton = createFloatingButton();
        layeredPane.setLayer(floatingButton, JLayeredPane.POPUP_LAYER);
        layeredPane.add(floatingButton, "pos 100%-50 100%,h 40,w 40");
        layeredPane.add(scrollBody);
        setLayout(layout);
        add(header);
        add(layeredPane);
        add(bottom);
    }

    private void initAnimator() {
        animationScroll = new AnimationScroll(body);
        animationFloatingButton = new AnimationFloatingButton(layoutLayered, floatingButton);
    }

    private void addEventsAndRunnable() {
        addChatEvent(new ChatEvent() {
            @Override
            public void mousePressedSendButton(ActionEvent evt) {
                if (!getText().isBlank()) {
                    Message m = new Message(currentUser.getIdAsString(), getText());

                    conversationDAO.addMessage(conversationID, m);

                    Binary avt = currentUser.getAvt();
                    ImageIcon avatarImage;

                    if (avt != null) {
                        BufferedImage bufferedImage = ImageHelper.readBinaryAsBufferedImage(avt);
                        avatarImage = ImageHelper.toRoundImage(bufferedImage, 40);
                    } else {
                        avatarImage = ImageHelper.resizing(ImageHelper.getDefaultImage(), 40, 40);
                    }

                    addChatBox(m, avatarImage, ChatBox.BoxType.RIGHT);
                    clearTextAndGrabFocus();
                }
            }

            @Override
            public void mousePressedFileButton(ActionEvent evt) {
            }

            @Override
            public void keyTyped(KeyEvent evt) {
            }
        });

        getTxtMessage().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!getText().isBlank()) {
                        Message m = new Message(currentUser.getIdAsString(), getText());

                        conversationDAO.addMessage(conversationID, m);

                        Binary avt = currentUser.getAvt();
                        ImageIcon avatarImage;

                        if (avt != null) {
                            BufferedImage bufferedImage = ImageHelper.readBinaryAsBufferedImage(avt);
                            avatarImage = ImageHelper.toRoundImage(bufferedImage, 40);
                        } else {
                            avatarImage = ImageHelper.resizing(ImageHelper.getDefaultImage(), 40, 40);
                        }

                        addChatBox(m, avatarImage, ChatBox.BoxType.RIGHT);
                        clearTextAndGrabFocus();
                    }
                }
            }
        });

        ChangeStreamIterable<Conversation> changeStream = conversationDAO.getChangeStream();

        Thread changeStreamThread = new Thread(() -> {
            changeStream.forEach(changeDocument -> {
                Conversation conversation = changeDocument.getFullDocument();

                // Tin nhắn mới có người gửi là messagingUser và người nhận là user đăng nhập
                if (conversation != null && conversation.getId().equals(conversationID)) {
                    SwingUtilities.invokeLater(() -> {
                        Message newMessage = conversation.getNewestMessage();

                        if (!newMessage.getSenderID().equalsIgnoreCase(currentUser.getIdAsString())) {
                            User messagingUser = userDAO.getByID(newMessage.getSenderID());
                            Binary avt = messagingUser.getAvt();
                            ImageIcon avatarImage;

                            if (avt != null) {
                                BufferedImage bufferedImage = ImageHelper.readBinaryAsBufferedImage(avt);
                                avatarImage = ImageHelper.toRoundImage(bufferedImage, 40);
                            } else {
                                avatarImage = ImageHelper.resizing(ImageHelper.getDefaultImage(), 40, 40);
                            }

                            addChatBox(newMessage, avatarImage, ChatBox.BoxType.LEFT);
                        }
                    });
                }
            });
        });

        changeStreamThread.start();
    }

    public void getCoversation() {

        List<Message> messageList = chatAreaConversation.getMessagesHistory();

        if (!messageList.isEmpty()) {
            ImageIcon defaultAvatar = ImageHelper.resizing(ImageHelper.getDefaultImage(), 40, 40);
            Thread thread = new Thread(() -> {
                for (Message m : messageList) {
                    String senderID = m.getSenderID();
                    boolean isCurrentUser = senderID.equalsIgnoreCase(currentUser.getIdAsString());

                    Binary avt;

                    if (isCurrentUser) {
                        avt = currentUser.getAvt();
                    } else {
                        User messagingUser = userDAO.getByID(senderID);
                        avt = messagingUser.getAvt();
                    }

                    SwingUtilities.invokeLater(() -> {
                        ImageIcon avatarImage;

                        if (avt != null) {
                            BufferedImage bufferedImage = ImageHelper.readBinaryAsBufferedImage(avt);
                            avatarImage = ImageHelper.toRoundImage(bufferedImage, 40);
                        } else {
                            avatarImage = defaultAvatar;
                        }
                        addChatBox(m, avatarImage, isCurrentUser ? ChatBox.BoxType.RIGHT : ChatBox.BoxType.LEFT);
                    });
                }
            });

            thread.start();
        } else {
            SwingUtilities.invokeLater(() -> {
                body.revalidate();
                bottom.revalidate();
            });
            body.repaint();
            body.revalidate();
            scrollBody.revalidate();
        }
    }

    public void setConversationID(String conversationID) {
        this.conversationID = new ObjectId(conversationID);

        if (chatAreaConversation == null) {
            chatAreaConversation = conversationDAO.getByID(conversationID.toString());
        }

        List<String> participants = chatAreaConversation.getParticipants();
        participants.remove(LoggedInUser.getCurrentUser().getIdAsString());

        if (participants.size() == 1) {
            User user = userDAO.getByID(participants.get(0));
            labelTitle.setText(user.getUsername());

        }

        if (participants.size() >= 2) {
            labelTitle.setText("Group Chat");
        }

        getCoversation();
    }

    private JPanel createHeader() {
        RoundPanel panel = new RoundPanel();
        panel.setLayout(new MigLayout("fill, inset 2"));
        panel.setBackground(new Color(255, 255, 255, 20));
        labelTitle = new MyLabel();
        labelTitle.setFont(labelTitle.getFont().deriveFont(14f));
        labelTitle.setBorder(new EmptyBorder(2, 10, 2, 2));
        labelTitle.setForeground(new Color(0, 0, 0));
        labelTitle.setName("labelTitle");
        panel.add(labelTitle);
        return panel;
    }

    private JPanel createBody() {
        RoundPanel panel = new RoundPanel();
        panel.setBackground(new Color(255, 255, 255, 255));
        panel.setLayout(new MigLayout("wrap,fillx"));
        return panel;
    }

    private JPanel createBottom() {
        RoundPanel panel = new RoundPanel();
        panel.setBackground(new Color(255, 255, 255, 255));
        panel.setLayout(new MigLayout("fill, inset 2", "[fill,34!]2[fill]2[fill,34!]", "[bottom][15!]"));
        GoogleMaterialIcon iconSend = new GoogleMaterialIcon(GoogleMaterialDesignIcon.SEND, GradientType.VERTICAL, new Color(0, 133, 237), new Color(90, 182, 255), 20);
        Button cmdFile = new Button();
        Button cmdSend = new Button();
        cmdFile.setFocusable(false);
        cmdSend.setFocusable(false);
        cmdSend.setIcon(iconSend.toIcon());
        textMessage = new TextField();
        textMessage.setName("textMessage");

        textMessage.setHint("Write a message here ...");
        textMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                revalidate();
            }

            @Override
            public void keyTyped(KeyEvent ke) {
                runEventKeyTyped(ke);
            }
        });
        cmdSend.addActionListener((ActionEvent e) -> {
            runEventMousePressedSendButton(e);
        });
        cmdFile.addActionListener((ActionEvent e) -> {
            runEventMousePressedFileButton(e);
        });
        JScrollPane scroll = createScroll();
        scroll.setViewportView(textMessage);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(cmdFile, "height 50!");
        panel.add(scroll);
        panel.add(cmdSend, "height 50!");
        return panel;
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layer = new JLayeredPane();
        layoutLayered = new MigLayout("fill,inset 0", "[fill]", "[fill]");
        layer.setLayout(layoutLayered);
        return layer;
    }

    private Button createFloatingButton() {
        Button button = new Button();
        button.setBorder(null);
        FontAwesomeIcon icon = new FontAwesomeIcon(FontAwesome.ANGLE_DOWN, GradientType.VERTICAL, new Color(79, 79, 79, 240), new Color(248, 248, 248, 240), 35);
        button.setIcon(icon.toIcon());
        button.setRound(40);
        button.setBackground(new Color(100, 100, 100, 100));
        button.setPaintBackground(true);
        button.addActionListener((ActionEvent e) -> {
            animationScroll.scrollVertical(scrollBody, scrollBody.getVerticalScrollBar().getMaximum());
        });
        return button;
    }

    private JScrollPane createScroll() {
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        scroll.setViewportBorder(null);
        return scroll;
    }

    public void addChatBox(Message message, ImageIcon avatar, ChatBox.BoxType type) {
        int values = scrollBody.getVerticalScrollBar().getValue();

        if (type == ChatBox.BoxType.LEFT) {
            body.add(new ChatBox(type, avatar, message), "width ::80%");
        } else {
            body.add(new ChatBox(type, avatar, message), "al right,width ::80%");
        }

        SwingUtilities.invokeLater(() -> {
            body.revalidate();
            scrollBody.getVerticalScrollBar().setValue(values);
            bottom.revalidate();
            scrollToBottom();
        });
        body.repaint();
        body.revalidate();
        scrollBody.revalidate();
    }

    public void clearChatBox() {
        body.removeAll();
        body.repaint();
        body.revalidate();
    }

    public void scrollToBottom() {
        animationScroll.scrollVertical(scrollBody, scrollBody.getVerticalScrollBar().getMaximum());
    }

    private void runEventMousePressedSendButton(ActionEvent evt) {
        for (ChatEvent event : events) {
            event.mousePressedSendButton(evt);
        }
    }

    private void runEventMousePressedFileButton(ActionEvent evt) {
        for (ChatEvent event : events) {
            event.mousePressedFileButton(evt);
        }
    }

    private void runEventKeyTyped(KeyEvent evt) {
        for (ChatEvent event : events) {
            event.keyTyped(evt);
        }
    }

    public String getText() {
        return textMessage.getText().trim();
    }

    public void setTitle(String title) {
        labelTitle.setText(title);
    }

    public void clearTextAndGrabFocus() {
        textMessage.setText("");
        textMessage.grabFocus();
    }

    public TextField getTxtMessage() {
        return textMessage;
    }

    private MigLayout layout;
    private MigLayout layoutLayered;
    private JLayeredPane layeredPane;
    private JPanel header;
    private JPanel body;
    private JPanel bottom;
    private TextField textMessage;
    private JScrollPane scrollBody;
    private Button floatingButton;
    private JLabel labelTitle;
}
