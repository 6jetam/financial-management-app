package app;

import java.sql.Statement;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
	private static DatabaseManager instance; 
	
	private DatabaseManager() {
		createDtbIfNotExists();
        createTableIfNotExists();
    }
	
	public static DatabaseManager getInstance() {
		if(instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}
	//kontrola alebo vytvorenie databazy
	private void createDtbIfNotExists() {
		String querry = "CREATE DATABASE IF NOT EXISTS FinManagement";
		
		Connection conn = null;
		
		try {
			conn = DatabaseConnection.getServerConnection();
			Statement stmt = conn.createStatement();
			 stmt.executeUpdate(querry);
			System.out.println("DB create FinManagement");
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DatabaseConnection.closeConnection(conn);
		}
	}
	//kontrola alebo vytvorenie tabulky
	private void createTableIfNotExists() {
		 String querry = "CREATE TABLE IF NOT EXISTS records (" +
                 "record_id INT PRIMARY KEY AUTO_INCREMENT, " +
                 "kind VARCHAR(20) NOT NULL, " +
                 "category VARCHAR(20) NOT NULL, " +
                 "description_record VARCHAR(255), " +
                 "amount DECIMAL(10,2), " +
                 "_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)";
		 
		 Connection conn = null; 	
		 try {
			 conn = DatabaseConnection.getDatabaseConnection();
			 Statement stmt = conn.createStatement();
			 stmt.executeUpdate(querry);
			 System.out.println(" Tabuľka skontrolovaná/vytvorená: records");
		 }catch (SQLException e ) {
			 e.printStackTrace();
	}
		 finally {
			 DatabaseConnection.closeConnection(conn);
		 }
	}
	
	public ObservableList<Record> getAllRecords() {
	    ObservableList<Record> records = FXCollections.observableArrayList();
	    
	    int userId = SessionManager.getCurrentUser().getId();
	    String query = "SELECT * FROM records WHERE user_id = ?";
	    
	    Connection conn = null;

	    try {
	        conn = DatabaseConnection.getDatabaseConnection();
	        PreparedStatement stmt = conn.prepareStatement(query);
	        stmt.setInt(1, userId);
	        
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Record record = new Record(
	                rs.getInt("record_id"),
	                rs.getString("kind"),
	                rs.getString("category"),
	                rs.getString("description_record"),
	                rs.getDouble("amount"),
	                rs.getString("_date")
	            );
	            records.add(record);
	        }

	        System.out.println("Načítanie záznamov: " + records.size());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DatabaseConnection.closeConnection(conn);
	    }

	    return records;
	}

	
	public void addRecordDab(Record record) {
	    String query = "INSERT INTO records (kind, category, description_record, amount, _date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
	    int userId = SessionManager.getCurrentUser().getId();

	    Connection conn = null;

	    try {
	        conn = DatabaseConnection.getDatabaseConnection();
	        conn.setAutoCommit(false);

	        PreparedStatement ps = conn.prepareStatement(query);

	        ps.setString(1, record.getKind());
	        ps.setString(2, record.getCategory());
	        ps.setString(3, record.getDescription());
	        ps.setDouble(4, record.getAmount());
	        ps.setString(5, record.getDate());
	        ps.setInt(6, userId);

	        int rowsAffected = ps.executeUpdate(); // ✅ stačí len toto
	        System.out.println("Počet ovplyvnených riadkov: " + rowsAffected);

	        conn.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    } finally {
	        DatabaseConnection.closeConnection(conn);
	    }
	}

	
	public void updateRecord(Record record) {
		String query= "UPDATE records SET kind = ?, category = ?, description_record = ?, amount = ?, _date = ? WHERE record_id = ? AND user_id = ?";

	    Connection conn = null;

	    try {
	        conn = DatabaseConnection.getDatabaseConnection();
	        conn.setAutoCommit(false); // Vypnutie auto-commit

	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, record.getKind());
	        ps.setString(2, record.getCategory());
	        ps.setString(3, record.getDescription());
	        ps.setDouble(4, record.getAmount());
	        ps.setString(5, record.getDate());
	        ps.setInt(6, record.getId());
	        ps.setInt(7, SessionManager.getCurrentUser().getId());

	        int rowsAffected = ps.executeUpdate();
	        System.out.println("Počet upravených riadkov: " + rowsAffected);

	        conn.commit(); // Uloženie zmien

	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback();
	                System.out.println("Chyba! Zmeny v databáze boli vrátené späť.");
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    } finally {
	        DatabaseConnection.closeConnection(conn);
	    }
	}

	public void deleteRecordDat(Record record) {
		String query = "DELETE FROM records WHERE record_id = ? and user_id = ?";

	    Connection conn = null;
	    
	    try {
	    	conn = DatabaseConnection.getDatabaseConnection();
	    	conn.setAutoCommit(false);
	    	
	    	PreparedStatement ps = conn.prepareStatement(query);
	    	ps.setInt(1,record.getId());
	    	ps.setInt(2, SessionManager.getCurrentUser().getId());
	    	
	    	int rowsAffected = ps.executeUpdate();
	    	System.out.println("Pocet zmazanych riadkov " + rowsAffected);
	    	
	    	conn.commit();
	    	
	    }catch (SQLException e) {
	    	e.printStackTrace();
	    	if(conn!= null) {
	    		try {
	    			conn.rollback();
	    			System.out.println("Error. Detele prebehlo");
	    		}catch (SQLException ex){
	    			ex.printStackTrace();
	    		}
	    	}
	    } finally {
	    	DatabaseConnection.closeConnection(conn);
	    }
	}
}
    
