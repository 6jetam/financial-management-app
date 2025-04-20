	package app;
	
	import java.io.IOException;
	import java.net.URL;
	import java.net.http.HttpClient;
	import java.util.Optional;
	import java.util.Scanner;
	
	import javax.net.ssl.HttpsURLConnection;
	
	import org.json.JSONObject;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Node;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Button;
	import javafx.scene.control.ButtonType;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.Label;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableRow;
	import javafx.scene.control.TableView;
	import javafx.stage.Stage;
	
	
	public class TableController {
		
		 @FXML
		 private Stage stage;
		 @FXML
		 private Scene scene;
		 @FXML
		 private Parent root;
		 
		 @FXML 
		 private TableColumn<Record, Integer> idColumn;
		 @FXML 
		 private TableColumn<Record, String> kindColumn;
		 @FXML 
		 private TableColumn<Record, String> categoryColumn;
		 @FXML 
		 private TableColumn<Record, String> descriptionColumn;
		 @FXML 
		 private TableColumn<Record, Number> amountColumn;
		 @FXML 
		 private TableColumn<Record, String> dateColumn;
		 @FXML 
		 private TableView<Record> tableData;
		 @FXML 
		 private Label weatherLabel;
		 @FXML 
		 private Label login;
		 
		 private static final String WEATHER_API_KEY = ConfigLoader.getProperty("WEATHER_API_KEY");
		 
		 
		 @FXML 
		 private Button addRecord;
		 @FXML 
		 private Button editRecord;
		 @FXML 
		 private Button deleteBtnRecord;
		 @FXML 
		 private Button back;
		 @FXML 
		 private ComboBox<String>  sortOptions;
		 
		 
		 @FXML
		 private TableController tableController;
		
	    private ObservableList<Record> recordList = FXCollections.observableArrayList();
	    
	    public void initialize() {
	    	
	    	System.out.println("Inicializácia tabuľky...");
	    	
	    	fetchWeather("Presov");
	    	 
	    	setupRowColoring();
	    	
	    	if(SessionManager.isUserLoggedIn()) {
	    		System.out.println("login user" + SessionManager.getCurrentUser().getUsername());
	    		tableData.setItems(DatabaseManager.getInstance().getAllRecords());
	    		
	    	}else {
	    		System.out.println("noone is on ");
	    	}
	     
	    	// Správne nastavenie stĺpcov (už sú definované v FXML, len priradíme hodnoty)
	    	
	        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
	        kindColumn.setCellValueFactory(cellData -> cellData.getValue().kindProperty());
	        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
	        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
	        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
	        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty()); 
	        
	        sortOptions.setItems(FXCollections.observableArrayList(
	        	    "Amount",
	        	    "Date",
	        	    "Category",
	        	    "Kind"
	        	));
	        	sortOptions.getSelectionModel().selectFirst(); 

	        
	     System.out.println("Tabuľka inicializovaná. Počet záznamov: " + recordList.size());
	     
	     if (SessionManager.isUserLoggedIn()) {
	         String username = SessionManager.getCurrentUser().getUsername();
	         login.setText("Login: " + username );
	     } else {
	         login.setText("Noone");
	     }
	     
	    }@FXML
	    private void Summary(ActionEvent event) {
	        ObservableList<Record> records = tableData.getItems();

	        double totalIncome = 0;
	        double totalExpense = 0;

	        for (Record r : records) {
	            if (r.getKind().equalsIgnoreCase("Income")) {
	                totalIncome += r.getAmount();
	            } else if (r.getKind().equalsIgnoreCase("Expense")) {
	                totalExpense += r.getAmount();
	            }
	        }

	        double balance = totalIncome - totalExpense;

	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Summary");
	        alert.setHeaderText("Financial Summary");
	        alert.setContentText("Total Income: " + totalIncome + " €\n" +
	                             "Total Expense: " + totalExpense + " €\n" +
	                             "Balance: " + balance + " €");
	        alert.showAndWait();
	    }
	    
	    public void addRecordToTable(Record record) {
	    	  System.out.println("Pridávanie záznamu do tabuľky: " + record);
	    	  RecordManager.getInstance().addRecord(record);
	    	  
	    	  refreshTable();
	        System.out.println("Počet záznamov v tabuľke: " + recordList.size());
	    
	    }
	    
	    //pridat prepnutie sceny na edit 
	    
	    public void refreshTable() {
	    	
	    	ObservableList<Record> records = DatabaseManager.getInstance().getAllRecords();
	        tableData.setItems(records);
	        tableData.refresh();
	        System.out.println("Tabuľka aktualizovaná.");
	    }
	   
	    private void setupRowColoring() {
	        tableData.setRowFactory(tv -> new TableRow<Record>() {
	            @Override
	            protected void updateItem(Record record, boolean empty) {
	                super.updateItem(record, empty);
	
	                if (record == null || empty) {
	                    // Výpis vykonáme LEN AK bol štýl predtým nastavený
	                    if (!getStyle().isEmpty()) {
	                        setStyle(""); 
	                        System.out.println(" Resetujem štýl pre prázdny riadok (prvýkrát).");
	                    }
	                    return;
	                }
	
	                String typ = record.getKind().trim();
	
	                if (typ.equalsIgnoreCase("Income")) {
	                    if (!getStyle().equals("-fx-background-color: lightgreen;")) {
	                        setStyle("-fx-background-color: lightgreen;");
	                        System.out.println(" Riadok ZELENÝ (príjem) pre: " + record.getCategory());
	                    }
	                } else if (typ.equalsIgnoreCase("Expense")) {
	                    if (!getStyle().equals("-fx-background-color: lightcoral;")) {
	                        setStyle("-fx-background-color: lightcoral;");
	                        System.out.println(" Riadok ČERVENÝ (výdavok) pre: " + record.getCategory());
	                    }
	                } else {
	                    if (!getStyle().isEmpty()) {
	                        setStyle("");
	                        System.out.println("Resetujem štýl, neznámy typ.");
	                    }
	                }
	            }
	        });
	    }
	
	
	    @FXML
	    public void openFormDialog(ActionEvent event) throws IOException {
	        System.out.println("ADD open Dialog button clicked");
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormDialog.fxml"));
	        Parent root = loader.load();
	        
	        FormController formController = loader.getController();
	        formController.setTableController(this);
	
	        Stage dialogStage = new Stage();
	        dialogStage.setScene(new Scene(root));
	        dialogStage.showAndWait();
	        
	    	}
	    
	    @FXML
	    public void openEditForm(ActionEvent e) throws IOException {
	    	System.out.println("openEditForm" );
	    	
	    	Record selectedRecord = tableData.getSelectionModel().getSelectedItem();
	    	
	    	if(selectedRecord == null) {
	    		showAlert("Warning" , "No row has been selected.", Alert.AlertType.WARNING);
	    		return;
	    	}
	    	
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditDialog.fxml"));
	
	    	Parent root = loader.load();
	    	
	    	EditController editController = loader.getController();
	    	editController.setRecord(selectedRecord);
	    	
	    	Stage dialogStage = new Stage();
	        dialogStage.setScene(new Scene(root));
	        dialogStage.showAndWait();
	
	        tableData.refresh();
	    
	    }
	    
	    @FXML
	    private void deleteRecord(ActionEvent e ) {
	    	System.out.println("Delete button");
	    	
	    	Record selectedRecord = tableData.getSelectionModel().getSelectedItem();
	    	
	    	if(selectedRecord == null) {
	    		showAlert("Warning", "You are selected no record.", Alert.AlertType.WARNING);
	    	}
	    	
	    	String recInf = "\nKind:" + selectedRecord.getKind() + "\n" + 
	    					"Category:" + selectedRecord.getCategory() + "\n" + 
	    					"Description:" + selectedRecord.getDescription() + "\n"+
	    					"Price:" + selectedRecord.getAmount() + " 	€\n"+ 
	    					"Date:" + selectedRecord.getDate() + "/n";
	    	
	    	Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
	    	confirm.setTitle("Confirmation");
	    	confirm.setHeaderText("Are you sure, you want to delete this record?" + recInf);
	    	confirm.setContentText("This action we can not take back.");
	    	
	    	Optional<ButtonType> result = confirm.showAndWait();
	    	if(result.isPresent()&& result.get() == ButtonType.OK) {
	    		RecordManager.getInstance().removeRecord(selectedRecord);
	    		DatabaseManager.getInstance().deleteRecordDat(selectedRecord);
	    		refreshTable();
	    	}
	    }
	    
	    @FXML
	    private void sortBy(ActionEvent e) {

	        System.out.println(" sortBy() zavolané");
	        System.out.println(" Vybrané triedenie: " + sortOptions.getValue());

	        String selected = sortOptions.getValue();
	        TableColumn<Record, ?> columnToSort;

	        switch (selected) {
	            case "Amount":
	                columnToSort = amountColumn;
	                break;
	            case "Date":
	                columnToSort = dateColumn;
	                break;
	            case "Category":
	                columnToSort = categoryColumn;
	                break;
	            case "Kind":
	                columnToSort = kindColumn;
	                break;
	            default:
	                return;
	        }

	        // Obráti smer triedenia
	        TableColumn.SortType currentSortType = columnToSort.getSortType();
	        columnToSort.setSortType(
	            currentSortType == TableColumn.SortType.ASCENDING ?
	            TableColumn.SortType.DESCENDING :
	            TableColumn.SortType.ASCENDING
	        );

	        // ⬇️ Toto si predtým nemal
	        tableData.getSortOrder().clear();
	        tableData.getSortOrder().add(columnToSort);
	        tableData.sort();
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
	    			weatherLabel.setText(city + ": " + temp + "°C, " + "humidity:" + humidity + " ,"+ " wind: " + speed + ", " + descript);
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
	   
	   
	    private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    
	    }  
	}
	
	
	  
