package online.syncio.view.user;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextField;
import online.syncio.component.GlassPanePopup;
import online.syncio.component.MyDialog;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.LoggedInUser;
import online.syncio.model.User;
import online.syncio.utils.ImageHelper;
import online.syncio.utils.Validator;
import online.syncio.view.login.Login;

/**
 * A JPanel class representing the user interface for editing the user profile.
 */
public final class EditProfile extends JPanel {

    private User currentUser = LoggedInUser.getCurrentUser();
    private UserDAO userDAO = MongoDBConnect.getUserDAO();

    /**
     * Constructs an `EditProfile` panel. Initializes components and loads user data if a user is logged in.
     */
    public EditProfile() {
        initComponents();
        setBackground(new Color(0f, 0f, 0f, 0f));
        if(LoggedInUser.getCurrentUser() != null) loadUserData();
    }

    /**
     * Loads the user data into the corresponding UI components.
     */
    public void loadUserData() {
        // Set the account label and user-related text fields
        lblAccount.setText(currentUser.getUsername());
        txtUsername.setText(currentUser.getUsername());
        txtPassword.setText(currentUser.getPassword());
        txtEmail.setText(currentUser.getEmail());

        // Set the user's avatar to the account label
        ImageHelper.setAvatarToLabel(currentUser, lblAccount, 24);

        try {
            txtBio.setText(currentUser.getBio());
        } catch (NullPointerException e) {
            txtBio.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new online.syncio.component.MyPanel();
        lblSepratorLine = new javax.swing.JLabel();
        lblAccount = new online.syncio.component.MyLabel();
        lblTitle = new online.syncio.component.MyLabel();
        lblSepratorLine1 = new javax.swing.JLabel();
        lblUsername = new online.syncio.component.MyLabel();
        txtUsername = new online.syncio.component.MyTextField();
        lblChangeUsername = new online.syncio.component.MyLabel();
        lblPassword = new online.syncio.component.MyLabel();
        lblEmail = new online.syncio.component.MyLabel();
        txtEmail = new online.syncio.component.MyTextField();
        lblBio = new online.syncio.component.MyLabel();
        lblChangeBio = new online.syncio.component.MyLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBio = new online.syncio.component.MyTextArea();
        txtPassword = new online.syncio.component.MyPasswordField();
        lblPasswordQuestion = new online.syncio.component.MyLabel();
        lblEmailQuestion = new online.syncio.component.MyLabel();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlMain.setRoundBottomRight(20);

        lblSepratorLine.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 228), 2));
        lblSepratorLine.setPreferredSize(new java.awt.Dimension(2, 1));

        lblAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/profile_24px.png"))); // NOI18N
        lblAccount.setText(" 56duong");
        lblAccount.setMaximumSize(new java.awt.Dimension(57, 54));
        lblAccount.setMinimumSize(new java.awt.Dimension(57, 54));
        lblAccount.setPreferredSize(new java.awt.Dimension(57, 54));

        lblTitle.setText("Edit Profile");

        lblSepratorLine1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 228), 2));
        lblSepratorLine1.setPreferredSize(new java.awt.Dimension(2, 1));

        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsername.setText("Username");

        txtUsername.setBackground(new java.awt.Color(239, 239, 239));
        txtUsername.setText("duong_user");
        txtUsername.setBorderColor(new java.awt.Color(239, 239, 239));
        txtUsername.setName("txtUsername"); // NOI18N

        lblChangeUsername.setForeground(new java.awt.Color(0, 149, 246));
        lblChangeUsername.setText("Change");
        lblChangeUsername.setFontBold(2);
        lblChangeUsername.setName("lblChangeUsername"); // NOI18N
        lblChangeUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblChangeUsernameMousePressed(evt);
            }
        });

        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPassword.setText("Password");

        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("Email");

        txtEmail.setEditable(false);
        txtEmail.setText("duongcontact@gmail.com");
        txtEmail.setBorderColor(new java.awt.Color(239, 239, 239));
        txtEmail.setEnabled(false);
        txtEmail.setName("txtEmail"); // NOI18N

        lblBio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBio.setText("Bio");

        lblChangeBio.setForeground(new java.awt.Color(0, 149, 246));
        lblChangeBio.setText("Change");
        lblChangeBio.setFontBold(2);
        lblChangeBio.setName("lblChangeBio"); // NOI18N
        lblChangeBio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblChangeBioMousePressed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        txtBio.setBackground(new java.awt.Color(239, 239, 239));
        txtBio.setColumns(20);
        txtBio.setRows(4);
        txtBio.setText("fancy bio\n");
        txtBio.setBorderColor(new java.awt.Color(239, 239, 239));
        txtBio.setName("txtBio"); // NOI18N
        jScrollPane1.setViewportView(txtBio);

        txtPassword.setEditable(false);
        txtPassword.setText("matKhauSieuCapVjpPr0");
        txtPassword.setBorderColor(new java.awt.Color(239, 239, 239));
        txtPassword.setEnabled(false);
        txtPassword.setName("txtPassword"); // NOI18N

        lblPasswordQuestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/question_16px.png"))); // NOI18N
        lblPasswordQuestion.setToolTipText("Email cannot be updated");

        lblEmailQuestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online/syncio/resources/images/icons/question_16px.png"))); // NOI18N
        lblEmailQuestion.setToolTipText("Use \"Forgot Password\" for password updates");

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSepratorLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(803, Short.MAX_VALUE))
            .addComponent(lblSepratorLine1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(303, 303, 303)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChangeUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChangeBio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblPasswordQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblEmailQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(315, 315, 315))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSepratorLine, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(lblSepratorLine1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChangeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblPasswordQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblEmailQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblBio, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblChangeBio, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(277, Short.MAX_VALUE))
        );

        add(pnlMain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lblChangeUsernameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeUsernameMousePressed
        //validate
        String username = txtUsername.getText().trim();
        String errors = Validator.allowNumberTextUnderline((JTextField) txtUsername, "Username", username, false, "Username");
        if (!errors.equals("")) {
            GlassPanePopup.showPopup(new MyDialog("Error", errors), "dialog");
        } else {
            //update
            //check username
            if (userDAO.checkUsername(username)) {
                GlassPanePopup.showPopup(new MyDialog("Username Already Taken", "The username you've chosen is already taken.\nPlease select a different username."), "dialog");
                return;
            }
            
            int result = userDAO.updateUsernameByEmail(username, txtEmail.getText());
            if (result <= 0) {
                GlassPanePopup.showPopup(new MyDialog("Error", "An error occurs when updating your Username"), "dialog");
            } else {
                GlassPanePopup.showPopup(new MyDialog("Update Successful", "Updated successfully. Reopen the app to see the change."), "dialog");
            }
        }
    }//GEN-LAST:event_lblChangeUsernameMousePressed

    private void lblChangeBioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeBioMousePressed
        //validate
        String bio = txtBio.getText().trim();

        //update
        int result = userDAO.updateBioByEmail(bio, txtEmail.getText());
        if (result <= 0) {
            GlassPanePopup.showPopup(new MyDialog("Error", "An error occurs when updating your Bio"), "dialog");
        } else {
            GlassPanePopup.showPopup(new MyDialog("Update Successful", "Your Bio has been updated successfully."), "dialog");
        }
    }//GEN-LAST:event_lblChangeBioMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private online.syncio.component.MyLabel lblAccount;
    private online.syncio.component.MyLabel lblBio;
    private online.syncio.component.MyLabel lblChangeBio;
    private online.syncio.component.MyLabel lblChangeUsername;
    private online.syncio.component.MyLabel lblEmail;
    private online.syncio.component.MyLabel lblEmailQuestion;
    private online.syncio.component.MyLabel lblPassword;
    private online.syncio.component.MyLabel lblPasswordQuestion;
    private javax.swing.JLabel lblSepratorLine;
    private javax.swing.JLabel lblSepratorLine1;
    private online.syncio.component.MyLabel lblTitle;
    private online.syncio.component.MyLabel lblUsername;
    private online.syncio.component.MyPanel pnlMain;
    private online.syncio.component.MyTextArea txtBio;
    private online.syncio.component.MyTextField txtEmail;
    private online.syncio.component.MyPasswordField txtPassword;
    private online.syncio.component.MyTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
