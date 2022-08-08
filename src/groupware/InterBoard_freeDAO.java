package groupware;

import java.util.*;

public interface InterBoard_freeDAO {

	int write(Board_freeDTO bfdto); // 글쓰기 메소드

	int veiwcount(Map<String, String> paraMap); //글내용보기&조회수카운트 트랜젝션메소드

	int updateBoard(Map<String, String> paraMap); //글 수정하기 메소드 

	int deleteBoard(Map<String, String> paraMap); // 글삭제해주기

	Board_freeDTO veiwcount2(Map<String, String> paraMap); //트랜젝션없는 글내용보기 메소드
	
	public int count(Map<String, String> paraMap); //트랜젝션없는 조회수올라가는 메소드

	List<Map<String, String>> boardList(); // 글목록보기 메소드
	

	
	
	
	
	
	

}
