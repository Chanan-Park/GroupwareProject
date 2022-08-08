package groupware;

import java.sql.*;
import java.util.*;



public class EmployeesDAO implements InterEmployeesDAO {

	// field , attribute , property , 속성
	Connection conn = null;
	PreparedStatement pstmt;
	ResultSet rs;
	
	
	
	
	// ---------------------------------------------------------------------------- //
	
	
	
	
	// method , operation , 기능
	
	// === 자원반납을 해주는 메소드 === // -------------------------------------------------------------------
	private void close() {
		
		try {
			if( rs != null )  rs.close();
			if( pstmt != null ) pstmt.close();  // conn 을 뺀 나머지만 반납. conn 은 프로그램이 끝나면 반납함
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	} // end of private void close() ----------------------------------------------------------------
	
	
	
	
	

	
	// 회원가입(insert) 메소드 --------------------------------------------------------------------------------------------------------------- //
	@Override
	public int employeeRegister(EmployeesDTO empDto) {

		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " insert into tbl_employees(employee_id, department_id, name, passwd, position, mobile, email, address, birthdate, gender, manager_id) "
					   + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, empDto.getEmployee_id() );
			pstmt.setString( 2, empDto.getDepartment_id() );
			pstmt.setString( 3, empDto.getName() );
			pstmt.setString( 4, empDto.getPasswd() );
			pstmt.setString( 5, empDto.getPosition() );
			pstmt.setString( 6, empDto.getMobile() );
			pstmt.setString( 7, empDto.getEmail() );
			pstmt.setString( 8, empDto.getAddress() );
			pstmt.setString( 9, empDto.getBirthdate() );
			pstmt.setString( 10, empDto.getGender() );
			pstmt.setString( 11, empDto.getManager_id() );
			
			
			result = pstmt.executeUpdate();
			
			
		} catch(SQLIntegrityConstraintViolationException e) { // 제약조건
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	} // end of public int employeeRegister(EmployeesDTO empDto) ------------------------------------




	
	
	

	
	
	
	// *** 사원번호를 생성해주는 메소드 *** // ---------------------------------------------------------------------
	public String makeEmployeeid(String department_name, String position) {
		
		String employee_id;
		

		String s_dpt = ""; // 부서명 고유번호
		if("경영지원".equals(department_name)) {
			s_dpt = "1";  // 경영지원팀이라면 사원번호가 1**
		}
		else if("인사".equals(department_name)) {
			s_dpt = "2"; // 인사팀이라면 사원번호가 2**
		}
		else if("IT".equals(department_name)) {
			s_dpt = "3"; // IT팀이라면 사원번호가 3**
		}
		else if("마케팅".equals(department_name)) {
			s_dpt = "4"; // 마케팅팀이라면 사원번호가 4**
		}
		else if("재경".equals(department_name)) {
			s_dpt = "5"; // 재경팀이라면 사원번호가 5**
		}
		
		String s_pos = ""; // 직급 고유번호
		if("팀장".equals(position)) {
			s_pos = "1"; // 팀장이라면 사원번호가 *1*
		}
		else if("책임".equals(position)) {
			s_pos = "2"; // 책임이라면 사원번호가 *2*
		}
		else if("선임".equals(position)) {
			s_pos = "3"; // 선임이라면 사원번호가 *3*
		}
		else if("사원".equals(position)) {
			s_pos = "4"; // 사원이라면 사원번호가 *4*
		}
		
		String s_cnt = "";
	//	int n_cnt = 0;
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select count(*)\n"+
						 "from tbl_employees E join tbl_departments D\n"+
						 "on E.department_id = D.department_id\n"+
						 "where E.position = ? and D.department_name = ?";
			// 동일한 직급과 부서명을 가진 사람의 인원수 count
			// 사원번호의 마지막 숫자는 인원수 + 1
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, position );
			pstmt.setString(2, department_name );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				
				s_cnt = rs.getString(1);
				/*
				n_cnt = rs.getInt(1)+1; 
				s_cnt = String.valueOf(n_cnt);
				*/
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		employee_id = s_dpt + s_pos + s_cnt;
		
		
		return employee_id;
		
	} // end of public String makeEmployeeid() --------------------------------------------------------------------
		
		
		
	
	
	
	// *** 직속상사 성명 찾는 메소드 *** // -------------------------------------------------------------------------------
	public String searchManager_id(String manager_name) {
		String manager_id = "";

		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, manager_name );
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				manager_id = rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return manager_id;
	} // end of public String searchManager_id(String manager_name) ----------------------------------------------
	
	

	
	
	
	// *** 부서번호를 알려주는 메소드 *** // -----------------------------------------------------------------------
	@Override
	public String searchDept_id(String department_name) {
		String department_id = "";
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select department_id\n"+
					"from tbl_departments\n"+
					"where department_name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, department_name );
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				department_id = rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return department_id;
	} // end of public String searchDept_id(String department_name) ---------------------------------------
	
	
	
	

	// *** 회원가입시 이미 존재하는 연락처인지 확인하는 메소드 *** // --------------------------------------------------------
	@Override
	public boolean checkMobile(String mobile) {
		boolean result = false;
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where mobile = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mobile );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public void checkMobile(String mobile) ------------------------------------------------------



	
	

	// *** 회원가입시 이미 존재하는 이메일인지 확인하는 메소드 *** // --------------------------------------------------------
	@Override
	public boolean checkEmail(String email) {
		boolean result = false;
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where email = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public boolean checkEmail(String email) -----------------------------------------------------



	
	
	
	// 로그인 // ==============================================================================================
	
	// *** 로그인 - 존재하는 사원번호인지 확인 *** // ------------------------------------------------------------
	@Override
	public boolean isExistEmpId(String employee_id) {
		boolean result = false; // false = 0
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where employee_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				employee_id = rs.getString(1);
				result = true; // true = 1
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public void isExistEmpId(String employee_id)

	
	
	
	// *** 로그인을 해주는 메소드 *** // -----------------------------------------------------------------
	@Override
	public EmployeesDTO login(Map<String, String> map) {
		EmployeesDTO empDto = null;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " select employee_id, name, position, address, department_id "
					   + " , mobile, email, salary, hire_date, birthdate, gender, manager_id "
					   + " from tbl_employees "
					   + " where employee_id = ? and passwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, map.get("employee_id") );
			pstmt.setString(2, map.get("passwd") );
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				empDto = new EmployeesDTO();  // 빈껍데기 생성
			
				empDto.setEmployee_id( rs.getString("employee_id") ); 
				empDto.setDepartment_id( rs.getString("department_id") ); 
				empDto.setName( rs.getString("name") ); 
				empDto.setPosition( rs.getString("position") );
				empDto.setAddress( rs.getString("address") );
				empDto.setMobile( rs.getString("mobile") );
				empDto.setEmail( rs.getString("email") );
				empDto.setSalary( rs.getString("salary") );
				empDto.setHire_date( rs.getString("hire_date") );
				empDto.setBirthdate( rs.getString("birthdate") );
				empDto.setGender( rs.getString("gender") );
				empDto.setManager_id( rs.getString("manager_id") );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return empDto;
	} // end of public EmployeesDTO login(Map<String, String> map) --------------------------






	// *** 직속상사 성명을 입력받고 존재하는 사원명이 맞는지 확인하는 메소드 *** // ------------------------------------------------
	@Override
	public int employeeCheck(String manager_name) {
		int result = 0; // false = 0
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select name\n"+
						 "from tbl_employees\n"+
						 "where name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, manager_name );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				manager_name = rs.getString(1);
				result = 1; // true = 1
			}
			else {
				result = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
		
	} // end of public int employeeCheck(String manager_name) --------------------------------



	
	
	
	
	

	// ### 일반사원용 메뉴 ### // ===========================================================================================

	
	// 내 사원정보를 읽어오는(select) 메소드 ----------------------------------------------------------
	@Override
	public EmployeesDTO myInfo(String employee_id) {
		
		EmployeesDTO empdto = null;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select E1.employee_id, E1.name, E1.gender, E1.position, E1.address\n"+
					 "     , E1.mobile, E1.email, to_char(E1.hire_date, 'yyyy-mm-dd' ) as hire_date, to_char(NVL(E1.salary, '0'), '99,999,999') as salary, B.department_name, E2.name || ' ' || E2.position as manager_name\n"+
					 "from tbl_employees E1 \n"+
					 "LEFT JOIN tbl_employees E2\n"+
					 "on E1.manager_id = E2.employee_id\n"+
					 "LEFT JOIN tbl_departments B\n"+
					 "on E1.department_id = B.department_id\n"+
					 "where E1.employee_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, employee_id );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				empdto = new EmployeesDTO();  // 빈껍데기 생성
				
				empdto.setEmployee_id( rs.getString("employee_id") );  // or 1
				empdto.setName( rs.getString("name") ); 
				empdto.setGender( rs.getString("gender") ); // or 2
				empdto.setPosition( rs.getString("position") );
				empdto.setAddress( rs.getString("address") );  // 각각 값을 넣어줌
				empdto.setMobile( rs.getString("mobile") );
				empdto.setEmail( rs.getString("email") );
				empdto.setHire_date( rs.getString("hire_date") );
				empdto.setSalary( rs.getString("salary") );
				DepartmentsDTO deptDto = new DepartmentsDTO();
				deptDto.setDepartment_name(rs.getString("department_name")); 
				empdto.setDeptDto(deptDto);
				empdto.setManager_name( rs.getString("manager_name") );
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return empdto; 
	} // end of public EmployeesDTO myInfo(String employee_id) -------------------------------------------------------
	
	
	
	
	
	// 내 사원정보 수정(update) 메소드 ------------------------------------------------------------------------------------------------
	
	public int updateMyinfo(String select_update, String update_info, String employee_id) {
		int result = 0; // 0이면 실패
		
		try {
			
			conn = ProjectDBConnection.getConn();
			String sql = "";
			if("passwd".equals(select_update)) {
				sql = " update tbl_employees set passwd = ? "
					+ " where employee_id = ? ";
			}
			else if("mobile".equals(select_update)) {
				sql = " update tbl_employees set mobile = ? "
						   + " where employee_id = ? ";
			}
			else if("email".equals(select_update)) {
				sql = " update tbl_employees set email = ? "
						   + " where employee_id = ? ";
			}
			else if("address".equals(select_update)) {
				sql = " update tbl_employees set address = ? "
						   + " where employee_id = ? ";
			}
			
			
			pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, update_info);
	        pstmt.setString(2, employee_id);
	        
	        result = pstmt.executeUpdate();
	        
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public int updatePasswd(String update_passwd, String employee_id) ---------------------------------------------

	
	
	// end of  회원정보 수정 메소드 ------------------------------------------------------------------------------------------------
	
	
	
	
	
	// ### 관리자용 메뉴 ### // ===========================================================================================
	
	
	// 관리자용 - 모든회원조회(select) 메소드 -----------------------------------------------------------------------------------------------
	@Override
	public List<EmployeesDTO> showAllMember() {
		
		List<EmployeesDTO> employeeList = new ArrayList<>();
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select E1.employee_id, rpad(substr(E1.passwd,1,4), length(E1.passwd), '*' ) as passwd, E1.name, E1.gender, E1.position, E1.address\n"+
					 "     , E1.mobile, E1.email, to_char(E1.hire_date, 'yyyy-mm-dd' ) as hire_date, to_char(NVL(E1.salary, '0'), '99,999,999') as salary, B.department_name, E2.name || ' ' || E2.position as manager_name\n"+
					 "from tbl_employees E1 \n"+
					 "LEFT JOIN tbl_employees E2\n"+
					 "on E1.manager_id = E2.employee_id\n"+
					 "LEFT JOIN tbl_departments B\n"+
					 "on E1.department_id = B.department_id\n"+
					 "order by E1.employee_id";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				EmployeesDTO empdto = new EmployeesDTO();
				
				empdto.setEmployee_id( rs.getString("employee_id") ); 
				empdto.setName( rs.getString("name") ); 
				empdto.setPasswd( rs.getString("passwd") ); 
				empdto.setGender( rs.getString("gender") ); 
				empdto.setPosition( rs.getString("position") );
				empdto.setAddress( rs.getString("address") ); 
				empdto.setMobile( rs.getString("mobile") );
				empdto.setEmail( rs.getString("email") );
				empdto.setHire_date( rs.getString("hire_date") );
				empdto.setSalary( rs.getString("salary") );
				DepartmentsDTO deptDto = new DepartmentsDTO();
				deptDto.setDepartment_name(rs.getString("department_name")); 
				empdto.setDeptDto(deptDto);
				empdto.setManager_name( rs.getString("manager_name") );
				
				employeeList.add(empdto); // select 된 행을 list에 담아줌
				
			} // end of while(rs.next())
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		return employeeList;
		
	} // end of public List<EmployeesDTO> showAllMember() --------------------------------------------------------------

	
	
	
	// 관리자용 - 부서별,직급별 사원조회(select) 메소드 -----------------------------------------------------------------------------------------------
		@Override
		public List<EmployeesDTO> showMember(String update_info, String select_update) {
			
			List<EmployeesDTO> employeeList = new ArrayList<>();
			String sql = "";
			try {
				conn = ProjectDBConnection.getConn();
				if( "department_id".equals(select_update) ) {
					sql = "select E1.employee_id, E1.name, rpad(substr(E1.passwd,1,4), length(E1.passwd), '*' ) as passwd, E1.gender, E1.position, E1.address\n"+
							 "     , E1.mobile, E1.email, to_char(E1.hire_date, 'yyyy-mm-dd' ) as hire_date, to_char(NVL(E1.salary, '0'), '99,999,999') as salary, B.department_name, E2.name || ' ' || E2.position as manager_name\n"+
							 "from tbl_employees E1 \n"+
							 "LEFT JOIN tbl_employees E2\n"+
							 "on E1.manager_id = E2.employee_id\n"+
							 "LEFT JOIN tbl_departments B\n"+
							 "on E1.department_id = B.department_id\n"+
							 "where E1.department_id = ?\n"+
							 "order by salary desc";
				}
				
				else if( "position".equals(select_update) ) {
					sql = "select E1.employee_id, E1.name, rpad(substr(E1.passwd,1,4), length(E1.passwd), '*' ) as passwd, E1.gender, E1.position, E1.address\n"+
							 "     , E1.mobile, E1.email, to_char(E1.hire_date, 'yyyy-mm-dd' ) as hire_date, to_char(NVL(E1.salary, '0'), '99,999,999') as salary, B.department_name, E2.name || ' ' || E2.position as manager_name\n"+
							 "from tbl_employees E1 \n"+
							 "LEFT JOIN tbl_employees E2\n"+
							 "on E1.manager_id = E2.employee_id\n"+
							 "LEFT JOIN tbl_departments B\n"+
							 "on E1.department_id = B.department_id\n"+
							 "where E1.position = ?\n"+
							 "order by salary desc";
				}
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, update_info );
				rs = pstmt.executeQuery();
				while(rs.next()) {
					
					EmployeesDTO empdto = new EmployeesDTO();
					
					empdto.setEmployee_id( rs.getString("employee_id") ); 
					empdto.setName( rs.getString("name") ); 
					empdto.setPasswd( rs.getString("passwd") ); 
					empdto.setGender( rs.getString("gender") ); 
					empdto.setPosition( rs.getString("position") );
					empdto.setAddress( rs.getString("address") ); 
					empdto.setMobile( rs.getString("mobile") );
					empdto.setEmail( rs.getString("email") );
					empdto.setHire_date( rs.getString("hire_date") );
					empdto.setSalary( rs.getString("salary") );
					DepartmentsDTO deptDto = new DepartmentsDTO();
					deptDto.setDepartment_name(rs.getString("department_name")); 
					empdto.setDeptDto(deptDto);
					empdto.setManager_name( rs.getString("manager_name") );
					
					employeeList.add(empdto); // select 된 행을 list에 담아줌
					
				} // end of while(rs.next())
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(); // 자원반납
			}
			
			return employeeList;
			
		} // end of public List<EmployeesDTO> showDeptMember(String select_dept) --------------------------------------------------------------
		
		
	
	


	// 관리자용 - 개별 회원정보를 읽어오는(select) 메소드 ----------------------------------------------------------
	@Override
	public EmployeesDTO empInfo(String s_employee_id) {
		
		EmployeesDTO empdto2 = null;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select E1.employee_id, E1.name, E1.gender, E1.position, E1.address\n"+
						 "     , E1.mobile, E1.email, to_char(E1.hire_date, 'yyyy-mm-dd' ) as hire_date, to_char(NVL(E1.salary, '0'), '99,999,999') as salary, B.department_name, E2.name || ' ' || E2.position as manager_name\n"+
						 "from tbl_employees E1 \n"+
						 "LEFT JOIN tbl_employees E2\n"+
						 "on E1.manager_id = E2.employee_id\n"+
						 "LEFT JOIN tbl_departments B\n"+
						 "on E1.department_id = B.department_id\n"+
						 "where E1.employee_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, s_employee_id );
			rs = pstmt.executeQuery();
			
			if( rs.next() ) { // select 된 값이 있는 경우
				empdto2 = new EmployeesDTO();  // 빈껍데기 생성
				
				empdto2.setEmployee_id( rs.getString("employee_id") );  // or 1
				empdto2.setName( rs.getString("name") ); 
				empdto2.setGender( rs.getString("gender") ); // or 2
				empdto2.setPosition( rs.getString("position") );
				empdto2.setAddress( rs.getString("address") );  // 각각 값을 넣어줌
				empdto2.setMobile( rs.getString("mobile") );
				empdto2.setEmail( rs.getString("email") );
				empdto2.setHire_date( rs.getString("hire_date") );
				empdto2.setSalary( rs.getString("salary") );
				DepartmentsDTO deptDto = new DepartmentsDTO();
				deptDto.setDepartment_name(rs.getString("department_name")); 
				empdto2.setDeptDto(deptDto);
				empdto2.setManager_name( rs.getString("manager_name") );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return empdto2; 
	} // end of public EmployeesDTO empInfo(String s_employee_id) -----------------------------------------

	
	

	// 관리자 메뉴 - 5.퇴직금 조회 // ---------------------------------------------------------------------
	@Override
	public String[] showPension(String employee_id) {
		String name, pension;
		String [] emp_arr;
		emp_arr = new String[2];
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select name, to_char( trunc(months_between(B.retire_day, hire_date)/12) * salary, '9,999,999,999') AS pension  \n"+
						 "from\n"+
						 "(   \n"+
						 "    select employee_id, name, birthdate, age, hire_date, salary,\n"+
						 "           last_day(\n"+
						 "                    to_date( to_char( add_months(sysdate, (63-age)*12), 'yyyy' )|| \n"+
						 "                             case when substr(birthdate, 3, 2) between '03' and '08' then '-08-01' else '-02-01' end \n"+
						 "                             , 'yyyy-mm-dd')\n"+
						 "                    ) AS retire_day\n"+
						 "    from\n"+
						 "    (\n"+
						 "    select name, employee_id, salary, hire_date, replace(birthdate,'/', '') as birthdate\n"+
						 "         , case\n"+
						 "           when substr(replace(birthdate,'/', ''),1,1) in (0,1,2)\n"+
						 "           then extract(year from sysdate) - (substr(replace(birthdate,'/', ''),1,2)+2000) + 1\n"+
						 "           else to_number( to_char(sysdate, 'yyyy'))  - ( substr(replace(birthdate,'/', ''),1,2) +1900 ) + 1\n"+
						 "           end AGE\n"+
						 "    from tbl_employees\n"+
						 "    where employee_id = ?\n"+
						 "    ) A\n"+
						 ") B";
					    
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				name = rs.getString(1);
				pension = rs.getString(2);
				
				emp_arr[0] = name;
				emp_arr[1] = pension;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return emp_arr;
		
	} // end of public String[] showPension(String employee_id) -----------------------------------------------------------


	
	
	
	
	// *** 관리자용 - 사원정보 수정 메소드 *** // -----------------------------------------------------
		@Override
		public int updateInfo_admin(String select_update, String update_info, String s_employee_id) {
			int result = 0; // 0이면 실패
			String sql = "";
			try {
				conn = ProjectDBConnection.getConn();
				
				if("salary".equals(select_update)){
					sql = " update tbl_employees set salary = ? "
							   + " where employee_id = ? ";
				}
				else if("position".equals(select_update)){
					sql = " update tbl_employees set position = ? "
							   + " where employee_id = ? ";
				}
				if("department_id".equals(select_update)){
					sql = " update tbl_employees set department_id = ? "
							   + " where employee_id = ? ";
				}
				
				pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, update_info);
		        pstmt.setString(2, s_employee_id);
		        result = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return result;
			
		} // end of public void updateSalary_admin(String s_employee_id)
	
	// *** 사원번호 / 비밀번호 찾기 *** // ============================================================================
	
	// 회원번호 찾기 메소드 // ------------------------------------------------------------------------
	public String searchEmpId(String name, String mobile) {
		String result = "";
		
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where name = ? and mobile = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name );
			pstmt.setString(2, mobile );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = rs.getString(1);
			}
			else { // select 된 값이 없는 경우
				result = "noExist";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public String searchEmpId(String name, String mobile) ------------------------------------------

	// 회원번호 찾기 - 사원명 존재여부 찾기 메소드 ----------------------------------------------------------------------------
	public boolean isNameExist(String name) {
		boolean result = false;
		
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public boolean isNameExist(String name)

		
	

	// *** 비밀번호 찾기 메소드 *** // -----------------------------------------------------------
	@Override
	public String searchEmpPwd(String employee_id, String mobile) {
		String result = "";
		try {
			conn = ProjectDBConnection.getConn();

			String sql = "select passwd\n"+
						 "from tbl_employees\n"+
						 "where employee_id = ? and mobile = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id );
			pstmt.setString(2, mobile );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = rs.getString(1);
			}
			else { // select 된 값이 없는 경우
				result = "noExist";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public String searchEmpPwd(String employee_id, String mobile) --------------------------------------

	// *** 비밀번호 찾기 - 사원번호 존재여부 확인 메소드 *** // -------------------------------------------
	@Override
	public boolean isEmployee_idExist(String employee_id) {
		boolean result = false;
		try {
			conn = ProjectDBConnection.getConn();
			String sql = "select employee_id\n"+
						 "from tbl_employees\n"+
						 "where employee_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, employee_id );
			rs = pstmt.executeQuery();
			if( rs.next() ) { // select 된 값이 있는 경우
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public boolean isEmployee_idExist(String employee_id) ----------------------
	
}
