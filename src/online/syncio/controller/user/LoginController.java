package online.syncio.controller.user;

import java.util.HashSet;
import java.util.Set;
import online.syncio.component.GlassPanePopup;
import online.syncio.component.MyDialog;
import online.syncio.component.MyPasswordField;
import online.syncio.component.MyTextField;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.LoggedInUser;
import online.syncio.model.User;
import online.syncio.utils.OtherHelper;
import online.syncio.view.login.Login;
import online.syncio.view.user.Main;
import online.syncio.view.admin.MainAdmin;

public class LoginController {

    private Login login;
    private UserDAO userDAO;

    public LoginController(Login login) {
        this.login = login;
        MongoDBConnect.connect();
        this.userDAO = MongoDBConnect.getUserDAO();
    }

    public void loginAuthentication() {
        MyTextField txtUser = login.getTxtUser();
        MyPasswordField txtPassword = login.getTxtPassword();

        String username = txtUser.getText();
        String password = new String(txtPassword.getPassword());

        Set<String> setError = new HashSet<>();

        //txtUser
        if (username.isEmpty() || username.equalsIgnoreCase("username")) {
            setError.add("Please enter username");
            txtUser.requestFocus();
        } else if (!username.matches("[a-zA-Z0-9_]+")) {
            setError.add("Username should only allow letters, digits and underscores (a-zA-Z0-9_).");
            txtUser.requestFocus();
        }

        //txtPassword
        if (password.isEmpty() || password.equalsIgnoreCase("password")) {
            setError.add("Please enter password");
            txtPassword.requestFocus();
        } else if (!password.matches("[a-zA-Z0-9]+")) {
            setError.add("Password should only allow letters and digits (a-zA-Z0-9).");
            txtPassword.requestFocus();
        }

        if (setError.isEmpty()) {
            User user = userDAO.authentication(username, password);
            if (user == null) {
                GlassPanePopup.showPopup(new MyDialog("Incorrect Username or Password", "The username or password you entered is incorrect.\nPlease double-check and try again."), "dialog");
                return;
            }

            if (user.getFlag() == 1) {
                GlassPanePopup.showPopup(new MyDialog("Account Unavailable", "We're sorry, but your account is currently unavailable.\nPlease try again later or contact support for assistance."), "dialog");
            } else {
                LoggedInUser.setCurrentUser(user); //set loggedin user

                // check role
                if (LoggedInUser.isAdmin()) {
                    new MainAdmin().setVisible(true);
                    login.dispose();
                }
                else {
                    if(login.getChkRememberMe().isSelected()) {
                        OtherHelper.saveSessionValue("LOGGED_IN_USER", user.getId().toString());
                    }
                    else {
                        OtherHelper.deleteSessionValue("LOGGED_IN_USER");
                    }
                    new Main().setVisible(true);
                    login.dispose();
                }
            }
        } else {
            //neu co loi => hien thi loi
            String errors = "";
            for (String error : setError) {
                errors += error + "<br>";
            }
            GlassPanePopup.showPopup(new MyDialog("Error", errors), "dialog");
        }
    }
}