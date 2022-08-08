package groupware;

import java.sql.*;
import java.util.*;

public class AnnualLeaveDAO implements InterAnnualLeaveDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//자원반납 메소드
	private void close() {
		try {
			if (rs != null ) rs.close();
			if (pstmt != null ) pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//end close()
	
	
	// 사용한 연차 개수 보여주기
	@Override
	public int showAnuualLeave(EmployeesDTO emp) {
		
		int leave_cnt = 0; 
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select sum(end_date - start_date + 1) as leave_cnt\n"+
						 "from tbl_annual_leave\n"+
						 "where fk_employee_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emp.getEmployee_id());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				leave_cnt =  rs.getInt(1); //기본값 12개에서 사용한 연차개수(select문의 leave_cnt)을 차감해준다.
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return leave_cnt; //사용한 연차개수
		
	}//end of public int showAnuualLeave(EmployeesDTO emp)
	
	
	// 자기자신 연차 신청 내역 조회
	@Override
	public List<AnnualLeaveDTO> showAnnualLeave(EmployeesDTO emp) {
		List<AnnualLeaveDTO> al_list = new ArrayList<AnnualLeaveDTO>();
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select fk_employee_id, E.name, to_char(start_date,'yyyy-mm-dd'), to_char(end_date,'yyyy-mm-dd')\n"+
						 "from tbl_annual_leave AL JOIN tbl_employees E\n"+
						 "ON AL.fk_employee_id = E.employee_id\n"+
						 "where E.employee_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emp.getEmployee_id());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AnnualLeaveDTO aldto = new AnnualLeaveDTO();
				aldto.setFk_employee_id(rs.getString(1));
				
				EmployeesDTO empdto = new EmployeesDTO();
				empdto.setName(rs.getString(2));
				aldto.setEmpdto(empdto);
				
				aldto.setStart_date(rs.getString(3));
				
				aldto.setEnd_date(rs.getString(4));
				
				al_list.add(aldto);
			}//end of while
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			close();
		}
		return al_list;
	}
	   //연차신청 => insert
	   @Override
	   public int requestAnnualLeave(EmployeesDTO emp, AnnualLeaveDTO al) {
	      int result = 0;
	      
	      try {
	         conn = ProjectDBConnection.getConn();
	         
	         String sql = "insert into tbl_annual_leave(fk_employee_id, start_date, end_date)\n"+
	                          "values(?, to_date(?,'yyyy-mm-dd'), to_date(?,'yyyy-mm-dd')) ";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, emp.getEmployee_id());
	         pstmt.setString(2, al.getStart_date() );
	         pstmt.setString(3, al.getEnd_date() );
	         
	         result = pstmt.executeUpdate(); // 1(성공) 또는 0
	      } catch(SQLException e) {
	         if( e.getErrorCode() == 1847) {
	            // ORA-01847: 달의 날짜는 1에서 말일 사이어야 합니다
	            System.out.println("[경고] 유효한 날짜로 다시 입력해주세요. \n");
	         }
	         else {
	            e.printStackTrace();   // 에러의 발생근원지를 찾아서 단계별로 에러를 출력합니다.
	         }
	      } finally {
	         close();
	      }
	      return result;// 0(SQL실패) 또는 1(insert 성공)
	   }//end of requestAnnualLeave
	
	
	//모든 사원 연차조회, select 여러행, 여러컬럼
	@Override
	public List<AnnualLeaveDTO> showAllAnnualLeave() {
		List<AnnualLeaveDTO> al_all_list = new ArrayList<AnnualLeaveDTO>();
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select fk_employee_id, E.name, to_char(start_date,'yyyy-mm-dd'), to_char(end_date,'yyyy-mm-dd')\n"+
						 "from tbl_annual_leave AL JOIN tbl_employees E\n"+
						 "ON AL.fk_employee_id = E.employee_id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AnnualLeaveDTO aldto = new AnnualLeaveDTO();
				aldto.setFk_employee_id(rs.getString(1));
				
				EmployeesDTO empdto = new EmployeesDTO();
				empdto.setName(rs.getString(2));
				aldto.setEmpdto(empdto);
				
				aldto.setStart_date(rs.getString(3));
				aldto.setEnd_date(rs.getString(4));
				
				al_all_list.add(aldto);
			}//end of while
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			close();
		}
		return al_all_list;
	}//end of showAllAnnualLeaves

	
	//특정 사원 연차조회, select 여러행, 여러컬럼
	@Override
	public List<AnnualLeaveDTO> showAnnualLeave(Map<String,String> empidMap) {
		List<AnnualLeaveDTO> al_list = new ArrayList<AnnualLeaveDTO>();
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select fk_employee_id, E.name, to_char(start_date,'yyyy-mm-dd'), to_char(end_date,'yyyy-mm-dd')\n"+
						 "from tbl_annual_leave AL JOIN tbl_employees E\n"+
						 "ON AL.fk_employee_id = E.employee_id\n"+
						 "where E.employee_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empidMap.get("empid"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AnnualLeaveDTO aldto = new AnnualLeaveDTO();
				aldto.setFk_employee_id(rs.getString(1));
				
				EmployeesDTO empdto = new EmployeesDTO();
				empdto.setName(rs.getString(2));
				aldto.setEmpdto(empdto);
				
				aldto.setStart_date(rs.getString(3));
				
				aldto.setEnd_date(rs.getString(4));
				
				al_list.add(aldto);
			}//end of while
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			close();
		}
		return al_list;
	}//end of showAllAnnualLeaves




	//부서별 연차조회
	@Override
	public List<AnnualLeaveDTO> showDeptAnnualLeave(Map<String, String> deptidMap) {
		List<AnnualLeaveDTO> dept_list = new ArrayList<AnnualLeaveDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select E.department_id, D.department_name, fk_employee_id, name, to_char(start_date,'yyyy-mm-dd'), to_char(end_date,'yyyy-mm-dd')\n"+
					"from tbl_annual_leave JOIN tbl_employees E\n"+
					"ON fk_employee_id = E.employee_id\n"+
					"JOIN tbl_departments D\n"+
					"ON E.department_id = D.department_id\n"+
					"where E.department_id = ? ";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deptidMap.get("deptid"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeesDTO empdto = new EmployeesDTO();
				AnnualLeaveDTO aldto = new AnnualLeaveDTO();
				
				empdto.setDepartment_id(rs.getString(1));
				DepartmentsDTO deptDto = new DepartmentsDTO();
				deptDto.setDepartment_name(rs.getString(2)); 
				empdto.setDeptDto(deptDto);
				empdto.setEmployee_id(rs.getString(3));
				empdto.setName(rs.getString(4));
				aldto.setEmpdto(empdto);
				
				aldto.setStart_date(rs.getString(5));
				aldto.setEnd_date(rs.getString(6));
				
				dept_list.add(aldto);
			}
		} catch(SQLException e) {
			
		}
		
		return dept_list; 
	}



	
	
	
	
	

}
