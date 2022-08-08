package groupware;

public class Board_freeDTO {

	
	private int bf_boardno;          // 글번호
	private String fk_employee_id;   // 작성자아이디
	private String bf_subject;		 // 글제목	
	private String bf_contents;      // 글내용
	private int bf_viewcount;        // 조회수
	private String bf_writedate;     // 작성일자
	

	//-------------------------------------------------
	
	
	public int getBf_boardno() {
		return bf_boardno;
	}
	
	
	public void setBf_boardno(int bf_boardno) {
		this.bf_boardno = bf_boardno;
	}
	public String getFk_employee_id() {
		return fk_employee_id;
	}
	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	public String getBf_subject() {
		return bf_subject;
	}
	public void setBf_subject(String bf_subject) {
		this.bf_subject = bf_subject;
	}
	public String getBf_contents() {
		return bf_contents;
	}
	public void setBf_contents(String bf_contents) {
		this.bf_contents = bf_contents;
	}
	public int getBf_viewcount() {
		return bf_viewcount;
	}
	public void setBf_viewcount(int bf_viewcount) {
		this.bf_viewcount = bf_viewcount;
	}
	public String getBf_writedate() {
		return bf_writedate;
	}
	public void setBf_writedate(String bf_writedate) {
		this.bf_writedate = bf_writedate;
	}

    //글 위의 탭 보여주는 메소드
	public String showInfo() {
		return bf_boardno+"\t"+bf_viewcount+"\t"+bf_writedate;
		
	} 
	
	
}
