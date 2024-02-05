package com.office.library.admin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	//���� ��ü �ڵ� ������ ���� AdminMemberService�� ����� �� �־���
	@Autowired
	AdminMemberService adminMemberService;
	
	//ȸ���������� �����ִ� �ڵ�
	@GetMapping("/createAccountForm") //@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)�� �ٿ��� ��밡����
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		
		return nextPage;
	}
	
	//ȸ������ Ȯ��
	@PostMapping("/createAccountConfirm")
	public String createAccountConfirm(AdminMemberVo adminmemberVo) { 
		//�����ڰ� �Է��� ������ adminmemberVo ��ü�� ��Ƽ� createAccountConfirm -> AdminMemberController -> AdminMemberService���� �Ѱ��ֱ� ����
		System.out.println("[AdminMemberController] createAccountConfirm()");
		
		String nextPage = "admin/member/create_account_ok";
		int result = adminMemberService.createAccountCnfirm(adminmemberVo);
		
		//������ ȸ������ ������ ���� ó�� ���� �ʾ��� ���
		if(result <= 0) {
			nextPage = "admin/member/create-account-ng";
		}
		
		return nextPage;
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo) { //�Ű������� �־����� AdminMemberVo���� �����ڰ� �Է��� id, pw ���� ����Ǿ� ����.
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		//������ �α��� ���� ������ �����ϱ� ���� �Ű������� ���� AdminMemberVo�� �����Ͽ� loginConfirm ȣ����
		
		if (loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		}
		
		return nextPage;
	}
}