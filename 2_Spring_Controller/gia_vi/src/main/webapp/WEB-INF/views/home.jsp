<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chọn gia vị cho Sandwich</title>
</head>
<body>
<h2>Chọn gia vị bạn muốn thêm vào Sandwich:</h2>
<form action="save" method="get">
    <label><input type="checkbox" name="condiment" value="Lettuce"> Lettuce</label><br/>
    <label><input type="checkbox" name="condiment" value="Tomato"> Tomato</label><br/>
    <label><input type="checkbox" name="condiment" value="Mustard"> Mustard</label><br/>
    <label><input type="checkbox" name="condiment" value="Sprouts"> Sprouts</label><br/>
    <input type="submit" value="Lưu lựa chọn">
</form>
</body>
</html>