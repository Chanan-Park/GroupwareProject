package groupware;

import java.sql.*;
import java.util.*;

public class ReportApprovalDAO implements InterReportApprovalDAO {

	// field
	Connection conn = ProjectDBConnection.getConn();;
	PreparedStatement pstmt;
	ResultSet rs;
	CallableStatement cstmt;

	// method

	// 자원반납메소드
	private void close() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // end of private void close()

	// 기안문서번호 채번하기
	private int getReportNo() {

		int report_no = 0;

		try {
			String sql = "select seq_report_no.nextval as report_no from dual"; // 기안문서번호 채번
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next())
				report_no = rs.getInt("report_no"); // 기안문서번호

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return report_no;
	} // end of private int getReportNo()

	// 결재번호 채번하기
	private int getApprovalNo() {

		int approval_no = 0;

		try {
			String sql = "select seq_approval_no.nextval as approval_no from dual"; // 기안문서번호 채번
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next())
				approval_no = rs.getInt("approval_no"); // 기안문서번호

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return approval_no;
	} // end of private int getApprovalNo()

	// 결재할 사람들 알아오기 (select)
	private List<Map<String, String>> whoApprove(ReportDTO rdto) {

		List<Map<String, String>> mapList = new ArrayList<>();

		try {
			String sql = "select level - 1 AS levelno\n" + "     , employee_id as approval_employee_id\n"
					+ "from tbl_employees\n" + "where level > 1\n" + "start with employee_id = ? \n"
					+ "connect by prior manager_id = employee_id";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rdto.getFk_report_employee_id());
			rs = pstmt.executeQuery();

			// mapList 추가하기
			// 키: 컬럼명 값: 데이터값
			while (rs.next()) { // map 한개당 사람 한명
				Map<String, String> map = new HashMap<>();

				map.put("levelno", rs.getString("levelno")); // 서열
				map.put("approval_employee_id", rs.getString("approval_employee_id")); // 사번

				mapList.add(map);
			} // end of while

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return mapList;
	} // end of private List<Map<String, String>> whoApprove(ReportDTO rdto)

	// 결재 상신 (tbl_report에 insert + tbl_approval에 insert)
	@Override
	public int submitReport(ReportDTO rdto) {
		int result = 0;

		try {
			conn.setAutoCommit(false); // 수동커밋으로 전환

			int report_no = getReportNo(); // 기안문서번호 채번하기
			rdto.setReport_no(report_no); // dto에 문서번호 넣기

			String sql = "insert into tbl_report(report_no, fk_report_employee_id, rp_subject, rp_contents)\n"
					+ "values(?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, rdto.getReport_no());
			pstmt.setString(2, rdto.getFk_report_employee_id());
			pstmt.setString(3, rdto.getRp_subject());
			pstmt.setString(4, rdto.getRp_contents());

			int n1 = pstmt.executeUpdate();
			// insert가 성공되면 1

			if (n1 == 1) { // 위의 insert가 성공했을때
				int n2 = 0;

				List<Map<String, String>> mapList = whoApprove(rdto);// 결재할 사람들 알아오기
				int manager_cnt = mapList.size(); // 결재할 사람수

				for (Map<String, String> map : mapList) { // mapList(사람들 목록)안에 있는 map(사람)의 개수만큼 반복
					int approval_no = getApprovalNo(); // 결재번호채번

					sql = "insert into tbl_approval(approval_no, fk_report_no, levelno, fk_approval_employee_id)\n"
							+ "values(?, ?, ?, ?)";

					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, approval_no);
					pstmt.setInt(2, rdto.getReport_no());
					pstmt.setString(3, map.get("levelno"));
					pstmt.setString(4, map.get("approval_employee_id"));

					n2 += pstmt.executeUpdate(); // insert가 성공되면 1씩 누적
				}

				if (n2 == manager_cnt) { // insert가 사람수만큼 완료되었으면
					conn.commit(); // 커밋
					result = 1; // 1 리턴
				}

			}

		} catch (SQLException e) { // 오류발생하면
			result = -1; // -1 리턴
			try {
				conn.rollback(); // 롤백
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			close();
			try {
				conn.setAutoCommit(true); // 오토커밋으로 전환
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	} // end of public int submitReport(ReportDTO rdto)

	// 결재 조회하기
	@Override
	public List<ReportDTO> viewReportList(EmployeesDTO empDto, String choice_number) {
		List<ReportDTO> reportList = new ArrayList<>();

		try {
			cstmt = conn.prepareCall("{call pcd_reportInfo(?,?,?,?)}");
			cstmt.setString(1, empDto.getEmployee_id());
			cstmt.setString(2, choice_number);
			cstmt.setString(3, empDto.getDepartment_id());
			cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.executeQuery();

			rs = (ResultSet) cstmt.getObject(4);

			if ("1".equals(choice_number)) { // 진행중 결재
				while (rs.next()) {
					ReportDTO rdto = new ReportDTO();

					rdto.setReport_no(rs.getInt("report_no")); // 기안문서번호
					rdto.setWritedate(rs.getString("writedate")); // 기안작성일자
					rdto.setRp_subject(rs.getString("rp_subject")); // 기안제목

					EmployeesDTO rpEDto = new EmployeesDTO();
					rpEDto.setName(rs.getString("report_name")); // 기안자 이름
					rdto.setRpEDto(rpEDto); // 기안자 정보

					EmployeesDTO apEDto = new EmployeesDTO();
					apEDto.setName(rs.getString("approval_name")); // 결재자 이름
					rdto.setApEDto(apEDto); // 결재자 정보

					ApprovalDTO apDto = new ApprovalDTO();
					apDto.setLevelno(rs.getInt("levelno"));
					; // 결재단계
					rdto.setApDto(apDto); // 결재정보

					reportList.add(rdto);
				} // end of while
			}

			else { // 완료된 결재
				while (rs.next()) {
					ReportDTO rdto = new ReportDTO();

					rdto.setReport_no(rs.getInt("report_no")); // 기안문서번호
					rdto.setWritedate(rs.getString("writedate")); // 기안작성일자
					rdto.setRp_subject(rs.getString("rp_subject")); // 기안제목

					EmployeesDTO rpEDto = new EmployeesDTO();
					rpEDto.setName(rs.getString("report_name")); // 기안자 이름
					rdto.setRpEDto(rpEDto); // 기안자 정보

					EmployeesDTO apEDto = new EmployeesDTO();
					apEDto.setName(rs.getString("approval_name")); // 결재자 이름
					rdto.setApEDto(apEDto); // 결재자 정보

					ApprovalDTO apDto = new ApprovalDTO();
					apDto.setApproval_status(rs.getString("approval_status")); // 결재상태
					apDto.setApproval_date(rs.getString("approval_date")); // 결재일자
					rdto.setApDto(apDto); // 결재정보

					reportList.add(rdto);
				} // end of while
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return reportList;
	} // end of public List<ReportDTO> viewReportList(EmployeesDTO empDto, String
		// choice_number)

	// 기안 문서 내용을 조회하기 (select)
	@Override
	public ReportDTO viewReportContents(EmployeesDTO empDto, String reportNo) {
		ReportDTO rdto = null;

		try {
			cstmt = conn.prepareCall("{call pcd_view_report(?,?,?)}");
			cstmt.setString(1, reportNo);
			cstmt.setString(2, empDto.getDepartment_id());
			cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.executeUpdate();

			rs = (ResultSet) cstmt.getObject(3);

			if (rs.next()) {
				rdto = new ReportDTO();
				EmployeesDTO rpEDto = new EmployeesDTO(); // tbl_employees 기안자

				rdto.setReport_no(Integer.parseInt(reportNo)); // 문서번호
				rdto.setRp_subject(rs.getString("rp_subject")); // 제목
				rdto.setRp_contents(rs.getString("rp_contents")); // 내용
				rdto.setWritedate(rs.getString("writedate")); // 작성일시

				rpEDto.setName(rs.getString("rp_name")); // 기안자 이름
				DepartmentsDTO deptDto = new DepartmentsDTO();
				deptDto.setDepartment_name(rs.getString("department_name")); // 기안자 부서
				rpEDto.setDeptDto(deptDto);
				rpEDto.setPosition(rs.getString("POSITION")); // 기안자 직급
				rpEDto.setEmployee_id(rs.getString("fk_report_employee_id"));
				rdto.setRpEDto(rpEDto);

			} else {
				System.out.println("\n>> [경고] 문서번호 " + reportNo + "은(는) 존재하지 않습니다. <<");
			}

		} catch (SQLException e) {
			if (e.getErrorCode() == 20001)
				System.out.println("\n>> [경고] 다른 부서의 문서는 접근할 수 없습니다. <<");
			else if (e.getErrorCode() == 1403)
				System.out.println("\n>> [경고] 문서번호 " + reportNo + "은(는) 존재하지 않습니다. <<");
			else if (e.getErrorCode() == 6502)
				System.out.println("\n>> [경고] 문서번호는 숫자로 입력하세요. <<");
			else
				System.out.println(e.getMessage());
		} finally {
			close();
		}

		return rdto;

	} // end of public ReportDTO viewReportContents(EmployeesDTO empDto, Map<String,
		// String> paraMap)

	// 특정 문서에 대한 코멘트 가져오기 메소드
	@Override
	public List<ApprovalDTO> commentList(String reportNo) {

		List<ApprovalDTO> commentList = new ArrayList<>();

		try {

			String sql = "select nvl(comments, '(없음)') as comments, decode(approval_status, 1, '승인', 2, '반려', 3, '후결') as approval_status, name, approval_date\n"+
					"from tbl_approval join tbl_employees\n"+
					"on fk_approval_employee_id = employee_id\n"+
					"where fk_report_no = ? and approval_status in (1, 2, 3) order by approval_date";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reportNo);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ApprovalDTO apDto = new ApprovalDTO();
				apDto.setComments(rs.getString("comments")); // 결재의견
				apDto.setApproval_status(rs.getString("approval_status")); // 결재상태
				apDto.setApproval_date(rs.getString("approval_date")); // 결재일자

				EmployeesDTO apEDto = new EmployeesDTO();
				apEDto.setName(rs.getString("name")); // 결재자이름
				apDto.setApEDto(apEDto);

				commentList.add(apDto);

			} // end of while

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return commentList;
	} // end of public List<ApprovalDTO> commentList(String reportNo)

	// 문서내용 수정하기 메소드
	@Override
	public int editReport(Map<String, String> paraMap) {
		int result = 0;
		try {

			cstmt = conn.prepareCall("{call pcd_edit_report(?,?,?)}");

			cstmt.setString(1, paraMap.get("subject"));
			cstmt.setString(2, paraMap.get("contents"));
			cstmt.setString(3, paraMap.get("report_no"));

			result = cstmt.executeUpdate();

		} catch (SQLException e) {
			if (e.getErrorCode() == 20003)
				System.out.println("\n[경고] 이미 결재 진행중인 문서이므로 수정이 불가합니다.");
			else
				e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		return result;
	} // end of public void editReportContents(EmployeesDTO empDto, Scanner sc)

	// 결재 처리하기 메소드
		@Override
		public int approve(EmployeesDTO empDto, Map<String, String> paraMap) {

			int result = 0;
			
			try {
				// 이미 처리된 기안은 처리 불가
				// 부하직원이 승인을 안했으면 처리 불가
				boolean available = canIApprove(empDto, paraMap);

				if (!available) {
					
					result = -1;
					return result;
				}

				else {
					cstmt = conn.prepareCall("{call pcd_tbl_approval_update(?,?,?,?)}");

					cstmt.setString(1, paraMap.get("report_no")); // 기안문서번호
					cstmt.setString(2, empDto.getEmployee_id()); // 결재자 사번
					cstmt.setString(3, paraMap.get("decision")); // 결재처리 (1 결재 2 반려)
					cstmt.setString(4, paraMap.get("comments"));

					int n = cstmt.executeUpdate();

					if (n > 0)
						result = 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				result = -1;
			} finally {
				close();
			}
			return result;
		} // end of public int approve(EmployeesDTO empDto)

	// 처리 가능한 기안인지 아닌지 알려주는 메소드
	private boolean canIApprove(EmployeesDTO empDto, Map<String, String> paraMap) {
		
		boolean result = false;

		// 이미 처리된 기안은 처리 불가
		// 부하직원이 승인을 안했으면 처리 불가

		try {
			cstmt = conn.prepareCall("{call pcd_can_approve(?,?,?)}");

			cstmt.setString(1, paraMap.get("report_no")); // 기안문서번호
			cstmt.setString(2, empDto.getEmployee_id()); // 결재자 사번
			cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

			cstmt.executeUpdate();

			int n = cstmt.getInt(3);

			if (n == 100) {
				result = true;
			} else if (n == 200) {
				result = false;
				System.out.println("\n[경고] 이미 결재 처리된 기안입니다.");
			} else if (n == 300) {
				result = false;
				System.out.println("\n[경고] 아직 처리할 수 없는 기안입니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	} // end of private boolean canIApprove(EmployeesDTO empDto, Map<String, String>
		// paraMap)

	// 기안서 삭제하기 메소드
	@Override
	public int deleteReport(String report_no) {

		int result = 0;
		try {

			cstmt = conn.prepareCall("{call pcd_delete_report(?)}");

			cstmt.setString(1, report_no);
			result = cstmt.executeUpdate();

		} catch (SQLException e) {
			if (e.getErrorCode() == 20003)
				System.out.println("\n[경고] 이미 결재 진행중인 문서이므로 삭제가 불가합니다.");
			else
				e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		return result;

	}

}
