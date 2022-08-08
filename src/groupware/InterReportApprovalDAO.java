package groupware;

import java.util.*;

public interface InterReportApprovalDAO {

	// 결재 상신하기 (insert)
	int submitReport(ReportDTO rdto);

	// 결재 목록 조회하기 (select)
	List<ReportDTO> viewReportList(EmployeesDTO empDto, String choice_number);

	// 기안 문서 내용을 조회하기 (select)
	ReportDTO viewReportContents(EmployeesDTO empDto, String reportNo);

	// 기안 문서 코멘트 조회하기 (select)
	List<ApprovalDTO> commentList(String reportNo);
	
	// 결재 처리하기 (select + update)
	int approve(EmployeesDTO empDto, Map<String, String> paraMap);

	// 내가 쓴 기안서 수정하기 (update)
	int editReport(Map<String, String> paraMap);
	
	// 내가 쓴 기안서 삭제하기 (delete)
	int deleteReport(String report_no);
}
