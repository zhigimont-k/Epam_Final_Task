<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${locale}">
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>
    <fmt:message bundle="${locale}" key="locale.page.title.home" var="pageTitle"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.full" var="projectNameFull"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/login"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<jsp:include page="/WEB-INF/jsp/page_structure/welcomePanel.jsp"/>

<div id="header">
    <div id="header-title">${projectNameFull}</div>
    <div id="header-text">
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
