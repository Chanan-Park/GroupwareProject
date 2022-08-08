package groupware;

public class AttendanceDTO {

	
	private String  fk_employee_id;    // 사원번호
	private String  arrival_time;      // 출근시간
	private String  depart_time;       // 퇴근시간
	private String  attendance_date;   // 출근날짜
	
	///////////////////////////////////////////////////////////////////////////////////
	

	private String status; //출근상태
	private EmployeesDTO empdto;
	
	/////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	

	public String getFk_employee_id() {
		return fk_employee_id;
	}

	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	
	
	
	
	public String getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}
	
	
	
	
	public String getDepart_time() {
		return depart_time;
	}
	public void setDepart_time(String depart_time) {
		this.depart_time = depart_time;
	}
	
	
	public String getAttendance_date() {
		return attendance_date;
	}
	public void setAttendance_date(String attendance_date) {
		this.attendance_date = attendance_date;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	public EmployeesDTO getEmpdto() {
		return empdto;
	}
	public void setEmpdto(EmployeesDTO empdto) {
		this.empdto = empdto;
	}
	
	
}