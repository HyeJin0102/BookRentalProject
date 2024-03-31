package com.office.library.admin.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	//���� ��ü �ڵ� ������ ���� AdminMemberService�� ����� �� �־���
	@Autowired
	AdminMemberService adminMemberService;
	
	//ȸ������ ����� �ִ� �������� �������ִ� �ڵ�
	@GetMapping("/createAccountForm") //@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)�� �ٿ��� ��밡����
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		//servlet-context.xml�� ���ǵ� InternalResourceViewResolver�� ���� create_account_form.jsp�� Ŭ���̾�Ʈ�� ��ȯ��
		
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
	
	//login_form.jsp ������ �̿��ؼ� �並 ����� Ŭ���̾�Ʈ�� �����ϴ� DAO������� �ʴ� �ܼ��� ����
	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	//�α��� Ȯ��
	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
	//�Ű������� �־����� AdminMemberVo���� �����ڰ� �Է��� id, pw ���� ����Ǿ� ����.
	//session���� ���� ������ ��� �ְ�, ���⿡ �α��ο� ������ ������ ����(loginedAdminMemberVo)�� ������
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		//������ �α��� ���� ������ �����ϱ� ���� �Ű������� ���� AdminMemberVo�� �����Ͽ� loginConfirm ȣ����
		
		if (loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		} else {
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo); //setAttribute(String name(������ ������ �̸�), Object value(���� ������))
			session.setMaxInactiveInterval(60*30); //���� ��ȿ�Ⱓ ����(60�� * 30)_30�е��� �������� �ƹ��� ���� �� �ϸ� ���� ���� ����
		}
		
		return nextPage;
	}
	
	//�α׾ƿ� Ȯ��
	@GetMapping("/logoutConfirm")
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		String nextPage = "redirect:/admin";
		session.invalidate(); //���� ��ȿȭ. ���ǿ� ����� ������(loginedAdminMemberVo)��� �Ұ�.
		return nextPage;
	}
	
	//������ ���
	@GetMapping("/listupAdmin")
	public ModelAndView listupAdmin() {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		String nextPage = "admin/member/listup_admins";
		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		
		ModelAndView modelAndView = new ModelAndView(); //ModelAndView ��ü ����
		modelAndView.setViewName(nextPage); //ModelAndView�� �� ����
		modelAndView.addObject("adminMemberVos", adminMemberVos); //ModelAndView�� ������ �߰�
		
		return modelAndView;
	}
	
	//������ ����
	@GetMapping("/setAdminApproval")
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		System.out.println("[AdminMemberController] setAdminApproval()");
		
		String nextPage = "redirect:/admin/member/listupAdmin";
		
		adminMemberService.setAdminApproval(a_m_no);
		
		return nextPage;
	}
	
	//ȸ�� ���� ����
	@GetMapping("modifyAccountForm")
	public String modifyAccountForm(HttpSession session) { //session�� ������ ���� ������ ���� ���� �����ڰ� �α��εǾ� �ִ��� Ȯ���ϴ� �뵵�� ���
		System.out.println("[AdminMemberController] modifyAccountForm()");
		
		String nextPage = "admin/member/modify_account_form";
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
		if(loginedAdminMemberVo == null) //���� ������ ����� ������ ������ null�̶�� �α��� ������ �Ǵ��ؼ� �α��� ȭ������ �����̷�Ʈ��.
			//�������� ���� ���� ��û ���� �� �α��� �� �ڸ� ��� ���� ����� ��Ȳ���� ���� ��ȣ
			nextPage = "redirect:/admin/member/loginForm";
		
		return nextPage;
	}
	
	//ȸ�� ���� ���� Ȯ��
	@PostMapping("/modifyAccountConfirm")
	public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) { 
		//session���� ������ ������ ������ ����� AdminMemberVo�� ���� �����ϴ� HttpSession�� �Ű������� ����
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "admin/member/modify_account_ok";
		
		int result = adminMemberService.modifyAccountConfirm(adminMemberVo);
		//�����ͺ��̽��� ����� ������ ������ �����ϱ� ���� AdminMemberService�� modifyAccountConfirm�� adminMemberVo�� �����ϸ� ȣ��
		
		if(result > 0) { //���� ���濡 ������ ��� ���� �� ������ ���� ����
			AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());
			
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60*30);
		} else {
			nextPage = "admin/member/medify_account_ng";
		}
		
		return nextPage;
	}
	
	//��й�ȣ ã��
	@GetMapping("/findPasswordForm")
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordform()");
		
		String nextPage = "admin/member/find_password_form";
		
		return nextPage;
	}
	
	//��й�ȣ ã�� Ȯ��
	@PostMapping("/findPasswordConfirm")
	public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberController] findPasswordConfirm()");
		
		String nextPage = "admin/member/find_password_ok";
		
		int result = adminMemberService.findPasswordConfirm(adminMemberVo);
		
		if(result <= 0)
			nextPage = "admin/member/find_password_ng";
		
		return nextPage;
	}
}