package online.syncio.view.user;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import javax.swing.JPanel;
import online.syncio.component.MyPanel;
import online.syncio.component.MyScrollPane;
import online.syncio.controller.user.HomeController;

/**
 * Represents a user interface for the home page of the application.
 * This class provides functionality for displaying user feeds and managing UI components.
 */
public class Home extends JPanel {

    private Main main = Main.getInstance();

    private HomeController controller;

    
    /**
     * Initializes the Home UI components and sets up event listeners.
     * Also, rechecks the logged-in user and adjusts UI elements accordingly.
     */
    public Home() {
        initComponents();
        setBackground(new Color(0f, 0f, 0f, 0f));

        controller = new HomeController(this);

        controller.recheckLoggedInUser();

        // Add an AdjustmentListener to the vertical scrollbar of the scroll pane
        scrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            // Update the position of pnlSearch relative to the pnlHome container
            main.getPnlSearch().setBounds(main.getPnlSearch().getX(), getY(), main.getPnlSearch().getWidth(), main.getPnlSearch().getHeight());
            main.getPnlSearch().revalidate();
            main.getPnlSearch().repaint();

            // Update the position of pnlSearch relative to the pnlHome container
            main.getPnlNotifications().setBounds(main.getPnlNotifications().getX(), getY(), main.getPnlNotifications().getWidth(), main.getPnlNotifications().getHeight());
            main.getPnlNotifications().revalidate();
            main.getPnlNotifications().repaint();
        });

    }

    /**
     * Adds a loading label to the feed panel.
     */
    public void addLoading() {
        feedPanel.add(lblLoading);
    }

    /**
     * Removes the loading label from the feed panel.
     */
    public void removeLoading() {
        lblLoading.setText("");
        feedPanel.remove(lblLoading);
    }

    /**
     * Returns the feed panel.
     *
     * @return The MyPanel instance representing the feed panel.
     */
    public MyPanel getFeedPanel() {
        return feedPanel;
    }

    /**
     * Returns the scroll pane used for scrolling through the feed.
     *
     * @return The MyScrollPane instance representing the scroll pane.
     */
    public MyScrollPane getScrollPane() {
        return scrollPane;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new online.syncio.component.MyPanel();
        scrollPane = new online.syncio.component.MyScrollPane();
        feedPanel = new online.syncio.component.MyPanel();
        lblLoading = new online.syncio.component.MyLabel();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setRoundBottomRight(20);
        pnlMain.setLayout(new java.awt.BorderLayout());

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        feedPanel.setBackground(new java.awt.Color(255, 255, 255));
        feedPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 340, 0, 340));
        feedPanel.setMaximumSize(new java.awt.Dimension(1080, 679));
        feedPanel.setMinimumSize(new java.awt.Dimension(1080, 679));
        feedPanel.setRoundBottomRight(20);
        feedPanel.setLayout(new javax.swing.BoxLayout(feedPanel, javax.swing.BoxLayout.Y_AXIS));

        lblLoading.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblLoading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/loading.gif"))); // NOI18N
        lblLoading.setText("It may take some time for loading the first post...");
        lblLoading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLoading.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        feedPanel.add(lblLoading);

        scrollPane.setViewportView(feedPanel);

        pnlMain.add(scrollPane, java.awt.BorderLayout.CENTER);

        add(pnlMain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private online.syncio.component.MyPanel feedPanel;
    private online.syncio.component.MyLabel lblLoading;
    private online.syncio.component.MyPanel pnlMain;
    private online.syncio.component.MyScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
