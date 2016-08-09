<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>ATM get cash</title>
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
				<h4><a href="/atm/selection">Back to choose operations</a></h4>
				<h3>Operation - get cash</h3>
				<form action="getcash" method="post">
					<h4>Enter the amount of each denomination banknotes which you want receive. Banknote that you don't want, you have to specify 0 in the field.</h4>
					<table width="50%" cellspacing="0" cellpadding="2">
						<tr>
							<td align="right"><label>500=</label></td>
							<td><input type="text" name="0" maxlength="10" size="10"><td>
						</tr>
						<tr>
							<td align="right"><label>200=</label></td>
							<td><input type="text" name="1" maxlength="10" size="10"></td>
						</tr>
						<tr>
							<td align="right"><label>100=</label></td>
							<td><input type="text" name="2" maxlength="10" size="10"></td>
						</tr>
						<tr>
							<td align="right"><label>50=</label></td>
							<td><input type="text" name="3" maxlength="10" size="10"></td>
						</tr>
						<tr>
							<td align="right"><label>20=</label></td>
							<td><input type="text" name="4" maxlength="10" size="10"></td>
						</tr>
						<tr>
							<td></td>
							<td><input class="styleB" type="submit" value="Get"></td>
						</tr>
					</table>
				</form>
				<c:set var="resultChoose" value="${resultChoose}" />
				<c:if test="${resultChoose==true}">
					<h4>${message}</h4>
				</c:if>
			</div>				
			<div class="col-md-12 col-sm-6 footer-block">
				<p>If you have any questions you can always contact customer support. Telephone number: <strong>+1 10 11781600</strong></p>
				<p>With best wishes, your <strong><em>"Reliable Bank"</em></strong>
			</div>	
		</div>	
	</div>	
</body>
</html>