<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>购物车</title>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>
<h1>高端大气上档次的购物车页面</h1>
<table style="border:1px solid #F00;">
    <tr>
        <td></td>
        <td>图片</td>
        <td>名字</td>
        <td>数量</td>
        <td>价格</td>
    </tr>
    <#if carList??>
    <#list carList as car>
    <tr>
        <td><input type="checkbox" name="pid" value="${car.productId}"></td>
        <td><img src="${car.image}" style="height: 50px;height: 25px"></td>
        <td>${car.name}</td>
        <td>${car.qty}</td>
        <td>￥:${car.price}</td>
    </tr>
    </#list>
    </#if>
</table>
<button onclick="morecheck()">提交订单</button><br><br><br><br><br><br><br>
<a href="/findOrder">查看订单</a>
</body>

<script>
    function morecheck() {
        var bb = "";
        var temp = "";
        var a = document.getElementsByName("pid");
        for ( var i = 0; i < a.length; i++) {
            if (a[i].checked) {
                temp = a[i].value;
                bb = bb + "," +temp;
            }
        }
       var parm =bb .substring(1, bb.length); //赋值给隐藏域

        $.ajax({
            type: "get",
            url: "/testOrder?products="+parm,
            dataType: "json",
            success: function (obj) {

            }
        });
        location.href="/findOrder";
    }
</script>
</html>