<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.service.label.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.common.button.add" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.login" var="loginWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.email" var="emailWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.phonenumber" var="phoneNumberWarning"/>

</head>
<body>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test = "${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home" />
</c:if>
<form name="addServiceForm" method="POST" action="app">
    <input type="hidden" name="command" value="addService"/>
    <label>${serviceNameLabel}
        <input type="text" name="serviceName" maxlength="20" minlength="4" required/></label>

    <br/>
    <label>${serviceDescriptionLabel}
    <textarea name="serviceDescription" cols="30" rows="10" required></textarea></label>
    <br/>
    <label>${servicePriceLabel}
        <input type="text" name="email" maxlength="5" minlength="1" required/></label>

    <br/>
    <label>Image
    <input type="file" name="image"/></label>

    <br/>
    <input type="submit" value="${button}"/>
</form>
</body>
</html>
