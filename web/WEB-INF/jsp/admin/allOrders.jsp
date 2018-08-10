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
    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
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
            <th>time</th>
            <th>status</th>
            <th>services</th>
            <th>price</th>
            <th>change status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <form name="orderForm" method="POST" action="app">
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <td>${order.id}</td>
                    <td><fmt:formatDate value="${order.dateTime}" type="both"
                                        dateStyle="short" timeStyle="short"/></td>
                    <td>${order.status}</td>
                    <td><c:forEach var="activity" items="${order.activityList}">
                        <a href="app?command=viewActivity&activityId=${activity.id}">${activity.name}</a>
                        <br/>
                    </c:forEach>
                    </td>
                    <td>${order.price}</td>
                    <td>
                        <input type="hidden" name="command" value="changeOrderStatus"/>
                        <select name="orderStatus">
                            <option value="pending">pending</option>
                            <option value="cancelled">cancelled</option>
                            <option value="confirmed">confirmed</option>
                            <option value="finished">finished</option>
                        </select>
                        <input type="submit" value="Submit">
                    </td>
                </form>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<ul class="pagination">
    <c:if test="${currentPage != 1}">
        <li><a href="app?command=vieAllOrders&pageNumber=${currentPage - 1}">&laquo;</a></li>
    </c:if>
    <c:forEach begin="1" end="${numberOfPages}" var="i">
        <c:choose>
            <c:when test="${currentPage eq i}">
                <li class="active"><a href="#">${i}</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="app?command=viewAllOrders&pageNumber=${i}">${i}</a></li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${currentPage lt numberOfPages}">
        <li><a href="app?command=viewAllOrders&pageNumber=${currentPage + 1}">&raquo;</a></li>
    </c:if>
</ul>

<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
