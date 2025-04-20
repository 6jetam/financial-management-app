package app;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HouseController {
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	public TextField usageTF;
	@FXML
	public TextField childrenTF;
	@FXML
	public TextField jarringTF;
	@FXML
	public TextField protectionPropertyTF;
	@FXML
	public TextField retirePayTF;
	@FXML
	public TextField investmentsTF;
	@FXML
	public TextField loanTF;
	@FXML
	public TextField backupTF;
	
	@FXML
	public TextField pUsageTF;
	@FXML
	public TextField pchildrenTF;
	@FXML
	public TextField pJarringTF;
	@FXML
	public TextField pProtectionPropertyTF;
	@FXML
	public TextField pRetirePay;
	@FXML
	public TextField pInvestmentsTF;
	@FXML
	public TextField pLoanTF;
	@FXML
	public TextField pbackupTF;
	@FXML 
	public Label custLoan,custUsag, custChild, custJarring, custProtect, custInvest, custRetire, custBackup; 
	@FXML
	public Button personal;
	@FXML
	public Button recommendation;
	@FXML
	public Button back;
	
	private static final double USAGE_PERCENTAGE = 0.6;
	private static final double CHILDREN_PERCENTAGE = 0.3;
	private static final double JARRING_PERCENTAGE = 0.1;
	
	private double consumptionPercentage = 40.0;
	private double loanPercentage = 30.0;
	private double investmentPercentage = 20.0;
	private double backupPercentage = 10.0;
	
	private static String savedUsage = "";
	private static String savedChildren = "";
	private static String savedJarring = "";
	private static String savedProtectionProperty = "";
	private static String savedRetirePay = "";
	private static String savedInvestments = "";
	private static String savedLoan = "";
	private static String savedBackup = "";
	
	private static String savedPUsage = "";
	private static String savedPChildren = "";
	private static String savedPJarring = "";
	private static String savedPProtectionProperty = "";
	private static String savedPRetirePay = "";
	private static String savedPInvestments = "";
	private static String savedPLoan = "";
	private static String savedPBackup = "";
		
	private static boolean pFieldsVisible = false; 

	
	@FXML
	public void analyze (ActionEvent event) throws IOException {
		income();
	}
	
	@FXML
	public void performRecomendAnalysis(ActionEvent event) {
	    double userIncome = income();
	    if (userIncome > 0) {
	        calcFinPlan(userIncome, consumptionPercentage, loanPercentage, investmentPercentage, backupPercentage, false);
	        
	       
	    }
	}

	@FXML
	public void customAnalysis (ActionEvent event) throws IOException {
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Custom Financial Analysis");
		
		//layout
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);
		
		//label
		
		Label incomeLabel = new Label("Enter monthly income:");
		Label consumptionLabel = new Label("Consumption (%):");
		Label loanLabel = new Label("Loan (%):");
		Label investmentLabel = new Label("Investments (%):");
		Label backupLabel = new Label("Backup (%):");
		Label remainingLabel = new Label("Remaining: 100%");
		
		//textfieldy
		
		TextField incomeField = new TextField();
		TextField consumptionField = new TextField();
		TextField loanField = new TextField();
		TextField investmentField = new TextField();
		TextField backupField = new TextField();
		
		Button confirmButton = new Button("Analyze");
		confirmButton.setDisable(true);
		
		// Pridanie komponentov do gridu
	    grid.add(incomeLabel, 0, 0);
	    grid.add(incomeField, 1, 0);
	    grid.add(consumptionLabel, 0, 1);
	    grid.add(consumptionField, 1, 1);
	    grid.add(loanLabel, 0, 2);
	    grid.add(loanField, 1, 2);
	    grid.add(investmentLabel, 0, 3);
	    grid.add(investmentField, 1, 3);
	    grid.add(backupLabel, 0, 4);
	    grid.add(backupField, 1, 4);
	    grid.add(remainingLabel, 0, 5, 2, 1);
	    grid.add(confirmButton, 0, 6, 2, 1);
		
	    // Event Handler na automatickú aktualizáciu zostávajúceho percenta
	    
	    ChangeListener<String> updateRemainingPercentage = (obs, oldVal, newVal) -> {
	        try {
	            double consumption = parseDoubleOrZero(consumptionField.getText());
	            double loan = parseDoubleOrZero(loanField.getText());
	            double investment = parseDoubleOrZero(investmentField.getText());
	            double backup = parseDoubleOrZero(backupField.getText());

	            double total = consumption + loan + investment + backup;
	            double remaining = 100 - total;

	            remainingLabel.setText("Remaining: " + remaining + "%");

	            // Aktivovať tlačidlo iba ak celkový súčet je 100%
	            confirmButton.setDisable(total != 100);
	        } catch (NumberFormatException e) {
	            remainingLabel.setText("Invalid input!");
	            confirmButton.setDisable(true);
	        }
	    };

	    // Pripojenie listenerov na TextFieldy
	    consumptionField.textProperty().addListener(updateRemainingPercentage);
	    loanField.textProperty().addListener(updateRemainingPercentage);
	    investmentField.textProperty().addListener(updateRemainingPercentage);
	    backupField.textProperty().addListener(updateRemainingPercentage);

	    // Ak používateľ klikne na "Analyze"
	    confirmButton.setOnAction(ev -> {
	        try {
	            double income = Double.parseDouble(incomeField.getText());
	            double consumption = Double.parseDouble(consumptionField.getText());
	            double loan = Double.parseDouble(loanField.getText());
	            double investment = Double.parseDouble(investmentField.getText());
	            double backup = Double.parseDouble(backupField.getText());

	            // Zatvorenie dialógu
	            dialogStage.close();

	            // Spustenie analýzy
	            calcFinPlan(income, consumption, loan, investment, backup,true);

	        } catch (NumberFormatException e) {
	            showError("Error", "Please enter valid numbers.");
	        }
	    });

	    // Nastavenie scény
	    Scene scene = new Scene(grid, 300, 250);
	    dialogStage.setScene(scene);
	    dialogStage.showAndWait();
	
		 
	}
	
	private double parseDoubleOrZero(String text) {
	    try {
	        return Double.parseDouble(text);
	    } catch (NumberFormatException e) {
	        return 0.0;
	    }
	}
	
	
	private double income() {
	    // Vytvorenie modálneho dialógu pre zadanie príjmu
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Your income");
	    dialog.setHeaderText("Financial analysis:");
	    dialog.setContentText("Please fill your monthly income:");

	    Optional<String> result = dialog.showAndWait();

	    if(result.isPresent()) {
	        try {
	            return Double.parseDouble(result.get());
	        } catch (NumberFormatException e) {
	            showError("Wrong input", "Invalid number");
	        }
	    };
	    return 0.0;
	}
	
	
	private void calcFinPlan(double income, double consumptionPercentage, double loanPercentage, double investmentPercentage, double backupPercentage, boolean isCustom) {
		double consumption = income  *(consumptionPercentage/ 100 ); // consumption 40%
		double loanRepayment = income * (loanPercentage / 100); // loans 30% 
		double assetCreation = income * (investmentPercentage / 100); // asset 20 %
		double financialReserve = income * (backupPercentage / 100) ; //backup 10% 
		
		double usage = consumption * USAGE_PERCENTAGE; //60% from 40 % usage for neccesary 
		double children = consumption * CHILDREN_PERCENTAGE; // 30 % from 40% on children 
		double jarring = consumption * JARRING_PERCENTAGE; //10 % from 40 on jarring 
		
		double loan = loanRepayment; 
		
		double investments = assetCreation * 0.6;// 60% from 20% investments 
		double retire = assetCreation * 0.4; // 40% from 20% retire 
		
		double protectionProperty = financialReserve * 0.5;
		double backup = financialReserve * 0.5; 
		
		 // Výstup
        System.out.println("Income:" + income);
        System.out.println("Usage (40% - Spotreba):");
        System.out.println("Basic Usage: " + usage);
        System.out.println("Children: " + children);
        System.out.println("Jarring: " + jarring);
        System.out.println("Loan Repayment (30%): " + loan);
        System.out.println("Asset Creation (20%):");
        System.out.println("Investments: " + investments);
        System.out.println("Retirement: " + retire);
        System.out.println("Protection of property " + protectionProperty);
        System.out.println("Financial Reserve (10%): " + backup);
        
        if (isCustom) {
            updateCustomTextFields(usage, children, jarring, protectionProperty, retire, investments, loanRepayment, backup);
        } else {
        	updateTextFields(usage, children, jarring, loan, investments,protectionProperty, retire, backup);
        }
	}
        
	
	
	private void updateTextFields(double usage, double children, double jarring, double loan, double investments,double protectionProp, double retire, double backup) {
	    setFormattedText(usageTF, usage);
	    setFormattedText(childrenTF, children);
	    setFormattedText(jarringTF, jarring);
	    setFormattedText(loanTF, loan);
	    setFormattedText(investmentsTF, investments);
	    setFormattedText(protectionPropertyTF, protectionProp);
	    setFormattedText(retirePayTF, retire);
	    setFormattedText(backupTF, backup);
	}
	
	private void updateCustomTextFields(double usage, double children, double jarring, double loan, double investments,double protectionProp, double retire, double backup) {

	    setFormattedText(pUsageTF, usage);
		setFormattedText(pchildrenTF, children);
		setFormattedText(pJarringTF, jarring);
		setFormattedText(pProtectionPropertyTF, protectionProp);
		setFormattedText(pRetirePay, retire);
		setFormattedText(pInvestmentsTF, investments);
		setFormattedText(pLoanTF, loan);
		setFormattedText(pbackupTF, backup);
		
		showResult();
	}

	
	 //zaokruhli na 2 dest miesta

	private void setFormattedText(TextField field, double value) {
	    field.setText(String.format("%.2f", value));
	}
	
	
	 private void showError(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	 
	 private void showResult() {
		 pUsageTF.setVisible(true);
		 pchildrenTF.setVisible(true);
		 pJarringTF.setVisible(true);
		 pProtectionPropertyTF.setVisible(true);
		 pRetirePay.setVisible(true);
		 pInvestmentsTF.setVisible(true);
		 pLoanTF.setVisible(true);
		 pbackupTF.setVisible(true);
		 
		 custBackup.setVisible(true);
		 custLoan.setVisible(true);
		 custUsag.setVisible(true);
		 custChild.setVisible(true);
		 custJarring.setVisible(true);
		 custProtect.setVisible(true);	
		 custInvest.setVisible(true);
		 custRetire.setVisible(true);
		 custBackup.setVisible(true);
		 
	 }
	 //custom aby sa zobrazilo 
	 @FXML
	 private void clearBtn(ActionEvent event) throws IOException {
		  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		  alert.setTitle("Clear fields");
		  alert.setHeaderText("What do you want to clear?");
		  alert.setContentText("Choose which values to clear:");
		  
		  ButtonType clearStandard = new ButtonType("Standard");
		  ButtonType clearCustom = new ButtonType("Custom");
		  ButtonType all = new ButtonType("All");
		  ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
				  
		  alert.getButtonTypes().setAll(clearStandard,clearCustom,all, cancel);
		  
		  Optional<ButtonType> result = alert.showAndWait();
		  if(result.isPresent()) {
			  if(result.get()== clearStandard) {
				  clearStandFields();
			  }else if(result.get() == clearCustom ) {
				  clearCustomField();
			  }
			  else if(result.get() == all) {
				  clearStandFields();
				  clearCustomField();
			  }
		  }
	 }
	 
	 private void saveFieldValues() {
		    savedUsage = usageTF.getText();
		    savedChildren = childrenTF.getText();
		    savedJarring = jarringTF.getText();
		    savedProtectionProperty = protectionPropertyTF.getText();
		    savedRetirePay = retirePayTF.getText();
		    savedInvestments = investmentsTF.getText();
		    savedLoan = loanTF.getText();
		    savedBackup = backupTF.getText();
		    
		    savedPUsage = pUsageTF.getText();
		    savedPChildren = pchildrenTF.getText();
		    savedPJarring = pJarringTF.getText();
		    savedPProtectionProperty = pProtectionPropertyTF.getText();
		    savedPRetirePay = pRetirePay.getText();
		    savedPInvestments = pInvestmentsTF.getText();
		    savedPLoan = pLoanTF.getText();
		    savedPBackup = pbackupTF.getText();
		    
		    pFieldsVisible = pUsageTF.isVisible();
		}

	 private void clearStandFields() {
		    usageTF.clear();
		    childrenTF.clear();
		    jarringTF.clear();
		    protectionPropertyTF.clear();
		    retirePayTF.clear();
		    investmentsTF.clear();
		    loanTF.clear();
		    backupTF.clear();
		}
	 
	 private void clearCustomField() {
		    pUsageTF.clear();
		    pchildrenTF.clear();
		    pJarringTF.clear();
		    pProtectionPropertyTF.clear();
		    pRetirePay.clear();
		    pInvestmentsTF.clear();
		    pLoanTF.clear();
		    pbackupTF.clear();
		}

	 @FXML
	 private void initialize() {
		 pUsageTF.setVisible(false);
		 pchildrenTF.setVisible(false);
		 pJarringTF.setVisible(false);
		 pProtectionPropertyTF.setVisible(false);
		 pRetirePay.setVisible(false);
		 pInvestmentsTF.setVisible(false);
		 pLoanTF.setVisible(false);
		 pbackupTF.setVisible(false);
		 
		 custBackup.setVisible(false);
		 custLoan.setVisible(false);
		 custUsag.setVisible(false);
		 custChild.setVisible(false);
		 custJarring.setVisible(false);
		 custProtect.setVisible(false);	
		 custInvest.setVisible(false);
		 custRetire.setVisible(false);
		 custBackup.setVisible(false);
		 
		 if (!savedUsage.isEmpty()) usageTF.setText(savedUsage);
		 if (!savedChildren.isEmpty()) childrenTF.setText(savedChildren);
		 if (!savedJarring.isEmpty()) jarringTF.setText(savedJarring);
		 if (!savedProtectionProperty.isEmpty()) protectionPropertyTF.setText(savedProtectionProperty);
		 if (!savedRetirePay.isEmpty()) retirePayTF.setText(savedRetirePay);
		 if (!savedInvestments.isEmpty()) investmentsTF.setText(savedInvestments);
		 if (!savedLoan.isEmpty()) loanTF.setText(savedLoan);
		 if (!savedBackup.isEmpty()) backupTF.setText(savedBackup);
		 

		 if (!savedPUsage.isEmpty()) {
		        pUsageTF.setText(savedPUsage);
		        pUsageTF.setVisible(true);
		        custUsag.setVisible(true);
		    }
		 
		 if (pFieldsVisible) {
			    showCustomFields();
		 }
		 
		 if (!savedPChildren.isEmpty()) {
		        pchildrenTF.setText(savedPChildren);
		        pchildrenTF.setVisible(true);
		        custChild.setVisible(true);
		        
		    }
		    if (!savedPJarring.isEmpty()) {
		        pJarringTF.setText(savedPJarring);
		        pJarringTF.setVisible(true);
		        custJarring.setVisible(true);
		        
		    }
		    if (!savedPProtectionProperty.isEmpty()) {
		        pProtectionPropertyTF.setText(savedPProtectionProperty);
		        pProtectionPropertyTF.setVisible(true);
		        custProtect.setVisible(true);
		    }
		    if (!savedPRetirePay.isEmpty()) {
		        pRetirePay.setText(savedPRetirePay);
		        pRetirePay.setVisible(true);
		        custRetire.setVisible(true);
		    }
		    if (!savedPInvestments.isEmpty()) {
		        pInvestmentsTF.setText(savedPInvestments);
		        pInvestmentsTF.setVisible(true);
		        custInvest.setVisible(true);
		    }
		    if (!savedPLoan.isEmpty()) {
		        pLoanTF.setText(savedPLoan);
		        pLoanTF.setVisible(true);
		        custLoan.setVisible(true);
		    }
		    if (!savedPBackup.isEmpty()) {
		        pbackupTF.setText(savedPBackup);
		        pbackupTF.setVisible(true);
		        custBackup.setVisible(true);
		    }
	 }
		 
		 private void showCustomFields() {
			    pUsageTF.setVisible(true);
			    pchildrenTF.setVisible(true);
			    pJarringTF.setVisible(true);
			    pProtectionPropertyTF.setVisible(true);
			    pRetirePay.setVisible(true);
			    pInvestmentsTF.setVisible(true);
			    pLoanTF.setVisible(true);
			    pbackupTF.setVisible(true);
			}
	 
	 
	 @FXML 
	private void buttonBack(ActionEvent event) throws IOException {
		 saveFieldValues();
			System.out.println("button back");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
			root = loader.load();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	
}
