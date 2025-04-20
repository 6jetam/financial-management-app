package app;

import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinancieMain extends Application {	
	
	@Override
    public void start(Stage primaryStage) throws Exception {
        // Načtení FXML souboru
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
        Parent root = loader.load();

        // Nastavení scény
        Scene scene = new Scene(root);
        primaryStage.setTitle("Financial Management ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	


    public static void main(String[] args) {
        launch(args);
        
        try (Connection conn = DatabaseConnection.getDatabaseConnection()) {
            System.out.println("✅ Pripojenie úspešné!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

