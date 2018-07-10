<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="login"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/chooseLang.jsp"/>
<c:if test="${empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/register">${signUp}</a>
    <a href="${pageContext.request.contextPath}/login">${login}</a>
</c:if>
<br/>
<c:if test="${sessionScope.user ne null}">
    <jsp:include page="/WEB-INF/jsp/welcomePanel.jsp"/>
</c:if>
<c:if test="${sessionScope.user ne null}">
    <jsp:include page="/WEB-INF/jsp/logout.jsp"/>
</c:if>
</body>
</html>
