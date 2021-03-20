# SpringJDBC
Spring에서 JDBC를 사용하는 세가지 방식

1. JdbcTemplate 클래스를 사용한 실습
	tommy.spring.guestbook.vo
		GuestMessage : VO에 해당하는 클래스
		GuestMessageList : VO 목록을 나타내는 클래스
	
	tommy.spring.guestbook.dao
		GuestMessageDAO : DAO 기능을 추상화한 인터페이스
		GuestMessageRowMapper : 목록 조회 시 사용할 클래스
		JdbcTemplateGuestMessageDao : 실제적인 데이터베이스 처리를 담당할 구현 클래스

	tommy.spring.guestbook.controller
		JdbcTemplateMain : 테스트를 위한 메인 클래스

2.  NamedParameterJdbcTemplate를 이용한 프로그래밍 실습 : 기본적인 모델과 서비스는 그대로 이용하고 DAO만 변경해서 실습
	tommy.spring.guestbook.vo
		GuestMessage : VO에 해당하는 클래스
		GuestMessageList : VO 목록을 나타내는 클래스

	tommy.spring.guestbook.dao
		GuestMessageDAO : DAO 기능을 추상화한 인터페이스
		GuestMessageRowMapper : 목록 조회 시 사용할 클래스
		NamedParamGuestmessageDao : NamedParameter JdbcTemplate을 이용한 DAO 클래스

	tommy.spring.guestbook.controller
		NamedParameterMain : 테스트를 위한 메인 클래스

3. SimpleJdbcInsert 클래스를 이용한 데이터 삽입 : NamedParamGuestMessageDao의 일부분만 변경

### 사용한 데이터베이스 테이블 예시

~~~
CREATE TABLE GUESTBOOK (
    MESSAGE_ID NUMBER(5,0),
    GUEST_NAME VARCHAR2(20) DEFAULT NULL,
    MESSAGE VARCHAR2(4000),
    REGISTRY_DATE DATE DEFAULT NULL,
    CONSTRAINT GUESTBOOK_PK PRIMARY KEY (MESSAGE_ID)
);

CREATE SEQUENCE GUEST_SEQ MINVALUE 1 MAXVALUE 99999
INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;
~~~
