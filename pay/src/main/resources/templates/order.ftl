<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>确定订单</title>
</head>
<body>

<h1>高端大气上档次的订单页面</h1>
<table style="border:1px solid #F00;">
    <tr>
        <td>订单号</td><td></td>
        <td>价格</td><td></td>
        <td>状态</td><td></td>
    </tr>
    <#if oderList??>
        <#list oderList as order>
            <tr>
                <td>${order.id}</td><td></td>
                <td>￥:${order.amount}</td><td></td>
                <td>
                    <#if (order.state==0)>
                        <a href="/testCreatePayToken?payAmount=${order.amount}&orderId=${order.id}&userId=${order.userId}">去付款</a>
                    </#if>
                     <#if (order.state==1)>
                         已付款
                     </#if>
                     <#if (order.state==5)>
                         已退款
                     </#if>
                </td>
            </tr>
        </#list>
    </#if>
</table>



</body>
</html>