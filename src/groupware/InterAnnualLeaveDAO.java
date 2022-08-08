package groupware;

import java.util.*;

public interface InterAnnualLeaveDAO {

	
	//모든 사원 연차조회
	List<AnnualLeaveDTO> showAllAnnualLeave();
	
	//특정 사원 연차 조회
	List<AnnualLeaveDTO> showAnnualLeave(Map<String,String> empidMap);
	
	//부서별 연차조회
	List<AnnualLeaveDTO> showDeptAnnualLeave(Map<String,String> deptidMap);


	
	////////////////////////////////////////////////////////////////////////////////////
	
	
	// 사용한 연차 개수 조회
	int showAnuualLeave(EmployeesDTO emp);

	// 자기자신 연차 신청 내역 조회
	List<AnnualLeaveDTO> showAnnualLeave(EmployeesDTO emp);
	
	//연차신청 => 시작한날짜, 마지막날짜 insert
	int requestAnnualLeave(EmployeesDTO emp, AnnualLeaveDTO al);
	

	
}
