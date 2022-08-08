package groupware;

import java.util.*;

public class TotalController {

	InterEmployeesDAO edao = new EmployeesDAO();
	ApprovalController ap_ctrl = new ApprovalController();
	AttendanceController ad_ctrl = new AttendanceController();
	InfoController infoCtrl = new InfoController();
	ca_ctrl cacatrl = new ca_ctrl();
	BoardCtroller bo_ctrl = new BoardCtroller();

	// *** 시작메뉴 *** // ------------------------------------------------------------
	public void menu_Start(Scanner sc) {

		String s_choice = "";

		do {
			System.out.println("----------------- 시작메뉴 -----------------");
			System.out.println("1. 회원가입   2. 로그인   3. ID/PW찾기   4. 종료");
			System.out.println("------------------------------------------");

			System.out.print("▷ 메뉴번호 선택: ");
			s_choice = sc.nextLine();

			switch (s_choice) {
			case "1": // 회원가입
				infoCtrl.employeeRegister(sc);
				break;

			case "2": // 로그인
				login(sc);// 로그인 메뉴로 가기
				break;

			case "3": // ID/PW찾기
				infoCtrl.searchIdPwd(sc);
				break;

			case "4": // 종료
				ProjectDBConnection.closeConnection();
				
				break;
			default:
				break;
			}
		} while (!"4".equals(s_choice));

	} // end of public void menu_Start(Scanner sc)
		// -------------------------------------------

	// *** 로그인 후 메뉴 *** //
	// -------------------------------------------------------------
	public void logined_menu(EmployeesDTO empDto, Scanner sc) {
		String s_menuNo;

		if ( !"999".equals(empDto.getEmployee_id())) { // 일반 직원일 경우
			do {
				System.out.println("\n>>> ------ [" + empDto.getName() + "님 로그인중..] ------ <<<");
				System.out.println("1. 나의정보   2. 근태관리   3. 일정관리");
				System.out.println("4. 전자결재   5. 게시판 가기   6. 로그아웃");
				System.out.println("------------------------------------------------------");
				System.out.print("▷ 메뉴번호 선택 : ");
				s_menuNo = sc.nextLine();

				switch (s_menuNo) {
				case "1": // 나의 정보
					infoCtrl.myInfo(empDto, sc);
					break;

				case "2": // 근태 관리
					ad_ctrl.attendance(empDto, sc);
					break;

				case "3": // 일정 관리
					cacatrl.calendar(empDto, sc);
					break;

				case "4": // 전자 결재
					ap_ctrl.approval_menu(empDto, sc);
					break;

				case "5": // 게시판 가기				
					
					bo_ctrl.menu_Board(empDto, sc);
					
					break;

				case "6":
					System.out.println(">> 로그아웃합니다. <<");
			
					break;

				default:
					break;
				}

			} while (!"6".equals(s_menuNo));
		}

		else { // 관리자일경우
			do {
				System.out.println("\n>>> ------ [관리자 로그인중..] ------ <<<");
				System.out.println("1. 직원정보   2. 근태관리   3. 일정관리");
				System.out.println("4. 게시판 가기    5. 로그아웃");
				System.out.println("---------------------------------------");
				System.out.print("▷ 메뉴번호 선택 : ");
				s_menuNo = sc.nextLine();
				String s_menu = "";
				switch (s_menuNo) {
				case "1": // 1. 직원정보조회/수정
					do {
						System.out.println("\n------------------------------------");
						System.out.println("1.직원정보 조회   2.직원정보 수정   3.뒤로가기");
						System.out.println("------------------------------------");
						System.out.print("▷ 메뉴번호 선택 : ");
						s_menu = sc.nextLine();
	
						switch (s_menu) {
						case "1": // 1. 직원정보조회
							if( empDto != null  && "999".equals(empDto.getEmployee_id())) {
								do {
									System.out.println("\n------------------------------------");
									System.out.println("1.모든 직원정보 조회   2.부서별 조회   3.직급별 조회\n"
											         + "4.퇴직금 조회        5.뒤로가기");
									System.out.println("------------------------------------");
									System.out.print("▷ 메뉴번호 선택 : ");
									s_menu = sc.nextLine();
									switch (s_menu) {
									case "1": // 모든 직원정보 조회
										System.out.println(edao.showAllMember());
										break;
									case "2": // 부서별 직원정보 조회
										infoCtrl.showDeptEmp(sc);
										break;
									case "3": // 직급별 직원정보 조회
										infoCtrl.showPositionEmp(sc);
										break;
									case "4": // 퇴직금 조회
										infoCtrl.showPension(sc);
										break;
									case "5":
										break;
		
									default:
										System.out.println("[경고] 메뉴번호를 올바르게 입력하세요.");
										break;
									}
								} while(!("5".equals(s_menu)));
									
							} // end of if( empDto != null  && "999".equals(empDto.getEmployee_id()))
	
						case "2": // 2. 직원정보수정
							infoCtrl.updateEmpInfo(empDto, sc);
							break;
							
						case "3": // 3. 뒤로가기
							break;
							
						default:
							System.out.println("\n[경고] 메뉴번호를 올바르게 입력하세요.");
							break;
						}
					} while(!("3".equals(s_menu)));

				case "2": // 근태 관리
					ad_ctrl.attendance(empDto, sc);
					break;

				case "3": // 일정 관리
					cacatrl.calendar(empDto, sc);
					break;

				case "4": // 게시판 가기
					bo_ctrl.menu_Board(empDto, sc);
					break;

				case "5": // 로그아웃
					System.out.println(">> 로그아웃합니다. <<");
					break;

				default:
					break;
				}
			} while (!"5".equals(s_menuNo));
		}

	} // end of private void logined_menu(EmployeesDTO empDto, Scanner sc)
		// ----------------------------

	// *** 로그인을 해주는 메소드 *** // -----------------------------------------------------------------
		private EmployeesDTO login(Scanner sc) {
			EmployeesDTO empDto = null;

			System.out.println("\n >>> --- 로그인 --- <<<");
			String employee_id = "";
			do {
				System.out.print("▷ 사원번호: ");
				employee_id = sc.nextLine();
				if(edao.isExistEmpId(employee_id)) {
					break;
				}
				else {
					System.out.println("\n>> 존재하지 않는 사원번호입니다. 다시 입력해주세요. <<\n");
				}
			} while(true);
			
			System.out.print("▷ 비밀번호: ");
			String passwd = sc.nextLine();

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("employee_id", employee_id);
			paraMap.put("passwd", passwd);

			empDto = edao.login(paraMap);
			
			if (empDto != null) {
				System.out.println("\n>>> 로그인 성공!! <<<");
				logined_menu(empDto, sc); // 로그인 후 메뉴
			} else
				System.out.println("\n>>> 로그인 실패!! <<<");

			return empDto;

		} // end of private EmployeesDTO login(Scanner sc) ---------------------------------
	
	

}
