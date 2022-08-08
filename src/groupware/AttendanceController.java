package groupware;

import java.text.*;
import java.util.*;

public class AttendanceController {
	
//	로그인 뒤 2.근태관리를 누를때 나타나는 메소드
	public void attendance(EmployeesDTO emp, Scanner sc) {
		
		InterAttendanceDAO addao = new AttendanceDAO();
		InterAnnualLeaveDAO aldao = new AnnualLeaveDAO();
		
		if(emp != null && "999".equals(emp.getEmployee_id())) { //관리자계정 로그인시
			String ad_menuno="";
			do {
				try {
					System.out.println("\n-------- [근태관리 메뉴] --------");
					System.out.println("1.근태조회   2.연차조회   3.나가기");
					System.out.println("------------------------------");
					System.out.print("▷ 메뉴 번호 선택 : ");
					ad_menuno = sc.nextLine();
					Integer.parseInt(ad_menuno);
				} catch(NumberFormatException e) {
					System.out.println("[경고] 메뉴 번호를 숫자로 입력하세요.\n");
				}
				
				switch (ad_menuno) {
				case "1": //근태조회
					showAttendance(sc,addao);
					break;
				case "2": //연차조회
					showAnnualLeave(sc,aldao,addao);
					break;
				case "3": //나가기
					break;
				default:
					System.out.println("[경고] 메뉴에 없는 번호입니다. 다시 입력하세요.\n");
					break;
				}
			} while(!"3".equals(ad_menuno));
		}//end of if (관리자 로그인)
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if(emp != null) { //일반계정 로그인시
			String ad_menuno="";
			do {
				try {
					System.out.println("\n----------------- [근태관리 메뉴] ------------------");
					System.out.println("1.출근   2.퇴근   3.내 근태조회   4.휴가신청   5.나가기");
					System.out.println("-------------------------------------------------");
					System.out.print("▷ 메뉴 번호 선택 : ");
					ad_menuno = sc.nextLine();
					
					Integer.parseInt(ad_menuno);
				} catch (NumberFormatException e) {
					System.out.println("[경고] 메뉴 번호를 숫자로 입력하세요.\n");
				}
				
				switch (ad_menuno) {
				case "1": //출근
					requestArrival(emp,addao,sc);
					break;
				case "2": //퇴근
					requestDepart(emp,addao,sc);
					break;
				case "3": //내 근태조회
					showMyAttendance(emp,addao,sc);
					break;
				case "4": //휴가신청
					// 남은 연차개수 조회
					int alCnt = aldao.showAnuualLeave(emp); //사용한 연차 개수
					if(alCnt == 12) { //사용한 연차 개수가 12개라면 => 연차 신청 반려
						System.out.println("[경고]" + emp.getName() + "님의 남은 연차가 없으므로 휴가 신청이 불가능합니다.\n");
						//연차 신청내역 보여주기
						showMyAnnualLeave(emp,aldao);
					}
					else { // 사용한 연차개수가 12개 미만이면 => 연차 신청
						System.out.println("[안내]" + emp.getName() + "님의 남은 연차는 " + (12-alCnt) + "개 입니다.");
						//연차신청내역 보여주기
						showMyAnnualLeave(emp,aldao);

						//연차 신청하기
						requestAnnualLeave(aldao,emp,sc,alCnt);
					}
					break;
				case "5":
					break;

				default:
					System.out.println("[경고] 메뉴에 없는 번호입니다. 다시 입력하세요.\n");
					break;
				}
			} while(!"5".equals(ad_menuno));
			
		}//end of else if(일반계정 로그인)
		
	}//end of public void attendance(EmployeesDTO emp, Scanner sc) 제일큰 메뉴

	
//관리자계정 메뉴 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//관리자계정 => 1. 근태조회 메뉴
	private void showAttendance(Scanner sc, InterAttendanceDAO addao) { 
		String ad_ad_menuno = "";
		do {
			System.out.println("\n---------------- [근태조회 메뉴] -----------------");
			System.out.println("1.모든 근태조회   2.월별 근태 조회   3.날짜별 근태 조회");
			System.out.println("4.사원별 근태 조회   5.나가기");
			System.out.println("-----------------------------------------------");
			System.out.print("▷ 메뉴 번호 선택 : ");
			ad_ad_menuno = sc.nextLine();
			
			switch (ad_ad_menuno) {
			case "1": //모든사원 근태조회
				showAllAttendance(addao);
				break;
			case "2": //월별 근태조회
				showMonthlyAttendance(addao,sc);
				break;
			case "3": //날짜별 근태조회
				showDailyAttendance(addao,sc);
				break;
			case "4": //사원번호별 근태조회
				showEmpidAttendance(addao,sc);
				break;
	
			case "5": //나가기
				break;
				
			default:
				System.out.println("[경고] 메뉴에 없는 번호입니다.\n");
				break;
			}
		} while(!"5".equals(ad_ad_menuno));
	}//end of 관리자계정 => 근태조회 메뉴
	

	//관리자계정 => 연차조회 메뉴
	private void showAnnualLeave(Scanner sc, InterAnnualLeaveDAO aldao, InterAttendanceDAO addao) {
		String ad_al_menuno = "";
		do {
			try {
				System.out.println("\n----------------------- [연차조회 메뉴] -----------------------");
				System.out.println("1.모든사원 연차조회   2.사원별 연차 조회   3.팀별 연차조회   4. 나가기");
				System.out.println("------------------------------------------------------------");
				System.out.print("▷ 메뉴 선택 : ");
				ad_al_menuno = sc.nextLine();
				Integer.parseInt(ad_al_menuno);
				
				switch (ad_al_menuno) { 
					case "1": //모든사원 연차조회
						showAllAnnualLeave(aldao);
						break;
					case "2": //일반사원 연차조회
						showEmpidAnnualLeave(addao,aldao,sc);
						break;
					case "3": //팀별 연차조회
						showDeptAnnualLeave(aldao,sc);
						break;
					case "4": //나가기
						break;
					default:
						System.out.println("[경고] 메뉴에 없는 번호입니다.\n");
						break;
				}
			} catch(NumberFormatException e) {
				System.out.println("[경고] 메뉴 번호는 정수로 입력하세요.\n");
			}
		} while(!"4".equals(ad_al_menuno));
	}//end of 연차조회 메뉴

	
	
//관리자계정 메뉴별 메소드///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//관리자계정 => 1. 근태조회 메뉴 => 1. 모든 근태조회
	private void showAllAttendance(InterAttendanceDAO addao) {
		List<AttendanceDTO> ad_all_list = addao.showAllAttendace();
		if(ad_all_list.size()>0) {
			StringBuilder sb = new StringBuilder();
			for(AttendanceDTO addto : ad_all_list) {
				sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() +"\t" + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t" + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
			}
			System.out.println("\n-------------------------------------------------------");
			System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
			System.out.println("-------------------------------------------------------");
			System.out.println(sb.toString());
		}
		else { //조회된 근태가 없을 경우
			System.out.println("[안내] 조회할 근태정보가 없습니다. 출퇴근 처리를 입력해주세요.\n");
		}
	}//end of 모든 근태조회
	
	// 관리자계정 => 1. 근태조회 메뉴 => 2. 월별 근태조회
	   private void showMonthlyAttendance(InterAttendanceDAO addao, Scanner sc) {

	      while (true) {
	         System.out.print("▷ 조회 날짜 입력 [YYYY/MM] : ");
	         String ad_month = sc.nextLine();

	         try {
	            new SimpleDateFormat("yyyy/MM").parse(ad_month); // 형식에 맞게 입력했는지 조회

	            Map<String, String> ad_month_map = new HashMap<String, String>();
	            ad_month_map.put("ad_month", ad_month);

	            List<AttendanceDTO> ad_month_list = addao.showMonthlyAttendance(ad_month_map);
	            if (ad_month_list.size() > 0) {
	               StringBuilder sb = new StringBuilder();
	               for (AttendanceDTO addto : ad_month_list) {
	                  sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() + "\t"
	                        + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t"
	                        + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
	               }
	               System.out.println("\n" + ad_month + " 의 근태 정보");
	               System.out.println("-------------------------------------------------------");
	               System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
	               System.out.println("-------------------------------------------------------");
	               System.out.println(sb.toString());
	            } else { // 조회된 근태가 없을 경우
	               System.out.println("[안내] 조회 날짜 " + ad_month + " 의 근태 정보가 없습니다. \n");
	            }

	            break;
	         } catch (ParseException e) {
	            System.out.println("[경고] 날짜를 형식에 맞게 다시 입력해주세요.\n");
	         }
	      }

	   } // end of private void showMonthlyAttendance(InterAttendanceDAO addao, Scanner sc) 

	// 관리자계정 => 1. 근태조회 메뉴 => 3. 날짜별 근태조회
	   private void showDailyAttendance(InterAttendanceDAO addao, Scanner sc) {

	      while (true) {
	         System.out.print("▷ 조회 날짜 입력 [YYYY/MM/DD] : ");
	         String ad_date = sc.nextLine();

	         try {
	            new SimpleDateFormat("yyyy/MM/dd").parse(ad_date); // 형식에 맞게 입력했는지 조회하기 위해서

	            Map<String, String> ad_date_map = new HashMap<String, String>();
	            ad_date_map.put("ad_date", ad_date);

	            List<AttendanceDTO> ad_date_list = addao.showDailyAttendance(ad_date_map);
	            if (ad_date_list.size() > 0) {
	               StringBuilder sb = new StringBuilder();
	               for (AttendanceDTO addto : ad_date_list) {
	                  sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() + "\t"
	                        + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t"
	                        + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
	               }
	               System.out.println("\n" + ad_date + " 의 근태 정보");
	               System.out.println("-------------------------------------------------------");
	               System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
	               System.out.println("-------------------------------------------------------");
	               System.out.println(sb.toString());
	            } else { // 조회된 근태가 없을 경우
	               System.out.println("[안내] 조회 날짜 " + ad_date + " 의 근태 정보가 없습니다. \n");
	            }
	            break;
	         } catch (ParseException e) {
	            System.out.println("[경고] 날짜를 형식에 맞게 다시 입력해주세요.\n");
	         }
	      }
	   }// end of 날짜별 근태조회
	
	
	//관리자계정 => 1. 근태조회 메뉴 => 4. 사원번호별 근태조회
	private void showEmpidAttendance(InterAttendanceDAO addao, Scanner sc) {
		do {
			System.out.print("▷ 사원번호 입력 : ");
			String ad_empid = sc.nextLine();
			
			int cnt=0;
			List<EmployeesDTO> empid_list = addao.showAllEmployeeId(); 
			if(empid_list.size()>0) {
				for(EmployeesDTO empdto : empid_list ) {
					if(ad_empid.equals(empdto.getEmployee_id()))
						cnt++;
				}
			}
			
			if( cnt!=1 ) //cnt== 0, 존재하지 않는 사원번호일 경우
				System.out.println("[경고] 존재하지 않는 사원번호입니다. 다시 입력해주세요.\n");
			else { //cnt==1, 즉 존재하는 사원번호인 경우
				Map<String,String> ad_empid_map = new HashMap<String, String>();
				ad_empid_map.put("ad_empid", ad_empid);
				
				List<AttendanceDTO> ad_empid_list = addao.showEmpidAttendance(ad_empid_map);
				if(ad_empid_list.size()>0) {
					StringBuilder sb = new StringBuilder();
					for(AttendanceDTO addto : ad_empid_list) {
						sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() + "\t" + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t" + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
					}
					System.out.println("-------------------------------------------------------");
					System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
					System.out.println("-------------------------------------------------------");
					System.out.println(sb.toString());
				}
				else { //조회된 근태가 없을 경우
					System.out.println("[안내] 사원번호 " + ad_empid + " 님의 조회된 근태상태가 없습니다. 출퇴근 처리를 입력해주세요.\n");
				}
				break;
			}
		} while(true);		
	}//end of 사원번호별 근태조회
	
	
	
	//관리자계정 => 2.연차조회 메뉴 => 1.모든사원 연차조회
	private void showAllAnnualLeave(InterAnnualLeaveDAO aldao) {
		List<AnnualLeaveDTO> al_all_list = aldao.showAllAnnualLeave();
		if (al_all_list.size()>0) { //연차조회한 list가 있으면
			StringBuilder sb = new StringBuilder();
			for(AnnualLeaveDTO aldto : al_all_list) {
				sb.append(aldto.getFk_employee_id() + "\t" + aldto.getEmpdto().getName() + "\t" + aldto.getStart_date() + "\t" + aldto.getEnd_date() + "\n");
			}
			System.out.println("\n-------------------------------------------");
			System.out.println("사원번호\t이름\t시작날짜\t\t마지막날짜");
			System.out.println("-------------------------------------------");
			System.out.println(sb.toString());
		}
		else {
			System.out.println("[안내] 조회할 연차 정보가 없습니다.\n");
		}		
	}//end of 모든사원 연차조회

	
	
	//관리자계정 => 2.연차조회 메뉴 => 2.일반사원 연차조회
	private void showEmpidAnnualLeave(InterAttendanceDAO addao, InterAnnualLeaveDAO aldao, Scanner sc) {
		do {
			System.out.print("▷ 사원번호 입력 : ");
			String empid = sc.nextLine();
			
			int cnt=0;
			List<EmployeesDTO> empid_list = addao.showAllEmployeeId(); 
			if(empid_list.size()>0) {
				for(EmployeesDTO empdto : empid_list ) {
					if(empid.equals(empdto.getEmployee_id()))
						cnt++;
				}
			}
			if( cnt==0 ) { //존재하지 않는 사원번호일 경우
				System.out.println("[경고] 존재하지 않는 사원번호입니다. 다시 입력해주세요.\n");
			}
			else { //존재하는 사원번호일 경우
				//해당 사원번호의 남은 연차개수 보여주기
				EmployeesDTO normal_emp = new EmployeesDTO();
				normal_emp.setEmployee_id(empid);
				int normal_emp_alcnt = aldao.showAnuualLeave(normal_emp);
				System.out.println("[안내] 사원번호 " + empid + " 님의 남은 연차 개수는 " + (12-normal_emp_alcnt) + "개 입니다." );
				
				//해당 사원번호의 연차신청 내역 보여주기
				Map<String,String> empidMap = new HashMap<String, String>();
				empidMap.put("empid", empid);
				List<AnnualLeaveDTO> al_list = aldao.showAnnualLeave(empidMap);
				if(al_list.size()>0) {
					StringBuilder sb = new StringBuilder();
					for(AnnualLeaveDTO aldto : al_list ) {
						sb.append(aldto.getFk_employee_id() + "\t" + aldto.getEmpdto().getName() + "\t" + aldto.getStart_date() + "\t" + aldto.getEnd_date() + "\n");
					}
					System.out.println("\n-------------------------------------------");
					System.out.println("사원번호\t이름\t시작날짜\t\t마지막날짜");
					System.out.println("-------------------------------------------");
					System.out.println(sb.toString());
				} else {
					System.out.println("[안내] 사원번호 " + empid + " 님의 연차사용 내역이 없습니다.\n");
				}
				break;
			}
		} while(true);	
	}//end of 일반사원 연차조회
	
	

	//관리자계정 => 2.연차조회 메뉴 => 3.팀별 연차조회
	private void showDeptAnnualLeave(InterAnnualLeaveDAO aldao, Scanner sc) {
		String deptid = "";
		do {
			System.out.print("▷ 팀번호 입력 : ");
			deptid = sc.nextLine();
			
			if(!("100".equals(deptid) || "200".equals(deptid) || "300".equals(deptid) || "400".equals(deptid) || "500".equals(deptid)))
				System.out.println("[경고] 존재하지 않는 팀번호입니다. 팀번호를 다시 입력해주세요.\n");
			
			else {
				Map<String,String> deptidMap = new HashMap<String, String>();
				deptidMap.put("deptid", deptid);
				
				List<AnnualLeaveDTO> al_dept_list = aldao.showDeptAnnualLeave(deptidMap);
				if(al_dept_list.size()>0) {
					StringBuilder sb = new StringBuilder();
					for(AnnualLeaveDTO aldto : al_dept_list) {
						sb.append(aldto.getEmpdto().getDepartment_id() + "\t" + aldto.getEmpdto().getDeptDto().getDepartment_name() + "\t" + aldto.getEmpdto().getEmployee_id() + "\t" + aldto.getEmpdto().getName()  + "\t" + aldto.getEmpdto().getEmployee_id() + "\t" + aldto.getEmpdto().getName() + "\t" + aldto.getStart_date() + "\t" + aldto.getEnd_date() + "\n");
					}
					System.out.println("\n-----------------------------------------------------------");
					System.out.println("팀번호\t팀이름\t사원번호\t이름\t시작날짜\t\t마지막날짜");
					System.out.println("-----------------------------------------------------------");
					System.out.println(sb.toString());
				} else {
					System.out.println("[안내] 팀번호 " + deptid + " 번의 연차사용 내역이 없습니다.\n");
				}
			}
		} while(!("100".equals(deptid) || "200".equals(deptid) || "300".equals(deptid) || "400".equals(deptid) || "500".equals(deptid)));
	}//end of 팀별 연차조회
	
	

//일반계정 메뉴//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//일반계정 => 1. 출근
	private void requestArrival(EmployeesDTO emp, InterAttendanceDAO addao, Scanner sc) {
		
		//현재 입력 시간이 출근 처리가능한 시간인지 알아보기 (9시~18시)
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date curtime = new Date();
		
		String today = sdf.format(curtime);
		String min = "09:00";
		String max = "18:00";
		
		Date today_date=null;
		Date comp_mindate=null;
		Date comp_maxdate=null;
		try {
			today_date = sdf.parse(today);
			comp_mindate = sdf.parse(min);
			comp_maxdate = sdf.parse(max);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		int minCompare = today_date.compareTo(comp_mindate); //크면 1 같으면 0 작으면 -1
		int maxCompare = today_date.compareTo(comp_maxdate); //크면 1 같으면 0 작으면 -1
		
		
		if(minCompare!=-1 && maxCompare!=1) { //입력가능한 시간인 경우 (오전 09시 ~ 오후 6시)
			String yn = "";
			do {
				System.out.print(">> 출근 처리 하시겠습니까? [Y/N] : ");
				yn = sc.nextLine();
				if("y".equalsIgnoreCase(yn)) { //y => 출근처리 insert
					int arrival = addao.requestArrival(emp.getEmployee_id());
					if(arrival == 1) System.out.println("[안내] 출근 처리되었습니다.\n");
					else if(arrival == -1) break; //이미 출근처리 한 경우 => 반복문을 빠져나감
				}
				else if ("n".equalsIgnoreCase(yn)) {
					System.out.println("[안내] 출근 처리를 취소하였습니다.\n");
				}
				else {
					System.out.println("[경고] Y 또는 N을 입력하세요.\n");
				}
			} while(!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
		}
		else {//입력가능한 시간이 아님
			System.out.println("[경고] 현재 출근 처리가 가능한 시간이 아닙니다. (출근 가능 시간 : 오전 9시~오후 6시)\n");
		}	
	}//end of 출근
	

	//일반 계정 => 2. 퇴근
	private void requestDepart(EmployeesDTO emp, InterAttendanceDAO addao, Scanner sc) {
		
		String yn3 = "";
		do {
			System.out.print("▷ 퇴근 처리 하시겠습니까? [Y/N] : ");
			yn3 = sc.nextLine();
			if(! ("y".equalsIgnoreCase(yn3) || "n".equalsIgnoreCase(yn3))) {
			System.out.println("[경고] Y 또는 N을 입력하세요.\n");
			}
			else {
				break;
			}
		}while(true);
			//퇴근컬럼이 null인지 아닌지 조회 (null일때만 퇴근처리할 수 있도록)
			String checkDepart = addao.checkDepart(emp.getEmployee_id());
			
			if("y".equalsIgnoreCase(yn3)) { //y 입력 => 퇴근처리 update
				if(checkDepart == null) {
					int depart = addao.requestDepart(emp.getEmployee_id()); //퇴근컬럼 update
					if(depart == 1) 
						System.out.println("[안내] 퇴근 처리되었습니다.\n");
				}
				else if(checkDepart != null && "nodata".equals(checkDepart)){ //퇴근컬럼이 null이 아니면서 "nodata"인 경우
					System.out.println("[경고] 오늘의 출근 이력이 없습니다.\n");
				}
				else { //퇴근컬럼이 null인 경우
					System.out.println("[경고] 이미 퇴근 처리 하셨습니다.\n"); 
				}
				
			}
			else if ("n".equalsIgnoreCase(yn3) ) {
				System.out.println("[안내] 퇴근 처리를 취소하였습니다.\n");
			}
	
	}//end of 퇴근


	//일반 계정 => 3.내 근태조회
	private void showMyAttendance(EmployeesDTO emp, InterAttendanceDAO addao, Scanner sc) {
		//이번달 근태 조회하기
		List<AttendanceDTO> myad_list = addao.showMyAttendance(emp);
		if(myad_list.size()>0) {
			StringBuilder sb = new StringBuilder();
			for(AttendanceDTO addto : myad_list) {
				sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() + "\t" + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t" + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
			}
			System.out.println("\n-------------------------------------------------------");
			System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
			System.out.println("-------------------------------------------------------");
			System.out.println(sb.toString());
		}
		else {
			System.out.println("[안내] 이번달 " + emp.getName() + " 님의 조회된 근태 정보가 없습니다.\n");
		}
		
		//다른달 근태 조회하기
		String yn = "";
		String month = "";
		do {
			System.out.print("▷ 다른 달의 근태 정보를 조회하시겠습니까? [Y/N] : ");
			yn = sc.nextLine();
			if("y".equalsIgnoreCase(yn)) {
				do {
					System.out.print("▷ 조회할 달 [YYYY/MM] 입력 (취소하려면 N을 입력하세요) : ");
					month = sc.nextLine();
					
					try {
			          new SimpleDateFormat("yyyy/MM").parse(month);
			          
			          Map<String, String> monthMap = new HashMap<>();
						monthMap.put("month", month);
						
						List<AttendanceDTO> myad_monthlyList = addao.showMyMonthlyAttendance(emp,monthMap);
						if(myad_monthlyList.size()>0) {
							StringBuilder sb = new StringBuilder();
							for(AttendanceDTO addto : myad_monthlyList) {
								sb.append(addto.getFk_employee_id() + "\t" + addto.getEmpdto().getName() + "\t" + addto.getAttendance_date() + "\t" + addto.getArrival_time() + "\t" + addto.getDepart_time() + "\t" + addto.getStatus() + "\n");
							}
							System.out.println("\n-------------------------------------------------------");
							System.out.println("사원번호\t이름\t날짜\t\t출근시각\t퇴근시각\t출결");
							System.out.println("-------------------------------------------------------");
							System.out.println(sb.toString());
						}
						else {
							System.out.println("[안내] " + month + "의 " + emp.getName() + "님의 조회된 근태 정보가 없습니다.\n");
						}
					}catch(ParseException e) {
						if(!"n".equalsIgnoreCase(month))
							System.out.println("[경고] 날짜를 형식에 맞게 입력하세요.\n");
					}//end of trycatch
				}while(!"n".equalsIgnoreCase(month));
			}
			else if("n".equalsIgnoreCase(yn)) {
				System.out.println("[안내] 다른 달의 근태 정보 조회를 취소하였습니다.\n");
			}
			else {
				System.out.println("[경고] Y 또는 N 만 입력하세요. \n");
			}
			
		} while(!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
	}//end of 내 근태조회
	
	
	
	//일반계정 => 4.휴가신청 => 연차신청내역 보여주기
	private void showMyAnnualLeave(EmployeesDTO emp, InterAnnualLeaveDAO aldao) {
		List<AnnualLeaveDTO> al_list = aldao.showAnnualLeave(emp);
		if(al_list.size()>0) {
			StringBuilder sb = new StringBuilder();
			for(AnnualLeaveDTO aldto : al_list) {
	   			sb.append(aldto.getFk_employee_id() + "\t" + aldto.getStart_date() + "\t"  + aldto.getEnd_date() + "\n");
			}
			System.out.println(emp.getName() + "님의 연차 신청 내역");
			System.out.println("------------------------------------");
			System.out.println("사원번호\t시작날짜\t\t마지막날짜");
			System.out.println("------------------------------------");
			System.out.println(sb.toString());
		}		
	}//end of 연차신청내역 보여주기
	
	//일반계정 => 4.휴가신청 => 연차 신청하기
	private void requestAnnualLeave(InterAnnualLeaveDAO aldao, EmployeesDTO emp, Scanner sc, int alCnt) {
		long request_alCnt = 0;
		String start_date = "";
		String end_date = "";
		String yn2 = "";
		do {
			System.out.print("▷ 휴가를 신청하시겠습니까? [Y/N] : ");
			yn2 = sc.nextLine();
			if("y".equalsIgnoreCase(yn2)) { // y를 누르면 => 휴가신청  
				do {
					System.out.print("▷ 휴가 시작 날짜 입력 [YYYY/MM/DD] : ");
					start_date = sc.nextLine();
					System.out.print("▷ 휴가 마지막 날짜 입력 [YYYY/MM/DD] : ");
					end_date = sc.nextLine();
					
					// 신청할 연차 개수 구하기
					try {
						Date fm1 = new SimpleDateFormat("yyyy/MM/dd").parse(start_date);
						Date fm2 = new SimpleDateFormat("yyyy/MM/dd").parse(end_date);
						
						request_alCnt = ((fm2.getTime() - fm1.getTime()) / 1000)/(24*60*60) + 1;
						break;
					} catch (ParseException e) {
						System.out.println("[경고] 날짜를 형식에 맞게 다시 입력해주세요.\n");
					}
				} while(true);
			
				if((12-alCnt) < request_alCnt) { //남은 연차 개수보다 신청할 연차 개수가 더 많으면 => 취소
					System.out.println("[경고] 남은 연차 개수가 모자라므로 다시 입력하세요.\n신청한 연차 개수는 " + request_alCnt + "개, 현재 남은 연차개수는 " + (12-alCnt) + "개 입니다.\n");
				}
				else { //남은 연차 개수가 신청할 연차개수보다 적거나 같으면 => 신청.
					AnnualLeaveDTO al = new AnnualLeaveDTO();
					al.setStart_date(start_date);
					al.setEnd_date(end_date);
					int n = aldao.requestAnnualLeave(emp, al);
					if(n==1) {
						System.out.println("[안내] 휴가 신청을 완료하였습니다.\n");
					}
				}
			}
			else if ("n".equalsIgnoreCase(yn2)) {
				System.out.println("[안내] 휴가 신청을 취소하셨습니다.");
			}
			else {
				System.out.println("[경고] Y 또는 N 을 입력하세요.\n");
			}
		} while(!("y".equalsIgnoreCase(yn2) || "n".equalsIgnoreCase(yn2)));
	}//end of 연차 신청하기





	
	
}
