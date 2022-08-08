package groupware;

public class Comments_noticeDTO {

	
	private int cn_commentno;      // 댓글번호    
	private int fk_bn_boardno;     // 원글 글번호  
	private String fk_employee_id; // 작성자 아이디   
	private String cn_contents;    // 댓글 내용   
	private String cn_writedate;   // 작성 일자
	
	
	//-------------------------------------------
	
	public int getCn_commentno() {
		return cn_commentno;
	}
	public void setCn_commentno(int cn_commentno) {
		this.cn_commentno = cn_commentno;
	}
	public int getFk_bn_boardno() {
		return fk_bn_boardno;
	}
	public void setFk_bn_boardno(int fk_bn_boardno) {
		this.fk_bn_boardno = fk_bn_boardno;
	}
	public String getFk_employee_id() {
		return fk_employee_id;
	}
	public void setFk_employee_id(String fk_employee_id) {
		this.fk_employee_id = fk_employee_id;
	}
	public String getCn_contents() {
		return cn_contents;
	}
	public void setCn_contents(String cn_contents) {
		this.cn_contents = cn_contents;
	}
	public String getCn_writedate() {
		return cn_writedate;
	}
	public void setCn_writedate(String cn_writedate) {
		this.cn_writedate = cn_writedate;
	}
	public String noticeViewCommentInfo() {
		// 댓글번호\t작성자\t\t댓글내용\t작성일자
		
		return  cn_commentno +"\t" + fk_employee_id+"\t"+cn_contents+"t"+cn_writedate;
	
	}
	
	
}
