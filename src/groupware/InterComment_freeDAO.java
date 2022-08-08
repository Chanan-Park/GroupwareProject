package groupware;

import java.util.List;
import java.util.Map;

public interface InterComment_freeDAO {

	int write(Comments_freeDTO cfdto); // 댓글쓰기

	List<Comments_freeDTO> commentList(String boardno); //글내용 밑에 댓글보이게하기

	int updateComments(Map<String, String> paraMap); // 댓글 수정하기

	Comments_freeDTO commentid(Map<String, String> paraMap); // 댓글 쓴사람 아이디 가져오는 메소드

	int deleteComments(Map<String, String> paraMap); // 댓글 삭제하기

}
