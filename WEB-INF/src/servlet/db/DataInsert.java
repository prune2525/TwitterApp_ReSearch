package servlet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import servlet.anal.Analysis;

public class DataInsert extends DBOP{
	public int insert(List<Analysis> analList){
		Connection conn;
		PreparedStatement stmt = null;
		int num = 0;
		try {
			conn = getConnection();
			for(Analysis analysis : analList){
				String hinshi = analysis.getMorpheme().feature.split(",")[0];
				String hinshi2 = analysis.getMorpheme().feature.split(",")[1];
				String hinshi3 = analysis.getMorpheme().feature.split(",")[2];
				String hinshi4 = analysis.getMorpheme().feature.split(",")[3];
				
				if(analysis.getMorpheme().surface.length() >=2 ){
					String sql = "insert into data (hashtag, word, morpheme, morpheme2, morpheme3, morpheme4) "
							+ "values (?, ?, ?, ?, ?, ?)";
					stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, analysis.getHashtag());
					stmt.setString(2, analysis.getMorpheme().surface);
					stmt.setString(3, hinshi);
					stmt.setString(4, hinshi2);
					stmt.setString(5, hinshi3);
					stmt.setString(6, hinshi4);
					
					num = stmt.executeUpdate();
					logger.info(sql);
				}
			}
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
