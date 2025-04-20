package app;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChatController {
	
	@FXML 
	private TextArea responseArea;
	@FXML 
	private TextField userInput;
	@FXML 
	private Button send;
	
	@FXML 
	private Label weatherLabel;
	
	@FXML 
	private Button back;
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	
	private static final String WEATHER_API_KEY = "fa5d1abc49cea268349b70b44a3ee799";
	
	private ChatGPTuser chatGPTuser;
	
	public ChatController() {
		try {
			chatGPTuser = new ChatGPTuser();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML 
	private void handleSend() {
		
		System.out.println("send Question");
		String userQuestion = userInput.getText();
		
		if(userQuestion.isEmpty()) {
			return;
		}
		
		try {
			String response = ChatGPTuser.sendMessage(userQuestion);
			responseArea.appendText("\n──────────────────────────────────\n");
			responseArea.appendText("**You:** " + userQuestion + "\n\n" );
			responseArea.appendText("**AI:** " + response + "\n");
			responseArea.appendText("\n──────────────────────────────────\n");
		}catch(IOException e) {
			responseArea.appendText("Error: AI is not available right now");
			e.printStackTrace();
		}
		
		userInput.clear();
	}
	
	private void fetchWeather(String city) {
    	String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + WEATHER_API_KEY;
    	
    	try {
    		String response = getApiCon(urlString);
    		if(response!= null ) {
    			JSONObject json = new JSONObject (response);
    			
    			double temp = json.getJSONObject("main").getDouble("temp");
    			int humidity = json.getJSONObject("main").getInt("humidity");
    			double speed = json.getJSONObject("wind").getDouble("speed");
    			String descript = json.getJSONArray("weather").getJSONObject(0).getString("description");
    			weatherLabel.setText(city + ": " + temp + "°C, " + "humidity:" + humidity + ","+ " wind: " + speed + ", " + descript);
    		}
    	}catch (Exception e) {
    		weatherLabel.setText("Pocasie sa nenacitalo");
    		e.printStackTrace();
    	}
    	
   }
	
	private String getApiCon(String urlString) throws IOException {
		   URL url = new URL(urlString);
		   HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		   conn.setRequestMethod("GET");
		   conn.setRequestProperty("Application", "application/json");
		   
		   if(conn.getResponseCode() != 200) { 
			   return null; 
		   }
		   
		   Scanner scanner = new Scanner(url.openStream());
		   StringBuilder response = new StringBuilder();
		   	while(scanner.hasNext()) {
		   		response.append(scanner.nextLine());
		   	}
		   	scanner.close();
		   	return response.toString();
	   }
	
	 @FXML 
	private void buttonBack(ActionEvent event) throws IOException {
				System.out.println("button back");
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
				root = loader.load();
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
	}
	 
	 public void initialize() {
		 fetchWeather("Presov");
	 }
}
