<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.user.label.login" var="login"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userName"/>
    <fmt:message bundle="${locale}" key="locale.user.label.status" var="status"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="email"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumber"/>
    <fmt:message bundle="${locale}" key="locale.user.role.admin" var="adminRole"/>
    <fmt:message bundle="${locale}" key="locale.user.role.user" var="userRole"/>
    <fmt:message bundle="${locale}" key="locale.user.role.banned" var="bannedRole"/>

    <title>${pageTitle} | Cat Beauty Bar</title>
</head>
<body>
<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>


<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>id</th>
            <th>${login}</th>
            <th>${userName}</th>
            <th>${status}</th>
            <th>${email}</th>
            <th>${phoneNumber}</th>
            <th>Change status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${userList}">
            <tr>
                <form name="loginForm" method="POST" action="app">
                    <input type="hidden" name="command" value="changeUserStatus"/>
                    <input type="hidden" name="userId" value="${user.id}"/>
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.userName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.status == 'admin'}">
                                ${adminRole}
                            </c:when>
                            <c:when test="${user.status == 'banned'}">
                                ${bannedRole}
                            </c:when>
                            <c:otherwise>
                                ${userRole}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${user.email}</td>
                    <td>${user.phoneNumber}</td>
                    <td>
                        <select name="userStatus">
                            <option value="user">${userRole}</option>
                            <option value="admin">${adminRole}</option>
                            <option value="banned">${bannedRole}</option>
                        </select>
                        <input type="submit" value="Submit"></td>
                </form>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
