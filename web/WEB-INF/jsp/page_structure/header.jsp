<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.button.logout" var="logout"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/page_structure/navigation.jsp"/>
<jsp:include page="/WEB-INF/jsp/page_structure/chooseLang.jsp"/>
</body>
</html>
