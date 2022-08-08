package groupware;

public class CalendarDTO {
	
	private int calendarno;            // 일정번호
	private String fk_employee_id;     // 사원번호
	private String calendar_contents;  // 일정내용
	private String calendar_date;      // 작성일자
	
	/////////////////////////////////////////////////////////////////////////
	// select 용
	private EmployeesDTO empDto; // tbl_calendar 테이블과 tbl_employees 테이블을 JOIN.  작성자에 대한 모든 정보  
	                             // EmployeesDTO empDto 은 오라클의 tbl_employees 테이블(부모 테이블)에 해당함.
	/////////////////////////////////////////////////////////////////////////
	
	public int getCalendarno() {
		return calendarno;
	}
	
	public void setCalendarno(int calendarno) {
		this.calendarno = calendarno;
	}
	
	public String getFk_employee_id() {
		return fk_employee_id;
	}

	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	
	public String getCalendar_contents() {
		return calendar_contents;
	}

	public void setCalendar_contents(String calendar_contents) {
		this.calendar_contents = calendar_contents;
	}

	public String getCalendar_date() {
		return calendar_date;
	}

	public void setCalendar_date(String calendar_date) {
		this.calendar_date = calendar_date;
	}

	public EmployeesDTO getEmpDto() {
		return empDto;
	}
	
	public void setEmpDto(EmployeesDTO empDto) {
		this.empDto = empDto;
	}
	
	/////////////////////////////////////////////////////////////////////////


	public String showCalendarTitle() {
		
		String calendarTitle = calendarno +"\t"+ calendar_date +"\t"+ calendar_contents+"\t"+ empDto.getName();
		
		return calendarTitle;
	}// end of public String showBoardTitle()----------------

	
	// 테스트용
	@Override
	public String toString() {
		return "CalendarDTO [calendarno=" + calendarno + ", fk_employee_id=" + fk_employee_id + ", calendar_contents="
				+ calendar_contents + ", calendar_date=" + calendar_date + ", empDto=" + empDto + "]";
	}
	
	
	
}
