package com.office.library.admin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	//의존 객체 자동 주입을 통해 AdminMemberService를 사용할 수 있어짐
	@Autowired
	AdminMemberService adminMemberService;
	
	//회원가입으로 보내주는 코드
	@GetMapping("/createAccountForm") //@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)을 줄여서 사용가능함
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		
		return nextPage;
	}
	
	//회원가입 확인
	@PostMapping("/createAccountConfirm")
	public String createAccountConfirm(AdminMemberVo adminmemberVo) { 
		//관리자가 입력한 정보를 adminmemberVo 객체에 담아서 createAccountConfirm -> AdminMemberController -> AdminMemberService까지 넘겨주기 위함
		System.out.println("[AdminMemberController] createAccountConfirm()");
		
		String nextPage = "admin/member/create_account_ok";
		int result = adminMemberService.createAccountCnfirm(adminmemberVo);
		
		//관리자 회원가입 업무가 정상 처리 되지 않았을 경우
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
	public String loginConfirm(AdminMemberVo adminMemberVo) { //매개변수로 주어지는 AdminMemberVo에는 관리자가 입력한 id, pw 정보 저장되어 있음.
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		//관리자 로그인 인증 업무를 지시하기 위해 매개변수로 받은 AdminMemberVo를 전달하여 loginConfirm 호출함
		
		if (loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		}
		
		return nextPage;
	}
}