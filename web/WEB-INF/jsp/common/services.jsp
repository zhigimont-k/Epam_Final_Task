<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
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

    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<div class="container">
    <div class="row col-xs-offset-1">
        <%--<div class="panel-group">--%>
            <c:forEach var="activity" items="${activityList}">
                <div class="col-md-3">
                    <c:choose>
                        <c:when test="${activity.status == 'available'}">
                            <div class="panel panel-success">
                                <div class="panel-heading">
                                    <a href="app?command=viewActivity&activityId=${activity.id}">
                                            ${activity.name}
                                    </a>
                                    <p class="text-muted">${activity.status}</p>
                                </div>
                                <div class="panel-body">
                                        ${activity.description}
                                    <h4>${activity.price}</h4>
                                </div>
                                <c:if test="${sessionScope.user.status == 'admin'}">
                                    <form name="activityListForm" method="POST" action="app">
                                        <input type="hidden" name="command" value="editActivity"/>
                                        <input type="hidden" name="activityId" value="${activity.id}"/>
                                        <div class="panel-footer">
                                            <input type="submit" class="btn btn-link" value="edit"/>
                                        </div>
                                    </form>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <a href="app?command=viewActivity&activityId=${activity.id}">
                                            ${activity.name}
                                    </a>
                                    <p class="text-muted">${activity.status}</p>
                                </div>
                                <div class="panel-body">
                                        ${activity.description}
                                    <h4>${activity.price}</h4>
                                </div>
                                <c:if test="${sessionScope.user.status == 'admin'}">
                                    <form name="activityListForm" method="POST" action="app">
                                        <input type="hidden" name="command" value="editActivity"/>
                                        <input type="hidden" name="activityId" value="${activity.id}"/>
                                        <div class="panel-footer">
                                            <input type="submit" class="btn btn-link" value="edit"/>
                                        </div>
                                    </form>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        <%--</div>--%>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
