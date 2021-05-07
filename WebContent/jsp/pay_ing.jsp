<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>

    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>支付中</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
        <script src="${pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- 引入自定义css文件 style.css -->
        <link rel="stylesheet" href="${pageContext.request.contextPath }/css/style.css" type="text/css" />
        <style>
            body {
                margin-top: 20px;
                margin: 0 auto;
            }
            
            .carousel-inner .item img {
                width: 100%;
                height: 300px;
            }
        </style>

		<script>
		    var contextPath = "${ pageContext.request.contextPath }"
		    var oid = "${ oid }"
		    var money = "${ money }"
            var url = contextPath + "/OrderServlet?method=callBack" + "&r6_Order=" + oid + "&r3_Amt=" + money
            console.log("url: " + url)
		    var t = 5;
		    var time = $("#aaa").text(t)
		    function fun() {
		        t--
		        $("#aaa").text(t)
		        if(t <= 0) {
		            clearInterval(inter)
                    window.location.href=url
		        }
		    }

		    var inter = setInterval("fun()", 1000)
		</script>
    </head>

    <body>
        <%@ include file="menu.jsp" %>
        <div class="container">
            <div class="row">
                <div style="margin: 20px auto; width: 500px">
                    <input type="hidden" value="${ oid }" id="oid">
				    <h2>支付成功：<span id="aaa" style="color: red">5</span>s后返回</h2>
				</div>
            </div>
        </div>

    </body>

</html>