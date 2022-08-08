package groupware;

public class Board_noticeDTO {

	
	private int bn_boardno;          // 글번호
	private String fk_employee_id;   // 작성자아이디  
	private String bn_subject;       // 글제목  
	private String bn_contents;      // 글내용  
	private int bn_viewcount;        // 조회수
	private String bn_writedate;	 // 작성일자
	
	
	//------------------------------------------------
	
	public int getBn_boardno() {
		return bn_boardno;
	}
	public void setBn_boardno(int bn_boardno) {
		this.bn_boardno = bn_boardno;
	}
	public String getFk_employee_id() {
		return fk_employee_id;
	}
	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	public String getBn_subject() {
		return bn_subject;
	}
	public void setBn_subject(String bn_subject) {
		this.bn_subject = bn_subject;
	}
	public String getBn_contents() {
		return bn_contents;
	}
	public void setBn_contents(String bn_contents) {
		this.bn_contents = bn_contents;
	}
	public int getBn_viewcount() {
		return bn_viewcount;
	}
	public void setBn_viewcount(int bn_viewcount) {
		this.bn_viewcount = bn_viewcount;
	}
	public String getBn_writedate() {
		return bn_writedate;
	}
	public void setBn_writedate(String bn_writedate) {
		this.bn_writedate = bn_writedate;
	}
	
	
	public String noticeShowInfo() {
		
		return bn_boardno+"\t"+bn_viewcount+"\t"+bn_writedate;
	}      

	
	
}
