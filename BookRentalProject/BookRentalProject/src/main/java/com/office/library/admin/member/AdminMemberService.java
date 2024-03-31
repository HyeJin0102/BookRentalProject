package com.office.library.admin.member;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberService {
	
	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0; //아이디 중복
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1; //insert 성공
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1; //insert 실패12
	
	//서비스 객체는 데이터베이스와 통신하기 위해 DAO를 사용하므로 의존 객체 자동 주입 해줌.
	@Autowired
	AdminMemberDao adminMemberDao;
	
	@Autowired
	JavaMailSenderImpl javaMailSenderImpl;
	
	//회원 가입 처리
	public int createAccountCnfirm(AdminMemberVo adminMemberVo) {
		
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id()); //중복 아이디 확인. flase여야 사용 가능
		
		if(!isMember) {
			int result = adminMemberDao.insertAdminAccount(adminMemberVo); 
			//저장에 성공하면 1, 실패시 0 반환. 여기서 1과 0은 실제 데이터베이스에 추가된 행의 개수
			
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
	
	//DAO를 이용해 데이터베이스의 관리자 목록 요청
	public List<AdminMemberVo> listupAdmin() {
		System.out.println("[AdminMemberService] listupAdmin");
		
		return adminMemberDao.selectAdmins();
	}
	
	public void setAdminApproval(int a_m_no) {
		System.out.println("[AdminMembersService] setAdminApproval()");
		
		int result = adminMemberDao.updateAdminAccount(a_m_no);
	}
	
	public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] modifyAccountConfirm()");
		
		return adminMemberDao.updateAdminAccount(adminMemberVo);
	}
	
	public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
		System.out.println("[AdminMemberService] getLoginedAdminMemberVo");
		
		return adminMemberDao.selectAdmin(a_m_no);
	}
	
	public int findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] findPasswordConfirm()");
		
		AdminMemberVo selectedAdminMemberVo = 
				adminMemberDao.selectAdmin(adminMemberVo.getA_m_id(), adminMemberVo.getA_m_name(), adminMemberVo.getA_m_mail());
		
		int result = 0;
		
		if (selectedAdminMemberVo != null) {
			
			System.out.println("잇는거 확인?");
			String newPassword = createNewPassword();
			result = adminMemberDao.updatePassword(adminMemberVo.getA_m_id(), newPassword);

			if (result > 0)
				sendNewPasswordByMail(adminMemberVo.getA_m_mail(), newPassword);
		}
		return result;
	}
	
	//새로운 비밀번호 생성
	private String createNewPassword() {
		System.out.println("[AdminMemberService] createNewPassword()");
		
		char[] chars = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		
		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom(); //SecureRandom은 Random을 상속하며 Random보다 강력한 난수 생성. 
		secureRandom.setSeed(new Date().getTime());
		
		int index = 0;
		int length = chars.length;
		for(int i = 0; i < 8; i++) { //chars의 인덱스 범위에인 난수 8번 생성 후 index에서 저장 + chars 배열에서 index에 해당하는 문자 선택해 stringBuffer에 저장   
			index = secureRandom.nextInt(length);
			
			if(index % 2 == 0) //인덱스가 짝수라면 대문자로 변경 
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
		}
		
		System.out.println("[AdminMemberService] NEW PASSWORD: " + stringBuffer.toString());
		
		return stringBuffer.toString();
	}
	
	//메일 발송
	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		System.out.println("[AdminMemberService] sendNewPasswordByMail()");
		
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception { //MimeMessagePreparator를 구현한 익명 클래스 생성
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo("jjin9801@gmail.com"); //받는 메일 주소
				mimeMessageHelper.setSubject("[한국도서관] 새 비밀번호 안내입니다.");
				mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true);
			}
		};
		
		javaMailSenderImpl.send(mimeMessagePreparator);
	}
}
