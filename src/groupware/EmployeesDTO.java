package groupware;

public class EmployeesDTO {

	private String employee_id; // 사원번호
	private String department_id; // 부서번호
	private String name; // 사원명
	private String passwd; // 비밀번호
	private String position; // 직급
	private String address; // 주소
	private String mobile; // 연락처
	private String email; // 이메일
	private String salary; // 월급
	private String hire_date; // 입사일자
	private String birthdate; // 생년월일
	private String gender; // 성별
	private String manager_id; // 상사 사원번호
	private String manager_name; // 상사 이름

	// ------------------------------------------------------- //

	// select 용 //
	private DepartmentsDTO deptDto; // tbl_departments

	// ------------------------------------------------------- //

	public String getEmployee_id() {

		return employee_id;
	}

	public void setEmployee_id(String employee_id) {

		this.employee_id = employee_id;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;

	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getHire_date() {
		return hire_date;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getManager_id() {
		return manager_id;
	}

	public void setManager_id(String manager_id) {
		this.manager_id = manager_id;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

	public DepartmentsDTO getDeptDto() {
		return deptDto;
	}

	public void setDeptDto(DepartmentsDTO deptDto) {
		this.deptDto = deptDto;
	}
	
	// *** 비밀번호 유효성 검사 메소드 *** //
	// ------------------------------------------------------------

	public boolean checkPwd(String passwd) {

		boolean upperFlag = false; // 대문자인지 기록하는 용도
		boolean lowerFlag = false; // 소문자인지 기록하는 용도
		boolean digitFlag = false; // 숫자인지 기록하는 용도
		boolean specialFlag = false; // 특수문자인지 기록하는 용도

		// 입력받은 String pwd 의 글자길이 알아보기

		int pwd_length = passwd.length(); // 비밀번호의 글자수

		if (pwd_length < 8 || pwd_length > 15) { // 비밀번호의 글자수가 8글자 이상 15글자 이하가 아닌 경우
			return false;
		} else { // 비밀번호의 글자수가 8글자 이상 15글자 이하인 경우
					// 암호가 어떤 글자로 이루어졌는지 검사를 시도한다.

			// pwd ==> "qWer1234$"
			// index ==> 012345678
			for (int i = 0; i < pwd_length; i++) { // 입력받은 글자의 길이만큼 검사를 한다.
				char ch = passwd.charAt(i);

				if (Character.isUpperCase(ch)) { // 대문자라면
					upperFlag = true;
				} else if (Character.isLowerCase(ch)) { // 소문자라면
					lowerFlag = true;
				} else if (Character.isDigit(ch)) { // 숫자라면
					digitFlag = true;
				} else { // 특수문자라면
					specialFlag = true;
				}
			} // end of for

			if (upperFlag && lowerFlag && digitFlag && specialFlag) {
				return true;
			} else {
				return false;
			}

		}

	} // end of boolean checkPwd(String pwd)

	@Override
	public String toString() {
		return "\n------------------------------\n" + "◇ 사원번호: " + employee_id + "\n" + "◇ 사원명: " + name + "\n"
				+ "◇ 성별: " + gender + "\n" + "◇ 부서: " + getDeptDto().getDepartment_name() + "\n" + "◇ 직급: " + position
				+ "\n" + "◇ 상관명: " + manager_name + "\n" + "◇ 주소: " + address + "\n" + "◇ 연락처: " + mobile + "\n"
				+ "◇ 이메일: " + email + "\n" + "◇ 입사일자: " + hire_date + "\n" + "◇ 월급: " + salary + "원\n"
				+ "------------------------------\n";
	}

}
