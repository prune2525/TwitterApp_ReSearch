package servlet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;

import twitter4j.auth.AccessToken;

public class KeyInsert extends DBOP{
	public int insert (AccessToken accessToken, String str) throws ServletException{
		Connection conn;
		int num = 0;
		
		try {
			conn = getConnection();
			String sql = "insert ignore into keyword (user_id, hashtag) values (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, accessToken.getUserId());
			stmt.setString(2, str);
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
