package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterData {
	
	static Connection connection;
	static String nameDatabase = "FinManagement";
	static String userDatabase = "root";
	static String passwordDatabase = "root";
	static String url = "jdbc:mysql://localhost/" + nameDatabase;

	//register data in registration form 

	public static boolean registerUserRegistration(String firstName, String nickName, String email, String password) {
	    String insertSQL = "INSERT INTO Users (firstName, nickName, email, passwordField) VALUES (?, ?, ?, ?)";
	    
	    if (userExists(nickName, email)) {
	        return false;
	    }

	    try (Connection connection = DatabaseConnection.getDatabaseConnection();
	         PreparedStatement ps = connection.prepareStatement(insertSQL)) {
	    	
	    	// Hashovanie hesla
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

	        // Nastavenie hodnôt do SQL dotazu
	        ps.setString(1, firstName);
	        ps.setString(2, nickName);
	        ps.setString(3, email);
	        ps.setString(4, hashedPassword);

	        // Vykonanie dotazu
	        ps.executeUpdate();
	        System.out.println("User was successfully registered.");
	        return true; // Registrácia úspešná
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Chyba pri registrácii
	    }
	}
	

	static boolean userExists(String nickName, String email) {
	    String query = "SELECT COUNT(*) FROM Users WHERE nickName = ? OR email = ?";

	    try (Connection connection = DatabaseConnection.getDatabaseConnection();
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        stmt.setString(1, nickName);
	        stmt.setString(2, email);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}
