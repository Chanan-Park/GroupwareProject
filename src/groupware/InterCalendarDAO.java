package groupware;

import java.util.*;

public interface InterCalendarDAO {

	
	// 내 일정보기(select) 메소드
	List<CalendarDTO> myViewCalendar(EmployeesDTO empDto);
	
	// 팀 일정보기(select) 메소드
	List<CalendarDTO> teamViewCalendar(EmployeesDTO empDto);

	// 전체 일정보기(select) 메소드
	List<CalendarDTO> totalViewCalendar();
		
	// 일정추가(insert) 메소드
	int write(CalendarDTO cdto);
	
	// 일정보기(select) 메소드
	CalendarDTO viewCalendar_contents(String calendarno);

	// 일정수정하기(update) 메소드
	int updateCalendar(Map<String, String> paraMap);

	// 일정수정하기(update) 메소드2
	int updateCalendar_2(Map<String, String> paraMap);
	
	// 글삭제하기(delete) 메소드
	int deleteCalendar(String calendarno);









}
