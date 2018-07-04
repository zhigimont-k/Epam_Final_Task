<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.error.text" var="errorText"/>
    <fmt:message bundle="${locale}" key="locale.page.title.error" var="pageTitle"/>

    <title>${pageTitle}</title>
</head>
<body>
<div>
    ${errorText}
    <br/>
    ${errorMessage}
</div>
</body>
</html>
