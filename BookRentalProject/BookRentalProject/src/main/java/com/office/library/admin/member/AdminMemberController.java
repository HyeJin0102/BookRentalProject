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
	
	//의존 객체 자동 주입을 통해 AdminMemberService를 사용할 수 있어짐
	@Autowired
	AdminMemberService adminMemberService;
	
	//회원가입 양식이 있는 페이지를 응답해주는 코드
	@GetMapping("/createAccountForm") //@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)을 줄여서 사용가능함
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		//servlet-context.xml에 정의된 InternalResourceViewResolver에 의해 create_account_form.jsp를 클라이언트에 반환함
		
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
	
	//login_form.jsp 파일을 이용해서 뷰를 만들고 클라이언트에 응답하는 DAO사용하지 않는 단순한 구조
	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	//로그인 확인
	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
	//매개변수로 주어지는 AdminMemberVo에는 관리자가 입력한 id, pw 정보 저장되어 있음.
	//session에는 세션 정보가 들어 있고, 여기에 로그인에 성공한 관리자 정보(loginedAdminMemberVo)를 저장함
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		//관리자 로그인 인증 업무를 지시하기 위해 매개변수로 받은 AdminMemberVo를 전달하여 loginConfirm 호출함
		
		if (loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		} else {
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo); //setAttribute(String name(저장할 데이터 이름), Object value(실제 데이터))
			session.setMaxInactiveInterval(60*30); //세션 유효기간 설정(60초 * 30)_30분동안 브라우저가 아무런 동작 안 하면 서버 세션 종료
		}
		
		return nextPage;
	}
	
	//로그아웃 확인
	@GetMapping("/logoutConfirm")
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		String nextPage = "redirect:/admin";
		session.invalidate(); //세션 무효화. 세션에 저장된 데이터(loginedAdminMemberVo)사용 불가.
		return nextPage;
	}
	
	//관리자 목록
	@GetMapping("/listupAdmin")
	public ModelAndView listupAdmin() {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		String nextPage = "admin/member/listup_admins";
		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		
		ModelAndView modelAndView = new ModelAndView(); //ModelAndView 객체 생성
		modelAndView.setViewName(nextPage); //ModelAndView에 뷰 설정
		modelAndView.addObject("adminMemberVos", adminMemberVos); //ModelAndView에 데이터 추가
		
		return modelAndView;
	}
	
	//관리자 승인
	@GetMapping("/setAdminApproval")
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		System.out.println("[AdminMemberController] setAdminApproval()");
		
		String nextPage = "redirect:/admin/member/listupAdmin";
		
		adminMemberService.setAdminApproval(a_m_no);
		
		return nextPage;
	}
	
	//회원 정보 수정
	@GetMapping("modifyAccountForm")
	public String modifyAccountForm(HttpSession session) { //session은 관리자 정보 수정을 위해 현재 관리자가 로그인되어 있는지 확인하는 용도로 사용
		System.out.println("[AdminMemberController] modifyAccountForm()");
		
		String nextPage = "admin/member/modify_account_form";
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
		if(loginedAdminMemberVo == null) //만일 세선에 저장된 관리자 정보가 null이라면 로그인 전으로 판단해서 로그인 화면으로 리다이렉트함.
			//도메인을 통한 직접 요청 방지 및 로그인 후 자리 비워 세션 만료된 상황에서 계정 보호
			nextPage = "redirect:/admin/member/loginForm";
		
		return nextPage;
	}
	
	//회원 정보 수정 확인
	@PostMapping("/modifyAccountConfirm")
	public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) { 
		//session에는 수정된 관리자 정보가 저장된 AdminMemberVo와 세션 관리하는 HttpSession을 매개변수로 받음
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "admin/member/modify_account_ok";
		
		int result = adminMemberService.modifyAccountConfirm(adminMemberVo);
		//데이터베이스에 저장된 관리자 정보를 수정하기 위해 AdminMemberService의 modifyAccountConfirm에 adminMemberVo를 전달하며 호출
		
		if(result > 0) { //정보 변경에 성공한 경우 세션 내 관리자 정보 변경
			AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());
			
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60*30);
		} else {
			nextPage = "admin/member/medify_account_ng";
		}
		
		return nextPage;
	}
	
	//비밀번호 찾기
	@GetMapping("/findPasswordForm")
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordform()");
		
		String nextPage = "admin/member/find_password_form";
		
		return nextPage;
	}
	
	//비밀번호 찾기 확인
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