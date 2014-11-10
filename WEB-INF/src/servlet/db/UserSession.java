package servlet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;

import twitter4j.auth.AccessToken;

public class UserSession extends DBOP{
	public int insert (AccessToken accessToken) throws ServletException{
		Connection conn;
		int num = 0;
		
		try {
			conn = getConnection();
			String sql = "insert into user (user_id, access_token, access_token_secret) values"
					+ "(?, ?, ?) on duplicate key update access_token=" + "\"" + accessToken.getToken() 
					+ "\"" + ',' + "access_token_secret=" + "\"" + accessToken.getTokenSecret() + "\"";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, accessToken.getUserId());
			stmt.setString(2, accessToken.getToken());
			stmt.setString(3, accessToken.getTokenSecret());
			num = stmt.executeUpdate();
			
			logger.info(sql);
			
			stmt.close();
			conn.commit();
			conn.close();
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return num;
		
	}

}
