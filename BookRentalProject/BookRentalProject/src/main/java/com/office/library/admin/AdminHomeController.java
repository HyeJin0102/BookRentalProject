package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller //AdminHomeController를 스프링 IoC 컨테이너에 빈 객체로 생성
@RequestMapping("/admin") // "/admin" 경로의 요청을 AdminHomeController에서 해결하도록 함.
// 만일 @RequestMapping를 명시하지 않으면 클라이언트의 모든 요청을 처리할 수 있는 컨트롤러 됨
public class AdminHomeController {
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	// value = {"", "/"}를 통해 /admin과 /admin/ 요청을 모두 home()이 처리할 수 있게 됨.
	// method가 없을 경우 기본 디폴트는 GET
	public String home() {
		System.out.println("[AdminHomeController] home()");
		
		String nextPage = "admin/home";
		
		return nextPage;
	}
}
