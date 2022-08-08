package groupware;

public class ReportDTO { // tbl_report

	private int report_no; // 문서번호(기본키)
	private String fk_report_employee_id; // 기안자 사원번호
	private String rp_subject; // 제목
	private String rp_contents; // 내용
	private String writedate; // 작성일자
	
	// select 용
	private ApprovalDTO apDto; // tbl_approval
	private EmployeesDTO rpEDto; // tbl_employees 기안자
	private EmployeesDTO apEDto; // tbl_employees 결재자
	
	
	public int getReport_no() {
		return report_no;
	}
	public void setReport_no(int report_no) {
		this.report_no = report_no;
	}

	public String getFk_report_employee_id() {
		return fk_report_employee_id;
	}
	public void setFk_report_employee_id(String fk_report_employee_id) {
		this.fk_report_employee_id = fk_report_employee_id;
	}
	
	public String getRp_subject() {
		return rp_subject;
	}
	public void setRp_subject(String rp_subject) {
		this.rp_subject = rp_subject;
	}
	public String getRp_contents() {
		return rp_contents;
	}
	public void setRp_contents(String rp_contents) {
		this.rp_contents = rp_contents;
	}
	public String getWritedate() {
		return writedate;
	}
	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}
	public ApprovalDTO getApDto() {
		return apDto;
	}
	public void setApDto(ApprovalDTO apDto) {
		this.apDto = apDto;
	}
	public EmployeesDTO getRpEDto() {
		return rpEDto;
	}
	public void setRpEDto(EmployeesDTO rpEDto) {
		this.rpEDto = rpEDto;
	}
	public EmployeesDTO getApEDto() {
		return apEDto;
	}
	public void setApEDto(EmployeesDTO apEDto) {
		this.apEDto = apEDto;
	}
	
}