package tommy.spring.guestbook.dao;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import tommy.spring.guestbook.vo.GuestMessage;

public class JdbcTemplateGuestMessageDao implements GuestMessageDAO {//GuestMessageDAO를 상속받아 실제적인 데이터베이스 처리를 담당할 구현 클래스
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplateGuestMessageDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int count() {
		return jdbcTemplate.queryForObject("select count(*) from GUESTBOOK", Integer.class);
	}

	@Override
	public List<GuestMessage> selet(int begin, int end) {
		return jdbcTemplate.query("select * from (select ROWNUM rnum, MESSAGE_ID, GUEST_NAME, "
				+ "MESSAGE, REGISTRY_DATE from (select * from GUESTBOOK order by MESSAGE_ID desc)) "
				+ "where rnum>=? and rnum<=?", new GuestMessageRowMapper(), new Object[] { begin, end });
	}

	@Override
	public int insert(final GuestMessage message) {
		int insertedCount = jdbcTemplate.update("insert into GUESTBOOK(MESSAGE_ID, GUEST_NAME, "
				+ "MESSAGE, REGISTRY_DATE) values (guest_seq.nextval, ?, ?, ?)" , 
				message.getGuestName(), message.getMessage(), message.getRegistryDate());
		return insertedCount;
	}

	@Override
	public int delete(int id) {
		return jdbcTemplate.update("delete from GUESTBOOK where MESSAGE_ID = ?", id);
	}

	@Override
	public int update(GuestMessage message) {
		return jdbcTemplate.update("update GUESTBOOK set MESSAGE = ? where MESSAGE_ID = ?", 
				new int[] {Types.VARCHAR, Types.INTEGER}, new Object[] {message.getMessage(), message.getId()});
	}

}
