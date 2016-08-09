<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>ATM another sum</title>
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/resources/css/atmstyle.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-sm-6 head-block">
				<h2 class="colh2">ATM <em>Reliable Bank</em></h2>
			</div>
			<div class="col-md-12 col-sm-6 main-block">
				<c:set var="resultChoose" value="${resultChoose}" />
				
				<c:if test="${resultChoose==true}">
					<h3>${message}</h3>
					<ul>
						<c:forEach var="banknote" items="${banknoteStorage.banknotes}">
							<c:if test="${banknote.count!=0}">
								<li><label>banknote ${banknote.value} count=${banknote.count}</label></li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
				<form action="anothersum" method="post">
					<h2>Do you want to continue operation with a sum of ${resultSum}?</h2><br> 
					<input type="submit" name="submitted" value="Confirm">
					<input type="submit" name="submitted" value="Cancel">
				</form>
			</div>
			<div class="col-md-12 col-sm-6 footer-block">
				<p>If you have any questions you can always contact customer support. Telephone number: <strong>+1 10 11781600</strong></p>
				<p>With best wishes, your <strong><em>"Reliable Bank"</em></strong>
			</div>
		</div>
	</div>
</body>
</html>