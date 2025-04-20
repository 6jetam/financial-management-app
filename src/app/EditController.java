package app;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditController {

	@FXML
	private ComboBox<String> categEditField;
	@FXML
	private TextField descriptEditField;
	@FXML
	private TextField priceEditField;
	@FXML
	private DatePicker dateEditField;
	@FXML
	private Button saveEditRecord;
	@FXML
	private Button cancelScene;
	
	private Record record;
	
	public void setRecord(Record record) {
		
		if(record == null ) {
			System.out.println("setRecord() bolo zavolané, ale parameter record je null!");
		}else {
			this.record = record;
			 System.out.println(" setRecord() bolo úspešne zavolané. Kategória: " + record.getCategory());

		}
		categEditField.setValue(record.getCategory());
		descriptEditField.setText(record.getDescription());
		priceEditField.setText(String.valueOf(record.getAmount()));
		dateEditField.setValue(LocalDate.parse(record.getDate()));
	}
	
	@FXML 
	private void saveChanges() {
		record.categoryProperty().setValue(categEditField.getValue());
		record.descriptionProperty().set(descriptEditField.getText());
		
		try {
			double newAmount = Double.parseDouble(priceEditField.getText());
			record.amountProperty().set(newAmount);
		}catch (NumberFormatException e) {
			System.out.println("Wrong input");
			return;
		}
		record.dateProperty().set(dateEditField.getValue().toString());
		System.out.println("uprava ulozena" + record);
		
		DatabaseManager.getInstance().updateRecord(record);
		System.out.println("Uprava ulozena" + record);
		closeEditDialog();
	}
	
	@FXML
	private void closeEditDialog() {
	    Stage stage = (Stage) categEditField.getScene().getWindow();
	    stage.close();
	}
	
	
}
