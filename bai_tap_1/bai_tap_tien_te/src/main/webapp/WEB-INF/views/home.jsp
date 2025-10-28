<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chuyển đổi USD sang VNĐ</title>
</head>
<body>
<h2> Chuyển đổi tiền tệ</h2>
<form action="convert" method="post">
    <label>Nhập số tiền (USD):</label>
    <input type="number" step="0.01" name="usd" required><br><br>
    <button type="submit">Chuyển đổi</button>
</form>
</body>
</html>