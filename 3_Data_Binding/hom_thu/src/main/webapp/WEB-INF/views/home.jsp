<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Simple Calculator</title>
</head>
<body>

<h1>Calculator</h1>

<form action="calculate" method="post">
    <%-- Giữ lại giá trị cũ sau khi submit --%>
    <input type="number" name="firstNumber" value="${firstNumber}" required>
    <input type="number" name="secondNumber" value="${secondNumber}" required>
    <br><br>

    <%-- Các nút bấm, mỗi nút có name="operator" và value là phép toán tương ứng --%>
    <button type="submit" name="operator" value="Addition(+)">Addition(+)</button>
    <button type="submit" name="operator" value="Subtraction(-)">Subtraction(-)</button>
    <button type="submit" name="operator" value="Multiplication(X)">Multiplication(X)</button>
    <button type="submit" name="operator" value="Division(/)">Division(/)</button>
</form>

<br>

<%-- Chỉ hiển thị kết quả nếu biến 'result' tồn tại trong model --%>
<c:if test="${not empty result}">
    <h3>${result}</h3>
</c:if>

</body>
</html>