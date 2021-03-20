package tommy.spring.guestbook.dao;

import java.util.List;

import tommy.spring.guestbook.vo.GuestMessage;

public interface GuestMessageDAO {//DAO 기능을 추상화한 GuestMessageDAO 인터페이스 작성
	public int count();
	public List<GuestMessage> selet(int begin, int end);
	public int insert(GuestMessage message);
	public int delete(int id);
	public int update(GuestMessage message);
}
