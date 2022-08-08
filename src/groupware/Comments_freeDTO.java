package groupware;

public class Comments_freeDTO {

	private int cf_commentno;       // 댓글번호
	private int fk_bf_boardno; 		// 원글의 글번호
	private String fk_employee_id;  // 작성자 아이디
	private String cf_comments;  	// 댓글내용
	private String cf_writedate;	// 작성일자
	
	//----------------------------------------------------
	
	public int getCf_commentno() {
		return cf_commentno;
	}
	public void setCf_commentno(int cf_commentno) {
		this.cf_commentno = cf_commentno;
	}
	public int getFk_bf_boardno() {
		return fk_bf_boardno;
	}
	public void setFk_bf_boardno(int fk_bf_boardno) {
		this.fk_bf_boardno = fk_bf_boardno;
	}
	public String getFk_employee_id() {
		return fk_employee_id;
	}
	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	public String getCf_comments() {
		return cf_comments;
	}
	public void setCf_comments(String cf_comments) {
		this.cf_comments = cf_comments;
	}
	public String getCf_writedate() {
		return cf_writedate;
	}
	public void setCf_writedate(String cf_writedate) {
		this.cf_writedate = cf_writedate;
	}
	
	//댓글 메소드
	public String viewCommentInfo() {
		// 댓글번호\t작성자\t\t댓글내용\t작성일자
		
		return  cf_commentno +"\t" + fk_employee_id+"\t"+cf_comments+"\t"+cf_writedate;
	} 
		
}
