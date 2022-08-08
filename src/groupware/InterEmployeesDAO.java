package groupware;

import java.util.List;
import java.util.Map;

public interface InterEmployeesDAO {

	int employeeRegister(EmployeesDTO empDto);

	// *** 사원번호를 생성해주는 메소드 *** //
	String makeEmployeeid(String department_name, String position);
	
	// *** 직속상사 성명 찾는 메소드 *** //
	String searchManager_id(String manager_name);
	
	// *** 부서번호를 알려주는 메소드 *** //
	String searchDept_id(String department_name);
	
	// *** 등록된 사원인지 확인하는 메소드 *** //
	int employeeCheck(String manager_name);
	
	// *** 회원가입시 이미 존재하는 연락처인지 확인하는 메소드 *** //
	boolean checkMobile(String mobile);
	// *** 회원가입시 이미 존재하는 이메일인지 확인하는 메소드 *** //
	boolean checkEmail(String email);
	
	// *** 로그인을 해주는 메소드 *** // 
	EmployeesDTO login(Map<String, String> paraMap);
	// *** 로그인 - 존재하는 사원번호인지 확인 *** //
	boolean isExistEmpId(String employee_id);

	// *** 내정보 조회 메소드 *** // 
	EmployeesDTO myInfo(String employee_id);

	// *** 관리자용 - 모든 회원 조회 메소드 *** //
	List<EmployeesDTO> showAllMember();
	// 관리자용 - 부서별,직급별 사원조회(select) 메소드 //
	List<EmployeesDTO> showMember(String update_info, String select_update);
	// 관리자 메뉴 - 5.퇴직금 조회 //
	String[] showPension(String employee_id);

	// *** 관리자용 - 회원정보 조회 메소드 *** //
	EmployeesDTO empInfo(String employee_id);
	// *** 관리자용 - 사원정보 수정(update) 메소드 *** //
	int updateInfo_admin(String select_update, String update_info, String s_employee_id);

	
	// *** 회원정보 수정(update) 메소드 *** //
	int updateMyinfo(String select_update, String update_info, String employee_id);

	
	// *** 사원번호 / 비밀번호 찾기 *** //
	
	// *** 사원번호 찾기 - 사원명 존재여부 확인 메소드 *** //
	boolean isNameExist(String name);
	// *** 사원번호 찾기 메소드 *** //
	String searchEmpId(String name, String mobile);
	// *** 비밀번호 찾기 - 사원번호 존재여부 확인 메소드 *** //
	boolean isEmployee_idExist(String employee_id);
	// *** 비밀번호 찾기 메소드 *** //
	String searchEmpPwd(String employee_id, String mobile);


	





	

	


	
	







	
	

}
