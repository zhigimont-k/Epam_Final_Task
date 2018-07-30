<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:message bundle="${locale}" key="locale.page.title.home" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.info.text.welcome" var="welcome"/>


    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/login"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<jsp:include page="/WEB-INF/jsp/page_structure/welcomePanel.jsp"/>
</body>
</html>
