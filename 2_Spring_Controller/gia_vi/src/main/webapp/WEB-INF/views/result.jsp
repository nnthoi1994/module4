<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kết quả lựa chọn</title>
</head>
<body>
<h2>Các gia vị bạn đã chọn:</h2>

<c:choose>
    <c:when test="${selectedCondiments != null}">
        <ul>
            <c:forEach var="item" items="${selectedCondiments}">
                <li>${item}</li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>Bạn chưa chọn gia vị nào.</p>
    </c:otherwise>
</c:choose>

<a href="/home">Quay lại</a>
</body>
</html>