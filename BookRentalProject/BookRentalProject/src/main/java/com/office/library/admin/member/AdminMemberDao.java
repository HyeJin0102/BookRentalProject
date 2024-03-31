package com.office.library.admin.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public boolean isAdminMember(String a_m_id) { //아이디 중복 체크
		System.out.println("[AdminMemberDao] isAdminMember()");
		
		String sql = "SELECT COUNT(*) FROM tbl_admin_member " + "WHERE a_m_id = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);
		//jdbcTemplate.queryForObject(a,b,c)를 통해 SQL문을 실행한 결과를 result에 받음. 
		//a:SQL문 b:쿼리 실행 후 반환되는 데이터 타입 c:관리자가 입력한 아이디
		
		return result > 0? true : false; //1을 반환하게 되면 이미 사용중임을 알 수 있음
	}
	
	public int insertAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] insertAdminAccount()");
		
		List<String> args = new ArrayList<String>();
		
		String sql = "INSERT INTO tbl_admin_member(";
			if(adminMemberVo.getA_m_id().equals("super admin")) {
				sql += "a_m_approval, ";
				args.add("1");
			} //id가 최고 관리자(super admin)와 같을 경우 a_m_approval(관리자 승인)을 1로 설정하여 로그인 가능하게 해줌.
			//그 외 관리자들의 경우 a_m_approval이 0이라서 최고 관리자가 승인하지 않으면 로그인 불가
			
			sql += "a_m_id, ";
			args.add(adminMemberVo.getA_m_id());
			
			sql += "a_m_pw, ";
			args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw())); //비밀번호 암호화 작업
			
			sql += "a_m_name, ";
			args.add(adminMemberVo.getA_m_name());
			
			sql += "a_m_gender, ";
			args.add(adminMemberVo.getA_m_gender());
			
			sql += "a_m_part, ";
			args.add(adminMemberVo.getA_m_part());
			
			sql += "a_m_position, ";
			args.add(adminMemberVo.getA_m_position());
			
			sql += "a_m_mail, ";
			args.add(adminMemberVo.getA_m_mail());
			
			sql += "a_m_phone, ";
			args.add(adminMemberVo.getA_m_phone());
			
			sql += "a_m_reg_date, a_m_mod_date) ";
			
			if(adminMemberVo.getA_m_id().equals("super admin"))
				sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())"; //아이디가 super admin이면 9개, 
			else
				sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())"; //그렇지 않으면 8개 전달
			
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, args.toArray()); 
			//update의 요소로 sql문과 statement 값을 전달함. 단, 여기서 args는 list형이므로 배열로 변경하여 전달함.
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//서비스에서 넘겨준 id, pw와 일치하는 관리자 정보를 tbl_admin_member 테이블에서 조회한 후 결과를 AdminMemberVo 타입으로 서비스에 반환해주는 메서드
	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql = "SELECT * FROM tbl_admin_member " + "WHERE a_m_id = ? AND a_m_approval > 0";
		//tbl_admin_member에서 관리자가 입력한 아이디가 a_m_id와 일치하고 관리자 승인(a_m_approval)이 완료(1)된 회원 조회
		//비밀번호의 경우 암호화 되어 있어 추후 복호화 하여 별도 비교해야함.
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>(); 
		//조회한 회원 정보를 저장하는 리스트. 일치하는 회원이 검색된 경우 adminMemberVos의 길이는 1 그 외에는 0
		
		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				//jdbcTemplate.query(a,b,c)에서 a:쿼리문 b:RowMapper 인터페이스 구현한 익명 클래스 c:관리자가 입력한 아이디(서비스에서 전달받음)
				//RowMapper의 경우 데이터베이스의 row(행)를 어딘가에 매핑하는 역할을 함. 여기서는 row를 AdminMemberVo 객체에 매핑함
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					/*
					 * RowMapper를 구현한 익명 클래스는 추상 메서드 mapRow를 구현해야할 의무가 있음.
					 * mapRow()는 ResultSet과 행의 개수를 파라미터로 받음. rs에는 데이터베이스에서 조회된 데이터셋이 저장되어 있고,
					 * rowNum에는 데이터셋의 현재 행 번호가 저장되어 있음.  
					 * 
					 * 따라서 mapRow()에서 조회된 데이터를 java형식으로 변경하는 코드가 필요한데, 이때 사용되는 데이터 형식 객체가 AdminMemberVo
					 * */
					AdminMemberVo adminMemberVo = new AdminMemberVo(); //리스트형의 AdminMemberVo 객체 생성
					
					//setter를 이용해 rs에 있는 데이터를 AdminMemberVo 객체에 저장
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo; //반환된 adminMemberVo객체가 리스트 타입으로 저장된후 adminMemberVos에 할당됨
					
				}
			}, adminMemberVo.getA_m_id());
			
			if(!passwordEncoder.matches(adminMemberVo.getA_m_pw(), adminMemberVos.get(0).getA_m_pw()))
				//passwordEncoder.matches(CharSequence rawPassword(관리자가 입력한 암호화 x pw), String encodedPassword(데이터베이스의 암호화 pw))
				//를 통해 관리자가 입력한 비밀번호와 RowMapper에 의해서 VO에 매핑된 비밀번호를 복호화 한 후 비교함
				adminMemberVos.clear();
				//비밀번호 틀렸을 경우 조회된 데이터 삭제함
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
		//adminMemberVos의 길이가 1보다 긴 로그인 인증이 성공한 경우라면 조회된 관리자 정보를 서비스에 반환함. 그렇지 않을 경우 null 반환
	}

//	com.office.library.config의 BeanPropertyRowMapper로 아래와 같은 방법으로도 코딩 가능
//	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
//		System.out.println("[AdminMemberDao] selectAdmin()");
//		
//		String sql =  "SELECT * FROM tbl_admin_member "
//					+ "WHERE a_m_id = ? AND a_m_approval > 0";
//	
//		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
//		
//		try {
//			//BeanPropertyRowMapper의 static 메서드인 newInstance 이용해 RowMapper 구현 객체를 쉽게 구하고 이것을 jdbcTemplate.query()에 전달함.
//			RowMapper<AdminMemberVo> rowMapper = BeanPropertyRowMapper.newInstance(AdminMemberVo.class);
//			adminMemberVos = jdbcTemplate.query(sql, rowMapper, adminMemberVo.getA_m_id());
//			
//			if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(), adminMemberVos.get(0).getA_m_pw()))
//				adminMemberVos.clear();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}
//		
//		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
//	
//	}
	
	
	//tbl_admin_member 테이블에서 모든 관리자를 조회(select)하고 RowMapper를 이용해서 List<AdminMemberVo> adminMemberVos 리스트에 저장하는 메서드
	public List<AdminMemberVo> selectAdmins(){
		System.out.println("[AdminMemberDao] selectAdmins()");
		String sql =  "SELECT * FROM tbl_admin_member";
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException{
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos;
	}
	
	public int updateAdminAccount(int a_m_no) {
		System.out.println("[AdminMemberDao] updateAdminAccount()");
		
		String sql = "UPDATE tbl_admin_member SET a_m_approval = 1 WHERE a_m_no = ?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, a_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int updateAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] updateAdminAccount()");
		
		String sql = "UPDATE tbl_admin_member SET a_m_name = ?, a_m_gender = ?, a_m_part = ?, a_m_position = ?,"
				+ "a_m_mail = ?, a_m_phone = ?, a_m_mod_date = NOW() WHERE a_m_no = ?";
		//수정된 날짜와 시간을 기록하기 위해 a_m_mod_date에 시스템의 날짜와 시간을 구하는 NOW() 이용함.
		
		int result = -1;
		try {
			result = jdbcTemplate.update(sql, adminMemberVo.getA_m_name(), adminMemberVo.getA_m_gender(), adminMemberVo.getA_m_part(),
					adminMemberVo.getA_m_position(), adminMemberVo.getA_m_mail(), adminMemberVo.getA_m_phone(), adminMemberVo.getA_m_no());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result; //결과는 실제 데이터 베이스에 업데이트 된 행의 개수로, 성공적으로 업데이트 되면 1, 실패하면 0 반환 
	}
	
	//데이터베이스에 입력된 가입 정보 있는지 확인하기
	public AdminMemberVo selectAdmin(int a_m_no) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql =  "SELECT * FROM tbl_admin_member WHERE a_m_no = ?";
	
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		
		try{
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					//조회된 관리자 정보를 RowMapper의 mapRow()를 이용해 adminMemberVo에 매핑. 이때 추가되는 adminMemberVo 개수는 0 or 1
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			}, a_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
		//관리자 정보 조회에 성공하여 size가 1일 경우 adminMemberVo를 서비스에 반환. 정보 조회 실패시 null 반환
	}
	
	//관리자 인증
	public AdminMemberVo selectAdmin(String a_m_id, String a_m_name, String a_m_mail) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql = "SELECT * FROM tbl_admin_member WHERE a_m_id =? AND a_m_name = ? AND a_m_mail = ?";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		
		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException{ 
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			}, a_m_id, a_m_name, a_m_mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
	}
	
	//새 비밀번호로 업데이트
	public int updatePassword(String a_m_id, String newPassword) {
		System.out.println("[AdminMemberDao] updatePassword()");
		
		String sql = "UPDATE tbl_admin_member SET a_m_pw =?, a_m_mod_date = NOW() WHERE a_m_id = ?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), a_m_id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}