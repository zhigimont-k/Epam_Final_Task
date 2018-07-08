<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:message bundle="${locale}" key="locale.page.title.main" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.info.text.welcome" var="welcome"/>


    <title>${pageTitle} | Cat Beauty Bar</title>
</head>
<body>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="/jsp/register.jsp"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/header.jsp"/>
${welcome}
</body>
</html>
