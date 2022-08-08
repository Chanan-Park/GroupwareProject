package groupware;

import java.sql.*;
import java.util.*;

public class CalendarDAO implements InterCalendarDAO {

	// field , attribute , property , 속성
	Connection conn; // Connection conn = null; 과 동일하다.
	PreparedStatement pstmt;
	ResultSet rs;

	
	// method , operation , 기능
	// === 자원반납을 해주는 메소드 === //
	private void close() { // 해당 메소드는 EmployeesDAO 테이블에서만 사용되기 때문에 접근제한자 private 을 준다.
		
		try {
			if(rs != null)	   rs.close();   
			if(pstmt != null)  pstmt.close(); 
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}// end of private void close()----------------------
	
	
	// 내 일정보기(select) 메소드
	@Override
	public List<CalendarDTO> myViewCalendar(EmployeesDTO empDto) {
		List<CalendarDTO> myViewCalendar = new ArrayList<>();
		
		try {
			conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
					
			String sql = " select C.calendarno, to_char(C.calendar_date, 'yyyy-mm-dd hh24:mi') as calendar_date, C.calendar_contents, E.name "+
						 " from "+
						 " (select calendarno, fk_employee_id, calendar_contents, calendar_date "+
						 " from tbl_calendar) C "+
						 " JOIN "+
						 " (select employee_id, name "+
						 " from tbl_employees) E "+
						 " ON C.fk_employee_id = E.employee_id "+
						 " where employee_id = ? "+
						 " order by calendar_date desc ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empDto.getEmployee_id()); 
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) { // 만약 넘어간다면,
				CalendarDTO cdto = new CalendarDTO();
				
				cdto.setCalendarno(rs.getInt("CALENDARNO"));
				cdto.setCalendar_date(rs.getString("CALENDAR_DATE"));
				cdto.setCalendar_contents(rs.getString("CALENDAR_CONTENTS"));

				EmployeesDTO emDto = new EmployeesDTO();
				emDto.setName(rs.getString("NAME"));
				
				cdto.setEmpDto(emDto);
				
				myViewCalendar.add(cdto);
				
			}// end of while(rs.next())---------------------
			
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return myViewCalendar;
	}// end of public List<CalendarDTO> myViewCalendar()-------------------------
	

	
	// 팀 일정보기(select) 메소드
	@Override
	public List<CalendarDTO> teamViewCalendar(EmployeesDTO empDto) {
		List<CalendarDTO> teamViewCalendar = new ArrayList<>();
		
		try {
			conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
					
			String sql = " select C.calendarno, to_char(C.calendar_date, 'yyyy-mm-dd hh24:mi') as calendar_date, C.calendar_contents, E.name "+
						 " from "+
						 " (select calendarno, calendar_date, calendar_contents, fk_employee_id "+
						 " from tbl_calendar) C "+
						 " JOIN "+
						 " (select employee_id, name, department_id "+
						 " from tbl_employees) E "+
						 " ON C.fk_employee_id = E.employee_id "+
						 " JOIN "+
						 " (select department_id "+
						 " from tbl_departments) D "+
						 " ON E.department_id = D.department_id "+
						 " where E.department_id = ? "+
						 " order by calendar_date desc ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empDto.getDepartment_id()); 
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) { // 만약 넘어간다면,
				CalendarDTO cdto = new CalendarDTO();
				
				cdto.setCalendarno(rs.getInt("CALENDARNO"));
				cdto.setCalendar_date(rs.getString("CALENDAR_DATE"));
				cdto.setCalendar_contents(rs.getString("CALENDAR_CONTENTS"));

				EmployeesDTO emDto = new EmployeesDTO();
				emDto.setName(rs.getString("NAME"));
				
				cdto.setEmpDto(emDto);
				
				teamViewCalendar.add(cdto);
			}// end of while(rs.next())---------------------
			
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return teamViewCalendar;
	}// end of public List<CalendarDTO> teamViewCalendar(EmployeesDTO empDto)--------------------------
	
	
	
	// 전체 일정보기(select) 메소드
	@Override
	public List<CalendarDTO> totalViewCalendar() {
		List<CalendarDTO> totalViewCalendar = new ArrayList<>();
		
		try {
			conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
					
			String sql = " select C.calendarno, to_char(C.calendar_date, 'yyyy-mm-dd hh24:mi') as calendar_date, C.calendar_contents, E.name "+
						 " from "+
						 " (select calendarno, fk_employee_id, calendar_contents, calendar_date "+
						 " from tbl_calendar) C "+
						 " JOIN "+
						 " (select employee_id, name "+
						 " from tbl_employees) E "+
						 " ON C.fk_employee_id = E.employee_id "+
						 " order by calendar_date desc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) { // 만약 넘어간다면,
				CalendarDTO cdto = new CalendarDTO();
				
				cdto.setCalendarno(rs.getInt("CALENDARNO"));
				cdto.setCalendar_date(rs.getString("CALENDAR_DATE"));
				cdto.setCalendar_contents(rs.getString("CALENDAR_CONTENTS"));

				EmployeesDTO emDto = new EmployeesDTO();
				emDto.setName(rs.getString("NAME"));
				
				cdto.setEmpDto(emDto);
				
				totalViewCalendar.add(cdto);
				
			}// end of while(rs.next())---------------------
			
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return totalViewCalendar;
	}// end of public List<CalendarDTO> totalViewCalendar()----------------------------

	
	
	// 일정추가(insert) 메소드
	@Override
	public int write(CalendarDTO cdto) {
		
		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " insert into tbl_calendar(calendarno, fk_employee_id, calendar_contents, calendar_date) "+
						 " values(seq_calendar_no.nextval, ?, ?, to_date(?,'yyyy-mm-dd hh24:mi')) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cdto.getFk_employee_id());
			pstmt.setString(2, cdto.getCalendar_contents());
			pstmt.setString(3, cdto.getCalendar_date());
			
			int n = pstmt.executeUpdate();

			if(n == 1) {
				result = 1;
			}
			
		} catch (SQLException e) {
			if( e.getErrorCode() == 1847 || e.getErrorCode() == 1843) {
				// ORA-01847: 달의 날짜는 1에서 말일 사이어야 합니다,  ORA-01843: 지정한 월이 부적합합니다
				System.out.println("[경고] 유효한 날짜로 다시 입력해주세요. \n");
				result = -1;
			}
			else if( e.getErrorCode() == 1850 || e.getErrorCode() == 1851 ) {
				// ORA-01850: 시간은 0에서 23 사이어야 합니다  ORA-01851: 분은 0에서 59 사이어야 합니다
				System.out.println("[경고] 유효한 시간으로 다시 입력해주세요. \n");
				result = -1;				
			}
			else {
				System.out.println("[경고] 형식에 맞게 입력하세요. \n ");
				result = -1;
			}
			
		} finally {  // 에러 발생여부와 상관없이 반드시 실행
			close(); // 자원반납을 해주는 메소드
		}
		
		return result;
	}// end of public int write(CalendarDTO cdto)----------------------------

	
	
	// 일정보기(select) 메소드
	@Override
	public CalendarDTO viewCalendar_contents(String calendarno) {
		
		CalendarDTO cdto = null;
		
		try {
			conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
			
			String sql = "select to_char(C.calendar_date, 'yyyy-mm-dd hh24:mi') as calendar_date, C.calendar_contents, E.name, C.fk_employee_id\n"+
						 "from\n"+
						 "(select calendarno, fk_employee_id, calendar_contents, calendar_date\n"+
						 "from tbl_calendar) C\n"+
						 "JOIN\n"+
						 "(select employee_id, name\n"+
						 "from tbl_employees) E\n"+
						 "ON C.fk_employee_id = E.employee_id\n"+
						 "where calendarno = ?\n"+
						 "order by C.calendar_date desc";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, calendarno);
			
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				
				cdto = new CalendarDTO();
				
				cdto.setCalendar_date(rs.getString("CALENDAR_DATE"));
				cdto.setCalendar_contents(rs.getString("CALENDAR_CONTENTS"));
				
				
				EmployeesDTO emDto = new EmployeesDTO();
				emDto.setName(rs.getString("NAME"));
				
				cdto.setEmpDto(emDto);
				
				cdto.setFk_employee_id(rs.getString("FK_EMPLOYEE_ID"));
				
			}// end of if(rs.next())----------------------------
			
			
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return cdto;
	}// end of public CalendarDTO viewCalendar_contents(String calendarno)-----------------------------------

	
	
	// 일정수정하기(update) 메소드
	@Override
	public int updateCalendar(Map<String, String> paraMap) {
		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
			
			String sql = " update tbl_calendar set calendar_date = to_date(?,'yyyy-mm-dd hh24:mi'), calendar_contents = ? "+
						 " where calendarno = ? ";
			
			pstmt = conn.prepareStatement(sql);   // 우편배달부(pstmt) 생성하기
			pstmt.setString(1, paraMap.get("calendar_date")); 
			pstmt.setString(2, paraMap.get("calendar_contents"));
			pstmt.setString(3, paraMap.get("calendarno"));  // 파라미터 Map<String, String>의 value 값도 String 이므로 String 타입이다.
			
			result = pstmt.executeUpdate(); 
			// update 가 성공되어지면 result 에는 1 이 들어간다. 
			
		} catch (SQLException e) {
			if( e.getErrorCode() == 1847 || e.getErrorCode() == 1843) {
				// ORA-01847: 달의 날짜는 1에서 말일 사이어야 합니다,  ORA-01843: 지정한 월이 부적합합니다
				System.out.println("[경고] 유효한 날짜로 다시 입력해주세요. \n");
				result = -1;
			}
			else if( e.getErrorCode() == 1850 || e.getErrorCode() == 1851 ) {
				// ORA-01850: 시간은 0에서 23 사이어야 합니다  ORA-01851: 분은 0에서 59 사이어야 합니다
				System.out.println("[경고] 유효한 시간으로 다시 입력해주세요. \n");
				result = -1;				
			}
			else {
				System.out.println("[경고] 형식에 맞게 입력하세요. \n ");
				e.printStackTrace();   // 에러의 발생근원지를 찾아서 단계별로 에러를 출력합니다.
			}
		} finally {
			close(); // 자원반납을 해주는 메소드
		}
		
		return result;
	}// end of public int updateCalendar(Map<String, String> paraMap)------------------------
	
	
	
	// 일정수정하기(update) 메소드-2, 날짜를 엔터했을때
	   @Override
	   public int updateCalendar_2(Map<String, String> paraMap) {
	      int result = 0;
	      
	      try {
	         conn = ProjectDBConnection.getConn(); // 오라클 서버에 연결해준다.
	         
	         String sql = " update tbl_calendar set calendar_contents = ? "+
	                   " where calendarno = ? ";
	         
	         pstmt = conn.prepareStatement(sql);   // 우편배달부(pstmt) 생성하기
	         pstmt.setString(1, paraMap.get("calendar_contents"));
	         pstmt.setString(2, paraMap.get("calendarno"));  // 파라미터 Map<String, String>의 value 값도 String 이므로 String 타입이다.
	         
	         result = pstmt.executeUpdate(); 
	         // update 가 성공되어지면 result 에는 1 이 들어간다. 
	         
	      } catch (SQLException e) {
	         if( e.getErrorCode() == 1847 || e.getErrorCode() == 1843) {
	            // ORA-01847: 달의 날짜는 1에서 말일 사이어야 합니다,  ORA-01843: 지정한 월이 부적합합니다
	            System.out.println("[경고] 유효한 날짜로 다시 입력해주세요. \n");
	            result = -1;
	         }
	         else if( e.getErrorCode() == 1850 || e.getErrorCode() == 1851 ) {
	            // ORA-01850: 시간은 0에서 23 사이어야 합니다  ORA-01851: 분은 0에서 59 사이어야 합니다
	            System.out.println("[경고] 유효한 시간으로 다시 입력해주세요. \n");
	            result = -1;            
	         }
	         else {
	            System.out.println("[경고] 형식에 맞게 입력하세요. \n ");
	            e.printStackTrace();   // 에러의 발생근원지를 찾아서 단계별로 에러를 출력합니다.
	         }
	      } finally {
	         close(); // 자원반납을 해주는 메소드
	      }
	      
	      return result;
	   }// end of public int updateCalendar(Map<String, String> paraMap)------------------------
	   
	   
	
	
	// 글삭제하기(delete) 메소드
	@Override
	public int deleteCalendar(String calendarno) {

		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " delete from tbl_calendar "
					   + " where calendarno = ? ";
			
			pstmt = conn.prepareStatement(sql);   
			pstmt.setString(1, calendarno); 
			
			result = pstmt.executeUpdate(); 
			// delete 가 성공되어지면 result 에는 1 이 들어간다. 
			
		} catch (SQLException e) {
		//	e.printStackTrace();  ==> TotalController 클래스에서 System.out.println(">> SQL 구문 오류 발생으로 글수정이 실패되었습니다. << \n"); 로 대체한다.
			result = -1; // 결과값 -1(일정삭제를 하려고 하나 SQLException 이 발생한 경우)
		} finally {
			close(); // 자원반납을 해주는 메소드
		}
		
		return result;
	}// end of public int deleteCalendar(String calendarno)----------------------------















	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
