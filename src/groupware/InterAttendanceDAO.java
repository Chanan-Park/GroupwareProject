package groupware;

import java.util.*;

public interface InterAttendanceDAO {
	
	//////////////////////////////////////////////////////////////////////////////

	//모든 사원근태조회
	List<AttendanceDTO> showAllAttendace();
	
	//월별 근태조회
	List<AttendanceDTO> showMonthlyAttendance(Map<String, String> ad_month_map);
	
	//날짜별 근태조회
	List<AttendanceDTO> showDailyAttendance(Map<String, String> ad_date_map);

	//사원번호별 근태조회
	List<AttendanceDTO> showEmpidAttendance(Map<String, String> ad_empid_map);

	//사원번호 가져오기
	List<EmployeesDTO> showAllEmployeeId();


	
	///////////////////////////////////////////////////////////////////////////////
	
	//출근 처리
	int requestArrival(String employee_id);
	
	//퇴근 처리
	int requestDepart(String employee_id);
	
	//퇴근처리시 퇴근 컬럼 null인지 확인
	String checkDepart(String employee_id);
	
	//내 근태 조회
	List<AttendanceDTO> showMyAttendance(EmployeesDTO emp);
	
	//내 근태 월별
	List<AttendanceDTO> showMyMonthlyAttendance(EmployeesDTO emp, Map<String, String> monthMap);
	
	
	


	
	
	
	

}
