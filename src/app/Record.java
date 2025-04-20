package app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Record {
	
	private IntegerProperty id = new SimpleIntegerProperty(0);
	private final StringProperty kind  = new SimpleStringProperty("");
	private final StringProperty category = new SimpleStringProperty("");
	private final StringProperty description =new SimpleStringProperty("");
	private final DoubleProperty amount =  new SimpleDoubleProperty(0.0);
	private final StringProperty date = new SimpleStringProperty("");
	
	 // Konštruktor, ktorý správne nastaví hodnoty do Property objektov
    	public Record(int id,String kind,String category, String description, double amount, String date) {
    		
    		 System.out.println("Vytváranie záznamu...");
    		 
    		 this.id.set(id);
    		 this.kind.set(kind);
    		 this.category.set(category);  // Používame set() na priradenie hodnoty do StringProperty
    		 this.description.set(description);  // Používame set() na priradenie hodnoty do StringProperty
    		 this.amount.set(amount);  // Používame set() na priradenie hodnoty do DoubleProperty
    		 this.date.set(date);  // Používame set() na priradenie hodnoty do StringProperty
        
    		 System.out.println("Záznam vytvorený: " + this);
        
    	}
    	
    	public void removeRecord(Record record) {
    	    record.removeRecord(record);
    	}
    	
    	public void add(Record record) {
			record.add(record);	
		}
    	
    	public IntegerProperty idProperty() {
    		return id;
    	}
    	
    	public StringProperty categoryProperty() {
        return category;
    	}
    	
    	public StringProperty descriptionProperty() {
	        return description;
	    }

	    public DoubleProperty amountProperty() {
	        return amount;
	    }

	    public StringProperty dateProperty() {
	        return date;
	    }
	    
	    public StringProperty kindProperty() {
	    	return kind;
	    }
	    
	    public int getId() {
	    	return id.get();
	    }
	    
	    public String getKind() {
	    	return kind.get();
	    }
	    
	    public String getCategory() {
	        return category.get();
	    }
	    
	    public Double getAmount() {
	    	return amount.get();
	    }
	    
	    public String getDescription() {
	    	return description.get();
	    }
	    public String getDate() {
	    	return date.get();
	    }
	    
	    public void setId(int newId) {
	    	this.id.set(newId);
	    }
}
