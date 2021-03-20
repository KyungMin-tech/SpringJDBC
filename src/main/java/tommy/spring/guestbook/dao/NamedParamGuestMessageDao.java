package tommy.spring.guestbook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import tommy.spring.guestbook.vo.GuestMessage;

public class NamedParamGuestMessageDao implements GuestMessageDAO {//NamedParameter JdbcTemplate를 이용한 DAO클래스
	private NamedParameterJdbcTemplate template;
	private SimpleJdbcInsert insertMessage;		

	public NamedParamGuestMessageDao(SimpleJdbcInsert insertMessage) {
		this.insertMessage = insertMessage;
		insertMessage.withTableName("GUESTBOOK");//데이터를 삽입할 테이블의 이름을 지정
		insertMessage.usingColumns("MESSAGE_ID", "GUEST_NAME", "MESSAGE", "REGISTRY_DATE");// 메서드를 사용하면 쿼리를 생성할때 사용할 컬럼을 직접 지정
																//주의할 점은 지정한 컬럼에 대해서만 값이 삽입된다는 점 
	}
	public int nextVal() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return template.queryForObject("select guest_seq.nextval from dual", paramMap, Integer.class);
	}

	public void setTemplate(NamedParameterJdbcTemplate template) {
		this.template = template;
	}

	@Override
	public int count() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return template.queryForObject("select count(*) from GUESTBOOK", paramMap, Integer.class);
	}

	@Override
	public List<GuestMessage> selet(int begin, int end) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startRowNum", begin);
		paramMap.put("count", end - begin + 1);
		return template.query("select * from (select ROWNUM rnum, MESSAGE_ID, "
				+ "GUEST_NAME, MESSAGE, REGISTRY_DATE from "
				+ "(select * from GUESTBOOK order by MESSAGE_ID desc)) "
				+ "where rnum>=:startRowNum and rnum<=:count", paramMap, new RowMapper<GuestMessage>() {

					@Override
					public GuestMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
						GuestMessage message = new GuestMessage();
						message.setId(rs.getInt("MESSAGE_ID"));
						message.setGuestName(rs.getString("GUEST_NAME"));
						message.setMessage(rs.getString("MESSAGE"));
						message.setRegistryDate(rs.getDate("REGISTRY_DATE"));
						return message;
					}
					
				});
	}

	@Override
	public int insert(GuestMessage message) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		message.setId(nextVal());
		paramSource.addValue("MESSAGE_ID", message.getId());
		paramSource.addValue("GUEST_NAME", message.getGuestName());
		paramSource.addValue("MESSAGE", message.getMessage());
		paramSource.addValue("REGISTRY_DATE", message.getRegistryDate());
		return insertMessage.execute(paramSource);
	}

	@Override
	public int delete(int id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return template.update("delete from GUESTBOOK where MESSAGE_ID = :id", paramMap);
	}

	@Override
	public int update(GuestMessage message) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("message", message.getMessage());
		paramSource.addValue("id", message.getId(), Types.INTEGER);
		return template.update("update GUESTBOOK set MESSAGE = :message "
				+ "where MESSAGE_ID = :id", paramSource);
	}

}
