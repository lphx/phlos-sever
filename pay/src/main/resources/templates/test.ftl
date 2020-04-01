<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>提交订单</title>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div id="show">

</div>



        <a href="/findShopping">查看购物车</a>
</body>

<script>

        $.ajax({
            type: "get",
            url: "/productList?currPage=0&pageSize=10",
            dataType: "json",
            success: function (obj) {
                var data = obj.data;

                for (var i = 0;i<data.length;i++){
                    $("#show").append(
                            "<div style='width: 260px;height:380px;background: aquamarine;display: inline-block;margin-left: 15px'>"
                            +"<img src='"+data[i].mainImage+"' style='width: 260px;height: 130px'>"
                            +" <p>"+data[i].name+"</p><br>"
                            +" <p>￥:"+data[i].price+"</p><br>"
                            +" <p><button onclick='addShopping("+data[i].id+","+"\""+data[i].name+"\""+","+"\""+data[i].mainImage+"\""+","+data[i].price+")'>加入购物车</button></p><br>"
                            +"</div>"
                    )
                }


            }
        });

        function addShopping(id,name,mainImage,price) {
            $.ajax({
                type: "get",
                url: "/addShopping",
                data:{"productId":id,"name":name,"qty":1,"image":mainImage,"price":price},
                dataType: "json",
                success: function (obj) {
                    alert("加入购物车成功");

                }
            });
            location.href="/findShopping";
        }

</script>

</html>