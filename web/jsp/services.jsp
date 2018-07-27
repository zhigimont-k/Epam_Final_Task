<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div>
    <table>
        <c:forEach var="activity" items="${activityList}">
            <tr>
                <form name="activityListForm" method="POST" action="app">
                    <input type="hidden" name="command" value="addActivityToOrder"/>
                    <input type="hidden" name="activityId" value="${activity.id}"/>
                    <td><a href="app?command=viewActivity&activityId=${activity.id}">${activity.name}</a></td>
                    <td>${activity.description}</td>
                    <td>${activity.price}</td>
                    <td>${activity.status}</td>
                    <c:if test="${sessionScope.user.status == 'admin'}">
                    <td><a href="app?command=editActivity&activityId=${activity.id}">edit</a></td>
                    </c:if>
                </form>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
