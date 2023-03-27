<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
response.sendRedirect(request.getContextPath() + "/home/index");
%>
<%-- 
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	当前在线用户人数:${onlineNum}<br />
	所有访问者的信息列表
	<br />
	<c:if test="${ empty  onlineUserlist}">  
        当前不存在访问者  
    </c:if>
	<c:if test="${ not empty  onlineUserlist}">
		<c:forEach var="user" items="${onlineUserlist}">  
    sessionId:${user.sessionId}    <br />
    ip:${user.ip}    <br />
    loginTime :${user.firstTime}    
    <hr />
		</c:forEach>
	</c:if>
</body>
</html> --%>