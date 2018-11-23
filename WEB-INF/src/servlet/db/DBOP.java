package servlet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBOP {
	protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	static{
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost/twitterdb";
		String user = "";
		String pass = "";
		
		Connection conn = DriverManager.getConnection(url, user, pass);
		conn.setAutoCommit(false);
		
		return conn;
	}
	
}
