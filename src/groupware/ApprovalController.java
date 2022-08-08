package groupware;

import java.util.*;

public class ApprovalController {

	ReportApprovalDAO rdao = new ReportApprovalDAO();

	// *** 전자결재 메뉴를 띄워주는 메소드 *** //
	public void approval_menu(EmployeesDTO empDto, Scanner sc) {

		String s_menuNo;
		int n;

		do {
			System.out.println("\n      >>> ------- 전자결재 ------- <<<");
			System.out.println("1. 결재 상신  2. 결재 목록  3. 기안문서 조회\n" + "4. 기안문서 수정/삭제  5. 결재 처리  6. 나가기");
			System.out.println("-----------------------------------------");
			System.out.print("\n▷ 확인할 메뉴 선택: ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 결재 상신
				n = submitReport(empDto, sc);

				if (n == 1) {
					System.out.println("\n>> 결재 상신 완료 <<");
				} else if (n == 0) {
					System.out.println("\n>> 결재 상신 취소 <<");
				} else if (n == -1) {
					System.out.println("\n>> 결재 상신 실패 <<");
				}

				break;

			case "2": // 결재 목록 조회
				reportList_menu(empDto, sc);
				break;

			case "3": // 기안문서 조회
				viewReport_menu(empDto, sc);
				break;

			case "4": // 기안문서 수정/삭제
				editReport_menu(empDto, sc);
				break;

			case "5": // 결재 처리
				n = approve(empDto, sc);
				if (n == 1) {
					System.out.println("\n>> 결재 완료 <<");
				} else if (n == 0) {
					System.out.println("\n>> 결재 취소 <<");
				} else if (n == -1) {
					System.out.println("\n>> 결재 실패 <<");
				}
				break;

			case "6": // 나가기
				System.out.println("\n>> 전자결재 메뉴를 나갑니다. <<");
				break;

			default:
				System.out.println("\n[경고] 메뉴에 없는 번호입니다!!");
				break;
			}
		} while (!"6".equals(s_menuNo));

	} // end of private void approval(EmployeesDTO empDto, Scanner sc)

	// *** 결재 상신 메소드 *** //
	private int submitReport(EmployeesDTO empDto, Scanner sc) {

		int result = 0;

		if ("사장".equals(empDto.getPosition())) {
			System.out.println("\n[경고] 결재를 상신할 대상이 없습니다.");
			result = -1;
			return result;
		}

		System.out.println("\n>>> ----- 결재 상신 ----- <<<");

		System.out.println("▷ 작성자명: " + empDto.getName());

		String subject = null;
		while (true) { // 제목 입력받기
			System.out.print("▷ 제목: ");
			subject = sc.nextLine();

			if (subject.trim().isEmpty()) // 공백이라면
				System.out.println("[경고] 제목을 입력하세요.");

			else if (subject.length() > 100)
				System.out.println("[경고] 제목은 100글자를 초과할 수 없습니다.");

			else
				break;
		}

		String contents = null;
		while (true) { // 내용 입력받기
			System.out.print("▷ 내용: ");
			contents = sc.nextLine();

			if (contents.trim().isEmpty()) // 공백이라면
				System.out.println("[경고] 내용을 입력하세요.");

			else if (contents.length() > 200)
				System.out.println("[경고] 내용은 200글자를 초과할 수 없습니다.");

			else
				break;
		}

		System.out.println("\n >>> ----- 미리보기 ----- <<<");
		System.out.println("▷ 작성자명: " + empDto.getName());
		System.out.println("▷ 제목: " + subject);
		System.out.println("▷ 내용: " + contents);
		System.out.println("----------------------------");

		boolean reallyWrite = false; // 입력할건지 말건지
		do {
			System.out.print("\n>> 위와 같은 내용으로 결재 상신하시겠습니까? [Y/N] => ");
			String yn = sc.nextLine();

			if ("y".equalsIgnoreCase(yn)) {
				reallyWrite = true;
				break;
			} else if ("n".equalsIgnoreCase(yn)) {
				break;
			} else
				System.out.println(">> y 또는 n만 입력하세요. <<");
		} while (true);

		if (reallyWrite) { // 상신하겠다

			ReportDTO rdto = new ReportDTO();
			rdto.setFk_report_employee_id(empDto.getEmployee_id()); // 사원번호
			rdto.setRp_subject(subject); // 제목
			rdto.setRp_contents(contents); // 내용

			result = rdao.submitReport(rdto); // 결재 상신 -> 1이면 성공 -1이면 실패

		}

		return result;
	} // end of private int submitReport(EmployeesDTO empDto, Scanner sc)

	// *** 결재목록 메뉴 띄워주는 메소드 *** //
	private void reportList_menu(EmployeesDTO empDto, Scanner sc) {

		String s_menuNo;
		do {

			System.out.println("\n >>> ----- 결재목록 ----- <<<");
			System.out.println("1. 진행 중 결재  2. 완료된 결재  3. 나가기");
			System.out.println("---------------------------------");
			System.out.print("\n▷ 확인할 메뉴 선택: ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 1. 진행 중 결재
			case "2": // 2. 완료된 결재
				viewReportList(empDto, s_menuNo);
				break;

			case "3":
				System.out.println("\n>> 결재목록 메뉴를 빠져나갑니다. <<");
				break;

			default:
				System.out.println("\n[경고] 메뉴에 없는 번호입니다!!");
				break;
			}
		} while (!"3".equals(s_menuNo));

	} // private void viewReportList(EmployeesDTO empDto, Scanner sc)

	// *** 결재목록 조회 메소드 ***//
	private void viewReportList(EmployeesDTO empDto, String choice_number) {

		List<ReportDTO> reportList = rdao.viewReportList(empDto, choice_number);

		if (reportList.size() == 0) {
			System.out.println("\n >> 조회할 결재 목록이 없습니다. <<");
		}

		else {
			StringBuilder sb = new StringBuilder();

			if ("1".equals(choice_number)) { // 진행중 결재 목록
				System.out.println("\n-------------------------------------------------------------");
				System.out.println("문서번호\t작성일자\t\t제목\t\t기안자\t결재단계\t결재자");
				System.out.println("-------------------------------------------------------------");

				for (int i = 0; i < reportList.size(); i++) {
					if (i > 0 && reportList.get(i).getReport_no() != reportList.get(i - 1).getReport_no())
						sb.append("***\n");
					sb.append(reportList.get(i).getReport_no() + "\t"); // 기안문서번호
					sb.append(reportList.get(i).getWritedate() + "\t"); // 작성일자
					sb.append(reportList.get(i).getRp_subject() + "\t"); // 제목
					sb.append(reportList.get(i).getRpEDto().getName() + "\t"); // 기안자
					sb.append(reportList.get(i).getApDto().getLevelno() + "\t"); // 결재단계
					sb.append(reportList.get(i).getApEDto().getName() + "\n"); // 결재자
				}
				System.out.println(sb);

			} else { // 완료된 결재 목록
				System.out.println("\n--------------------------------------------------------------------------");
				System.out.println("문서번호\t작성일자\t\t제목\t\t기안자\t결재자\t결재상태\t결재일자");
				System.out.println("--------------------------------------------------------------------------");

				for (int i = 0; i < reportList.size(); i++) {
					if (i > 0 && reportList.get(i).getReport_no() != reportList.get(i - 1).getReport_no())
						sb.append("***\n");
					sb.append(reportList.get(i).getReport_no() + "\t"); // 기안문서번호
					sb.append(reportList.get(i).getWritedate() + "\t"); // 작성일자
					sb.append(reportList.get(i).getRp_subject() + "\t"); // 제목
					sb.append(reportList.get(i).getRpEDto().getName() + "\t"); // 기안자
					sb.append(reportList.get(i).getApEDto().getName() + "\t"); // 결재자
					sb.append(reportList.get(i).getApDto().getApproval_status() + "\t"); // 결재상태
					sb.append(reportList.get(i).getApDto().getApproval_date() + "\n"); // 결재일자

				}

				System.out.println(sb);
			}
		}
	} // end of private void viewReport(EmployeesDTO empDto, String choice_number)

	// *** 기안문서 조회 메뉴 띄워주는 메소드 *** //
	private void viewReport_menu(EmployeesDTO empDto, Scanner sc) {

		String s_menuNo;
		do {
			System.out.println("\n>>> ----- 기안문서 조회 ----- <<<");
			System.out.println("1. 문서 검색하기   2. 나가기");
			System.out.println("-------------------------------");
			System.out.print("\n▷ 확인할 메뉴 선택: ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 문서번호로 검색
				viewReportContents(empDto, sc);
				break;

			case "2": // 나가기
				System.out.println("\n>> 기안문서 조회 메뉴를 빠져나갑니다. <<");
				break;

			default:
				System.out.println("\n[경고] 메뉴에 없는 번호입니다!!");
				break;
			}

		} while (!"2".equals(s_menuNo));

	}

	// *** 기안문서 내용 조회 메소드 *** //
	private void viewReportContents(EmployeesDTO empDto, Scanner sc) {

		String reportNo = null;

		while (true) {
			System.out.println("\n>>> ----- 기안문서 조회 ----- <<<");

			System.out.print("▷ 문서번호: ");
			reportNo = sc.nextLine();

			if (reportNo.trim().isEmpty())
				System.out.println("\n[경고] 문서번호를 입력해야 합니다.");
			else
				break;

		} // end of while

		ReportDTO rdto = rdao.viewReportContents(empDto, reportNo);

		if (rdto != null) { // 해당하는 문서가 있으면
			System.out.println("\n====================================================");
			System.out.print("\n[제목] ");
			System.out.println(rdto.getRp_subject());
			System.out.print("\n[작성자] ");
			System.out.println(rdto.getRpEDto().getDeptDto().getDepartment_name() + "팀 / " + rdto.getRpEDto().getName()
					+ " / " + rdto.getRpEDto().getPosition());
			System.out.print("\n[작성일시] ");
			System.out.println(rdto.getWritedate());
			System.out.println("\n[내용]\n");
			System.out.println(rdto.getRp_contents());

			List<ApprovalDTO> commentList = rdao.commentList(reportNo); // 결재의견(코멘트) 가져오기

			if (commentList.size() > 0) { // 코멘트가 있으면
				System.out.println("\n[결재 의견]\n");
				System.out.println("결재의견\t처리\t결재자\t처리일시");
				System.out.println("----------------------------------------------------");

				StringBuilder sb = new StringBuilder();

				for (ApprovalDTO apDto : commentList) {
					sb.append(apDto.viewComments() + "\n");
				}

				System.out.println(sb);
			}
			System.out.println("====================================================");

		}
	} // end of private void viewReportContents(EmployeesDTO empDto, Scanner sc)

	// *** 기안문서 수정/삭제 메뉴 띄워주는 메소드 *** //
	private void editReport_menu(EmployeesDTO empDto, Scanner sc) {

		String s_menuNo;
		do {
			System.out.println("\n>>> ----- 기안문서 수정/삭제 ----- <<<");
			System.out.println("1. 수정하기   2. 삭제하기   3. 나가기");
			System.out.println("-------------------------------");
			System.out.print("\n▷ 확인할 메뉴 선택: ");
			s_menuNo = sc.nextLine();

			switch (s_menuNo) {
			case "1": // 수정하기
			case "2": // 삭제하기
				int n = editReport(empDto, sc, s_menuNo);
				if (n == 1) {
					System.out.println("\n>> 수정/삭제 완료 <<");
				} else if (n == 0) {
					System.out.println("\n>> 수정/삭제 취소 <<");
				} else if (n == -1) {
					System.out.println("\n>> 수정/삭제 실패 <<");
				}
				break;

			case "3": // 나가기
				System.out.println("\n>> 기안문서 수정/삭제 메뉴를 빠져나갑니다. <<");
				break;

			default:
				System.out.println("\n[경고] 메뉴에 없는 번호입니다!!");
				break;
			}

		} while (!"3".equals(s_menuNo));

	} // end of private void editReport_menu(EmployeesDTO empDto, Scanner sc)

	// *** 기안문서 수정/삭제하기 메소드 *** //
	private int editReport(EmployeesDTO empDto, Scanner sc, String s_menuNo) {
		int result = 0;

		System.out.println("\n>>> ----- 기안문서 수정/삭제 ----- <<<");

		String report_no;
		do {
			System.out.print("▷ 수정/삭제할 문서 번호: ");
			report_no = sc.nextLine();

			try {
				Integer.parseInt(report_no);
				break;
			} catch (NumberFormatException e) {
				System.out.println("\n[경고] 문서 번호는 숫자로만 입력하세요.\n");
			}

		} while (true);

		ReportDTO rdto = rdao.viewReportContents(empDto, report_no);

		if (rdto != null) { // 글이 존재하면
			String subject = " ";
			String contents = " ";

			if (!rdto.getRpEDto().getEmployee_id().equals(empDto.getEmployee_id())) {
				// 문서 작성자와 로그인된 사람이 다르면
				System.out.println("\n[경고] 다른 사람의 문서는 수정/삭제할 수 없습니다.");
				result = -1;
				return result;
			}

			else { // 문서 작성자와 로그인된 사람이 같으면

				System.out.println("--------------------------------------");
				System.out.println("[문서 제목] " + rdto.getRp_subject());
				System.out.println("[문서 내용] " + rdto.getRp_contents());
				System.out.println("--------------------------------------");

				if ("1".equals(s_menuNo)) { // 수정일 경우
					while (true) {
						System.out.println("▷ 변경할 문서 제목 [변경하지 않으려면 엔터]");
						subject = sc.nextLine();
						if (subject != null && subject.trim().isEmpty()) {
							subject = rdto.getRp_subject();
						}

						System.out.println("▷ 변경할 문서 내용 [변경하지 않으려면 엔터]");
						contents = sc.nextLine();
						if (contents != null && contents.trim().isEmpty()) {
							contents = rdto.getRp_contents();
						}

						if (subject.length() > 100 || contents.length() > 200) {
							System.out.println("[경고] 제목 최대 100글자, 내용 최대 200글자를 초과할 수 없습니다. \n");
						} else
							break;
					}

					while (true) {
						System.out.println(">> 정말로 수정하시겠습니까? [Y/N] <<");
						String yn = sc.nextLine();

						if ("y".equalsIgnoreCase(yn)) {
							break;
						} else if ("n".equalsIgnoreCase(yn)) {
							result = 0;
							return result;
						} else
							System.out.println(">> Y 또는 N만 입력하세요. <<");
					}
					Map<String, String> paraMap = new HashMap<>();
					paraMap.put("subject", subject);
					paraMap.put("contents", contents);
					paraMap.put("report_no", report_no);

					result = rdao.editReport(paraMap); // 글 수정하기
					// 1 or -1
				}

				else { // 삭제일 경우

					while (true) {
						System.out.println(">> 정말로 삭제하시겠습니까? [Y/N] <<");
						String yn = sc.nextLine();

						if ("y".equalsIgnoreCase(yn)) {
							break;
						} else if ("n".equalsIgnoreCase(yn)) {
							result = 0;
							return result;
						} else
							System.out.println(">> Y 또는 N만 입력하세요. <<");
					}
					Map<String, String> paraMap = new HashMap<>();
					paraMap.put("subject", subject);
					paraMap.put("contents", contents);
					paraMap.put("report_no", report_no);

					result = rdao.deleteReport(report_no); // 글 삭제하기
					// 1 or -1

				}
			}
		}

		else { // 글이 존재하지 않으면
			result = -1;
			return result;
		}

		return result;

	} // end of private int editReport(EmployeesDTO empDto, Scanner sc)

	// *** 결재 처리하기 메소드 *** //
	private int approve(EmployeesDTO empDto, Scanner sc) {
		
		if ("사원".equals(empDto.getPosition())) {
			System.out.println("\n[경고] 결재를 처리할 권한이 없습니다.");
			return -1;
		}
		
		int result; // 실행 결과
		String report_no; // 기안문서번호
		String decision; // 처리방법(결재/반려)
		String comments; // 결재 코멘트

		while (true) {
			System.out.println("\n >>> ----- 결재처리 ----- <<<");
			System.out.print("▷ 기안문서 번호를 입력하십시오 : ");
			report_no = sc.nextLine();

			try {
				Integer.parseInt(report_no); // 숫자로 변환
			} catch (NumberFormatException e) {
				System.out.println("\n[경고] 문서번호는 숫자로만 입력하세요.");
				continue;
			}

			ReportDTO rdto = rdao.viewReportContents(empDto, report_no); // 해당하는 문서가 있는지, 다른 부서의 것인지 검사
			if (rdto == null) // 만약 번호에 해당하는 문서가 없거나 다른 부서의 문서이면 결재 실패
				return -1;

			System.out.print("▷ 이 문서를 어떻게 처리하시겠습니까? [1. 결재  2. 반려] : ");

			decision = sc.nextLine();

			if ("1".equals(decision) || "2".equals(decision)) {

				System.out.print("▷ 코멘트 입력: (입력을 원치 않으시면 엔터를 누르세요.)");
				comments = sc.nextLine();

				while (true) {
					System.out.println(">> 정말로 처리하시겠습니까? [Y/N] <<");
					String yn = sc.nextLine();

					if ("y".equalsIgnoreCase(yn)) {
						break;
					} else if ("n".equalsIgnoreCase(yn)) {
						return 0;
					} else
						System.out.println(">> Y 또는 N만 입력하세요. <<");
				}

				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("report_no", report_no);
				paraMap.put("decision", decision);
				paraMap.put("comments", comments);

				if (rdto.getRpEDto().getEmployee_id().equals(empDto.getEmployee_id())) {// 자신이 자신의 문서를 결재하려 할 때
					System.out.println("\n[경고] 자신이 작성한 문서는 결재할 수 없습니다.");
					return -1;
				}
				result = rdao.approve(empDto, paraMap); // 1 or -1
				break;
			}

			else {
				System.out.println("\n[경고] 1, 2로만 입력하세요.");
			}

		} // end of while
		return result;
	} // end of private void approve(EmployeesDTO empDto, Scanner sc)

}
