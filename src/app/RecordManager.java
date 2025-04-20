package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecordManager {
	
	    private static final RecordManager instance = new RecordManager();
	    private final ObservableList<Record> records = FXCollections.observableArrayList();
	    private int idCounter = 1;
	 

	    private RecordManager() {}

	    public static RecordManager getInstance() {
	        return instance;
	    }

	    public ObservableList<Record> getRecords() {
	        return records;
	    }

	    public void addRecord(Record record) {
	    	record.setId(idCounter++);
	        records.add(record);
	    }
	    
	    public void removeRecord(Record record) {
	        records.remove(record);
	    }
	}

