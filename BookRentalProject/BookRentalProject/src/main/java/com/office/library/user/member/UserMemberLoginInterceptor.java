package com.office.library.user.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 * HandlerInterceptor 인터페이스를 직접 구현하지 않고 HandlerInterceptor를 구현한 HandlerInterceptorAdapter 클래스를 상속해서 사용하게 되면
 * HandlerInterceptor 인터페이스의 모든 메서드를 구현할 필요 없이 필요한 메서드만 재정의 해서 사용 가능  
 */

public class UserMemberLoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//preHandler 함수 재정의. 3번째 매개변수로 Handler(컨트롤러)를 받음
		
		HttpSession session = request.getSession(false);
		
		if (session != null) { //세션의 회원 정보가 null이 아니면 true를 반환해서 Handler(컨트롤러) 실행
			Object object = session.getAttribute("loginedUserMemberVo");
			
			if (object != null)
				return true;
		}
		//세션의 회원 정보가 null일 경우 회원 인증이 안 된 상태이므로 로그인 화면으로 유도
		response.sendRedirect(request.getContextPath() + "/user/member/loginForm");
		return false;
	}
}
