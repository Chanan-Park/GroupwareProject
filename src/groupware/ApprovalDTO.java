package groupware;

public class ApprovalDTO {

	private int approval_no; // 결재일련번호 
	private int fk_report_no; // 문서번호
	private int levelno; // 결재단계번호
	private String fk_approval_employee_id; // 결재할사원번호 
	private String approval_status; // 승인여부  0:미결  1:결재  -1:반려
	private String comments; // 승인여부에 관한 코멘트
	private String approval_date; // 승인날짜
	
	//select용
	private EmployeesDTO apEDto; // tbl_employees 결재자
	
	
	public int getApproval_no() {
		return approval_no;
	}
	public void setApproval_no(int approval_no) {
		this.approval_no = approval_no;
	}
	public int getFk_report_no() {
		return fk_report_no;
	}
	public void setFk_report_no(int fk_report_no) {
		this.fk_report_no = fk_report_no;
	}
	public int getLevelno() {
		return levelno;
	}
	public void setLevelno(int levelno) {
		this.levelno = levelno;
	}
	public String getFk_approval_employee_id() {
		return fk_approval_employee_id;
	}
	public void setFk_approval_employee_id(String fk_approval_employee_id) {
		this.fk_approval_employee_id = fk_approval_employee_id;
	}
	public String getApproval_status() {
		return approval_status;
	}
	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(String approval_date) {
		this.approval_date = approval_date;
	}
	public EmployeesDTO getApEDto() {
		return apEDto;
	}
	public void setApEDto(EmployeesDTO apEDto) {
		this.apEDto = apEDto;
	}
	
	public String viewComments() {
		//결재의견\t\t처리\t작성자\t작성일자
		return comments + "\t" + approval_status + "\t" + apEDto.getName() + "\t" + approval_date;
	}
	
}
