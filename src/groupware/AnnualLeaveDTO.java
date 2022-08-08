package groupware;

public class AnnualLeaveDTO { // tbl_annual_leave
	
	private String fk_employee_id; // 사원번호(외래키)
	private String start_date; // 연차시작일
	private String end_date; // 연차마지막일
	
	/////////////////////////////////////////////////
	
	private EmployeesDTO empdto; // JOIN select용 
	
	/////////////////////////////////////////////////
	


	public String getFk_employee_id() {
		return fk_employee_id;
	}

	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	public EmployeesDTO getEmpdto() {
		return empdto;
	}

	public void setEmpdto(EmployeesDTO empdto) {
		this.empdto = empdto;
	}

}