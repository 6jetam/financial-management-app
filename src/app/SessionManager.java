package app;

public class SessionManager {
	
	    private static User currentUser = null;

	    public static void setCurrentUser(User user) {
	        currentUser = user;
	    }

	    public static User getCurrentUser() {
	        return currentUser;
	    }

	    public static boolean isUserLoggedIn() {
	        return currentUser != null;
	    }

	    public static void logout() {
	        currentUser = null;
	    }
	

}
