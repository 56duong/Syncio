package online.syncio.view.admin;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import online.syncio.component.GlassPanePopup;
import online.syncio.component.MyLabel;
import online.syncio.component.MyNotification;
import online.syncio.component.Options;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.PostDAO;
import online.syncio.dao.UserDAO;
import online.syncio.model.Post;
import online.syncio.model.UserIDAndDate;
import online.syncio.model.UserIDAndDateAndText;
import online.syncio.utils.ImageHelper;
import online.syncio.utils.TextHelper;
import online.syncio.view.user.ErrorDetail;
import online.syncio.view.user.PostDetailUI;

/**
 * The PostUIReport class represents a panel displaying a reported post's details and options.
 * It provides functionality to display post information, handle like interactions, show images, and manage reporting options.
 */
public class PostUIReport extends javax.swing.JPanel implements Options.ReasonSelectedCallback {

    private PostDAO postDAO;
    private UserDAO userDAO;
    private String userID;
    private String postID;
    private Post post;
    private int totalLike;
    private int imageIndex = 0;
    private List<Post> listPost;

    ImageIcon liked = new ImageIcon();
    ImageIcon unliked = new ImageIcon();

    /**
     * Constructs a new PostUIReport instance to display reported post details.
     */
    public PostUIReport(String postID, String userID) {
        this.userDAO = MongoDBConnect.getUserDAO();
        this.postDAO = MongoDBConnect.getPostDAO();

        this.userID = userID;
        this.postID = postID;
        post = postDAO.getByID(postID);

        initComponents();

        try {
            liked = new ImageIcon(ImageIO.read(getClass().getResource("/online/syncio/resources/images/icons/heart-red_24px.png")));
            unliked = new ImageIcon(ImageIO.read(getClass().getResource("/online/syncio/resources/images/icons/heart-white_24px.png")));
        } catch (IOException ex) {
            String errorInfo = ex.getMessage();
            GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
        }

        showInfoPost(postID);

    }

    /**
     * Checks whether the current user has liked the post.
     */
    public boolean isLiked() {
        // Check if any documents matched the condition
        if (post.getLikeList().stream().anyMatch(entry -> entry.getUserID().equals(userID))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the like status of the post based on the current user.
     */
    public void updateLike() {
        if (isLiked()) {
            lblHeart.setIcon(unliked);
            postDAO.removeLike(postID, userID);
        } else {
            post.getLikeList().add(new UserIDAndDate(userID));
            lblHeart.setIcon(liked);
            postDAO.addLike(postID, userID);
        }

        post = postDAO.getByID(postID);
        lblTotalLike.setText(post.getLikeList().size() + " likes");
    }

    /**
     * Loads the reporting status of the post and updates the "Hide" or "Show" button accordingly.
     */
    public void loadReport() {
        if (post.getFlag() == 1) {
            btnHide.setText("Show");
        } else if (post.getFlag() == 0) {
            btnHide.setText("Hide");

            //btnHide.setToolTipText(post.getReportList());
        }
    }

    /**
     * Displays the details of the reported post.
     */
    private void showInfoPost(String postID) {
        loadReport();
        lblUsername.setText(userDAO.getByID(post.getUserID()).getUsername());
        lblUsername2.setText(userDAO.getByID(post.getUserID()).getUsername());

        lblDateCreated.setText(post.getDatePosted());

        if (!post.getCaption().equals("")) {
            txtCaption.setText("");
            TextHelper.addColoredText(txtCaption, post.getCaption());
        } else {
            jScrollPane1.setVisible(false);
        }

        lblTotalLike.setText(post.getLikeList().size() + " likes");

        if (isLiked()) {
            lblHeart.setIcon(liked);
        }

        //raito
        pnlImages.setSize(400, 400);
        if (post.getPhotoList().size() > 0) {
            if (post.getPhotoList().size() <= 1) {
                btnNext.setVisible(false);
                btnPrev.setVisible(false);
            }

            imageIndex = 0;
            pnlImages.setImg(ImageHelper.readBinaryAsBufferedImage(post.getPhotoList().get(imageIndex)));
            int imgHeight = pnlImages.getImgHeight();

            if (imgHeight > 400) {
                pnlImages.setPreferredSize(new Dimension(400, 400));
            } else if (imgHeight > 300) {
                pnlImages.setPreferredSize(new Dimension(400, 300));
            } else if (imgHeight > 200) {
                pnlImages.setPreferredSize(new Dimension(400, 200));
            } else {
                pnlImages.setPreferredSize(new Dimension(400, 100));
            }
        } else {
            pnlImages.setPreferredSize(new Dimension(0, 0));
            pnlImages.setImg("");
        }

        List<UserIDAndDateAndText> lReport = post.getReportList();
        String reports = "";
        for (UserIDAndDateAndText report : lReport) {
            reports += userDAO.getByID(report.getUserID()).getUsername() + "-" + Options.getReportReasonLabel(Integer.parseInt(report.getText()));
            reports += "<br>";
        }
        btnHide.setToolTipText("<html>" + reports + "</html>");
    }


  
    /**
     * Selects the specified image to display within the panel.
     * @param i The index of the image to be displayed.
     */
    public void selectImage(int i) {
//        Post post = postDAO.getByID(postID);
        if (i >= 0 && i < post.getPhotoList().size()) {
            imageIndex = i;
            lblCountImage.setText(imageIndex + 1 + "/" + post.getPhotoList().size());
            pnlImages.setImg(ImageHelper.readBinaryAsBufferedImage(post.getPhotoList().get(i)));
            pnlImages.revalidate();
            pnlImages.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblViewAllCmt = new javax.swing.JLabel();
        pnlImages = new online.syncio.component.MyPanel();
        btnNext = new online.syncio.component.MyButton();
        btnPrev = new online.syncio.component.MyButton();
        lblCountImage = new online.syncio.component.MyLabel();
        pnlOwner = new online.syncio.component.MyPanel();
        lblUsername = new javax.swing.JLabel();
        lblDateCreated = new javax.swing.JLabel();
        btnHide = new online.syncio.component.MyButton();
        pnlAction = new online.syncio.component.MyPanel();
        lblHeart = new online.syncio.component.MyLabel();
        lblTotalLike = new javax.swing.JLabel();
        lblComment = new online.syncio.component.MyLabel();
        lblUsername2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCaption = new online.syncio.component.MyTextPane();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        setMaximumSize(new java.awt.Dimension(400, 32767));
        setMinimumSize(new java.awt.Dimension(400, 0));

        lblViewAllCmt.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        lblViewAllCmt.setForeground(new java.awt.Color(102, 102, 102));
        lblViewAllCmt.setText("View all comments");
        lblViewAllCmt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblViewAllCmt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblViewAllCmtMousePressed(evt);
            }
        });

        pnlImages.setMaximumSize(new java.awt.Dimension(400, 400));
        pnlImages.setMinimumSize(new java.awt.Dimension(400, 400));
        pnlImages.setPreferredSize(new java.awt.Dimension(400, 400));

        btnNext.setBackground(null);
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/next_24px.png"))); // NOI18N
        btnNext.setBorderThickness(0);
        btnNext.setRadius(24);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setBackground(null);
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/previous_24px.png"))); // NOI18N
        btnPrev.setBorderThickness(0);
        btnPrev.setRadius(24);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblCountImage.setBackground(new Color(0f, 0f, 0f, 0f));
        lblCountImage.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        lblCountImage.setForeground(new java.awt.Color(219, 219, 219));
        lblCountImage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout pnlImagesLayout = new javax.swing.GroupLayout(pnlImages);
        pnlImages.setLayout(pnlImagesLayout);
        pnlImagesLayout.setHorizontalGroup(
            pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImagesLayout.createSequentialGroup()
                .addGroup(pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCountImage, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlImagesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        pnlImagesLayout.setVerticalGroup(
            pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblCountImage, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(161, 161, 161)
                .addGroup(pnlImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(184, Short.MAX_VALUE))
        );

        pnlOwner.setBackground(new java.awt.Color(255, 255, 255));
        pnlOwner.setPreferredSize(new java.awt.Dimension(0, 54));

        lblUsername.setFont(new java.awt.Font("SF Pro Display", 1, 14)); // NOI18N
        lblUsername.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/profile_24px.png"))); // NOI18N
        lblUsername.setText(" sanhvc");
        lblUsername.setToolTipText("");
        lblUsername.setMaximumSize(new java.awt.Dimension(255, 24));
        lblUsername.setMinimumSize(new java.awt.Dimension(255, 24));
        lblUsername.setPreferredSize(new java.awt.Dimension(255, 24));

        lblDateCreated.setFont(new java.awt.Font("SF Pro Display", 0, 12)); // NOI18N
        lblDateCreated.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDateCreated.setText("2022-02-02 02:02:02");
        lblDateCreated.setMaximumSize(new java.awt.Dimension(115, 15));
        lblDateCreated.setMinimumSize(new java.awt.Dimension(115, 15));
        lblDateCreated.setPreferredSize(new java.awt.Dimension(115, 15));

        btnHide.setBackground(new java.awt.Color(238, 84, 102));
        btnHide.setForeground(new java.awt.Color(255, 255, 255));
        btnHide.setText("Hide");
        btnHide.setToolTipText("ssss");
        btnHide.setBorderColor(new java.awt.Color(238, 84, 102));
        btnHide.setRadius(10);
        btnHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlOwnerLayout = new javax.swing.GroupLayout(pnlOwner);
        pnlOwner.setLayout(pnlOwnerLayout);
        pnlOwnerLayout.setHorizontalGroup(
            pnlOwnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOwnerLayout.createSequentialGroup()
                .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDateCreated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(btnHide, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlOwnerLayout.setVerticalGroup(
            pnlOwnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOwnerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlOwnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDateCreated, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHide, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pnlAction.setBackground(new java.awt.Color(255, 255, 255));

        lblHeart.setForeground(new java.awt.Color(255, 0, 0));
        lblHeart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/heart-white_24px.png"))); // NOI18N
        lblHeart.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        lblHeart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblHeart.setMaximumSize(new java.awt.Dimension(20, 49));
        lblHeart.setMinimumSize(new java.awt.Dimension(20, 49));
        lblHeart.setPreferredSize(new java.awt.Dimension(20, 49));
        lblHeart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblHeartMousePressed(evt);
            }
        });

        lblTotalLike.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        lblTotalLike.setText("0 likes");

        lblComment.setForeground(new java.awt.Color(255, 0, 0));
        lblComment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblComment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/comment_24px.png"))); // NOI18N
        lblComment.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        lblComment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblComment.setMaximumSize(new java.awt.Dimension(20, 49));
        lblComment.setMinimumSize(new java.awt.Dimension(20, 49));
        lblComment.setPreferredSize(new java.awt.Dimension(20, 49));
        lblComment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCommentMouseClicked(evt);
            }
        });

        lblUsername2.setFont(new java.awt.Font("SF Pro Display", 1, 14)); // NOI18N
        lblUsername2.setText("sanhvc");

        javax.swing.GroupLayout pnlActionLayout = new javax.swing.GroupLayout(pnlAction);
        pnlAction.setLayout(pnlActionLayout);
        pnlActionLayout.setHorizontalGroup(
            pnlActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblUsername2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlActionLayout.createSequentialGroup()
                .addComponent(lblHeart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblComment, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblTotalLike, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlActionLayout.setVerticalGroup(
            pnlActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlActionLayout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(pnlActionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHeart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblComment, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(lblTotalLike)
                .addGap(10, 10, 10)
                .addComponent(lblUsername2))
        );

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        txtCaption.setEditable(false);
        txtCaption.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 0, 3, 0));
        txtCaption.setText("Examples of Instagram Captions · Forget the filters; live your life your way!");
        txtCaption.setBorderThickness(0);
        jScrollPane1.setViewportView(txtCaption);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlImages, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
            .addComponent(pnlAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblViewAllCmt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlOwner, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlImages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblViewAllCmt)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblHeartMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHeartMousePressed
        updateLike();
    }//GEN-LAST:event_lblHeartMousePressed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        selectImage(imageIndex + 1);
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        selectImage(imageIndex - 1);
    }//GEN-LAST:event_btnPrevActionPerformed

    private void lblCommentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCommentMouseClicked
        GlassPanePopup.showPopup(new PostDetailUI(postID), "postdetail");
    }//GEN-LAST:event_lblCommentMouseClicked

    private void lblViewAllCmtMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewAllCmtMousePressed
        GlassPanePopup.showPopup(new PostDetailUI(postID), "postdetail");
    }//GEN-LAST:event_lblViewAllCmtMousePressed

    private void btnHideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideMouseClicked
        if (btnHide.getActionCommand().equals("Hide")) {
            if (postDAO.updateFlagTo1(postID)) {
                //btnHide.setText("Hidden");
                System.out.println("Hide success");
                System.out.println(btnHide.getActionCommand());
                new MyNotification((JFrame) SwingUtilities.getWindowAncestor(this), true, "The post has been hidden").setVisible(true);
                // Remove this PostUIReport from the feedPanel
                SwingUtilities.invokeLater(() -> {
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(this);
                        parent.revalidate();
                        parent.repaint();
                    }
                });
            } else {
                System.out.println("Hide fail");
            }
        } else if (btnHide.getActionCommand().equals("Show")) {
            if (postDAO.updateFlagTo0(postID)) {
                //btnHide.setText("Hidden");
                System.out.println("Show success");
                System.out.println(btnHide.getActionCommand());
                new MyNotification((JFrame) SwingUtilities.getWindowAncestor(this), true, "The post has been shown").setVisible(true);
                // Remove this PostUIReport from the feedPanel
                SwingUtilities.invokeLater(() -> {
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(this);
                        parent.revalidate();
                        parent.repaint();
                    }
                });
            } else {
                System.out.println("Show fail");
            }
        }

    }//GEN-LAST:event_btnHideMouseClicked

    @Override
    public void onReasonSelected(int reason) {
//        if (reason != -1) {
//            if (postDAO.addReport(reason, userID, postID)) {
//                //lblReport.setText("Reported");
//            }
//        }
    }

    /**
     * Returns the label that displays the comment icon.
     * @return The label that displays the comment icon.
     */
    public MyLabel getLblComment() {
        return lblComment;
    }

    /**
     * Returns the label that displays the heart (like) icon.
     * @return The label that displays the heart (like) icon.
     */
    public MyLabel getLblHeart() {
        return lblHeart;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private online.syncio.component.MyButton btnHide;
    private online.syncio.component.MyButton btnNext;
    private online.syncio.component.MyButton btnPrev;
    private javax.swing.JScrollPane jScrollPane1;
    private online.syncio.component.MyLabel lblComment;
    private online.syncio.component.MyLabel lblCountImage;
    private javax.swing.JLabel lblDateCreated;
    private online.syncio.component.MyLabel lblHeart;
    private javax.swing.JLabel lblTotalLike;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsername2;
    private javax.swing.JLabel lblViewAllCmt;
    private online.syncio.component.MyPanel pnlAction;
    private online.syncio.component.MyPanel pnlImages;
    private online.syncio.component.MyPanel pnlOwner;
    private online.syncio.component.MyTextPane txtCaption;
    // End of variables declaration//GEN-END:variables
}
