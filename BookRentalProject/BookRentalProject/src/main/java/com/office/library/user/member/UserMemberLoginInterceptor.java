package com.office.library.user.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 * HandlerInterceptor �������̽��� ���� �������� �ʰ� HandlerInterceptor�� ������ HandlerInterceptorAdapter Ŭ������ ����ؼ� ����ϰ� �Ǹ�
 * HandlerInterceptor �������̽��� ��� �޼��带 ������ �ʿ� ���� �ʿ��� �޼��常 ������ �ؼ� ��� ����  
 */

public class UserMemberLoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//preHandler �Լ� ������. 3��° �Ű������� Handler(��Ʈ�ѷ�)�� ����
		
		HttpSession session = request.getSession(false);
		
		if (session != null) { //������ ȸ�� ������ null�� �ƴϸ� true�� ��ȯ�ؼ� Handler(��Ʈ�ѷ�) ����
			Object object = session.getAttribute("loginedUserMemberVo");
			
			if (object != null)
				return true;
		}
		//������ ȸ�� ������ null�� ��� ȸ�� ������ �� �� �����̹Ƿ� �α��� ȭ������ ����
		response.sendRedirect(request.getContextPath() + "/user/member/loginForm");
		return false;
	}
}
