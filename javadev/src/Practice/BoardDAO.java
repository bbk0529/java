package Practice;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BoardDAO {
	private static BoardDAO boardDAO = null; // 자기자신을 참조하는 공유 변수
	
	private String driver	 	= null;
	private String url		 	= null;
	private String username 	= null;
	private String password 	= null;
	

	public List<BoardVO> getBoardList(long startnum, long endnum, String select, String search) { 
		Connection			cn =	null;
	 	PreparedStatement 	ps	=	null;
	 	ResultSet			rs 	=	null;
	 	 	
	 	List<BoardVO> list = new ArrayList<BoardVO>();
	 	
	 	StringBuffer sql = new StringBuffer();
	 	sql.append(" select B.*");
	 	sql.append(" from (select rownum as rnum, A.*");
	 	sql.append(" 		from ( 	select no, title, name, regdate, viewcount, content");
	 	sql.append(" 				from tb_board");
	 	sql.append(" 				order by no desc) A) B");
	 	sql.append(" where rnum between ? and ?");
	 	
	 	if (search!=null && select!=null) 	{ // ? 가 where ? 에 먹지 않아서, 처리함. 
 			sql.append(" and "+ select + " like '%'||?||'%'"); 				 			
 		}
	 		 	
	 	try{
	 		cn = getConnection();
	 		ps = cn.prepareStatement(sql.toString());
	 		ps.setLong(1,startnum);
	 		ps.setLong(2,endnum); 
	 		
	 		if (search!=null && select!=null) 	{
	 			ps.setString(3,search);	 			
	 		}
	 			
	 		rs = ps.executeQuery(); 		 		
	 		System.out.println(startnum +" " + endnum +" " +select + " " + search );
	 		System.out.println(sql);	 		
	 		
	 		while(rs.next()){ // EOR (End Of Record)를 만나면 false 
	 			BoardVO boardVO = new BoardVO();
	 			boardVO.setNo(rs.getLong("no"));
	 			boardVO.setName(rs.getString("name"));
	 			boardVO.setTitle(rs.getString("title"));
	 			boardVO.setRegdate(rs.getDate("regdate"));
	 			boardVO.setViewcount(rs.getInt("viewcount"));
	 			list.add(boardVO); 			
	 		} 		 		 		
	 		
	 	} catch (Exception e) 	{
	 		e.printStackTrace();
	 		
	 	} finally {
	 		dbClose(rs, ps, cn); 
	 	}
	 	return list;
	}
	
	
	
	private BoardDAO (){
		Properties pr = new Properties();
		String props = this.getClass().getResource("").getPath() + "/database.properties"; 
			
		try {
			pr.load(new FileInputStream(props));
			driver		=	pr.getProperty("driver");
			url			=	pr.getProperty("url");
			username	=	pr.getProperty("username");
			password	=	pr.getProperty("password");
									
			Class.forName(driver);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}	
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
	
	public static BoardDAO getInstance() {  // 관례적으로 getBoardDAO 가 아니라 getInstance로 함. 
		if (boardDAO == null) {
			boardDAO = new BoardDAO();
		}		
		return boardDAO;
	}	
	

	private void dbClose(PreparedStatement ps, Connection cn) {
		if (ps !=null) try {ps.close();}catch(Exception e){}		
		if (cn !=null) try {cn.close();}catch(Exception e){}				
	}
	

	private void dbClose(ResultSet rs, PreparedStatement ps, Connection cn) {
		if (rs!=null) try{rs.close();} catch (Exception e){}
 		if (ps!=null) try{ps.close();} catch (Exception e){}
 		if (cn!=null) try{cn.close();} catch (Exception e){}		
		
	}
	
	
	public boolean insertBoard(BoardVO boardVO){
		Connection		 	cn	= null;
		PreparedStatement 	ps	= null;
		/* ResultSet 			rs	= null; */
		
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into tb_board(no, title, name, content, pwd)"); 
		sql.append(" values (seq_board.nextval, ?, ?, ?, ?)");	 
	
		try {
			cn = getConnection();
			ps = cn.prepareStatement(sql.toString());
			ps.setString(1, boardVO.getTitle());
			ps.setString(2, boardVO.getName());
			ps.setString(3, boardVO.getContent());
			ps.setString(4, boardVO.getPwd());
			ps.executeUpdate();
			result=true;
			
		} catch (Exception e){
			e.printStackTrace();		
		
		}finally { 
			dbClose(ps, cn);		
		}
		return result;
	}
	
	

	public boolean updateBoard(BoardVO boardVO){
		Connection		 	cn	= null;
		PreparedStatement 	ps	= null;
			
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tb_board SET"); 
		sql.append(" 		title=?, name=?, content=?");	 
		sql.append(" WHERE 	no=? AND pwd=?");	 
	
		try {
			
			cn = getConnection();
			ps = cn.prepareStatement(sql.toString());
			ps.setString(1, boardVO.getTitle());
			ps.setString(2, boardVO.getName());
			ps.setString(3, boardVO.getContent());
			ps.setLong  (4, boardVO.getNo());
			ps.setString(5, boardVO.getPwd());
			if (ps.executeUpdate() >0 ) {
				result=true;	// 반영된 결과가 record의 개수를 반드시 확인한다.
			}					 		
		} catch (Exception e){
			e.printStackTrace();		
		
		} finally { 
			dbClose(ps, cn);				
		}	
		return result;
	}
	
	
	
	public boolean deleteBoard(BoardVO boardVO) {
		
		Connection		 	cn	= null;
		PreparedStatement 	ps	= null;
			
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE from tb_board");	
		sql.append(" WHERE 	no=? AND pwd=?");	 

		try {
			cn = getConnection();
			ps = cn.prepareStatement(sql.toString());
			ps.setLong  (1, boardVO.getNo());
			ps.setString(2, boardVO.getPwd());
			if (ps.executeUpdate() >0 ) {
					result=true;
					// 반영된 결과가 record의 개수를 반드시 확인한다.
			}			
			 		
		} catch (Exception e){
			e.printStackTrace();		
		
		}finally { 
			dbClose(ps, cn);		
		}	
		return result;
	}
	

	
	

	public long getTotalCount() { // 게시물 전체 갯수를 구함
		Connection			cn =	null;
	 	PreparedStatement 	ps	=	null;
	 	ResultSet			rs 	=	null;
	 	 	
	 	String sql_totalcount="select count(*) as cnt from tb_board";		
	 	long result=0;
	 	
	 	try{
	 		cn = getConnection();
	 		ps = cn.prepareStatement(sql_totalcount);
	 		rs = ps.executeQuery();
	 		if (rs.next()) result = rs.getLong("cnt");
	 		
	 	} catch (Exception e) 	{
	 		e.printStackTrace();
	 		
	 	} finally {
	 		dbClose(rs, ps, cn); 
	 	}
	 	return result;
	}
	
	
	public BoardVO getBoard(long no){
		
		Connection 			cn =	null;
	 	PreparedStatement 	ps	=	null;
	 	ResultSet			rs 	=	null;
	 	
	 	StringBuffer sql = new StringBuffer();
	 	sql.append(" select no, name, title, regdate, viewcount, content");
	 	sql.append(" from tb_board");
	 	sql.append(" where no=?"); 

	 	StringBuffer sql2 = new StringBuffer();
	 	sql2.append(" update tb_board");
	 	sql2.append(" set viewcount = ?");
	 	sql2.append(" where no=?");

	 	BoardVO boardVO = null;
	 	
	 	try{
	 		cn = getConnection();
	 		ps = cn.prepareStatement(sql.toString());
	 		ps.setLong(1,no);
	 		rs = ps.executeQuery(); 		 		
	 		if (rs.next()) {
	 			boardVO = new BoardVO();
	 			boardVO.setNo(rs.getLong("no"));
	 			boardVO.setName(rs.getString("name"));
	 			boardVO.setTitle(rs.getString("title"));
	 			boardVO.setContent(rs.getString("content"));
	 			boardVO.setRegdate(rs.getDate("regdate"));
	 			boardVO.setViewcount(rs.getInt("viewcount")+1);
	 		
	 			ps = cn.prepareStatement(sql2.toString());
	 	 		ps.setLong(1,boardVO.getViewcount());
	 	 		ps.setLong(2,no);
	 	 		ps.executeUpdate();
	 		}
	 		
	 	} catch (Exception e) 	{
	 		e.printStackTrace();
	 		
	 	} finally {
	 		dbClose(rs, ps, cn); 
	 	}	
		return boardVO;
	}
	
}