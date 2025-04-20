package app;

import java.io.IOException;

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

public class RegistrationController {
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private TextField registrationFirstNameField;
	@FXML
	private TextField registrationNicknameField;
	@FXML
	private TextField registrationEmailField;
	@FXML
	private PasswordField registrationPasswordField;
	@FXML
	private PasswordField registrationConfirmPasswordField;
	@FXML 
	private Button registrationConfirmBtn;
	@FXML 
	private Button registrationCloseBtn;
	@FXML 
	private Label registrationFailLogIn;
	
	@FXML
	private void registrationClickonRegister(ActionEvent event) throws IOException {
	    System.out.println("Register button clicked");

	    boolean success = getDataFromUser();
	    if (!success) {
	        return;
	    }
	    
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
	    Parent root = loader.load();

	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	    
	    System.out.println("Prechod na login");
	}


	@FXML private void registrationClickonCancel(ActionEvent event) throws IOException {
		System.out.println("Cancel button clicked");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
	    Parent root = loader.load();

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
	    alert.showAndWait();
	}
	
	private boolean getDataFromUser() {
	    String firstName = registrationFirstNameField.getText();
	    String nickname = registrationNicknameField.getText();
	    String email = registrationEmailField.getText();
	    String password = registrationPasswordField.getText();
	    String confirmPassword = registrationConfirmPasswordField.getText();

	    if (!password.equals(confirmPassword)) {
	        showAlert("Passwords do not match!");
	        return false;
	    }

	    if (firstName.isEmpty() || nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
	        showAlert("Please, fill in all the fields");
	        return false;
	    }

	    if (!email.contains("@")) {
	        showAlert("Invalid email format! Email must contain '@'.");
	        return false;
	    }

	   
	    boolean registered = RegisterData.registerUserRegistration(firstName, nickname, email, password);
	   

	    if (!registered) {
	        if (RegisterData.userExists(nickname, email)) {
	            showAlert("User with this nickname or email already exists!");
	        } else {
	            showAlert("Error occurred during registration.");
	        }
	        return false;
	    }
	    showAlert("Registration was succesfull");
	    return true;
	}



}
	

		

