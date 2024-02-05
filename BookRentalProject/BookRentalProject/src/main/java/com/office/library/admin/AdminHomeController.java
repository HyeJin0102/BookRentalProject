package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller //AdminHomeController�� ������ IoC �����̳ʿ� �� ��ü�� ����
@RequestMapping("/admin") // "/admin" ����� ��û�� AdminHomeController���� �ذ��ϵ��� ��.
// ���� @RequestMapping�� ������� ������ Ŭ���̾�Ʈ�� ��� ��û�� ó���� �� �ִ� ��Ʈ�ѷ� ��
public class AdminHomeController {
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	// value = {"", "/"}�� ���� /admin�� /admin/ ��û�� ��� home()�� ó���� �� �ְ� ��.
	// method�� ���� ��� �⺻ ����Ʈ�� GET
	public String home() {
		System.out.println("[AdminHomeController] home()");
		
		String nextPage = "admin/home";
		
		return nextPage;
	}
}
