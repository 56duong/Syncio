package online.syncio.controller.user;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.FindIterable;
import javax.swing.SwingUtilities;
import online.syncio.component.GlassPanePopup;
import online.syncio.dao.MongoDBConnect;
import online.syncio.dao.PostDAO;
import online.syncio.model.LoggedInUser;
import online.syncio.model.Post;
import online.syncio.model.User;
import online.syncio.resources.images.other.MayULike;
import online.syncio.view.user.ErrorDetail;
import online.syncio.view.user.Home;
import online.syncio.view.user.Main;
import online.syncio.view.user.PostUI;

/**
 * Controller class for managing the home feed.
 */
public class HomeController {

    public Home pnlHome;

    private Main main = Main.getInstance();
    private User currentUser;
    private String currentUserID;

    private PostDAO postDAO = MongoDBConnect.getPostDAO();

    FindIterable<Post> posts;
    FindIterable<Post> postsOther;

    /**
    * Constructs a new instance of the HomeController class with the provided Home panel.
    *
    * @param pnlHome The Home panel associated with this controller.
    */
    public HomeController(Home pnlHome) {
        this.pnlHome = pnlHome;
    }

    /**
     * Checks the logged-in user's session and loads posts.
     */
    public void recheckLoggedInUser() {
        if (LoggedInUser.isUser()) {
            currentUser = LoggedInUser.getCurrentUser();
            currentUserID = currentUser.getId().toString();
            posts = postDAO.getAllPostOfFollowers(currentUser);

            loadMorePosts();
        } else {
            loadMorePosts();
            System.out.println("chưa đăng nhập");
        }

        // tỉ lệ khoảng cách dịch chuyển khi lăn chuột
        pnlHome.getScrollPane().getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Checks if the search panel is visible.
     *
     * @return True if the search panel is visible, false otherwise.
     */
    public boolean isSearchPanelVisible() {
        return main.getPnlSearch().isVisible() || main.getPnlNotifications().isVisible();
    }

    /**
     * Loads more posts asynchronously.
     */
    private void loadMorePosts() {
        // Create a thread for loading and displaying posts
        Thread thread = new Thread(() -> {
            if (LoggedInUser.getCurrentUser() != null) {
                int postsLoaded = 0;

                //CHANGE
                // Set up MongoDB change stream
                ChangeStreamIterable<Post> changeStream = postDAO.getChangeStream();

                // Listen for change stream events in a separate thread
                Thread changeStreamThread = new Thread(() -> {
                    changeStream.forEach(changeDocument -> {
                        // Handle the change event here
                        // For example, extract the new post data from changeDocument and update your feed UI
                        Post newPost = changeDocument.getFullDocument();
                        //catch on add
                        if (newPost != null && postDAO.getByID(newPost.getId().toString()) == null) {
                            SwingUtilities.invokeLater(() -> {
                                PostUI postUI = new PostUI(newPost.getId().toString(), newPost.getUserID());
                                addPostUI(postUI);
                            });
                        }
                    });
                });
                changeStreamThread.start();

                //REGULAR
                // Load initial posts from the regular database query
                for (Post post : posts) {
                    // Check if pnlSearch is visible before adding PostUI components
                    while (isSearchPanelVisible()) {
                        try {
                            Thread.sleep(40); // Wait for 100 milliseconds before checking again
                        } catch (InterruptedException e) {
                            String errorInfo = e.getMessage();
                            GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                        }
                    }

                    PostUI postUI = new PostUI(post.getId().toString(), post.getUserID());
                    SwingUtilities.invokeLater(() -> {
                        addPostUI(postUI);
                    });

                    postsLoaded++;
                    if (postsLoaded >= 5) {
                        // Introduce a 3-second delay after loading 5 posts
                        try {
                            Thread.sleep(3000); // 3000 milliseconds = 2 seconds
                        } catch (InterruptedException e) {
                            String errorInfo = e.getMessage();
                            GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                        }

                        postsLoaded = 0; // Reset the counter
                    }
                }
                
                
                //may you like
                pnlHome.removeLoading();
                pnlHome.getFeedPanel().add(new MayULike());
                pnlHome.addLoading();
                postsOther = postDAO.getAllPostOther(currentUser);

                // Load initial posts from the regular database query
                for (Post post : postsOther) {
                    // Check if pnlSearch is visible before adding PostUI components
                    while (isSearchPanelVisible()) {
                        try {
                            Thread.sleep(50); // Wait for 100 milliseconds before checking again
                        } catch (InterruptedException e) {
                            String errorInfo = e.getMessage();
                            GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                        }
                    }

                    PostUI postUI = new PostUI(post.getId().toString(), post.getUserID());
                    SwingUtilities.invokeLater(() -> {
                        addPostUI(postUI);
                    });

                    postsLoaded++;
                    if (postsLoaded >= 5) {
                        // Introduce a 3-second delay after loading 5 posts
                        try {
                            Thread.sleep(3000); // 3000 milliseconds = 2 seconds
                        } catch (InterruptedException e) {
                            String errorInfo = e.getMessage();
                            GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                        }

                        postsLoaded = 0; // Reset the counter
                    }
                }


                // Wait for the change stream thread to finish (you can use other synchronization mechanisms if needed)
                try {
                    changeStreamThread.join();
                } catch (InterruptedException e) {
                    String errorInfo = e.getMessage();
                    GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                }
            }

            //may you like
            pnlHome.removeLoading();
            pnlHome.getFeedPanel().add(new MayULike());
            pnlHome.addLoading();
            postsOther = postDAO.getAllPostOther(currentUser);
            int postsLoaded = 0;

            // Load initial posts from the regular database query
            for (Post post : postsOther) {
                // Check if pnlSearch is visible before adding PostUI components
                while (isSearchPanelVisible()) {
                    try {
                        Thread.sleep(50); // Wait for 100 milliseconds before checking again
                    } catch (InterruptedException e) {
                        String errorInfo = e.getMessage();
                        GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                    }
                }

                PostUI postUI = new PostUI(post.getId().toString(), post.getUserID());
                SwingUtilities.invokeLater(() -> {
                    addPostUI(postUI);
                });

                postsLoaded++;
                if (postsLoaded >= 5) {
                    // Introduce a 3-second delay after loading 5 posts
                    try {
                        Thread.sleep(3000); // 3000 milliseconds = 2 seconds
                    } catch (InterruptedException e) {
                        String errorInfo = e.getMessage();
                        GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                    }

                    postsLoaded = 0; // Reset the counter
                }
            }
        });

        // Start the thread
        thread.start();
    }

    /**
     * Adds a PostUI component to the feed panel.
     *
     * @param postUI The PostUI component to add.
     */
    private void addPostUI(PostUI postUI) {
        pnlHome.removeLoading();
        pnlHome.getFeedPanel().add(postUI);
        pnlHome.addLoading();
        pnlHome.getFeedPanel().revalidate();
        pnlHome.getFeedPanel().repaint();
    }
}
