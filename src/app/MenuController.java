package app;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class MenuController {
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private ImageView houseObr;
	@FXML
	private ImageView listObr;
	@FXML
	private ImageView chatObr;
	@FXML
	private ImageView moneyObr;
	
	@FXML
	private Button financialList;
	@FXML
	private Button chat;	
	@FXML
	private Button house;
	
	@FXML
	private Label loginL;
	
	private TableController tableController;
	
	
	@FXML 
	private void buttonHouse(ActionEvent event) throws IOException {
		System.out.println("button house");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/House.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML private void list(ActionEvent event) throws IOException {
		System.out.println("button list");
		//tu som skoncil a bod cislo 2
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/List.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML private void buttonChat(ActionEvent event) throws IOException {
		System.out.println("button chat");
		//tu som skoncil a bod cislo 2
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chat.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	 public void setTableController(TableController controller) {
	        this.tableController = controller;
	    }
	 

	 public void initialize() {
		    long start = System.currentTimeMillis();
		    
		    if (SessionManager.isUserLoggedIn()) {
		        String username = SessionManager.getCurrentUser().getUsername();
		        loginL.setText("Welcome, " + username + "!"); // napr. Label v Menu.fxml
		    }

		    Platform.runLater(() -> {
		        if (houseObr != null) {
		            houseObr.setImage(new Image(getClass().getResource("/imgs/house.png").toExternalForm()));
		        }

		        if (chatObr != null) {
		            chatObr.setImage(new Image(getClass().getResource("/imgs/chat.png").toExternalForm()));
		        }

		        if (listObr != null) {
		        	listObr.setImage(new Image(getClass().getResource("/imgs/listing.jpg").toExternalForm()));
		        }
		        
		        if (moneyObr != null) {
		        	moneyObr.setImage(new Image(getClass().getResource("/imgs/MoneyBag.png").toExternalForm()));
		        }
		        

		        long end = System.currentTimeMillis();
		        System.out.println("⏱️ Načítanie obrázkov trvalo: " + (end - start) + " ms");
		    });
		}
//		 
	 }

