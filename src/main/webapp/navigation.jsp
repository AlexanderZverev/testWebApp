<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${model.totalPages > 1}">

    <c:set var="currentPage" scope="page" value="${model.page}"/>
    <c:set var="totalPages" scope="page" value="${model.totalPages}"/>

    <%--Pagination related calculations begin--%>
    <c:set var="numberOfPageLinks" scope="page" value="${Math.min(Integer.valueOf(7), totalPages)}"/>
    <c:set var="pageNumberCorrection" scope="page" value="${numberOfPageLinks - 1}"/>

    <c:set var="safeLowerBound" scope="page" value="${Math.max(currentPage - Math.floor(numberOfPageLinks/2), 1.0)}"/>
    <c:set var="lowerBound" scope="page" value="${Math.max(totalPages - pageNumberCorrection, 1)}"/>

    <c:set var="paginationStartNumber" scope="page"
           value="${Math.min(safeLowerBound, Double.parseDouble(lowerBound))}"/>

    <c:set var="paginationLastNumber" scope="page"
           value="${Math.min(paginationStartNumber + pageNumberCorrection, Double.parseDouble(totalPages))}"/>
    <%--end--%>

    <c:url value="/questions" var="baseUrl">
        <c:param name="searchString" value="${model.searchString}"/>
        <c:if test="${model.inDescendingOrder}">
            <c:param name="orderDescending" value="on"/>
        </c:if>
    </c:url>

    <nav class="row ml-0 mr-0">
        <ul class="pagination col-10">
            <li class="page-item ${currentPage == 1 ? "disabled" : ""}">
                <a class="page-link" href=${baseUrl}&page=1>first</a>
            </li>

            <li class="page-item ${currentPage == 1 ? "disabled" : ""}">
                <a class="page-link" href=${baseUrl}&page=${currentPage - 1} aria-label="previous">&laquo;</a>
            </li>

            <c:forEach begin="${paginationStartNumber}" end="${paginationLastNumber}" varStatus="loop">
                <li class="page-item ${loop.index == currentPage ? "active" : ""}">
                    <a class="page-link"
                            <c:if test="${loop.index != currentPage}">
                                href=${baseUrl}&page=${loop.index}
                            </c:if>
                    >${loop.index}</a>
                </li>
            </c:forEach>

            <li class="page-item ${currentPage == totalPages ? "disabled" : "" }">
                <a class="page-link" href=${baseUrl}&page=${currentPage + 1} aria-label="next">&raquo;</a>
            </li>

            <li class="page-item ${currentPage == totalPages ? "disabled" : ""}">
                <a class="page-link" href=${baseUrl}&page=${totalPages}>last</a>
            </li>
        </ul>

        <div class="input-group mb-3 col-2 pr-0">
            <input id="page-input" type="text" class="form-control" placeholder="to page" aria-label="Enter page number"
                   aria-describedby="basic-addon2">
            <div class="input-group-append">
                <button id="go-to-page-btn" class="btn btn-primary" type="button">Go</button>
            </div>
        </div>
    </nav>

</c:if>

<script>
    let goToPageBtn = document.getElementById("go-to-page-btn");
    let pageInput = document.getElementById("page-input");

    if(goToPageBtn != null){

        goToPageBtn.addEventListener("click", () => {
            let pageNumber = Number(pageInput.value);

            if (pageNumber
                && pageNumber > 0
                && pageNumber <= ${model.totalPages}
                && pageNumber != ${model.page}) {

                window.location.href = "${baseUrl}" + "&page=" + pageNumber;

            } else {
                pageInput.value = "";
            }
        });
    }

    if(pageInput != null){
        pageInput.addEventListener("keyup", (e) => {
            e.preventDefault();

            if (e.key === "Enter") {
                goToPageBtn.click();
            }
        });
    }

</script>