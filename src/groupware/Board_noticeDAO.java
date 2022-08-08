package groupware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board_noticeDAO implements InterBoard_noticeDAO {

	// field , attribute , property , 속성
	Connection conn = null;

	PreparedStatement pstmt = null;

	ResultSet rs = null;

	// === 자원반납을 해주는 메소드 === //
	// -------------------------------------------------------------------
	private void close() {

		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	} // end of private void close() -----------------------------------

	// ***1.글목록 메소드 ***
	@Override
	public List<Map<String, String>> boardNoticeList() {

		List<Map<String, String>> boardNoticeList = new ArrayList<>();

		try {
			conn = ProjectDBConnection.getConn();

			String sql = " select bn_boardno, fk_employee_id, bn_subject ,bn_contents ,bn_viewcount ,bn_writedate \n"
					+ " from tbl_board_notice " + " order by bn_boardno desc ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				Map<String, String> boardList = new HashMap<>();

				boardList.put("bn_boardno", rs.getString("bn_boardno"));
				boardList.put("fk_employee_id", rs.getString("fk_employee_id"));
				boardList.put("bn_contents", rs.getString("bn_contents"));
				boardList.put("bn_viewcount", rs.getString("bn_viewcount"));
				boardList.put("bn_subject", rs.getString("bn_subject"));
				boardList.put("bn_writedate", rs.getString("bn_writedate"));

				boardNoticeList.add(boardList);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return boardNoticeList;

	}// end of public List<Map<String, String>> boardNoticeList()

	// *** 2. 글조회하기 & 조회수 카운트 메소드 (transaction 사용) ***
	// 글 조회시 select가 완료되면 조회수를 올려주는 update를 시행한다.

	@Override
	public int noticeVeiwcount(Map<String, String> paraMap) {

		Board_noticeDTO bndto = new Board_noticeDTO();
		;
		int result = 0;

		// 코드의 반복을 피하기 위해 메소드화해서 트랜젝션을 했다.(글조회메소드가 반복적으로 필요해서 개별 메소드화했다)
		// 글 조회해주는 메소드(select)
		bndto = noticeVeiwcount2(paraMap);

		// 글쓴이와 다른사람이 글을 봤을때만 조회수를 올려준다 (tranaction 사용으로 ctrl페이지에 조건을 넣어주지못해 paramap으로
		// 가져왔음)
		if (!(paraMap.get("empDto.getEmployee_id()").equals(bndto.getFk_employee_id())) & bndto != null) {

			// 조회수 올려주는 메소드(upadate)
			result = Noticecount(paraMap);

		}

		return result;
	}// end of Board_freeDTO veiwcount(Map<String, String> paraMap)

	// 트랜젝션없는 글조회기능
	@Override
	public Board_noticeDTO noticeVeiwcount2(Map<String, String> paraMap) {

		Board_noticeDTO bndto = new Board_noticeDTO();

		try {

			conn = ProjectDBConnection.getConn();

			String sql = " select bn_boardno, fk_employee_id, bn_subject, bn_contents, bn_viewcount, bn_writedate\n"
					+ " from tbl_board_notice\n" + " where bn_boardno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));

			rs = pstmt.executeQuery();

			while (rs.next()) {

				bndto.setBn_boardno(rs.getInt("bn_boardno"));
				bndto.setFk_employee_id(rs.getString("fk_employee_id"));
				bndto.setBn_subject(rs.getString("bn_subject"));
				bndto.setBn_viewcount(rs.getInt("bn_viewcount"));
				bndto.setBn_writedate(rs.getString("bn_writedate"));
				bndto.setBn_contents(rs.getString("bn_contents"));

			} // end of while( rs.next())

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			close();
		}

		return bndto;

	}// end of Board_noticeDTO noticVeiwcount2(Map<String, String> paraMap)

	// 트랜젝션없는 조회수 올라가는 메소드

	public int Noticecount(Map<String, String> paraMap) {

		int result = 0;

		try {

			conn = ProjectDBConnection.getConn();

			String sql_1 = " update tbl_board_notice set bn_viewcount= bn_viewcount+1\n" + " where bn_boardno = ? ";

			pstmt = conn.prepareStatement(sql_1);
			pstmt.setString(1, paraMap.get("boardno"));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return result;
	}// end of int count(Map<String, String> paraMap)

	// ***3.공지게시판 글쓰기 메소드***
	@Override
	public int writeNotice(Board_noticeDTO bndto) {

		int resultn2 = 0;

		try {
			conn = ProjectDBConnection.getConn();

			String sql = " insert into tbl_board_notice(bn_boardno, fk_employee_id, bn_contents, bn_subject)\n"
					+ " values (SEQ_BNNO.nextval,?,?,?) ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bndto.getFk_employee_id());
			pstmt.setString(2, bndto.getBn_subject());
			pstmt.setString(3, bndto.getBn_contents());

			resultn2 = pstmt.executeUpdate(); // 1 or 0

		} catch (SQLException e) {
			e.printStackTrace();
			resultn2 = -1;
		} finally {
			close();
		}
		return resultn2;
	}

	// *** 4.공지게시판 글 수정 메소드***
	@Override
	public int updateNoticeBoard(Map<String, String> paraMap) {
		int resultn3 = 0;

		try {

			conn = ProjectDBConnection.getConn();

			String sql = " update tbl_board_notice set bn_subject = ? ,  bn_contents = ? " + " where bn_boardno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("bn_subject"));
			pstmt.setString(2, paraMap.get("bn_contents"));
			pstmt.setString(3, paraMap.get("boardno"));

			resultn3 = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			resultn3 = -1;
		} finally {
			close();
		}

		return resultn3;

	}

	// ***5.글삭제 메소드***
	@Override
	public int deleteNoticeBoard(Map<String, String> paraMap) {
		int resultn4 = 0;

		try {
			conn = ProjectDBConnection.getConn();

			String sql = "delete from tbl_board_notice\n" + "where bn_boardno = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));

			resultn4 = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			resultn4 = -1;
		}

		return resultn4;
	}// end of public int deleteNoticeBoard(Map<String, String> paraMap)-----

}
