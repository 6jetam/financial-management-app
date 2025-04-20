package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection databaseLink;

    private static final String DB_NAME = ConfigLoader.getProperty("DB_NAME");
    private static final String DB_HOST = ConfigLoader.getProperty("DB_HOST");
    private static final String DB_PORT = ConfigLoader.getProperty("DB_PORT");
    private static final String DB_USER = ConfigLoader.getProperty("DB_USER");
    private static final String DB_PASSWORD = ConfigLoader.getProperty("DB_PASSWORD");

    private static final String SERVER_URL =
        "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";;


    public static Connection getServerConnection() {
        return getConnection("jdbc:mysql://127.0.0.1:3307/?serverTimezone=UTC&useSSL=false"); // bez DB
    }

    public static Connection getDatabaseConnection() {
        return getConnection(SERVER_URL); // ✅ tu už je DB zahrnutá
    }

    public static Connection getConnection(String url) {
    	System.out.println("🔧 DB URL: " + SERVER_URL);
    	        try {
    	            return DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASSWORD);
    	        } catch (SQLException e) {
    	            System.err.println("❌ Chyba pripojenia k databáze:");
    	            e.printStackTrace();
    	            return null;
    	        }
    	    }
    

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println(" Spojenie uzavreté.");
            } catch (SQLException e) {
                System.out.println(" Chyba pri zatváraní spojenia.");
                e.printStackTrace();
            }
        }
    }
}
