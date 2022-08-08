package groupware;

import java.sql.*;
import java.util.*;

public class Board_freeDAO implements InterBoard_freeDAO {

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

	// 1.***글 목록보기 메소드 ***
	@Override
	public List<Map<String, String>> boardList() {

		List<Map<String, String>> bfboardList = new ArrayList<>();

		try {
			conn = ProjectDBConnection.getConn();

			String sql = "with A as (\n"+
					"select fk_bf_boardno, count(*) as cmtcnt\n"+
					"from tbl_comments_free\n"+
					"group by fk_bf_boardno\n"+
					")\n"+
					"select distinct BF.bf_boardno, BF.fk_employee_id\n"+
					"     , BF.bf_subject || case when A.cmtcnt is not null then ' [' || A.cmtcnt || ']' else null end as bf_subject\n"+
					"     , BF.bf_contents, BF.bf_viewcount, BF.bf_writedate\n"+
					"from tbl_board_free BF LEFT JOIN A\n"+
					"on BF.bf_boardno = A.fk_bf_boardno\n"+
					"LEFT JOIN tbl_comments_free CF\n"+
					"on BF.bf_boardno = CF.fk_bf_boardno\n"+
					"order by bf_boardno desc";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				Map<String, String> boardList = new HashMap<>();

				boardList.put("bf_boardno", rs.getString("bf_boardno"));
				boardList.put("fk_employee_id", rs.getString("fk_employee_id"));
				boardList.put("bf_contents", rs.getString("bf_contents"));
				boardList.put("bf_viewcount", rs.getString("bf_viewcount"));
				boardList.put("bf_subject", rs.getString("bf_subject"));
				boardList.put("bf_writedate", rs.getString("bf_writedate"));

				bfboardList.add(boardList);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return bfboardList;
	}

	// *** 2. 글조회하기 & 조회수 카운트 메소드 (transaction 사용) ***
	// 글 조회시 select가 완료되면 조회수를 올려주는 update를 시행한다.
	@Override
	public int veiwcount(Map<String, String> paraMap) {

		Board_freeDTO bfdto = new Board_freeDTO();
		;
		int result = 0;
		// 코드의 반복을 피하기 위해 메소드화해서 트랜젝션을 했다.(글조회메소드가 반복적으로 필요해서 개별 메소드화했다)
		// 글 조회해주는 메소드(select)
		bfdto = veiwcount2(paraMap);

		// 글쓴이와 다른사람이 글을 봤을때만 조회수를 올려준다 (tranaction 사용으로 ctrl페이지에 조건을 넣어주지못해 paramap으로
		// 가져왔음)
		if (!(paraMap.get("empDto.getEmployee_id()").equals(bfdto.getFk_employee_id())) & bfdto != null) {

			// 조회수 올려주는 메소드(upadate)
			result = count(paraMap);

		}
		return result;
	}// end of Board_freeDTO veiwcount(Map<String, String> paraMap)

	// 트랜젝션없는 글조회기능

	public Board_freeDTO veiwcount2(Map<String, String> paraMap) {

		Board_freeDTO bfdto = null;

		try {

			conn = ProjectDBConnection.getConn();

		      String sql = "with A as (\n"+
		              "select fk_bf_boardno, count(*) as cmtcnt\n"+
		              "from tbl_comments_free\n"+
		              "group by fk_bf_boardno\n"+
		              ")\n"+
		              "select distinct BF.bf_boardno, BF.fk_employee_id\n"+
		              "     , BF.bf_subject || case when A.cmtcnt is not null then ' [' || A.cmtcnt || ']' else null end as bf_subject\n"+
		              "     , BF.bf_contents, BF.bf_viewcount, BF.bf_writedate\n"+
		              "from tbl_board_free BF LEFT JOIN A\n"+
		              "on BF.bf_boardno = A.fk_bf_boardno\n"+
		              "LEFT JOIN tbl_comments_free CF\n"+
		              "on BF.bf_boardno = CF.fk_bf_boardno\n"+
		              "where BF.bf_boardno = ?" +
		              "order by bf_boardno desc";
		      
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));

			rs = pstmt.executeQuery();

			if (rs.next()) {

				bfdto = new Board_freeDTO();

				bfdto.setBf_boardno(rs.getInt("bf_boardno"));
				bfdto.setFk_employee_id(rs.getString("fk_employee_id"));
				bfdto.setBf_subject(rs.getString("bf_subject"));
				bfdto.setBf_contents(rs.getString("bf_contents"));
				bfdto.setBf_viewcount(rs.getInt("bf_viewcount"));
				bfdto.setBf_writedate(rs.getString("bf_writedate"));

			} // end of while( rs.next())

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			close();
		}

		return bfdto;

	}// end of Board_freeDTO veiwcount(Map<String, String> paraMap)

	// 트랜젝션없는 조회수 올라가는 메소드

	public int count(Map<String, String> paraMap) {

		// Board_freeDTO bfdto = new Board_freeDTO();;

		int result = 0;

		try {

			conn = ProjectDBConnection.getConn();

			String sql_1 = " update tbl_board_free set bf_viewcount= bf_viewcount+1\n" + " where bf_boardno = ? ";

			pstmt = conn.prepareStatement(sql_1);
			pstmt.setString(1, paraMap.get("boardno"));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return result;
	}// end of int count(Map<String, String> paraMap)

	// *** 3.게시판 글쓰기
	@Override
	public int write(Board_freeDTO bfdto) {

		int resultn = 0;

		try {
			conn = ProjectDBConnection.getConn();

			String sql = " insert into tbl_board_free(bf_boardno, fk_employee_id, bf_subject ,  bf_contents)\n"
					+ " values (SEQ_BFNO.nextval,?,?,?) ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bfdto.getFk_employee_id());
			pstmt.setString(2, bfdto.getBf_subject());
			pstmt.setString(3, bfdto.getBf_contents());

			resultn = pstmt.executeUpdate(); // 1 or 0

		} catch (SQLException e) {
			e.printStackTrace();
			resultn = -1;
		} finally {
			close();
		}

		return resultn;
	}// end of write(Board_freeDTO bfdto)

	// ***4.글수정 메소드 ***
	@Override
	public int updateBoard(Map<String, String> paraMap) {

		int resultn2 = 0;

		try {

			conn = ProjectDBConnection.getConn();

			String sql = " update tbl_board_free set bf_subject = ? ,  bf_contents = ? " + " where bf_boardno = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("bf_subject"));
			pstmt.setString(2, paraMap.get("bf_contents"));
			pstmt.setString(3, paraMap.get("boardno"));

			resultn2 = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			resultn2 = -1;
		} finally {
			close();
		}

		return resultn2;
	}

	// ***5.글삭제하기 메소드***
	@Override
	public int deleteBoard(Map<String, String> paraMap) {

		int resultn3 = 0;

		try {
			conn = ProjectDBConnection.getConn();

			String sql = "delete from tbl_board_free\n" + "where bf_boardno = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardno"));

			resultn3 = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			resultn3 = -1;
		}

		return resultn3;
	}

}
