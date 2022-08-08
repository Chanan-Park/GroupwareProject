package groupware;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Comments_freeDAO implements InterComment_freeDAO {

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

		
		
		//****6. 댓글쓰기 ***
		@Override
		public int write(Comments_freeDTO cfdto) {
			int resultn4 = 0;
			
			try {
				conn = ProjectDBConnection.getConn();
				//cf_commentno
				String sql = " insert into tbl_comments_free(cf_commentno, fk_bf_boardno ,fk_employee_id ,cf_comments)\n"+
						" values(SEQ_CFNO.nextval,?,?,?)";
				pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, cfdto.getFk_bf_boardno());
				pstmt.setString(2, cfdto.getFk_employee_id());
				pstmt.setString(3, cfdto.getCf_comments());
				
				
				resultn4 = pstmt.executeUpdate(); // 1 or 0
				
			}catch(SQLException e) {
				e.printStackTrace();
				resultn4 = -1;
			}finally {
				close();
			}
			
			return resultn4;
		}//end of public int write(Comments_freeDTO cfdto)-------------------


		//글내용 밑에 댓글 보이게하기
		
		@Override
		public List<Comments_freeDTO> commentList(String boardno) {
			
			List<Comments_freeDTO> commentList = null;
			
			try {
				conn = ProjectDBConnection.getConn();
				
				String sql = "select  cf_commentno , fk_employee_id, cf_comments , cf_writedate  "+
							" from  tbl_comments_free "+
							" where fk_bf_boardno = ? ";
						
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardno);
				
				rs = pstmt.executeQuery();
				
				
				int cnt =0;
				
				
				while(rs.next()) {
					
					cnt++;
					
					Comments_freeDTO cfdto = new Comments_freeDTO(); 
					cfdto.setCf_commentno(rs.getInt("cf_commentno"));
					cfdto.setFk_employee_id(rs.getString("fk_employee_id"));
					cfdto.setCf_comments(rs.getString("cf_comments"));
					cfdto.setCf_writedate(rs.getString("cf_writedate"));
					
					if(cnt == 1) {
						commentList = new ArrayList<>();
					}	
					
		
					commentList.add(cfdto);
				}
				
				
		
			
				
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				close();
			}
			return commentList;
		}


		// *** 7.댓글 수정 메소드 ***
		
		@Override
		public int updateComments(Map<String, String> paraMap) {
			
				int resultn5=0;
				
				try {
					
					conn = ProjectDBConnection.getConn();
					
					String sql = " update tbl_comments_free set cf_comments  = ? \n"+
							" where fk_bf_boardno = ? and cf_commentno = ? ";
					
					pstmt =conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("cf_conmments"));
					pstmt.setString(2, paraMap.get("boardno"));
					pstmt.setString(3, paraMap.get("commentno"));
					
					resultn5 = pstmt.executeUpdate();
					
				}catch (SQLException e) {
					e.printStackTrace();
					resultn5 = -1;
				}finally{
					close();
				}
				
			
			return resultn5;
		}


		//*** 댓글 쓴사람 아이디, 댓글번호, 댓글내용 가져오는 메소드 ***//
		@Override
		public Comments_freeDTO commentid(Map<String, String> paraMap) {
			

			Comments_freeDTO cfdto = new Comments_freeDTO();
			
			try {
				
				conn = ProjectDBConnection.getConn();
				
				String sql = " select fk_employee_id,  cf_commentno, cf_comments \n "+
							 " from tbl_comments_free\n "+
							 " where cf_commentno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, paraMap.get("commentno"));
				
				rs = pstmt.executeQuery();

				
				while(rs.next()) {
					
					
					
					cfdto.setFk_employee_id(rs.getString("fk_employee_id"));
					cfdto.setCf_commentno(rs.getInt("cf_commentno"));
					cfdto.setCf_comments(rs.getString("cf_comments"));
					}	
					
			
				
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{
				close();
			}
			
			return cfdto;
		}


		// ***8.댓글 삭제하기 메소드***
		@Override
		public int deleteComments(Map<String, String> paraMap) {
				
				int resultn6=0;
				
				try {
					
					conn = ProjectDBConnection.getConn();
					
					String sql = " delete from tbl_comments_free\n"+
								 " where cf_commentno = ? ";
					
					pstmt =conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("commentno"));
					
					resultn6 = pstmt.executeUpdate();
					
				}catch (SQLException e) {
					e.printStackTrace();
					resultn6 = -1;
				}finally{
					close();
				}
				
			
			return resultn6;
		}// end of public int deleteComments(Map<String, String> paraMap)

		
		
		
		
}
