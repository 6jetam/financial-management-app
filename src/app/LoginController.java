package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private TextField nicknameField;
	@FXML
	private PasswordField passwordField;
	@FXML 
	private Button registrationBtn;
	@FXML 
	private Button cancelBtn;
	@FXML 
	private Button loginBtn;
	@FXML 
	private Label failLogIn ;
	
		static Connection connection;
		static String nameDatabase = "finmanagement";
		static String userDatabase = "root";
		static String passwordDatabase = "3306root";
		static String url = "jdbc:mysql://localhost/" + nameDatabase;
		
		private int loginAttempts = 0;
	    private final int maxAttempts = 3;
	
	
    @FXML
    private void handleLoginClick(ActionEvent event) throws SQLException, IOException {
        System.out.println("Login button clicked");
        
        boolean success = logInAccess();
        if (!success) return;
        
        // Presmerovanie na ďalšiu obrazovku
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
        Parent menuRoot = menuLoader.load();
        MenuController menuController = menuLoader.getController(); 
        
        FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("/fxml/List.fxml"));
        Parent tableRoot = tableLoader.load();
        TableController tableController = tableLoader.getController();
        
        // MenuController môže prepojiť so TableController
        menuController.setTableController(tableController);
        
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(menuRoot);
        stage.setScene(scene);
        stage.show();
    }
    

    @FXML
    private void handleCancelClick(ActionEvent event) {
        System.out.println("Cancel button clicked");
        System.exit(0);
    }
    
    @FXML private void registrationClick(ActionEvent event) throws IOException {
    		System.out.println("Registration button clicked");
    		root = FXMLLoader.load(getClass().getResource("/fxml/RegistrationForm.fxml"));
    		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    
    	private void showAlert(String message) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Alert");
    		alert.setHeaderText(null);
    		alert.setContentText(message);
    		alert.show();
    	}
    	
    	
    	private boolean logInAccess() throws SQLException {
    	  

    	    if (loginAttempts >= maxAttempts) {
    	        showAlert("Maximum number of login attempts reached. Try again later.");
    	        failLogIn.setVisible(true);
    	        return false;
    	    }

    	    String nick = nicknameField.getText();
    	    String password = passwordField.getText();
    	    String query = "SELECT user_id, passwordField, email FROM Users WHERE nickName = ?";

    	    try (
    	        Connection connection = DatabaseConnection.getDatabaseConnection();
    	        PreparedStatement preparedStatement = connection.prepareStatement(query)
    	    ) {
    	        preparedStatement.setString(1, nick);

    	        try (ResultSet result = preparedStatement.executeQuery()) {
    	            if (result.next()) {
    	                String storedHashedPassword = result.getString("passwordField");

    	                if (BCrypt.checkpw(password, storedHashedPassword)) {
    	                    // ✅ Úspešné prihlásenie
    	                    int userId = result.getInt("user_id");
    	                    String email = result.getString("email");

    	                    User loggedInUser = new User(userId, nick, email);
    	                    SessionManager.setCurrentUser(loggedInUser);

    	                    System.out.println("✅ Login successful! Welcome, " + nick);
    	                    failLogIn.setVisible(false);
    	                    return true;
    	                } else {
    	                    // ❌ Nesprávne heslo
    	                    loginAttempts++;
    	                    System.out.println("Invalid password. Attempt " + loginAttempts + " of " + maxAttempts);
    	                    showAlert("Invalid credentials. Attempt " + loginAttempts++ + " of " + maxAttempts);
    	                    failLogIn.setVisible(true);
    	                    return false;
    	                }
    	            } else {
    	            	
    	                // ❌ Neexistujúci používateľ
    	                loginAttempts++;
    	                System.out.println("Invalid username. Attempt " + loginAttempts + " of " + maxAttempts);
    	                showAlert("Invalid credentials. Attempt " + loginAttempts + " of " + maxAttempts);
    	                failLogIn.setVisible(true);
    	                return false;
    	            }
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        showAlert("An error occurred while connecting to the database.");
    	        return false;
    	    }
    	}


    	public void initialize() {
    		failLogIn.setVisible(false);
    	}
}
