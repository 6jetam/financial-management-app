	package app;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormController {
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	
	@FXML
	private ComboBox<String> categoryField;
	@FXML
	private ComboBox<String> incExpField;
	@FXML
	private TextField descriptionField;
	@FXML
	private TextField priceField;
	@FXML
	private DatePicker dateField;
	@FXML
	private Button addRecord;
	@FXML
	private Button backScene;
	
	private TableController tableController;

	
	  public void setTableController(TableController controller) {
	        this.tableController = controller; // Nastavenie referencie na TableController
	    }
	 
	public void initialize () {
		System.out.println("Form Controller initialized.");
		
		incExpField.setItems(FXCollections.observableArrayList("Income", "Expense"));
//		IncExpField.getSelectionModel().selectFirst();
		
		
		incExpField.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
			if(newValue != null) {
				updateCategoryOpt (newValue);
			}
		});
		
	}
	
	public void updateCategoryOpt(String type) {
		if (type.equals("Income")) {
			categoryField.setItems(FXCollections.observableArrayList("Salary", "Freelance Work", "Investments", "Rental Income", 
					"Gifts", "Other Income" ));
			
		}else if (type.equals("Expense")) {
			categoryField.setItems(FXCollections.observableArrayList("Housing","Utilities & Services","Food",
					"Transport","Leisure","Financial Commitments", "Unexpected Expenses", "Other" ));
		}
	}
	 
	
	public void addRecord() {
		System.out.println("addRecord");
		
		
		 String kind = incExpField.getValue();
		 String category = categoryField.getValue();
	     String description = descriptionField.getText();
	     double price = Double.parseDouble(priceField.getText());
	     String date = dateField.getValue().toString();
	     
	     
	     System.out.println("Pridávanie záznamu:");
	     System.out.println("Kind: "+ kind);
	     System.out.println("Kategória: " + category);
	     System.out.println("Popis: " + description);
	     System.out.println("Cena: " + price);
	     System.out.println("Dátum: " + date);
	     
	 
	     Record record = new Record(0,kind,category, description, price, date);
	     RecordManager.getInstance().addRecord(record);
	     DatabaseManager.getInstance().addRecordDab(record);
	     
	     System.out.println("Pridávanie záznamu do tabuľky: " + record);
	     System.out.println("Počet záznamov v tabuľke: " + RecordManager.getInstance().getRecords().size());
	     
	     tableController.refreshTable();
	     
	     // Zatvoríme dialóg
	     Stage stage = (Stage) categoryField.getScene().getWindow();
	     stage.close();
	   
	}
	
	    
	@FXML
	public void backBtnFromDialog(ActionEvent event) {
	    System.out.println("Back button clicked, closing dialog.");

	    // Získaj aktuálne okno a zatvor ho (iba dialóg, nie hlavnú scénu)
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.close();
	    
	}
}
