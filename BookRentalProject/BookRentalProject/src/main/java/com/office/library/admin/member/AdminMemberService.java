package com.office.library.admin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberService {
	
	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0; //아이디 중복
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1; //insert 성공
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1; //insert 실패12
	
	//서비스 객체는 데이터베이스와 통신하기 위해 DAO를 사용하므로 의존 객체 자동 주입 해줌.
	@Autowired
	AdminMemberDao adminMemberDao;
	
	//회원 가입 처리
	public int createAccountCnfirm(AdminMemberVo adminMemberVo) {
		
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());
		
		if(!isMember) {
			int result = adminMemberDao.insertAdminAccount(adminMemberVo);
			
			if (result > 0)
				return ADMIN_ACCOUNT_CREATE_SUCCESS;
			else
				return ADMIN_ACCOUNT_CREATE_FAIL;
		} else {
			return ADMIN_ACCOUNT_ALREADY_EXIST;
		}
	}
	
	//로그인 인증 처리를 위해 관리자가 입력한 정보(id, pw)가 데이터베이스에 있는지 확인하는 메서드
	public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) { 
		
		System.out.println("[AdminMemberService] loginConfirm()");
		
		AdminMemberVo loginedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);
		
		if(loginedAdminMemberVo != null) {
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN SUCCESS!!");
		} else {
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN FAIL!!");
		}
		
		return loginedAdminMemberVo;
	}
}
