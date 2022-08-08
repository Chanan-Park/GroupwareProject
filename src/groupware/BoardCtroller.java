package groupware;

import java.util.*;

public class BoardCtroller {

	EmployeesDTO empDto = new EmployeesDTO();
	Board_freeDTO bfdto = new Board_freeDTO();
	InterBoard_freeDAO bfdao = new Board_freeDAO();
	Comments_freeDTO cfdto = new Comments_freeDTO();
	InterComment_freeDAO cfdao = new Comments_freeDAO();
	Board_noticeDTO bndto = new Board_noticeDTO();
	InterBoard_noticeDAO bndao = new Board_noticeDAO();
	Comments_noticeDTO cndto = new Comments_noticeDTO();
	InterComment_noticeDAO cndao = new Comments_noticeDAO();

	// *** 게시판 메뉴 메소드 ****
	void menu_Board(EmployeesDTO empDto, Scanner sc) {
		String s_menuNo;
		do {
			System.out.println("\n-------------[" + empDto.getName() + "님 로그인중..]-------------");
			System.out.println("1.공지게시판        2.자유게시판       3.이전메뉴");
			System.out.println("-----------------------------------------");

			System.out.print(" ▷메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1":
				menu_BoardNotice(empDto, sc);

				break;

			case "2":
				menu_BoardFree(empDto, sc);
				break;

			case "3":
				break;

			default:
				System.out.println(" ▷메뉴에 없는 번호입니다");
				break;
			} // end of switch (s_menuNo)

		} while (!"3".equals(s_menuNo));
		/*
		 * if("1".equals(s_menuNo) ) {
		 * 
		 * menu_BoardNotice(empDto, sc); }
		 * 
		 * else if("2".equals(s_menuNo) ) { menu_BoardFree(empDto, sc); }
		 * 
		 * 
		 * else { System.out.println(" ▷메뉴에 없는 번호입니다"); } }while(
		 * !("3".equals(s_menuNo)) );
		 */

	}// void menu_Board(EmployeesDTO empDto, Scanner sc)

	// *** 공지게시판 메뉴 메소드 ****
	private void menu_BoardNotice(EmployeesDTO empDto, Scanner sc) {
		String s_menuNo;
		// 관리자 로그인중일떄
		if ("관리자".equals(empDto.getName())) {
			do {
				System.out.println("\n-------------[" + empDto.getName() + "님 로그인중..]-------------");
				System.out.println("1.글목록    2.글내용   3.글쓰기   4.글수정  5.글삭제\n" + "6.이전메뉴");
				System.out.println("-----------------------------------------");
				System.out.print(" ▷메뉴번호 선택 : ");
				s_menuNo = sc.nextLine();
				switch (s_menuNo) {
				case "1": // 공지게시판 글목록

					boardNoticeList();

					break;

				case "2": // 공지게시판 글내용

					NoticeVeiwcount(empDto, sc);
					break;

				case "3": // 공지게시판 글쓰기

					int resultn2 = writeNotice(empDto, sc);

					if (resultn2 == 1) {
						System.out.println("▷ 글쓰기 완료");
					}

					else if (resultn2 == 0) {
						System.out.println("▷ 글쓰기를 취소하였습니다.");
					}

					else {
						System.out.println("[경고]시스템 오류로 글쓰기를 실패하였습니다.");
					}

					break;

				case "4":// 공지게시판 글수정
					int resultn3 = updateNoticeBoard(empDto, sc);

					if (resultn3 == 1) {
						System.out.println("▷ 수정 완료");
					}

					else if (resultn3 == 0) {
						System.out.println("▷ 글수정을 취소하였습니다.");
					}

					else {
						System.out.println("[경고]시스템 오류로 글수정를 실패하였습니다.");
					}

					break;

				case "5":// 공지게시판 글 삭제

					int resultn4 = deleteNoticeBoard(empDto, sc);

					if (resultn4 == 1) {
						System.out.println("▷ 삭제 완료");
					}

					else if (resultn4 == 0) {
						System.out.println("▷ 삭제를 취소하였습니다.");
					}

					else {
						System.out.println("[경고]시스템 오류로 글삭제를 실패하였습니다.");
					}

					break;

				case "6": // 이전메뉴로 돌아가기
					break;

				default:

					System.out.println("▶ 메뉴에 없는 번호입니다");
					break;
				}
			} while (!("6".equals(s_menuNo)));

		}

		// 일반회원 로그인중일때
		else {
			do {
				System.out.println("\n-------------[" + empDto.getName() + "님 로그인중..]-------------");
				System.out.println("1.글목록    2.글내용   3.이전메뉴 ");
				System.out.println("-----------------------------------------");

				System.out.print(" ▷메뉴번호 선택 : ");
				s_menuNo = sc.nextLine();

				switch (s_menuNo) {
				case "1":// 글목록

					boardNoticeList();
					break;

				case "2": // 글내용

					NoticeVeiwcount(empDto, sc);
					break;

				case "3": // 이전메뉴
					break;

				default:

					System.out.println("▶ 메뉴에 없는 번호입니다");
					break;
				}
			} while (!("3".equals(s_menuNo)));
		}

	}

	// *** 1.공지게시판 글목록 메소드 ***

	private void boardNoticeList() {

		System.out.println("\n-----------------------[게시글 목록]----------------------------");
		System.out.println("글번호\t작성자\t글제목\t작성일자\t\t조회수");
		System.out.println("\n------------------------------------------------------------");

		List<Map<String, String>> bnboardList = bndao.boardNoticeList();

		if (bnboardList.size() > 0) {

			StringBuilder sb = new StringBuilder();

			for (Map<String, String> boardList : bnboardList) {

				sb.append(boardList.get("bn_boardno") + "\t" + boardList.get("fk_employee_id") + "\t"
						+ boardList.get("bn_subject") + "\t" + boardList.get("bn_writedate") + "\t"
						+ boardList.get("bn_viewcount") + "\n");

			}
			System.out.println(sb.toString());
		}

		else {
			System.out.println(" 작성된 게시글이 없습니다.");
		}
	}

	   //***2.공지게시판 글내용 메소드***
    private String NoticeVeiwcount(EmployeesDTO empDto, Scanner sc) {
       // 글목록 보여주는 메소드
       
       boardNoticeList();
       String Nboardno = "";
       
       do {
          System.out.println("\n□ □ □ 글 내용 □ □ □");
          
          System.out.print("▶ 조회 글의 번호를 입력해주세요 : ");
          Nboardno = sc.nextLine();
 
          try { // 유효성 검사를 해준다.
             Integer.parseInt(Nboardno); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
             break;
          } catch (NumberFormatException e) {
             System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
          }
       }while(true);
       
       
       Nboardno = String.valueOf(Nboardno);
       
       //DTO에 담아서 DAO로 보내기

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("boardno", Nboardno); // 파라미터는 map에 담아서 보내주기
		paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

		// 글조회 메소드
		bndto = bndao.noticeVeiwcount2(paraMap);

		// 조회 증가시켜주는 메소드(트랜젝션)
		bndao.noticeVeiwcount(paraMap);

		if (bndto != null & bndto.getBn_boardno() != 0) {

			// 글 조회하기 서식 메소드화
			noticeVeiw(empDto, bndto);

			// 댓글 내용 조회(없어진 기능)
			// noticeCommentlist(bndto,sc,Nboardno);

		} else {
			System.out.println("▶조회하려는 글 번호가 없습니다.");
		}

		return Nboardno;
	}// end of private void veiwcount(EmployeesDTO empDto, Scanner sc)

	// 3.***공지게시판 글 쓰기 메소드***
	private int writeNotice(EmployeesDTO empDto, Scanner sc) {

		int resultn2 = 0;

		String bn_subject = "";
		String bn_contents = "";

		StringBuilder sb = new StringBuilder();

		System.out.println(" \n□ □ □ 글 쓰기 □ □ □");

		System.out.println(" ▶ 작성자명 : " + empDto.getName());

		sb.append(" ▶ 작성자명 : " + empDto.getName() + "\n");

		do {
			System.out.print(" 글 제목 : ");
			bn_subject = sc.nextLine();
			sb.append(" 글 제목 : " + bn_subject + "\n");

			// 제목 유효성 검사
			if (bn_subject.length() > 100) {
				System.out.println("▷ 제목은 100글자를 넘을 수 없습니다. ");

			}

			else {
				break;
			}
		} while (true);
		do {
			System.out.print(" ▶ 글 내용 : ");
			bn_contents = sc.nextLine();
			sb.append(" 글 내용 : " + bn_contents + "\n");

			// 내용 유효성 검사
			if (bn_contents.length() > 200) {
				System.out.println("▷ 내용은 200글자를 넘을 수 없습니다. ");
			} else {
				break;
			}

		} while (true);

		do {
			System.out.println(" ▶ 글을 입력하시겠습니까? [Y/N] \n");
			System.out.println(sb.toString());
			String yn = sc.nextLine();

			// 글을 입력할 경우
			if (yn.equalsIgnoreCase("Y")) {
				// DTO에 담기

				bndto.setFk_employee_id(empDto.getEmployee_id());
				bndto.setBn_subject(bn_subject);
				bndto.setBn_contents(bn_contents);

				// dao로 dto에담아서 로 보내주기

				resultn2 = bndao.writeNotice(bndto);

				// 이제 dao에서 insert해주고 결과가 reslutn으로 온다. 1로 와야 정상

				break;
			}
			// 글을 입력하지 않을 경우
			else if (yn.equalsIgnoreCase("N")) {

				resultn2 = 0;
				break;
			}

			else {
				System.out.println(" ▶ Y또는 N만 입력해주세요");
			}
		} while (true);

		return resultn2;
	}

	   //***4.공지게시판 글수정 메소드***
	   private int updateNoticeBoard(EmployeesDTO empDto2, Scanner sc) {
	      
	      boardNoticeList();
	      String boardno = "";
	      
	      Map<String, String> paraMap = new HashMap<>();
	      
	      do {
	         
	         System.out.println("\n□ □ □ 글 수정하기 □ □ □");
	         System.out.print("▶ 수정할 글 번호를 입력하세요 : ");
	         boardno = sc.nextLine();
	         
	         try { // 유효성 검사를 해준다.
	        	 Integer.parseInt(boardno); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
	            break;
	         } catch (NumberFormatException e) {
	            System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
	         }
	      }while(true);
	      
	      boardno = String.valueOf(boardno);
	         
	      paraMap.put("boardno",boardno);
	      paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());
	      
	      bndto =  bndao.noticeVeiwcount2(paraMap);
	     
	      //글내용 조회 메소드

		// 글내용 출력 메소드
//		noticeVeiw(empDto, bndto);

		int resultn3 = 0;

		// System.out.println("확인용 : " + bfdto.getFk_employee_id());

		if (bndto != null & bndto.getBn_boardno() != 0) {

			String bn_subject = "";
			String bn_contents = "";
			do {
				System.out.println("▷ 수정할 제목을 입력하세요[변경하지 않으려면 엔터] : ");
				bn_subject = sc.nextLine();

				if (bn_subject != null && bn_subject.trim().isEmpty()) { // 기존 jubject
					bn_subject = bndto.getBn_subject();
				}

				// 제목 유효성 검사
				if (bn_subject.length() > 100) {
					System.out.println("▷ 제목은 100글자를 넘을 수 없습니다. ");

				}

				else {
					break;
				}
			} while (true);

			do {
				System.out.println("▷ 수정할 내용을 입력하세요[변경하지 않으려면 엔터] : ");
				bn_contents = sc.nextLine();

				if (bn_contents != null && bn_contents.trim().isEmpty()) { // 기존 contents
					bn_contents = bndto.getBn_contents();
				}
				// 내용 유효성 검사
				if (bn_contents.length() > 200) {
					System.out.println("▷ 내용은 200글자를 넘을 수 없습니다. ");
				} else {
					break;
				}
			} while (true);

			do {
				System.out.print("▷ 글 수정을 하시겠습니까?[Y/N] : ");
				String yn = sc.nextLine();

				if (yn.equalsIgnoreCase("y")) {

					paraMap.put("bn_subject", bn_subject);
					paraMap.put("bn_contents", bn_contents);
					// paraMap.put("boardno",boardno)

					resultn3 = bndao.updateNoticeBoard(paraMap);
					break;
				}

				else if (yn.equalsIgnoreCase("N")) {
					resultn3 = 0;
					break;
				}

				else {
					System.out.println("▷ Y 또는 N 만 입력해주세요\n");
				}
			} while (true);

		} // end of if (bfdto != null )

		else {
			System.out.println("▶ 일치하는 글 번호가 없습니다.");
		}

		return resultn3;
	}

	// ***5.공지게시판 글삭제 메소드***
	private int deleteNoticeBoard(EmployeesDTO empDto2, Scanner sc) {
		// 글 목록 메소드
		boardNoticeList();
		String boardno = "";

		Map<String, String> paraMap = new HashMap<>();
		int resultn4 = 0;

		do {

			do {
				System.out.print("▶ 삭제할 글번호를 입력하세요 : ");
				boardno = sc.nextLine();

				try { // 유효성 검사를 해준다.
					Integer.parseInt(boardno); // searchMonth 를 String 타입에서 int 타입으로만 입력받도록 검사해준다.
					break;
				} catch (NumberFormatException e) {
					System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
				}
			} while (true);

			boardno = String.valueOf(boardno);

			paraMap.put("boardno", boardno);
			paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

			// 글내용 조회 메소드
			bndto = bndao.noticeVeiwcount2(paraMap);
			// 글내용 출력 메소드
			noticeVeiw(empDto, bndto);

			if (bndto != null & bndto.getBn_boardno() != 0) {
				break;
			}

			else {
				System.out.println("존재하지 않는 글번호 입니다.");
			}
		} while (true);

		do {
			System.out.print("▷ 정말로 삭제 하시겠습니까?[Y/N] : ");
			String yn = sc.nextLine();
			if (yn.equalsIgnoreCase("y")) {

				resultn4 = bndao.deleteNoticeBoard(paraMap);

				break;
			}

			else if (yn.equalsIgnoreCase("N")) {
				resultn4 = 0;
				break;
			}

			else {
				System.out.println("▷ Y 또는 N 만 입력해주세요\n");
			}
		} while (true);

		return resultn4;
	}

	/***
	 * 댓글 조회하기 메소드*** public void noticeCommentlist (Board_noticeDTO bndto, Scanner
	 * sc, String Nboardno) {
	 * 
	 * 
	 * System.out.println("[댓글]\n-----------------------------------------------");
	 * 
	 * 
	 * 
	 * List<Comments_noticeDTO> noticeCommentList =
	 * cndao.noticeCommentList(Nboardno); //복수니까 List로 // 원글에 대한 댓글을 가져오는 것
	 * 
	 * System.out.println("댓글번호\t작성자\t\t댓글 내용\t작성 일자");
	 * System.out.println("------------------------------------------");
	 * 
	 * if(noticeCommentList !=null) { // 댓글이 존재하는 원글인 경우
	 * 
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * for (Comments_noticeDTO cndto : noticeCommentList ) {
	 * sb.append(cndto.noticeViewCommentInfo() + "\n"); }// end of
	 * for------------------------------------
	 * 
	 * System.out.println(sb.toString()); } else { // 댓글이 존재하지 않는 원글인 경우
	 * System.out.println(">> 댓글 내용 없음 << \n"); }
	 * 
	 * 
	 * 
	 * }//public void commentlist (EmployeesDTO empDto, Board_freeDTO bfdto, Scanner
	 * sc)
	 */

	/***
	 * 3. 공지게시판 댓글 쓰기 메소드 *** private int writeNoticeComment(EmployeesDTO empDto,
	 * Scanner sc) {
	 * 
	 * boardNoticeList();
	 * 
	 * int resultn = 0;
	 * 
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * 
	 * System.out.println(" \n□ □ □ 댓글 쓰기 □ □ □");
	 * 
	 * int c_boardno = 0;
	 * 
	 * String boardno ="";
	 * 
	 * do { System.out.print("▶ 댓글 작성하려는 글번호 : "); boardno = sc.nextLine(); try {
	 * c_boardno = Integer.parseInt(boardno); break; }catch(NumberFormatException e)
	 * { System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
	 * 
	 * } }while(true); // 유효성검사
	 * 
	 * Map <String, String> paraMap = new HashMap<>();
	 * 
	 * paraMap.put("boardno", boardno); // 파라미터는 map에 담아서 보내주기
	 * paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());
	 * 
	 * //글조회 메소드 bndto = bndao.noticeVeiwcount2(paraMap); noticeVeiw(empDto, bndto);
	 * 
	 * 
	 * System.out.println( " ▶ 작성자명 : " + empDto.getName());
	 * 
	 * sb.append(" ▶ 작성자명 : " + empDto.getName() + "\n");
	 * 
	 * String cn_comments =""; do{ System.out.print(" ▶ 댓글 내용 : "); cn_comments =
	 * sc.nextLine(); sb.append(" 댓글 내용 : " + cn_comments +"\n");
	 * 
	 * //내용 유효성 검사 if(cn_comments .length() > 100) { System.out.println("▷ 내용은
	 * 100글자를 넘을 수 없습니다. "); } else { break; } }while(true);
	 * 
	 * do {
	 * 
	 * System.out.println(" ▶ 댓글을 입력하시겠습니까? [Y/N] \n");
	 * System.out.println(sb.toString()); String yn = sc.nextLine();
	 * 
	 * //댓글을 입력할 경우 if(yn.equalsIgnoreCase("Y")) { //DTO에 담기
	 * 
	 * cndto.setFk_bn_boardno(c_boardno); cndto.setCn_contents(cn_comments);
	 * cndto.setFk_employee_id(empDto.getEmployee_id());
	 * 
	 * 
	 * //dao로 dto에담아서 로 보내주기
	 * 
	 * resultn = cndao.noticeWrite(cndto);
	 * 
	 * //이제 dao에서 insert해주고 결과가 reslutn으로 온다. 1로 와야 정상
	 * 
	 * break; } // 글을 입력하지 않을 경우 else if (yn.equalsIgnoreCase("N")){
	 * 
	 * resultn = 0; break; }
	 * 
	 * else { System.out.println(" ▶ Y또는 N만 입력해주세요"); } }while(true);
	 * 
	 * return resultn; }
	 * 
	 */

	// *** 자유게시판 메뉴 메소드 ***
	private void menu_BoardFree(EmployeesDTO empDto, Scanner sc) {
		String s_menuNo;
		// 관리자 로그인중일때 (관리는 일반회원 글,댓글까지 모두 삭제가능/수정은 불가능)
		// if("관리자".equals(empDto.getName())) {

		// 일반회원,관리자 로그인중일때

		do {
			System.out.println("\n-------------[" + empDto.getName() + "님 로그인중..]-------------");
			System.out.println("1.글목록    2.글내용   3.글쓰기   4.글수정  5.글삭제\n" + "6.댓글쓰기   7.댓글수정   8.댓글삭제  9.이전메뉴");
			System.out.println("-----------------------------------------");
			System.out.print(" ▷메뉴번호 선택 : ");
			s_menuNo = sc.nextLine();
			System.out.println("\n");

			switch (s_menuNo) {
			case "1":

				boardList();
				break;
			case "2": // 글내용

				veiwcount(empDto, sc);
				break;

			case "3": // 글쓰기

				int resultn = write(empDto, sc);

				if (resultn == 1) {
					System.out.println("▷ 글쓰기 완료");
				}

				else if (resultn == 0) {
					System.out.println("▷ 글쓰기를 취소하였습니다.");
				}

				else {
					System.out.println("[경고]시스템 오류로 글쓰기를 실패하였습니다.");
				}

				break;
			case "4": // 글수정

				int resultn2 = updateBoard(empDto, sc);

				if (resultn2 == 1) {
					System.out.println("▷ 수정 완료");
				}

				else if (resultn2 == 0) {
					System.out.println("▷ 글수정을 취소하였습니다.");
				}

				else {
					System.out.println("[경고]시스템 오류로 글수정를 실패하였습니다.");
				}

				break;
			case "5": // 글삭제

				int resultn3 = deleteBoard(empDto, sc);

				if (resultn3 == 1) {
					System.out.println("▷ 삭제 완료");
				}

				else if (resultn3 == 0) {
					System.out.println("▷ 삭제를 취소하였습니다.");
				}

				else {
					System.out.println("[경고]시스템 오류로 글삭제를 실패하였습니다.");
				}

				break;
			case "6": // 댓글쓰기

				int resultn4 = writeComment(empDto, sc);
				if (resultn4 == 1) {
					System.out.println("▷ 글쓰기 완료");
				}

				else if (resultn4 == 0) {
					System.out.println("▷ 글쓰기를 취소하였습니다.");
				}else if (resultn4 == -1) {
					System.out.println("▷ 댓글 쓰기를 실패하였습니다.");
				}


				else {
					System.out.println("▷ 시스템 오류로 글쓰기를 실패하였습니다.");
				}

				break;
			case "7": // 댓글수정
				int resultn5 = updateComment(empDto, sc);

				if (resultn5 == 1) {
					System.out.println("▷ 댓글쓰기 완료");
				}

				else if (resultn5 == 0) {
					System.out.println("▷ 댓글쓰기를 취소하였습니다.");
				}

				else {
					System.out.println("[경고]시스템 오류로 댓글쓰기를 실패하였습니다.");
				}

				break;
			case "8": // 댓글삭제

				int resultn6 = deleteComment(empDto, sc);

				if (resultn6 == 1) {
					System.out.println("▷ 댓글 삭제 완료");
				}

				else if (resultn6 == 0) {
					System.out.println("▷ 댓글 삭제를 취소하였습니다.");
				}
				
				
				else {
					System.out.println("[경고]시스템 오류로 댓글삭제를 실패하였습니다.");
				}

				break;
			case "9": // 이전메뉴로 돌아가기

				break;

			default:
				System.out.println(" ▷ 메뉴에 없는 번호입니다.");
				break;
			}// end of switch(s_menuNo)

		} while (!("9".equals(s_menuNo)));
	}

	// *** 1. 글목록 메소드 ****
	private void boardList() {

		System.out.println("\n-----------------------[게시글 목록]----------------------------");
		System.out.println("글번호\t작성자\t글제목\t작성일자\t\t조회수");
		System.out.println("\n------------------------------------------------------------");

		List<Map<String, String>> bfboardList = bfdao.boardList();

		if (bfboardList.size() > 0) {

			StringBuilder sb = new StringBuilder();

			for (Map<String, String> boardList : bfboardList) {

				sb.append(boardList.get("bf_boardno") + "\t" + boardList.get("fk_employee_id") + "\t"
						+ boardList.get("bf_subject") + "\t" + boardList.get("bf_writedate") + "\t"
						+ boardList.get("bf_viewcount") + "\n");

			}
			System.out.println(sb.toString());
		}

		else {
			System.out.println(" 작성된 게시글이 없습니다.");
		}

	}// end of private void boardList()--------------------------

	// *** 2. 글내용 조회하기 & 조회수 카운트 메소드 (transaction 사용) ***
	private String veiwcount(EmployeesDTO empDto, Scanner sc) {

		// 글목록 보여주는 메소드
		boardList();

		System.out.println("\n□ □ □ 글 내용 □ □ □");

		String boardno;
		for (;;) {
			System.out.print("▶ 조회 글의 번호를 입력해주세요 : ");
			boardno = sc.nextLine();
			try {
				Integer.parseInt(boardno);
				break;
			} catch (NumberFormatException e) {
				System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
			}
		}

		// DTO에 담아서 DAO로 보내기

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("boardno", boardno); // 파라미터는 map에 담아서 보내주기
		paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

		// 글조회 메소드
		bfdto = bfdao.veiwcount2(paraMap);

		// System.out.println("확인용 : " + boardno);
		// System.out.println("확인용 : " + bfdto.getBf_boardno());

		// boardno.equals(bfdto.getBf_boardno());

		// 조회 증가시켜주는 메소드
		bfdao.veiwcount(paraMap);

		if (bfdto != null & bfdto.getBf_boardno() != 0) {

			// 글 조회하기 서식 메소드화
			veiw(empDto, bfdto);
			commentlist(bfdto, sc, boardno);

		} else {
			System.out.println("▶조회하려는 글 번호가 없습니다.");
		}

		return boardno;
	}// end of private void veiwcount(EmployeesDTO empDto, Scanner sc)

	// ***댓글 조회하기 메소드***
	public void commentlist(Board_freeDTO bfdto, Scanner sc, String boardno) {

		System.out.println("[댓글]\n-----------------------------------------------");

		List<Comments_freeDTO> commentList = cfdao.commentList(boardno); // 복수니까 List로
		// 원글에 대한 댓글을 가져오는 것

		System.out.println("댓글번호\t작성자\t\t댓글 내용\t작성 일자");
		System.out.println("------------------------------------------");

		if (commentList != null) {
			// 댓글이 존재하는 원글인 경우

			StringBuilder sb = new StringBuilder();

			for (Comments_freeDTO cfdto : commentList) {
				sb.append(cfdto.viewCommentInfo() + "\n");
			} // end of for------------------------------------

			System.out.println(sb.toString());
		} else {
			// 댓글이 존재하지 않는 원글인 경우
			System.out.println(">> 댓글 내용 없음 << \n");
		}

	}// public void commentlist (EmployeesDTO empDto, Board_freeDTO bfdto, Scanner
		// sc)

	// ****3.글쓰기 메소드****
	private int write(EmployeesDTO empDto, Scanner sc) {

		int resultn = 0;
		String bf_subject = "";
		String bf_contents = "";

		StringBuilder sb = new StringBuilder();

		System.out.println(" \n□ □ □ 글 쓰기 □ □ □");

		System.out.println(" ▶ 작성자명 : " + empDto.getName());

		sb.append(" ▶ 작성자명 : " + empDto.getName() + "\n");

		do {
			System.out.print(" 글 제목 : ");
			bf_subject = sc.nextLine();
			sb.append(" 글 제목 : " + bf_subject + "\n");

			// 제목 유효성 검사
			if (bf_subject.length() > 100) {
				System.out.println("▷ 제목은 100글자를 넘을 수 없습니다. ");

			}

			else {
				break;
			}
		} while (true);
		do {
			System.out.print(" ▶ 글 내용 : ");
			bf_contents = sc.nextLine();
			sb.append(" 글 내용 : " + bf_contents + "\n");

			// 내용 유효성 검사
			if (bf_contents.length() > 200) {
				System.out.println("▷ 내용은 200글자를 넘을 수 없습니다. ");
			} else {
				break;
			}

		} while (true);

		do {
			System.out.println(" ▶ 글을 입력하시겠습니까? [Y/N] \n");
			System.out.println(sb.toString());
			String yn = sc.nextLine();

			// 글을 입력할 경우
			if (yn.equalsIgnoreCase("Y")) {
				// DTO에 담기

				bfdto.setFk_employee_id(empDto.getEmployee_id());
				bfdto.setBf_subject(bf_subject);
				bfdto.setBf_contents(bf_contents);

				// dao로 dto에담아서 로 보내주기

				resultn = bfdao.write(bfdto);

				// 이제 dao에서 insert해주고 결과가 reslutn으로 온다. 1로 와야 정상

				break;
			}
			// 글을 입력하지 않을 경우
			else if (yn.equalsIgnoreCase("N")) {

				resultn = 0;
				break;
			}

			else {
				System.out.println(" ▶ Y또는 N만 입력해주세요");
			}
		} while (true);

		return resultn;
	}// end of private int write(EmployeesDTO empDto, Scanner sc)

	// ***4.글 수정하기 메소드 update ***
	private int updateBoard(EmployeesDTO empDto, Scanner sc) {

		Map<String, String> paraMap = new HashMap<>();
		String boardno;
		for (;;) {
			System.out.println("\n□ □ □ 글 수정하기 □ □ □");
			System.out.print("▶ 수정할 글 번호를 입력하세요 : ");
			boardno = sc.nextLine();
			try {
				Integer.parseInt(boardno);
				break;
			} catch (NumberFormatException e) {
				System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
			}
		}

		paraMap.put("boardno", boardno);
		paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

		bfdto = bfdao.veiwcount2(paraMap);

		int resultn2 = 0;

		// System.out.println("확인용 : " + bfdto.getFk_employee_id());

		if (bfdto != null & bfdto.getBf_boardno() != 0) {

			if (!bfdto.getFk_employee_id().equals(empDto.getEmployee_id())) {
				// 수정할 글번호가 다른 사용자가 쓴 글인경우
				System.out.println("[경고] 다른 사용자의 글을 수정 불가합니다!! \n");
			}

			else {
				// 글 목록 보여주는 메소드
				veiw(empDto, bfdto);

				String bf_subject = "";
				String bf_contents = "";
				do {
					System.out.println("▷ 수정할 제목을 입력하세요[변경하지 않으려면 엔터] : ");
					bf_subject = sc.nextLine();

					if (bf_subject != null && bf_subject.trim().isEmpty()) { // 기존 jubject
						bf_subject = bfdto.getBf_subject();
					}

					// 제목 유효성 검사
					if (bf_subject.length() > 100) {
						System.out.println("▷ 제목은 100글자를 넘을 수 없습니다. ");

					}

					else {
						break;
					}
				} while (true);

				do {
					System.out.println("▷ 수정할 내용을 입력하세요[변경하지 않으려면 엔터] : ");
					bf_contents = sc.nextLine();

					if (bf_contents != null && bf_contents.trim().isEmpty()) { // 기존 contents
						bf_contents = bfdto.getBf_contents();
					}
					// 내용 유효성 검사
					if (bf_contents.length() > 200) {
						System.out.println("▷ 내용은 200글자를 넘을 수 없습니다. ");
					} else {
						break;
					}
				} while (true);

				do {
					System.out.print("▷ 글 수정을 하시겠습니까?[Y/N] : ");
					String yn = sc.nextLine();

					if (yn.equalsIgnoreCase("y")) {

						paraMap.put("bf_subject", bf_subject);
						paraMap.put("bf_contents", bf_contents);
						// paraMap.put("boardno",boardno)

						resultn2 = bfdao.updateBoard(paraMap);
						break;
					}

					else if (yn.equalsIgnoreCase("N")) {
						resultn2 = 0;
						break;
					}

					else {
						System.out.println("▷ Y 또는 N 만 입력해주세요\n");
					}
				} while (true);
			}
		} // end of if (bfdto != null )

		else {
			System.out.println("▶ 일치하는 글 번호가 없습니다.");
		}

		return resultn2;
	}// private int updateBoard(EmployeesDTO empDto, Scanner sc)

	// ***5.글삭제하기 메소드****
	private int deleteBoard(EmployeesDTO empDto, Scanner sc) {

		// 글 목록 메소드
		boardList();

		Map<String, String> paraMap = new HashMap<>();
		int resultn3 = 0;

		do {
			String boardno;
			for (;;) {
				System.out.println("▶ 삭제할 글번호를 입력하세요 : ");
				boardno = sc.nextLine();
				try {
					Integer.parseInt(boardno);
					break;
				} catch (NumberFormatException e) {
					System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
				}
			}

			paraMap.put("boardno", boardno);
			paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

			bfdto = bfdao.veiwcount2(paraMap);

			if (bfdto != null & bfdto.getBf_boardno() != 0) {

				// 관리자 로그인 중일때 모든 글 삭제가능
				if ("관리자".equals(empDto.getName())) {
					break;
				}

				else if (!bfdto.getFk_employee_id().equals(empDto.getEmployee_id())) {
					// 수정할 글번호가 다른 사용자가 쓴 글인경우
					System.out.println("[경고] 다른 사용자의 글을 삭제 불가합니다!! \n");
				}

				else {
					veiw(empDto, bfdto);

					break;
				}
			} // end of if (!bfdto.getFk_employee_id().equals(empDto.getEmployee_id() ))

			else {
				System.out.println("존재하지 않는 글번호 입니다.");
			}
		} while (true);

		do {
			System.out.print("▷ 정말로 삭제 하시겠습니까?[Y/N] : ");
			String yn = sc.nextLine();
			if (yn.equalsIgnoreCase("y")) {

				resultn3 = bfdao.deleteBoard(paraMap);

				break;
			}

			else if (yn.equalsIgnoreCase("N")) {
				resultn3 = 0;
				break;
			}

			else {
				System.out.println("▷ Y 또는 N 만 입력해주세요\n");
			}
		} while (true);

		return resultn3;
	}// end of private int deleteBoard(EmployeesDTO empDto2, Scanner sc)

	// *** 6. 댓글 쓰기 메소드 ***
	private int writeComment(EmployeesDTO empDto, Scanner sc) {

		boardList();

		int resultn4 = 0;

		StringBuilder sb = new StringBuilder();

		System.out.println(" \n□ □ □ 댓글 쓰기 □ □ □");

		int c_boardno = 0;
		String boardno = "";

		do {
			System.out.print("▶ 댓글 작성하려는 원글번호 : ");
			boardno = sc.nextLine();
			try {
				c_boardno = Integer.parseInt(boardno);
				break;
			} catch (NumberFormatException e) {
				System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");

			}
		} while (true); // 유효성검사

		Map<String, String> paraMap = new HashMap<>();

		paraMap.put("boardno", boardno); // 파라미터는 map에 담아서 보내주기
		paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

		// 글조회 메소드
		bfdto = bfdao.veiwcount2(paraMap);
		if (bfdto != null)
			veiw(empDto, bfdto);
		else {
			System.out.println("\n[경고] 존재하지 않는 글번호입니다.");
			return -1;
		}

		System.out.println(" ▶ 작성자명 : " + empDto.getName());

		sb.append(" ▶ 작성자명 : " + empDto.getName() + "\n");

		String cf_comments = "";

		do {
			System.out.print(" ▶ 댓글 내용 : ");
			cf_comments = sc.nextLine();
			sb.append(" 댓글 내용 : " + cf_comments + "\n");

			// 내용 유효성 검사
			if (cf_comments.length() > 100) {
				System.out.println("▷ 내용은 100글자를 넘을 수 없습니다. ");
			} else {
				break;
			}
		} while (true);

		do {

			System.out.println(" ▶ 댓글을 입력하시겠습니까? [Y/N] \n");
			System.out.println(sb.toString());
			String yn = sc.nextLine();

			// 댓글을 입력할 경우
			if (yn.equalsIgnoreCase("Y")) {
				// DTO에 담기

				cfdto.setFk_bf_boardno(c_boardno);
				cfdto.setCf_comments(cf_comments);
				cfdto.setFk_employee_id(empDto.getEmployee_id());

				// dao로 dto에담아서 로 보내주기

				resultn4 = cfdao.write(cfdto);

				// 이제 dao에서 insert해주고 결과가 reslutn으로 온다. 1로 와야 정상

				break;
			}
			// 글을 입력하지 않을 경우
			else if (yn.equalsIgnoreCase("N")) {

				resultn4 = 0;
				break;
			}

			else {
				System.out.println(" ▶ Y또는 N만 입력해주세요");
			}
		} while (true);

		return resultn4;
	}

	// *** 7.댓글 수정 메소드 ***

	private int updateComment(EmployeesDTO empDto, Scanner sc) {

		int resultn5 = 0;

		// 글목록 조회하기 메소드
		boardList();

		Map<String, String> paraMap = new HashMap<>();

		String boardno = "";
		do {

			for (;;) {
				System.out.println("\n□ □ □ 댓글 수정하기 □ □ □");
				System.out.print("▶ 수정할 댓글의 원글 번호를 입력하세요 : ");
				boardno = sc.nextLine();
				try {
					Integer.parseInt(boardno);
					break;
				} catch (NumberFormatException e) {
					System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
				}
			}

			paraMap.put("boardno", boardno);
			// paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

			// 글 조회 메소드 DAO
			bfdto = bfdao.veiwcount2(paraMap);

			/*
			 * System.out.println("확인용 : " + bfdto.getBf_boardno());
			 * 
			 * if(boardno.equals(bfdto.getBf_boardno())) { break; } else {
			 * System.out.println("조회할 글 번호가 없습니다."); }
			 * 
			 * }while(true);
			 */

			if (bfdto != null & bfdto.getBf_boardno() != 0) {

				// 글 조회 출력 메소드
				veiw(empDto, bfdto);

				// 댓글 보기 메소드(출력, cfdao처리까지 모두 포함이라 cfdao에서 정보 받아올수 있다)
				commentlist(bfdto, sc, boardno);

				do {
					String commentno;
					for (;;) {
						System.out.println("\n□ □ □ 댓글 수정하기 □ □ □");
						System.out.print("▶ 수정할 댓글의 번호를 입력하세요 : ");
						commentno = sc.nextLine();
						try {
							Integer.parseInt(commentno);
							break;
						} catch (NumberFormatException e) {
							System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
						}
					}

					paraMap.put("commentno", commentno);

					// 댓글 쓴사람 id, cf_commentno, cf_comments 가져오는 메소드

					cfdto = cfdao.commentid(paraMap);

					if (cfdto != null & cfdto.getCf_commentno() != 0) {
						break;
					}

					else {
						System.out.println("수정할 댓글번호가 없습니다.");
					}

				} while (true);

				// System.out.println("확인용 :" + cfdto.getFk_employee_id());
				if (!cfdto.getFk_employee_id().equals(empDto.getEmployee_id())) {
					// 수정할 댓글번호가 다른 사용자가 쓴 글인경우
					System.out.println("[경고] 다른 사용자의 댓글은 수정 불가합니다!! \n");
					break;
				}

				String cf_conmments = "";

				do {

					System.out.println("▷ 수정할 내용을 입력하세요[변경하지 않으려면 엔터] : ");
					cf_conmments = sc.nextLine();

					if (cf_conmments != null && cf_conmments.trim().isEmpty()) { // 기존 contents
						cf_conmments = cfdto.getCf_comments();

						break;
					}

					// 내용 유효성 검사
					else if (cf_conmments.length() > 100) {
						System.out.println("▷ 내용은 100글자를 넘을 수 없습니다. ");
					} else {

						break;
					}

				} while (true);

				do {
					System.out.print("▷ 댓글을 수정 하시겠습니까?[Y/N] : ");
					String yn = sc.nextLine();

					if (yn.equalsIgnoreCase("y")) {

						paraMap.put("cf_conmments", cf_conmments);

						// paraMap.put("boardno",boardno)

						resultn5 = cfdao.updateComments(paraMap);
						break;
					}

					else if (yn.equalsIgnoreCase("N")) {
						resultn5 = 0;
						break;
					}

					else {
						System.out.println("▷ Y 또는 N 만 입력해주세요\n");
					}
				} while (true);

				break;
			} // end of if
			else {
				System.out.println("조회할 글 번호가 없습니다.");
			}

		} while (true); // end of while
		return resultn5;

	} // private int updateComment(EmployeesDTO empDto, Scanner sc)-----------------

	// *** 8. 댓글 삭제 메소드 ***
	private int deleteComment(EmployeesDTO empDto, Scanner sc) {

		int resultn6 = 0;

		// 글목록 조회하기 메소드
		boardList();

		Map<String, String> paraMap = new HashMap<>();

		String boardno = "";
		do {

			for (;;) {
				System.out.println("\n□ □ □ 댓글 삭제하기 □ □ □");
				System.out.print("▶ 삭제할 댓글의 원글 번호를 입력하세요 : ");
				boardno = sc.nextLine();
				try {
					Integer.parseInt(boardno);
					break;
				} catch (NumberFormatException e) {
					System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
				}
			}

			paraMap.put("boardno", boardno);
			// paraMap.put("empDto.getEmployee_id()", empDto.getEmployee_id());

			// 글 조회 메소드 DAO
			bfdto = bfdao.veiwcount2(paraMap);

			if (bfdto != null & bfdto.getBf_boardno() != 0) {

				// 글 조회 출력 메소드
				veiw(empDto, bfdto);

				// 댓글 보기 메소드(출력, cfdao처리까지 모두 포함이라 cfdao에서 정보 받아올수 있다)
				commentlist(bfdto, sc, boardno);

				do {
					String commentno;
					for (;;) {
						System.out.println("\n□ □ □ 댓글 삭제하기 □ □ □");
						System.out.print("▶ 삭제할 댓글의 번호를 입력하세요 : ");
						commentno = sc.nextLine();

						try {
							Integer.parseInt(commentno);
							break;
						} catch (NumberFormatException e) {
							System.out.println("▷[경고] 글번호는 정수로만 입력하셔야 합니다.\n");
						}
					}
					paraMap.put("commentno", commentno);

					// 댓글 쓴사람 id, cf_commentno, cf_comments 가져오는 메소드

					cfdto = cfdao.commentid(paraMap);

					if (cfdto != null & cfdto.getCf_commentno() != 0) {

						// 관리자 로그인중일때 모든 댓글 삭제가능
						if ("관리자".equals(empDto.getName())) {
							break;
						}

						else if (!cfdto.getFk_employee_id().equals(empDto.getEmployee_id())) {
							// 삭제할 댓글번호가 다른 사용자가 쓴 글인경우
							System.out.println("[경고] 다른 사용자의 댓글은 삭제 불가합니다!! \n");

						}
						else {
							break;
						}

					}

					else {
						System.out.println("삭제할 댓글번호가 없습니다.");
					}

				} while (true);

				do {
					System.out.print("▷ 댓글을 삭제 하시겠습니까?[Y/N] : ");
					String yn = sc.nextLine();

					if (yn.equalsIgnoreCase("y")) {

						resultn6 = cfdao.deleteComments(paraMap);
						break;
					}

					else if (yn.equalsIgnoreCase("N")) {
						resultn6 = 0;
						break;
					}

					else {
						System.out.println("▷ Y 또는 N 만 입력해주세요\n");
					}
				} while (true);

				break;
			} // end of if
			else {
				System.out.println("조회할 글 번호가 없습니다.");
			}

		} while (true); // end of while

		return resultn6;

	}

	// *** 자유게시판 글조회 출력 메소드 ****
	public void veiw(EmployeesDTO empDto, Board_freeDTO bfdto) {

		System.out.println("\n------------------------------------");
		System.out.println("글번호\t조회수\t작성일자\n" + "-------------------------------------\n" + bfdto.showInfo() + "\n"// 글
																													// 위의탭보여주는
																													// 메소드
				+ "-------------------------------------\n" + "글쓴이 : " + bfdto.getFk_employee_id() + "\n" + "글내용 : "
				+ bfdto.getBf_contents());
		System.out.println("-----------------------------------------\n");

	}

	// *** 공지게시판 글조회 출력 메소드 ****
	public void noticeVeiw(EmployeesDTO empDto, Board_noticeDTO bndto) {

		System.out.println("\n------------------------------------");
		System.out
				.println("글번호\t조회수\t작성일자\n" + "-------------------------------------\n" + bndto.noticeShowInfo() + "\n"// 글
																														// 위의탭보여주는
																														// 메소드
						+ "-------------------------------------\n" + "글쓴이 : " + bndto.getFk_employee_id() + "\n"
						+ "글내용 : " + bndto.getBn_contents());
		System.out.println("-----------------------------------------\n");

	}

}// end of class
