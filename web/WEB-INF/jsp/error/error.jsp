<%@ page isErrorPage="true" isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.error.text" var="errorText"/>
    <fmt:message bundle="${locale}" key="locale.page.title.error" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div>
    <img src="${pageContext.request.contextPath}/images/cat500.png" alt="" height="150"><h2>Error 500</h2>
    Something went awfully wrong, oops.<br/>
    Here's the report:<br/>
    Request from ${pageContext.errorData.requestURI} failed
    <br/>
    Servlet name: ${pageContext.errorData.servletName}
    <br/>
    Status code: ${pageContext.errorData.statusCode}
    <br/>
    Exception: ${pageContext.exception}
    <br/>
    Message from exception: ${pageContext.exception.message}
    <br/>
    <a href="${pageContext.request.contextPath}/home">What in the world is this? I'm going home</a>
</div>
</body>
</html>
