<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${model != null && model.questions != null && model.questions.size() > 0}">

    <h3>
        <small class="text-muted">Results for:</small>
        '<c:out value="${model.searchString}"/>'
        <small class="ml-3 text-muted">Total pages:</small>
        '<c:out value="${model.totalPages}"/>'
    </h3>

    <jsp:include page="navigation.jsp"/>

    <table class="table table-sm">
        <thead>
        <tr>
            <th scope="col">Date</th>
            <th scope="col">Title</th>
            <th scope="col">Author</th>
            <th scope="col">Link</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${model.questions}" var="item">

            <jsp:useBean id="dateObject" class="java.util.Date"/>
            <jsp:setProperty name="dateObject" property="time" value="${item.creationDate*1000}"/>

            <tr class="${item.isAnswered ? "alert alert-success":""}">
                <td><fmt:formatDate value="${dateObject }" pattern="dd/MM/yyyy"/></td>
                <td>"${item.title}"</td>
                <td><a href="${item.owner.link}">"${item.owner.displayName}"</a></td>
                <td><a href="${item.link}">follow</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${model != null && model.questions != null &&  model.questions.size() == 0}">
    <div class="alert alert-danger mt-5" role="alert">
        Sorry, Nothing Found!
    </div>
</c:if>