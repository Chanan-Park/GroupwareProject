package groupware;

import java.util.Scanner;

public class InfoController {

	InterEmployeesDAO edao = new EmployeesDAO();

	// 회원가입 //
	// =============================================================================

	// *** 회원가입을 해주는 메소드 *** //
	// ----------------------------------------------------------------------------------------------
	public void employeeRegister(Scanner sc) {

		EmployeesDTO empDto = new EmployeesDTO();

		System.out.println("\n>>> ----- 회원가입 ----- <<<");

		// 1. 사원명
		String name = "";
		do {
			System.out.print("1. 사원명: ");
			name = sc.nextLine();

			if (name.trim().isEmpty()) {
				System.out.println("[경고] 공백으로는 입력할 수 없습니다. 다시 입력하세요.");
			} else {
				break;
			}

		} while (true);

		// 2. 비밀번호
		do { // 비밀번호 유효성 검사 ----------------------------------------------
			System.out.print("2. 비밀번호: ");
			String passwd = sc.nextLine();

			if (empDto.checkPwd(passwd)) {
				// 비밀번호 유효성검사 테스트 통과
				empDto.setPasswd(passwd);
				break;
			} else {
				// 비밀번호 유효성검사 테스트 통과 X
				System.out.println("\n[경고] 비밀번호는 대.소문자, 숫자, 특수문자가 혼합된 8글자 이상 15글자 이하의 글자로 입력하셔야 합니다.");
			}

		} while (true); // --------------------------------------------------

		// 3. 부서명
		// *** 부서명을 숫자로 입력받기 *** // -----------------------------------------
		String department_name = "";
		do {
			System.out.print("3. 부서명 [1.경영지원 / 2.인사 / 3.IT / 4.마케팅 / 5.재경 ] : ");
			department_name = sc.nextLine();

			// 입력받은 값이 1부터 5까지의 숫자라면 department_name 에 넣어준다.
			if ("1".equals(department_name) || "2".equals(department_name) || "3".equals(department_name)
					|| "4".equals(department_name) || "5".equals(department_name)) {

				if ("1".equals(department_name)) { // 입력받은 값이 1이라면 => 경영지원
					department_name = "경영지원";
				} else if ("2".equals(department_name)) { // 입력받은 값이 2라면 => 인사
					department_name = "인사";
				} else if ("3".equals(department_name)) { // 입력받은 값이 3이라면 => IT
					department_name = "IT";
				} else if ("4".equals(department_name)) { // 입력받은 값이 4라면 => 마케팅
					department_name = "마케팅";
				} else { // 입력받은 값이 5라면 => 재경
					department_name = "재경";
				}
				break;
			} else { // 입력받은 값이 1~5 외의 값이라면
				System.out.println("\n[경고] 부서명을 올바른 숫자로 입력해주세요.");
			}
		} while (true); // --------------------------------------------------------

		// 4. 직급
		// *** 직급을 숫자로 입력받기 *** // -----------------------------------------
		String position = "";
		do {
			System.out.print("4. 직급 [1.팀장 / 2.책임 / 3.선임 / 4.사원 ] : ");
			position = sc.nextLine();

			// 입력받은 값이 1부터 5까지의 숫자라면 position 에 넣어준다.
			if ("1".equals(position) || "2".equals(position) || "3".equals(position) || "4".equals(position)) {

				if ("1".equals(position)) { // 입력받은 값이 1이라면 => 팀장
					position = "팀장";
				} else if ("2".equals(position)) { // 입력받은 값이 2라면 => 책임
					position = "책임";
				} else if ("3".equals(position)) { // 입력받은 값이 3이라면 => 선임
					position = "선임";
				} else { // 입력받은 값이 4라면 => 사원
					position = "사원";
				}
				break;
			} else { // 입력받은 값이 1~5 외의 값이라면
				System.out.println("\n[경고] 직급을 올바른 숫자로 입력해주세요.");
			}

		} while (true); // --------------------------------------------------------

		// 5. 연락처
		// *** 연락처(휴대폰)을 11자리 숫자의 문자열로만 입력받아 000-0000-0000 형식의 문자열로 바꾸어 저장하기 *** //
		String mobile = "";

		do {
			System.out.print("5. 연락처(휴대폰) [11자리의 숫자로만 입력해주세요] : ");
			mobile = sc.nextLine();
			boolean isNumber = true;
			// 입력받은 mobile 이 숫자로만 되어있는지 확인
			for (int i = 0; i < mobile.length(); i++) {
				if (!Character.isDigit(mobile.charAt(i))) {
					isNumber = false;
				}
			} // end of for -------------------------

			if (mobile.length() == 11 && isNumber) { // 입력받은 값이 11자리의 숫자인지 확인하고 '-'를 넣어줌
				StringBuilder sb = new StringBuilder(mobile);
				sb.insert(3, "-");
				sb.insert(8, "-");
				mobile = sb.toString();

				if (!edao.checkMobile(mobile)) { // 사용가능한 연락처라면
					break;
				} else { // 이미 존재하는 연락처라면
					System.out.println("\n>> 이미 존재하는 연락처(휴대폰)입니다. 다시 입력해주세요. <<");
				}
			} else {
				System.out.println("\n[경고] 연락처(휴대폰)을 올바르게 입력하세요." + "(예시: 01012345678)");
			}

		} while (true); // ------------------------------------------------------------------

		// 6. 이메일
		String email = "";
		do {
			System.out.print("6. 이메일: ");
			email = sc.nextLine();

			if (!edao.checkEmail(email)) { // 사용가능한 이메일이라면
				break;
			} else { // 이미 존재하는 이메일이라면
				System.out.println("\n>> 이미 존재하는 이메일입니다. 다시 입력해주세요. <<");
			}
		} while (true);

		// 7. 주소
		System.out.print("7. 주소: ");
		String address = sc.nextLine();

		// 8. 생년월일
		// *** 생년월일을 6자리 숫자의 문자열로만 입력받아 "00/00/00" 형식의 문자열로 바꾸어 저장하기 *** //
		String birthdate = "";
		do {
			System.out.print("8. 생년월일: ");
			birthdate = sc.nextLine();

			int birthdate_length = birthdate.length();
			if (birthdate_length == 6) {
				StringBuilder sb = new StringBuilder(birthdate);
				sb.insert(2, "/");
				sb.insert(5, "/");

				birthdate = sb.toString();

				break;
			} else {
				System.out.println("\n[경고] 생년월일을 올바르게 입력하세요.\n" + "(예시: 990101)");
			}
		} while (true);

		// 9. 성별[남/여]
		// *** 성별 남/여 로만 입력받기 *** //
		// -------------------------------------------------------------
		String gender = "";
		do {
			System.out.print("9. 성별[남/여]: ");
			gender = sc.nextLine();

			if ("여".equals(gender) || "남".equals(gender)) {
				break;
			} else {
				System.out.println("\n[경고] 성별은 [남/여] 로만 입력해주세요.");
			}
		} while (true); // -------------------------------------------------------------------

		// 10. 직속상사 성명
		// *** 등록된 사원만 입력 가능하게 하고 <= edao.employeeCheck(manager_name)
		// 입력받은 사원명을 통해 직속상사의 사원번호를 manager_id에 입력받게 하기 <=
		// edao.makeEmployeeid(department_name, position) *** //
		String manager_name = "";
		do {
			System.out.print("10. 직속상사 성명: ");
			manager_name = sc.nextLine();

			if (edao.employeeCheck(manager_name) == 1) { // 리턴값이 1이라면 존재하는 사원명, 0이라면 존재하지 않는 사원명
				break;
			} else {
				System.out.println("\n[경고] 존재하지 않는 사원명입니다. 다시 입력해주세요.");
			}
		} while (true);

		String employee_id = edao.makeEmployeeid(department_name, position);

		// --------------------------------------------------------------------------------

		empDto.setEmployee_id(employee_id);
		empDto.setName(name);
		empDto.setPosition(position);
		empDto.setMobile(mobile);
		empDto.setEmail(email);
		empDto.setAddress(address);
		empDto.setBirthdate(birthdate);
		empDto.setGender(gender);
		empDto.setManager_id(edao.searchManager_id(manager_name));
		empDto.setDepartment_id(edao.searchDept_id(department_name));

		int n = edao.employeeRegister(empDto); // 리턴값이 int, 1이 나와야 정상

		if (n == 1) {
			System.out.println("\n>> 회원가입을 축하드립니다. <<");
		} else {
			System.out.println("\n>> 회원가입을 실패하였습니다. <<");
		}

	} // end of private void memberRegister(Scanner sc)
		// ----------------------------------------------------

	// 사원번호 / 비밀번호 찾기 //
	// =================================================================================

	// *** 사원번호/비밀번호를 찾아주는 메소드 *** //
	// -----------------------------------------------------
	public void searchIdPwd(Scanner sc) {
		String s_choice = "";
		do {
			System.out.println(">> 어떤 정보를 찾으시겠습니까? [ 1.사원번호 / 2.비밀번호 / 3.뒤로가기] <<");
			System.out.print("▷ 메뉴번호 입력: ");
			s_choice = sc.nextLine();

			switch (s_choice) {
			case "1": // 사원번호 찾기
				System.out.println("\n >>> --- 사원번호 찾기 --- <<<");
				String name = "";
				do {
					System.out.print("▷ 사원명: ");
					name = sc.nextLine();

					boolean result = edao.isNameExist(name); // 존재하는 사원명인지 확인
					if (result) { // 사원명이 존재하는 경우
						break;
					} else { // 사원명이 존재하지 않는 경우
						System.out.println("\n>> 존재하지 않는 사원입니다. <<");
					}
				} while (true);

				String mobile = "";
				do {
					System.out.print("▷ 연락처: ");
					mobile = sc.nextLine();
					boolean isNumber = true;
					// 입력받은 mobile 이 숫자로만 되어있는지 확인
					for (int i = 0; i < mobile.length(); i++) {
						if (!Character.isDigit(mobile.charAt(i))) {
							isNumber = false;
						}
					} // end of for -------------------------
						// 입력받은 mobile 이 11자리의 숫자인지 확인하고 '-'를 넣어줌
					if (isNumber && mobile.length() == 11) {
						StringBuilder sb = new StringBuilder(mobile);
						sb.insert(3, "-");
						sb.insert(8, "-");
						mobile = sb.toString();

						break;
					} else { // 입력받은 mobile 이 11자리의 숫자가 아닌 경우
						System.out.println("\n[경고] 연락처를 올바르게 입력하세요. (형식: 01012345678)");
					}
				} while (true);

				String searchResult = edao.searchEmpId(name, mobile);
				if (!"noExist".equals(searchResult)) { // 일치하는 사원이 있는 경우
					System.out.println("\n◇" + name + "님의 사원번호: " + searchResult);
				} else {
					System.out.println("\n>> 입력하신 사원정보와 일치하는 사원이 없습니다. <<");
				}

				break;

			// -------------------------------------------------------------------------------------

			case "2": // 비밀번호 찾기 ------------------------------------------
				System.out.println("\n>>> --- 비밀번호 찾기 --- <<<");
				String employee_id = "";
				do {
					System.out.print("▷ 사원번호: ");
					employee_id = sc.nextLine();

					boolean result = edao.isEmployee_idExist(employee_id); // 존재하는 사원명인지 확인
					if (result) { // 사원번호가 존재하는 경우
						break;
					} else { // 사원번호가 존재하지 않는 경우
						System.out.println("\n>> 존재하지 않는 사원입니다. <<");
					}
				} while (true);

				mobile = "";
				do {
					System.out.print("▷ 연락처: ");
					mobile = sc.nextLine();
					boolean isNumber = true;
					// 입력받은 mobile 이 숫자로만 되어있는지 확인
					for (int i = 0; i < mobile.length(); i++) {
						if (!Character.isDigit(mobile.charAt(i))) {
							isNumber = false;
						}
					} // end of for -------------------------
						// 입력받은 mobile 이 11자리의 숫자인지 확인하고 '-'를 넣어줌
					if (isNumber && mobile.length() == 11) {
						StringBuilder sb = new StringBuilder(mobile);
						sb.insert(3, "-");
						sb.insert(8, "-");
						mobile = sb.toString();

						break;
					} else { // 입력받은 mobile 이 11자리의 숫자가 아닌 경우
						System.out.println("\n[경고] 연락처를 올바르게 입력하세요. (형식: 01012345678)");
					}
				} while (true);

				searchResult = edao.searchEmpPwd(employee_id, mobile);
				if (!"noExist".equals(searchResult)) { // 일치하는 사원이 있는 경우
					System.out.println("\n◇ 사원번호 " + employee_id + "님의 비밀번호: " + searchResult);
				} else {
					System.out.println("\n>> 입력하신 사원정보와 일치하는 사원이 없습니다. <<");
				}

				break;
			case "3":

				break;
			default:
				System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
				break;
			} // end of switch
		} while (!("3".equals(s_choice)));

	} // end of public void searchIdPwd(Scanner sc)
		// ---------------------------------------------------------

	// 일반사원용 - 내 사원정보 수정 //
	// ----------------------------------------------------------------------------
	public void updateMyInfo(EmployeesDTO empDto, Scanner sc) {
		String s_menuNo = "";
		do {
			System.out.println("--------------------------------------------");
			System.out.println("1.비밀번호   2.연락처   3.이메일   4.주소   5.뒤로가기");
			System.out.println("-------------------------------------------");
			System.out.println(">> 어떤 정보를 수정하시겠습니까? <<");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {

			case "1": // 비밀번호 수정 ------------------------------------------
				String select_update = "passwd";
				String update_info = "";
				do {
					System.out.print("▷ 변경할 비밀번호 입력: ");
					update_info = sc.nextLine();

					if (empDto.checkPwd(update_info)) {
						// 비밀번호 유효성검사 테스트 통과
						empDto.setPasswd(update_info);
						break;
					} else {
						// 비밀번호 유효성검사 테스트 통과 X
						System.out.println("\n[경고] 비밀번호는 대.소문자, 숫자, 특수문자가 혼합된 8글자 이상 15글자 이하의 글자로 입력하셔야 합니다.");
					}
				} while (true);
				// 비밀번호 update 메소드 호출
				int result = edao.updateMyinfo(select_update, update_info, empDto.getEmployee_id());
				if (result == 1) {
					System.out.println("\n>> 회원정보 업데이트 성공 !! <<");
				} else {
					System.out.println("\n>> 회원정보 업데이트 실패 !! <<");
				}
				break; // end of 비밀번호 수정 ------------------------------------------

			case "2": // 연락처 수정 --------------------------------------
				select_update = "mobile";
				update_info = "";

				do {
					System.out.print("▷ 변경할 연락처 입력: ");
					update_info = sc.nextLine();
					boolean isNumber = true;
					// 입력받은 mobile 이 숫자로만 되어있는지 확인
					for (int i = 0; i < update_info.length(); i++) {
						if (!Character.isDigit(update_info.charAt(i))) {
							isNumber = false;
						}
					} // end of for -------------------------
					if (update_info.length() == 11 && isNumber) { // 입력받은 값이 11자리의 숫자가 맞는지 확인하고, 사이에 '-' 를 넣어서 저장
						StringBuilder sb = new StringBuilder(update_info);
						sb.insert(3, "-");
						sb.insert(8, "-");

						update_info = sb.toString();

						if (!edao.checkMobile(update_info)) { // 사용가능한 연락처라면
							break;
						} else { // 이미 존재하는 연락처라면
							System.out.println("\n>> 이미 존재하는 연락처(휴대폰)입니다. 다시 입력해주세요. <<");
						}
					} else {
						System.out.println("\n[경고] 연락처(휴대폰)을 올바르게 입력하세요.\n" + "(예시: 01012345678)");
					}

				} while (true);

				result = edao.updateMyinfo(select_update, update_info, empDto.getEmployee_id());

				if (result == 1) {
					System.out.println("\n>> 회원정보 업데이트 성공 !! <<");
				} else {
					System.out.println("\n>> 회원정보 업데이트 실패 !! <<");
				}
				break; // end of 연락처 수정 --------------------------------------

			case "3": // 이메일 수정 --------------------------------------------
				select_update = "email";
				update_info = "";
				System.out.print("▷ 변경할 이메일 입력: ");
				update_info = sc.nextLine();

				result = edao.updateMyinfo(select_update, update_info, empDto.getEmployee_id());
				if (result == 1) {
					System.out.println("\n>> 회원정보 업데이트 성공 !! <<");
				} else {
					System.out.println("\n>> 회원정보 업데이트 실패 !! <<");
				}
				break; // end of 이메일 수정 ------------------------------------------

			case "4": // 주소 수정 ---------------------------------------------------
				select_update = "address";
				update_info = "";
				System.out.print("▷ 변경할 주소 입력: ");
				update_info = sc.nextLine();

				result = edao.updateMyinfo(select_update, update_info, empDto.getEmployee_id());
				if (result == 1) {
					System.out.println("\n>> 회원정보 업데이트 성공 !! <<");
				} else {
					System.out.println("\n>> 회원정보 업데이트 실패 !! <<");
				}
				break; // end of 주소 수정 ------------------------------------------

			case "5": // 뒤로가기
				break;

			default:
				System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
				break;

			} // end of switch
				// -------------------------------------------------------------------

		} while (!("5".equals(s_menuNo)));

	} // end of public void updateMyInfo(EmployeesDTO empDto, Scanner sc)
		// ---------------------------------

	// 관리자용 메뉴 //
	// ========================================================================================

	// 관리자용 - 부서별 사원 조회 //
	// --------------------------------------------------------------------------------------
	public void showDeptEmp(Scanner sc) {
		String s_menuNo = "";
		String update_info = "";
		String select_update = "department_id";
		do {
			System.out.println("\n------------------------------------");
			System.out.println("1.경영지원   2.인사   3.IT   4.마케팅   5.재경\n" + "6.나가기");
			System.out.println("------------------------------------");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 경영지원
				update_info = "100";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "2": // 인사
				update_info = "200";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "3": // IT
				update_info = "300";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "4": // 마케팅
				update_info = "400";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "5": // 재경
				update_info = "500";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "6": // 뒤로가기
				break;
			default:
				System.out.println("[경고] 메뉴번호를 올바르게 입력하세요.");
				break;
			}
		} while (!("6".equals(s_menuNo)));
	} // end of public void showDeptEmp(Scanner sc)
		// -------------------------------------------------

	// 관리자용 - 직급별 사원 조회 //
	// --------------------------------------------------------------------------------------
	public void showPositionEmp(Scanner sc) {
		String s_menuNo = "";
		String update_info = "";
		String select_update = "position";
		do {
			System.out.println("\n------------------------------------");
			System.out.println("1.팀장   2.책임   3.선임   4.사원   5.나가기");
			System.out.println("------------------------------------");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 팀장
				update_info = "팀장";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "2": // 책임
				update_info = "책임";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "3": // 선임
				update_info = "선임";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "4": // 사원
				update_info = "사원";
				System.out.println(edao.showMember(update_info, select_update));
				break;
			case "5": // 뒤로가기
				break;
			default:
				System.out.println("[경고] 메뉴번호를 올바르게 입력하세요.");
				break;
			}
		} while (!("5".equals(s_menuNo)));
	} // end of public void showPositionEmp(Scanner sc)
		// -------------------------------------------

	// *** 관리자용 - 개별 사원정보조회 메소드 *** //
	// -----------------------------------------------------------------
	public EmployeesDTO empInfo(String s_employee_id) {
		EmployeesDTO empDto2 = null;

		empDto2 = edao.empInfo(s_employee_id);

		if (empDto2 != null) {
			empDto2 = edao.empInfo(s_employee_id);

		} else {
			System.out.println("\n>>> 존재하지 않는 회원번호입니다. <<<");
		}
		return empDto2;

	} // end of public EmployeesDTO empInfo(String s_employee_id)
		// -----------------------------------------

	// 관리자용 - 사원정보 수정 [연봉 / 직급 / 부서]
	// ----------------------------------------------------------------
	public void updateEmpInfo(EmployeesDTO empDto, Scanner sc) {

		String s_employee_id = "";
		do {
			System.out.println(">> 어떤 직원의 정보를 수정하시겠습니까? <<");
			System.out.print("▷ 사원번호 입력 : ");
			s_employee_id = sc.nextLine();

			if (empInfo(s_employee_id) == null) { // 존재하는 사원번호가 아니라면
				System.out.println("\n[경고] 존재하지 않는 사원번호입니다. 다시 입력하세요.");
			} else { // 존재하는 사원번호가 맞다면
				System.out.println(empInfo(s_employee_id).toString());
				break;
			}
		} while (true);

		String s_menu = "";
		do {
			System.out.println("\n------------------------------------");
			System.out.println("1.월급   2.직급   3.부서   4.나가기");
			System.out.println("------------------------------------");
			System.out.println(">> 어떤 정보를 수정하시겠습니까? <<");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_menu = sc.nextLine();

			switch (s_menu) {
			case "1": // 연봉 수정하기 ----------------------------------------------------------------
				String select_update = "salary";
				String update_info = "";
				do {
					System.out.print("▷ 변경할 월급 입력 : ");
					update_info = sc.nextLine();
					boolean isNumber = true;
					// 입력받은 salary가 숫자로만 되어있는지 확인

					for (int i = 0; i < update_info.length(); i++) {
						if (!Character.isDigit(update_info.charAt(i))) {
							isNumber = false;
						}
					} // end of for -------------------------
					if (isNumber && update_info.length() < 10) { // 숫자로만 입력했다면 break
						break;
					} else { // salary 를 잘못 입력받은 경우
						System.out.println("\n[경고] 월급를 올바른 숫자로 입력하세요.");
					}
				} while (true);

				int result = edao.updateInfo_admin(select_update, update_info, s_employee_id); // 업데이트 성공이라면 1, 실패라면 0
				if (result == 1) {
					System.out.println("\n>> 월급 업데이트 성공 <<");
				} else {
					System.out.println("\n>> 월급 업데이트 실패 <<");
				}
				break; // end of 연봉 수정하기
						// ----------------------------------------------------------------

			case "2": // 직급 수정하기 ----------------------------------------------------------
				select_update = "position";
				update_info = "";
				do {
					System.out.println("[ 1.팀장 / 2.책임 / 3.선임 / 4.사원 ]");
					System.out.print("▷ 변경할 직급 입력 : ");
					s_menu = sc.nextLine();
					if ("1".equals(s_menu) || "2".equals(s_menu) || "3".equals(s_menu) || "4".equals(s_menu)) {
						if ("1".equals(s_menu)) // 메뉴번호가 1이라면 팀장
							update_info = "팀장";
						else if ("2".equals(s_menu)) // 메뉴번호가 2이라면 책임
							update_info = "책임";
						else if ("3".equals(s_menu)) // 메뉴번호가 3이라면 선임
							update_info = "선임";
						else // 메뉴번호가 4이라면 사원
							update_info = "사원";
						break;
					} else {
						System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
					}
				} while (true);

				result = edao.updateInfo_admin(select_update, update_info, s_employee_id); // 업데이트 성공이라면 1, 실패라면 0
				if (result == 1) {
					System.out.println("\n>> 직급 업데이트 성공 <<");
				} else {
					System.out.println("\n>> 직급 업데이트 실패 <<");
				}
				break; // end of 직급 수정하기 ----------------------------------------------------------

			case "3": // 부서 수정하기 ----------------------------------------------------------------
				select_update = "department_id";
				update_info = "";
				do {
					System.out.println("[ 1.경영지원 / 2.인사 / 3.IT / 4.마케팅 / 5.재경 ]");
					System.out.print("▷ 변경할 부서 선택 : ");
					s_menu = sc.nextLine();
					if ("1".equals(s_menu) || "2".equals(s_menu) || "3".equals(s_menu) || "4".equals(s_menu)
							|| "5".equals(s_menu)) {
						if ("1".equals(s_menu)) // 메뉴번호가 1이라면 100번 부서(department_id)
							update_info = "100";
						else if ("2".equals(s_menu)) // 메뉴번호가 2이라면 200번 부서(department_id)
							update_info = "200";
						else if ("3".equals(s_menu)) // 메뉴번호가 3이라면 300번 부서(department_id)
							update_info = "300";
						else if ("4".equals(s_menu)) // 메뉴번호가 4이라면 400번 부서(department_id)
							update_info = "400";
						else // 메뉴번호가 5이라면 500번 부서(department_id)
							update_info = "500";
						break;
					} else {
						System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
					}
				} while (true);

				result = edao.updateInfo_admin(select_update, update_info, s_employee_id); // 업데이트 성공이라면 1, 실패라면 0
				if (result == 1) {
					System.out.println("\n>> 직급 업데이트 성공 <<");
				} else {
					System.out.println("\n>> 직급 업데이트 실패 <<");
				}
				break; // end of 부서 수정하기 -------------------------------------------------------

			case "4": // 뒤로가기
				break;

			default:
				System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
				break;

			} // end of switch
				// ----------------------------------------------------------------
		} while (!("4".equals(s_menu)));
	} // end of public void updateEmpInfo(EmployeesDTO empDto, Scanner sc)
		// -----------------------------------------

	// 관리자 메뉴 - 5.퇴직금 조회 //
	// ------------------------------------------------------------
	public void showPension(Scanner sc) {

		System.out.println("\n>>> --- 퇴직금 조회 --- <<<");
		String employee_id = "";
		String[] emp_arr;
		emp_arr = new String[2];
		do {
			System.out.print("▷ 사원번호 입력: ");
			employee_id = sc.nextLine();

			boolean result = edao.isEmployee_idExist(employee_id); // 존재하는 사원명인지 확인
			if (result) { // 사원번호가 존재하는 경우
				break;
			} else { // 사원번호가 존재하지 않는 경우
				System.out.println("\n>> 존재하지 않는 사원입니다. <<");
			}
		} while (true);

		emp_arr = edao.showPension(employee_id);

		System.out.println(emp_arr[0] + "님의 퇴직금은 " + emp_arr[1] + "원 입니다.");

	} // end of public void showPension()

	// 로그인 후 1.내정보조회/수정 ------------------------------------------------------------
	public void myInfo(EmployeesDTO empDto, Scanner sc) {
		if (empDto != null) { // null 이 아니라면
			String s_menuNo = "";
			do {
				System.out.println("------------------------------------");
				System.out.println("1.내정보 조회   2.내정보 수정   3.나가기");
				System.out.println("------------------------------------");
				System.out.print("▷ 메뉴번호 선택 : ");
				s_menuNo = sc.nextLine();
				switch (s_menuNo) {
				case "1": // 1. 내정보조회
					empDto = edao.myInfo(empDto.getEmployee_id()); // 내 사원정보 읽어오기
					System.out.println(empDto.toString()); // 출력
					break;

				case "2": // 2. 내정보수정
					updateMyInfo(empDto, sc);
					break;
				case "3": // 3.나가기

					break;

				default:
					System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
					break;
				}
			} while (!("3".equals(s_menuNo)));
		}
	} // end of public void myInfo(EmployeesDTO empDto, Scanner sc)
		// ---------------------------------

}
