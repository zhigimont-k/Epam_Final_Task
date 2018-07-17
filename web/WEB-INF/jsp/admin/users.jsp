<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.user.label.login" var="login"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userName"/>
    <fmt:message bundle="${locale}" key="locale.user.label.status" var="status"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="email"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumber"/>

    <title>${pageTitle} | Cat Beauty Bar</title>
</head>
<body>
<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<input type="hidden" name="page" value="${pageContext.request.requestURL}"/>
<input type="hidden" name="query" value="${pageContext.request.queryString}"/>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<div>
    <table>
        <tr>
            <th>${login}</th>
            <th>${userName}</th>
            <th>${status}</th>
            <th>${email}</th>
            <th>${phoneNumber}</th>
            <th>Change status</th>
        </tr>
        <c:forEach var="user" items="${userList}">
            <tr>
                <form name="loginForm" method="POST" action="app">
                    <input type="hidden" name="command" value="changeUserStatus"/>
                    <input type="hidden" name="login" value="${user.login}"/>
                    <td>${user.login}</td>
                    <td>${user.userName}</td>
                    <td>${user.status}</td>
                    <td>${user.email}</td>
                    <td>${user.phoneNumber}</td>
                    <td>
                        <select name="userStatus">
                        <option value="user">user</option>
                        <option value="admin">admin</option>
                        <option value="banned">banned</option>
                    </select>
                        <input type="submit" value="Submit"></td>
                </form>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
