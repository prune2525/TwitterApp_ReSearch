package servlet.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuteSelect extends DBOP{
	public List<String> select(String str) throws SQLException{
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stmt;
		ResultSet resultSet = null;
		try {
			
			conn = getConnection();
			String sql = "select * , count(word) as num from data where hashtag = ?"
					+ "and (morpheme2 = '自立' and morpheme not in ('動詞')"
					+ "or (morpheme = '名詞' and (morpheme2 = '固有名詞'"
					+ "and morpheme3 not in ('組織')) or morpheme4 = 'アニメキャラクター')"
					+ "or morpheme = 'アニメタイトル') group by word order by num desc";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, str);
			logger.info(stmt.toString());
			resultSet = stmt.executeQuery();
			
			while(resultSet.next()){
				list.add(resultSet.getString("word"));
			}
			
			stmt.close();
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally{
			resultSet.close();
			conn.commit();
			conn.close();
		}
		
		return list;
		
		
		
	}
}
