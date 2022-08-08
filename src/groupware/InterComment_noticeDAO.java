package groupware;

import java.util.List;

public interface InterComment_noticeDAO {

	int noticeWrite(Comments_noticeDTO cndto); // 댓글쓰기메소드

	List<Comments_noticeDTO> noticeCommentList(String boardno); // 글밑에 댓글 보여주는 메소드 

}
