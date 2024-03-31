<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<jsp:include page="../include/title.jsp" />

<link href="<c:url value='/resources/css/admin/home.css' />" rel="stylesheet" type="text/css">

</head>
<body>
	
	<jsp:include page="../include/header.jsp" />
	
	<jsp:include page="./include/nav.jsp" /> <!-- nav.jsp는 관리자 화면의 전체적인 메뉴 -->
	
	<section>
		
		<div id="section_wrap">
		
			<div class="word">
			
				<h3>ADMIN HOME</h3>
				
			</div>
			
		</div>
		
	</section>
	
	<jsp:include page="../include/footer.jsp" />
	
</body>
</html>