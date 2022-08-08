package groupware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ca_ctrl {
	
	// field
	InterEmployeesDAO edao = new EmployeesDAO();
	InterCalendarDAO cdao = new CalendarDAO();

	// 일정 관리 메뉴
	public void calendar(EmployeesDTO empDto, Scanner sc) {
		
		String s_menuNo = "";
		do {
			System.out.println("\n------------- 일정관리 메뉴 ["+empDto.getName()+" 님 로그인중..] --------------\n"
				           	 + " 1.일정보기    2.일정추가    3.일정수정    4.일정삭제    5.나가기\n"
				           	 + "------------------------------------------------------");
			
			System.out.print("▷ 메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();
			
			switch (s_menuNo) {
			case "1":  // 일정보기 메뉴
				menu_Calendar(empDto, sc);
				break;
				
			case "2":  // 일정 추가하기
				int n = write(empDto, sc);
				
				if(n == 1) {
					System.out.println(">> 일정이 추가되었습니다. << \n");    // 일정추가 성공한 경우
				}
				else if(n == -1) {
					System.out.println(">> SQL 구문 경고발생으로 일정 추가가 실패되었습니다. << \n");
					break;
				}
				else {
					System.out.println(">> 일정 추가를 취소하셨습니다. << \n"); // 유저가 취소한 경우
				}
				break;
				
			case "3":  // 일정 수정하기
				n = updateCalendar(empDto, sc); 
				
				if(n == 1) {
					System.out.println(">> 일정이 수정되었습니다. << \n");
				}
				else if(n == -1) {
					break;
				}
				break;	
				
			case "4":  // 글삭제하기
				n = deleteCalendar(empDto, sc);
				
				if(n == 1) {
					System.out.println(">> 일정이 성공적으로 삭제되었습니다. << \n");
				}
				else if(n == -1){ // 글수정을 입력했으나 실패된 경우
					System.out.println(">> SQL 구문 경고발생으로 일정 삭제가 실패되었습니다. << \n"); // 코드상 에러..
				}
				break;
				
			case "5":  // 나가기
				break;
				
			default:
				System.out.println(">> [경고] 메뉴에 없는 번호 입니다. << \n");
				break;
			}// end of switch (s_menuNo)----------------
			
		} while( !("5".equals(s_menuNo)) );
		
	}// end of private void calendar(EmployeesDTO empDto, Scanner sc)-------------------------------------

	
	
	// 전체 일정보기 메뉴
	private void menu_Calendar(EmployeesDTO empDto, Scanner sc) {
		
		String viewno = "";
		
		if(!"999".equals(empDto.getEmployee_id())) { // 관리자를 제외한 일반 직원계정일 경우
			do {
				System.out.println("\n-------------------- 일정보기 메뉴 --------------------\n"
					           	 + " 1.내 일정보기    2.팀 일정보기    3.전체 일정보기    4.나가기\n"
					           	 + "------------------------------------------------------");
		
				System.out.print("▷ 메뉴번호 선택 : ");
				viewno = sc.nextLine();
		
				switch (viewno) {  
				case "1":  // 내 일정보기
					myViewCalendar(empDto, sc);
					break;
		
				case "2":  // 팀 일정보기
					teamViewCalendar(empDto, sc);
					break;
					
				case "3":  // 전체 일정보기
					totalViewCalendar(sc);
					
					break;
					
				case "4":
					break;
					
				default:
					System.out.println(">> [경고] 메뉴에 없는 번호 입니다. << \n");
					break;
				}
			} while( !("4".equals(viewno)) );
		}// end of if(!"999".equals(empDto.getEmployee_id()))-------------------
		
		if("999".equals(empDto.getEmployee_id())) { // 관리자 계정일 경우
			do {
				System.out.println("\n-------------------- 일정보기 메뉴 --------------------\n"
					           	 + " 1.전체 일정보기    2.나가기\n"
					           	 + "------------------------------------------------------");
		
				System.out.print("▷ 메뉴번호 선택 : ");
				viewno = sc.nextLine();
		
				switch (viewno) {  
				case "1":  // 전체 일정보기
					totalViewCalendar(sc);
					
					break;
					
				case "2":
					break;
					
				default:
					System.out.println(">> [경고] 메뉴에 없는 번호 입니다. << \n");
					break;
				}
			} while( !("2".equals(viewno)) );
		}// end of if("999".equals(empDto.getEmployee_id()))-------------------
		
	}// end of private void menu_Calendar(EmployeesDTO empDto, Scanner sc)-------------------------------------	
	
	

	// 내 일정보기
	private void myViewCalendar(EmployeesDTO empDto, Scanner sc) {

		List<CalendarDTO> myViewCalendar = cdao.myViewCalendar(empDto);
		
		
		if( myViewCalendar.size() > 0 ) {
			// 일정이 존재하는 경우
			
				Calendar currentDate = Calendar.getInstance();
				// 현재 날짜와 시간을 얻어온다.
				
				SimpleDateFormat dateft = new SimpleDateFormat("yyyy년 MM월"); // "연월일" 만 본다.
				String currentMonth = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년 MM월" 포맷으로 바꾼다는 것이다.
				
				SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String currentMonth2 = sdfrmt.format(new Date()).substring(5,7);
				
				
				System.out.println("\n---------------------- ["+currentMonth+" 일정] ---------------------");
				System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
				System.out.println("------------------------------------------------------------");
	
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<myViewCalendar.size(); i++) { // select 된 행 개수만큼 돌린다.
					if(currentMonth2.equals(myViewCalendar.get(i).getCalendar_date().substring(5,7))) {
						sb.append(myViewCalendar.get(i).showCalendarTitle() + "\n"); 
					    // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
					}
				}// end of for-----------------------------------
			
				System.out.println(sb.toString()); // 쌓아둔 것을 찍는다.
		}
		else { // 일정이 1개도 존재하지 않는 경우
			System.out.println(">> 업로드된 일정이 없습니다. << \n");
		}
		
		//------------------//
		String yn = "";
		String searchMonth = "";
		do {
			System.out.print(">> 다른 달 일정도 보시겠습니까?[Y/N] => ");
			yn = sc.nextLine();
			
			if( "y".equalsIgnoreCase(yn) ) { 
				
				System.out.print("▷ 검색할 달 [ex. 07] : ");
				searchMonth = sc.nextLine();
				
				try { // 유효성 검사를 해준다.
						Integer.parseInt(searchMonth); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
					} catch (NumberFormatException e) {
						System.out.println(">> [검색 실패] 숫자로만 입력하세요! << \n");
					}
					
				
				String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
				int count = 0;
				for (int i=0; i<months.length; i++) {
					if( String.valueOf(searchMonth).equals(months[i]) ) { // 01~12 을 입력한다면,
						count++;
						break;
					}// end of if-------------
					
				}// end of for (int i=0; i<months.length; i++)-------------
				if(count==0) {
					System.out.println("[경고] 01 ~ 12 월을 입력하세요! \n");
					continue;
				}
					
			
	            Calendar currentDate = Calendar.getInstance();
	            SimpleDateFormat dateft = new SimpleDateFormat("yyyy년"); // "연월일" 만 본다.
	            String currentyear = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년" 포맷으로 바꾼다는 것이다.
	            
	            StringBuilder sb2 = new StringBuilder();
	            int count2 = 0;
	            System.out.println("\n---------------------- ["+currentyear+searchMonth+"월"+" 일정] ---------------------");
	            System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
	            System.out.println("------------------------------------------------------------");
	            for(int j=0; j<myViewCalendar.size(); j++) { // 선택한 월의 select 된 행 개수만큼 돌린다.
	               if(String.valueOf(searchMonth).equals(myViewCalendar.get(j).getCalendar_date().substring(5,7))) {  // 검색한 월과 저장한 월이 일치할 경우
	                  sb2.append(myViewCalendar.get(j).showCalendarTitle() + "\n"); 
	                  count2++;
	                   // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
	               }
	            }// end of for-----------------------------------
	            System.out.println(sb2.toString()); // 쌓아둔 것을 찍는다.
	            
				
				if(count2==0) {
					System.out.println(">> 해당 월에 업로드된 일정이 없습니다! << \n");
				}
				
			}
			else if( "n".equalsIgnoreCase(yn) ) {
				break;
			}
			else {
				System.out.println(">> [경고] Y 또는 N 만 입력하세요! << \n");
			}
		} while(true);
		
	}// end of private void myViewCalendar(EmployeesDTO empDto, Scanner sc)---------------------


	
	// 팀 일정보기
	private void teamViewCalendar(EmployeesDTO empDto, Scanner sc) {
		List<CalendarDTO> teamViewCalendar = cdao.teamViewCalendar(empDto);
		
		if( teamViewCalendar.size() > 0 ) {
			// 일정이 존재하는 경우
			
				Calendar currentDate = Calendar.getInstance();
				// 현재 날짜와 시간을 얻어온다.
				
				SimpleDateFormat dateft = new SimpleDateFormat("yyyy년 MM월"); // "연월일" 만 본다.
				String currentMonth = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년 MM월" 포맷으로 바꾼다는 것이다.
				
				SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String currentMonth2 = sdfrmt.format(new Date()).substring(5,7);
				
				
				System.out.println("\n---------------------- ["+currentMonth+" 일정] ---------------------");
				System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
				System.out.println("------------------------------------------------------------");
	
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<teamViewCalendar.size(); i++) { // select 된 행 개수만큼 돌린다.
					if(currentMonth2.equals(teamViewCalendar.get(i).getCalendar_date().substring(5,7))) {
						sb.append(teamViewCalendar.get(i).showCalendarTitle() + "\n"); 
					    // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
					}
				}// end of for-----------------------------------
			
				System.out.println(sb.toString()); // 쌓아둔 것을 찍는다.
		}
		else { // 일정이 1개도 존재하지 않는 경우
			System.out.println(">> 업로드된 일정이 없습니다. << \n");
		}
		
		//------------------//
		String yn = "";
		String searchMonth = "";
		do {
			System.out.print(">> 다른 달 일정도 보시겠습니까?[Y/N] => ");
			yn = sc.nextLine();
			
			if( "y".equalsIgnoreCase(yn) ) { 
				
				System.out.print("▷ 검색할 달 [ex. 07] : ");
				searchMonth = sc.nextLine();
				
				try { // 유효성 검사를 해준다.
						Integer.parseInt(searchMonth); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
					} catch (NumberFormatException e) {
						System.out.println(">> [검색 실패] 숫자로만 입력하세요! << \n");
					}
					
				
				String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
				int count = 0;
				for (int i=0; i<months.length; i++) {
					if( String.valueOf(searchMonth).equals(months[i]) ) { // 01~12 을 입력한다면,
						count++;
						break;
					}// end of if-------------
					
				}// end of for (int i=0; i<months.length; i++)-------------
				if(count==0) {
					System.out.println("[경고] 01 ~ 12 월을 입력하세요! \n");
					continue;
				}
					
			
				 	Calendar currentDate = Calendar.getInstance();
		            SimpleDateFormat dateft = new SimpleDateFormat("yyyy년"); // "연월일" 만 본다.
		            String currentyear = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년" 포맷으로 바꾼다는 것이다.
		            
				
				StringBuilder sb2 = new StringBuilder();
				int count2 = 0;
				System.out.println("\n---------------------- ["+currentyear+searchMonth+"월"+" 일정] ---------------------");
	            System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
	            System.out.println("------------------------------------------------------------");
				for(int j=0; j<teamViewCalendar.size(); j++) { // 선택한 월의 select 된 행 개수만큼 돌린다.
					if(String.valueOf(searchMonth).equals(teamViewCalendar.get(j).getCalendar_date().substring(5,7))) {  // 검색한 월과 저장한 월이 일치할 경우
						sb2.append(teamViewCalendar.get(j).showCalendarTitle() + "\n"); 
						count2++;
					    // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
					}
				}// end of for-----------------------------------
				System.out.println(sb2.toString()); // 쌓아둔 것을 찍는다.
				
				if(count2==0) {
					System.out.println(">> 해당 월에 업로드된 일정이 없습니다! << \n");
				}
				
			}
			else if( "n".equalsIgnoreCase(yn) ) {
				break;
			}
			else {
				System.out.println(">> [경고] Y 또는 N 만 입력하세요! << \n");
			}
		} while(true);		
	}// end of private void teamViewCalendar(EmployeesDTO empDto, Scanner sc)-----------------------------
	
	

	// 전체 일정보기
	private void totalViewCalendar(Scanner sc) {
		
		List<CalendarDTO> totalViewCalendar = cdao.totalViewCalendar();
		
		if(totalViewCalendar.size() > 0) {
			// 일정이 존재하는 경우
			
				Calendar currentDate = Calendar.getInstance();
				// 현재 날짜와 시간을 얻어온다.
				
				SimpleDateFormat dateft = new SimpleDateFormat("yyyy년 MM월"); // "연월일" 만 본다.
				String currentMonth = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년 MM월" 포맷으로 바꾼다는 것이다.
				
				SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String currentMonth2 = sdfrmt.format(new Date()).substring(5,7);
				
				
				System.out.println("\n---------------------- ["+currentMonth+" 일정] ---------------------");
				System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
				System.out.println("------------------------------------------------------------");
	
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<totalViewCalendar.size(); i++) { // select 된 행 개수만큼 돌린다.
					if(currentMonth2.equals(totalViewCalendar.get(i).getCalendar_date().substring(5,7))) {
						sb.append(totalViewCalendar.get(i).showCalendarTitle() + "\n"); 
					    // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
					}
				}// end of for-----------------------------------
			
				System.out.println(sb.toString()); // 쌓아둔 것을 찍는다.
		}
		else { // 일정이 1개도 존재하지 않는 경우
			System.out.println(">> 업로드된 일정이 없습니다. << \n");
		}
		
		//------------------//
		String yn = "";
		String searchMonth = "";
		do {
			System.out.print(">> 다른 달 일정도 보시겠습니까?[Y/N] => ");
			yn = sc.nextLine();
			
			if( "y".equalsIgnoreCase(yn) ) { 
				
				System.out.print("▷ 검색할 달 [ex. 07] : ");
				searchMonth = sc.nextLine();
				
				try { // 유효성 검사를 해준다.
						Integer.parseInt(searchMonth); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
					} catch (NumberFormatException e) {
						System.out.println(">> [검색 실패] 숫자로만 입력하세요! << \n");
					}
					
				
				String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
				int count = 0;
				for (int i=0; i<months.length; i++) {
					if( String.valueOf(searchMonth).equals(months[i]) ) { // 01~12 을 입력한다면,
						count++;
						break;
					}// end of if-------------
					
				}// end of for (int i=0; i<months.length; i++)-------------
				if(count==0) {
					System.out.println("[경고] 01 ~ 12 월을 입력하세요! \n");
					continue;
				}
					
				Calendar currentDate = Calendar.getInstance();
	            SimpleDateFormat dateft = new SimpleDateFormat("yyyy년"); // "연월일" 만 본다.
	            String currentyear = dateft.format(currentDate.getTime());  // dateft 는 "yyyy년" 포맷으로 바꾼다는 것이다.
	            
			
				StringBuilder sb2 = new StringBuilder();
				int count2 = 0;
				System.out.println("\n---------------------- ["+currentyear+searchMonth+"월"+" 일정] ---------------------");
	            System.out.println("번호\t날짜\t\t\t일정내용\t\t작성자");
	            System.out.println("------------------------------------------------------------");
				for(int j=0; j<totalViewCalendar.size(); j++) { // 선택한 월의 select 된 행 개수만큼 돌린다.
					if(String.valueOf(searchMonth).equals(totalViewCalendar.get(j).getCalendar_date().substring(5,7))) {  // 검색한 월과 저장한 월이 일치할 경우
						sb2.append(totalViewCalendar.get(j).showCalendarTitle() + "\n"); 
						count2++;
					    // totalViewCalendar() 메소드에서 "번호\t날짜\t\t\t일정내용\t\t작성자" 을 찍어줄 것이다.
					}
				}// end of for-----------------------------------
				System.out.println(sb2.toString()); // 쌓아둔 것을 찍는다.
				
				if(count2==0) {
					System.out.println(">> 해당 월에 업로드된 일정이 없습니다! << \n");
				}
					
				
			}
			else if( "n".equalsIgnoreCase(yn) ) {
				break;
			}
			else {
				System.out.println(">> [경고] Y 또는 N 만 입력하세요! << \n");
			}
		} while(true);
		
	}// end of private void totalViewCalendar()-------------------


	
	// 일정추가
		private int write(EmployeesDTO empDto, Scanner sc) {
			
			int result = 0;
			
			System.out.println("\n>>> 일정 추가하기 <<<");
			
			System.out.println("1. 작성자명 : " + empDto.getName());  // 직접 작성자명을 입력하지 않고도 로그인하면 자동적으로 작성자명을 불러온다.
			
			
			String calendar_date = "";
	        do {
	        	System.out.print("2. 날짜[YYYY-MM-DD hh:mm] : ");
	        	calendar_date = sc.nextLine();
	        	
	        	try {
					new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(calendar_date);
					break;
				} catch (ParseException e) {
					System.out.println("[경고] 날짜를 형식에 맞게 다시 입력해주세요.\n");
				}
	        	
	        } while(true);
	        	

	        System.out.print("3. 일정내용 : ");  
	        String calendar_contents = sc.nextLine();
			
			
			int flag = 0; // 아무 타입으로나 표식을 해준다.
			do {
				System.out.print("\n>> 저장하시겠습니까?[Y/N] => ");
				String yn = sc.nextLine();
				
				if("y".equalsIgnoreCase(yn)) {
					flag = 1;  // 저장하겠다[Y] ==>  break; 빠져나오기 전에, 글쓰기를 하겠다[Y]는 흔적을 남겨야 한다.↓ 
					break;
				}
				else if("n".equalsIgnoreCase(yn)) { 
					break; 
				}
				else {
					System.out.println(">> [경고] Y또는 N 만 입력하세요! << \n");
				}
			} while(true); // while 반복문은 true일 경우 반복, false가 되면 멈춘다.
			
			
			if(flag == 1) { // 저장하겠다[Y] 라는 경우
				
				CalendarDTO cdto = new CalendarDTO();
				cdto.setFk_employee_id(empDto.getEmployee_id()); // 부모인 empDto의 getEmployee_id()를 메소드를 참조하여(불러와) cdto의 setFk_employee_id 메소드를 재정의한다. foreign key 제약이 걸려있다.
				cdto.setCalendar_date(calendar_date);
				cdto.setCalendar_contents(calendar_contents);
				
				result = cdao.write(cdto); // 1(일정추가 성공한 경우) 또는 -1(일정추가 하고 싶지만 실패한 경우)

			}
			else { // 일정추가를 취소한 경우
				result = 0; 
			}
			
			return result;
		}// end of private int write(EmployeesDTO empDto, Scanner sc)--------------------------------------	
	

		// 일정 수정하기
		   private int updateCalendar(EmployeesDTO empDto, Scanner sc) {
		      int result = 0;
		      String calendarno = "";
		      do { //정수입력 유효성검사
		         System.out.println("\n>>> 일정 수정하기 <<<");
		         
		         System.out.print("▷ 수정할 일정번호 : ");
		         calendarno = sc.nextLine();
		         
		         try {
		            Integer.parseInt(calendarno);
		            break;
		         } catch (NumberFormatException e) {
		            System.out.println(">> [수정 실패] 숫자로만 입력하세요! << \n");
		         }
		      } while(true);
		      
		      CalendarDTO cdto = cdao.viewCalendar_contents(calendarno);
		   
		      if( cdto != null ) {  // 수정할 일정번호가 존재하는 경우
		         if( cdto.getFk_employee_id().equals(empDto.getEmployee_id()) || "999".equals(empDto.getEmployee_id())) {  // 수정할 일정번호의 작성자가 나와 동일하거나 관리자인 경우
		            
		            System.out.println("-----------------------------------------------------------------");
		            System.out.println("날짜                     일정내용                     작성자");
		            System.out.println("-----------------------------------------------------------------");
		            System.out.println(cdto.getCalendar_date()+ "\t" +cdto.getCalendar_contents()+ "\t" +cdto.getEmpDto().getName());
		            System.out.println("-----------------------------------------------------------------");
		         
		            String calendar_date = "";
		              do {
		                 System.out.print("1. 날짜[YYYY-MM-DD hh:mm] [수정하지 않으려면 엔터] : ");
		                 calendar_date = sc.nextLine();
		                 
		                 if( calendar_date != null && calendar_date.trim().isEmpty()) { // 수정할 날짜가 null값이 아니고, 공백이거나 텅빈 경우
		                   calendar_date = "noinsert_date";
		                   break;
		                 } // 수정하지 않으려면 기존 일정보기(select)에서 calendar_date 를 넣어준다.
		                 else { // 공백이거나 텅빈 것이 아닌 수정할 날짜를 입력한 경우
		                    try {
		                        new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(calendar_date);
		                        break;
		                    } catch (ParseException e) {
		                       System.out.println("[경고] 날짜를 형식에 맞게 다시 입력해주세요.\n");
		                    }
		                 }
		              } while(true);
		              
		              System.out.print("2. 일정내용[수정하지 않으려면 엔터]: ");
		              String calendar_contents = sc.nextLine();
		              
		              Map<String, String> paraMap = new HashMap<>();
		              
		              if("noinsert_date".equals(calendar_date)) { //수정할날짜에 엔터 입력한경우
		                  //내용만 update해준다
		                   if( calendar_contents != null && calendar_contents.trim().isEmpty()) { // 수정할 일정내용이 null값이 아니고, 공백이거나 텅빈 경우
		                         calendar_contents = cdto.getCalendar_contents(); // 수정하지 않으려면 기존 일정보기(select)에서 calendar_contents 를 넣어준다.
		                         System.out.println(">> 변경 내용이 없습니다.\n");
		                   } 
		                   else {
		                      paraMap.put("calendar_contents", calendar_contents);
		                      paraMap.put("calendarno", calendarno);
		                      
		                      result = cdao.updateCalendar_2(paraMap); //내용만 insert해주는 메소드
		                   }
		               }
		              else { //엔터입력하지않고 날짜입력한경우 => 날짜와 내용을 update해준다
		                   if( calendar_contents != null && calendar_contents.trim().isEmpty()) { // 수정할 일정내용이 null값이 아니고, 공백이거나 텅빈 경우
		                      calendar_contents = cdto.getCalendar_contents();//내용
		                   } // 수정하지 않으려면 기존 일정보기(select)에서 calendar_contents 를 넣어준다.
		                   else { //
		                      if( calendar_contents.length() > 100 ) {
		                            System.out.println(" [경고] 글자수는 최대 100글자로 제한되어 있습니다. \n");
		                         }
		                      else { // 그렇지 않다면, 입력한 수정값(날짜/내용)을 저장한다.
		                            paraMap.put("calendar_date", calendar_date);
		                            paraMap.put("calendar_contents", calendar_contents);
		                            paraMap.put("calendarno", calendarno); // calendarno 는 primary key 이기때문에 구분하기 위해 입력한다.
		                            
		                            result = cdao.updateCalendar(paraMap); // 일정 수정하기
		                            //       1(일정수정이 성공된 경우) 또는 -1(일정수정을 하려고 하나 SQLException 이 발생한 경우)
		                      }
		                   }
		               }
		         }//end of 수정할 일정번호가 존재하는 경우
		         else { // 수정할 일정번호가 다른 사용자가 쓴 글인 경우
		            System.out.println(" [경고] 다른 작성자의 일정은 수정 불가합니다. \n");
		        }
		       }
		      else {// 수정할 일정번호가 존재하지 않는 경우
		         System.out.println(">> [경고] "+calendarno+"는 존재하지 않는 일정번호입니다! \n");
		      }
		      
		      return result; //  0 또는 1 또는 -1
		   }// end of private int updateCalendar(EmployeesDTO empDto, Scanner sc)---------------------   
	
	
	
	
	// 글삭제하기
	private int deleteCalendar(EmployeesDTO empDto, Scanner sc) {
		int result = 0;
		
		String calendarno = "";
		do { 
			System.out.println("\n>>> 일정 삭제하기 <<<");
			
			System.out.print("▷ 삭제할 일정번호 : ");
			calendarno = sc.nextLine();
			
			try {
				Integer.parseInt(calendarno);  // 데이터베이스에 입력한 것은 숫자로 입력했을 때만 빠져나온다. "똘똘이" 입력 불가하도록 유효성 검사를 해주었다.
				break;
			} catch (NumberFormatException e) {
				System.out.println(">> [삭제 실패] 숫자로만 입력하세요! << \n");
			}
			
		} while(true);
		
		
		CalendarDTO cdto = cdao.viewCalendar_contents(calendarno); // viewContents() 은 글내용보기(select) 메소드
		
		if( cdto != null ) {  // 삭제할 일정번호가 존재하는 경우
			
			if( cdto.getFk_employee_id().equals(empDto.getEmployee_id()) || "999".equals(empDto.getEmployee_id())) { // 삭제할 일정번호가 자신이 쓴 일정이거나 관리자계정인 경우
			
				System.out.println("-----------------------------------------------------------------");
				System.out.println("날짜                     일정내용                     작성자");
				System.out.println("-----------------------------------------------------------------");
				System.out.println(cdto.getCalendar_date()+ "\t" +cdto.getCalendar_contents()+ "\t" +cdto.getEmpDto().getName());
				System.out.println("-----------------------------------------------------------------");
					
				do {
					System.out.print("▷ 정말로 일정을 삭제하시겠습니까?[Y/N] => ");
					String yn = sc.nextLine();
					
					if("y".equalsIgnoreCase(yn)) { 
						result = cdao.deleteCalendar(calendarno); // calendarno 는 primary key 이므로 해당 일정번호를 입력하여 글삭제한다. 1행만 리턴된다.
						//       1(정상적으로 글이 삭제된 경우) 또는 -1(SQLException 발생으로 글삭제가 실패한 경우)
						break; // 올바르게 입력하면 빠져나온다.
					}
					else if("n".equalsIgnoreCase(yn)) {
						System.out.println(">> 일정삭제를 취소하셨습니다. <<"); // result = -1
						break; // 올바르게 입력하면 빠져나온다.
					}
					else {
						System.out.println(" [경고] Y또는 N 만 입력하세요! \n");  // result = 0 
					}
						
				} while(true);
			}
			else { // 삭제할 일정번호가 다른 사용자가 쓴 일정인 경우 
				System.out.println("[경고] 다른 작성자의 일정은 삭제 불가합니다. \n");
			}
			
			
		}
		else {  // 삭제할 일정번호가 존재하지 않는 경우
			System.out.println(">> [경고] "+calendarno+"는 존재하지 않는 일정번호입니다! \n");
		}
		
		return result;
		//  0 또는 1 또는 -1
	}// end of private int deleteCalendar(EmployeesDTO empDto, Scanner sc)-------------------------
	
	



	
	
}
