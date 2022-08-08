package groupware;

import java.util.List;
import java.util.Map;

public interface InterBoard_noticeDAO {

	List<Map<String, String>> boardNoticeList(); // 글목록 메소드 

	Board_noticeDTO noticeVeiwcount2(Map<String, String> paraMap); // 글조회 메소드
	
	int noticeVeiwcount(Map<String, String> paraMap);  //글조회&조회수 올려주는 메소드

	int writeNotice(Board_noticeDTO bndto);// 공지게시판 글쓰기 메소드

	int updateNoticeBoard(Map<String, String> paraMap); // 공지게시판 글 수정 메소드

	int deleteNoticeBoard(Map<String, String> paraMap); //골지게시판 글 삭제 메소드




	

}
