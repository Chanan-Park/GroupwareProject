package groupware;

import java.sql.*;
import java.util.*;

public class AttendanceDAO implements InterAttendanceDAO {
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

	
	
	//모든사원 근태조회
	@Override
	public List<AttendanceDTO> showAllAttendace() {
		List<AttendanceDTO> at_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				at_list.add(addto);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return at_list;
	}//end of showAllAttendace

	
	
	//월별 근태조회
	@Override
	public List<AttendanceDTO> showMonthlyAttendance(Map<String, String> ad_month_map) {
		List<AttendanceDTO> at_month_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"where to_char(attendance_date,'yyyy/mm') = ? \n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ad_month_map.get("ad_month"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				at_month_list.add(addto);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return at_month_list;
	}
	
	
	
	//날짜별 근태조회
	@Override
	public List<AttendanceDTO> showDailyAttendance(Map<String, String> ad_date_map) {
		List<AttendanceDTO> at_date_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"where to_char(attendance_date,'yyyy/mm/dd') = ? \n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ad_date_map.get("ad_date"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				at_date_list.add(addto);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return at_date_list;
	}//end of showDateAttendance


	//사원번호별 근태조회
	@Override
	public List<AttendanceDTO> showEmpidAttendance(Map<String, String> ad_empid_map) {
		List<AttendanceDTO> ad_empid_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"where S.fk_employee_id = ?\n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ad_empid_map.get("ad_empid"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				ad_empid_list.add(addto);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return ad_empid_list;
	}//end of showEmpidAttendance


	//사원번호 가져오기
	@Override
	public List<EmployeesDTO> showAllEmployeeId() {
		List<EmployeesDTO> empid_list = new ArrayList<EmployeesDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select employee_id\n"+
					"from tbl_employees";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeesDTO empdto = new EmployeesDTO();
				empdto.setEmployee_id(rs.getString(1));
				empid_list.add(empdto);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return empid_list;
	}


	//출근 처리
	@Override
	public int requestArrival(String employee_id) {
		int arrival = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "insert into tbl_attendance(fk_employee_id,arrival_time,attendance_date)\n"+
						 "values(?, to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'), to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd'))";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id);
			
			arrival = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			if(e.getErrorCode()==1) {
				System.out.println("[안내] 이미 출근 처리 하셨습니다.\n");
				arrival = -1;
			}
			else
				e.printStackTrace();
		} finally {
			close();
		}
		return arrival;
	}//end of public int requestArrival(String employee_id)


	//퇴근 처리
	@Override
	public int requestDepart(String employee_id) {
		int depart = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "update tbl_attendance set depart_time = to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')\n"+
					"where fk_employee_id = ? and\n"+
					"to_date(to_char(attendance_date,'yyyy-mm-dd'),'yyyy-mm-dd') = to_date(to_char(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd'),'yyyy-mm-dd'),'yyyy-mm-dd')";

			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id);
			
			depart = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return depart;
	}//end of public int requestDepart(String employee_id)

	//퇴근처리시 퇴근 컬럼 null인지 확인
	@Override
	public String checkDepart(String employee_id) {
		String result = "nodata";
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select depart_time\n"+
						"from tbl_attendance\n"+
						"where fk_employee_id = ? and to_date(attendance_date,'yyyy-mm-dd') = to_date(sysdate,'yyyy-mm-dd')";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			close();
		}
		return result; //nodata => 출근처리 안함, null => 퇴근처리 안함, 값이 있음 => 퇴근처리함
	}
	
	
	
	//내 근태 조회
	@Override
	public List<AttendanceDTO> showMyAttendance(EmployeesDTO emp) {
		List<AttendanceDTO> myad_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"where S.fk_employee_id = ? and to_char(attendance_date,'yyyy-mm')  = to_char(sysdate,'yyyy-mm')\n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emp.getEmployee_id());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				myad_list.add(addto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		
		return myad_list;
	}


	//내 근태 월별 조회
	@Override
	public List<AttendanceDTO> showMyMonthlyAttendance(EmployeesDTO emp, Map<String, String> monthMap) {
		List<AttendanceDTO> myad_month_list = new ArrayList<AttendanceDTO>();
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "\n"+
					"select distinct S.fk_employee_id\n"+
					"       , S.name\n"+
					"       , to_char(S.attendance_date,'yyyy-mm-dd') as attendance_date\n"+
					"       , nvl(to_char(S.arrival_time,'hh24:mi'),'기록없음') as arrival_time\n"+
					"       , nvl(to_char(S.depart_time,'hh24:mi'),'기록없음') as depart_time\n"+
					"       ,case \n"+
					"            when arrival = '정상출근' and depart in ('정상퇴근','결근') then '출근'\n"+
					"            when arrival = '지각' and depart = '조퇴' then '지각/조퇴'\n"+
					"            when arrival = '지각' then '지각'\n"+
					"            when depart = '조퇴' then '조퇴'\n"+
					"            when arrival = '결근' and depart = '결근' and to_date(attendance_date,'yyyy/mm/dd') between to_date(start_date,'yyyy/mm/dd') and to_date(end_date,'yyyy/mm/dd')\n"+
					"                    then '연차'\n"+
					"            else '결근' \n"+
					"            end as status\n"+
					"from view_attendance_status S left JOIN tbl_annual_leave AL\n"+
					"ON S.fk_employee_id = AL.fk_employee_id and attendance_date between start_date and end_date\n"+
					"where S.fk_employee_id = ? and to_char(attendance_date,'yyyy/mm')  = ? \n"+
					"order by to_date(attendance_date,'yyyy/mm/dd')";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emp.getEmployee_id());
			pstmt.setString(2, monthMap.get("month"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AttendanceDTO addto = new AttendanceDTO();
				EmployeesDTO empdto = new EmployeesDTO();
				
				addto.setFk_employee_id(rs.getString(1));
				empdto.setName(rs.getString(2));
				addto.setEmpdto(empdto);
				addto.setAttendance_date(rs.getString(3));
				addto.setArrival_time(rs.getString(4));
				addto.setDepart_time(rs.getString(5));
				addto.setStatus(rs.getString(6));
				
				myad_month_list.add(addto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return myad_month_list;
	}

	
	
	

	}

