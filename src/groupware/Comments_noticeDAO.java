package groupware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Comments_noticeDAO implements InterComment_noticeDAO {
	
	// field , attribute , property , 속성
			Connection conn = null;
			
			PreparedStatement pstmt = null;
			
			ResultSet rs = null;
			

			// === 자원반납을 해주는 메소드 === // -------------------------------------------------------------------
			private void close() {
				
				try {
					if( rs != null )  rs.close();
					if( pstmt != null ) pstmt.close();  
				} catch(SQLException e) {
					e.printStackTrace();
				}
				
			} // end of private void close() -----------------------------------


	//댓글쓰기 메소드
	@Override
	public int noticeWrite(Comments_noticeDTO cndto) {
		
		int resultn = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			//cf_commentno
			String sql = " insert into tbl_comments_notice(cn_commentno, fk_bn_boardno ,fk_employee_id ,cn_contents)\n"+
					" values(SEQ_CNNO.nevtval,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, cndto.getFk_bn_boardno());
			pstmt.setString(2, cndto.getFk_employee_id());
			pstmt.setString(3, cndto.getCn_contents());
			
			
			resultn = pstmt.executeUpdate(); // 1 or 0
			
		}catch(SQLException e) {
			e.printStackTrace();
			resultn = -1;
		}finally {
			close();
		}
		
		return resultn;

	}

	//글 밑에 댓글내용 보이게하는 메소드
	@Override
	public List<Comments_noticeDTO> noticeCommentList(String Nboardno) {
		
		List<Comments_noticeDTO> noticecommentList = null;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select  cn_commentno , fk_employee_id, cn_contents , cn_writedate  "+
						" from  tbl_comments_notice "+
						" where fk_bn_boardno = ? ";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Nboardno);
			
			rs = pstmt.executeQuery();
			
			
			int cnt =0;
			
			
			while(rs.next()) {
				
				cnt++;
				
				Comments_noticeDTO cndto = new Comments_noticeDTO(); 
				cndto.setCn_commentno(rs.getInt("cn_commentno"));
				cndto.setFk_employee_id(rs.getString("fk_employee_id"));
				cndto.setCn_contents(rs.getString("cn_contents"));
				cndto.setCn_writedate(rs.getString("cn_writedate"));
				
				if(cnt == 1) {
					noticecommentList = new ArrayList<>();
				}	
				
	
				noticecommentList.add(cndto);
			}
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return noticecommentList;
	}

}
