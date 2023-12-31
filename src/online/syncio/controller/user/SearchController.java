package online.syncio.controller.user;

import com.mongodb.client.FindIterable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import online.syncio.component.SearchedCard;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.UserDAO;
import online.syncio.model.User;
import online.syncio.view.user.Main;
import online.syncio.view.user.SearchUserPanel;

/**
 * Controller class for managing user search functionality.
 */
public class SearchController {

    private SearchUserPanel pnlSearch;

    private Main main = Main.getInstance();
    private UserDAO userDAO = MongoDBConnect.getUserDAO();
    private FindIterable<User> userList;

    /**
     * Constructs a SearchController with the provided SearchUserPanel.
     *
     * @param pnlSearch The SearchUserPanel instance.
     */
    public SearchController(SearchUserPanel pnlSearch) {
        this.pnlSearch = pnlSearch;
    }

    /**
     * Performs a search based on the entered search text.
     */
    public void find() {
        String searchText = this.pnlSearch.getTxtSearch().getText().trim();

        if (searchText.equalsIgnoreCase("Looking for someone?") || searchText.isBlank()) {
            searchText = null;
        }

        userList = userDAO.getAllByUsernameOrEmailRoleFlag(false, searchText, 0, 0);

        if (userList != null) {
            loadResult();
        }
    }

    /**
     * Loads search results and displays them as cards in the result panel.
     */
    private void loadResult() {
        this.pnlSearch.getPnlResult().removeAll();
        Box.createVerticalStrut(20);

        MouseListener mouseEvent = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                SearchedCard card = (SearchedCard) e.getSource();

                main.profile.getController().loadProfile(card.getUser());
                main.showTab("profile");
                main.getBtnSearch().doClick();
            }
        };

        for (User user : userList) {
            SearchedCard card = new SearchedCard(user);
            card.addMouseListener(mouseEvent);

            this.pnlSearch.getPnlResult().add(card);
            Box.createVerticalStrut(20);

            this.pnlSearch.getPnlResult().revalidate();
            this.pnlSearch.getPnlResult().repaint();
        }
    }
}
