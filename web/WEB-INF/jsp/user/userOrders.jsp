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
    <fmt:message bundle="${locale}" key="locale.user.role.admin" var="adminRole"/>
    <fmt:message bundle="${locale}" key="locale.user.role.user" var="userRole"/>
    <fmt:message bundle="${locale}" key="locale.user.role.banned" var="bannedRole"/>
    <fmt:message bundle="${locale}" key="locale.common.button.cancel" var="cancelBtn"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${empty sessionScope.user || sessionScope.user.status== 'banned'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<div>
    <table>
        <tr>
            <th>id</th>
            <th>time</th>
            <th>status</th>
            <th>services</th>
            <th>price</th>
        </tr>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <form name="orderForm" method="POST" action="app">
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <td>${order.id}</td>
                    <td>${order.dateTime}</td>
                    <td>${order.status}</td>
                    <td><c:forEach var="activity" items="${order.activityList}">
                        <a href="app?command=viewActivity&activityId=${activity.id}">${activity.name}</a>
                        <br/>
                    </c:forEach>
                    </td>
                    <td>${order.price}</td>
                    <c:if test="${order.status ne 'cancelled' && order.status ne 'finished'}">
                    <td>
                        <input type="hidden" name="command" value="cancelOrder"/>
                        <input type="submit" value="${cancelBtn}">
                    </td>
                    </c:if>


                </form>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
