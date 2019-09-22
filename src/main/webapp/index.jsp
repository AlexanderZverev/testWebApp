<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <title>Test Web App</title>
</head>
<body>
<div class="container">
    <form class="pb-3" method="get" action="questions">
        <p class="h4 mb-4 text-center">Search Questions on StackOverflow</p>

        <div class="form-group">
            <label for="searchString">Enter Search String</label>
            <input class="form-control" type="text" id="searchString" name="searchString"
                   pattern=".*(?=\S).+" required="required">
        </div>

        <div class="form-check float-right">
            <input type="checkbox" class="form-check-input" id="orderDescending" name="orderDescending"
            ${model != null && model.inDescendingOrder ? "checked": ""}>
            <label class="form-check-label" for="orderDescending">Newest first</label>
            <button type="submit" class="ml-3 btn btn-primary">Submit</button>
        </div>
    </form>

    <jsp:include page="queryResult.jsp"/>

</div>
</body>
</html>
