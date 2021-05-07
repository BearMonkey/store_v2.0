<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>订单详情</title>
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
		<script type="text/javascript">
		$(function(){
            var contextPath = "${ pageContext.request.contextPath }"
            var oid = "${ order.oid }"
			$("#payOrderBtn").click(function (){
				var url = contextPath + "/OrderServlet"
				var method = "payOrder"
				var address = $("#address").val()
				var name = $("#name").val()
				var telephone = $("#telephone").val()
				var params = {}
				params['oid']=oid
				params['method']=method
				params['address']=address
				params['name']=name
				params['telephone']=telephone
				var rst = "false"
				$.post(url, params, function (data) {
                    var code = data.respCode;
                    console.log('respCode:' + code)
                    if (code == "0") {
                    	window.location.href=contextPath + "/OrderServlet?method=paying&oid=" + oid
                    } if (code == "1008") {
                    	$("#oidInfo").text("订单不存在")                	
                    } else if (code == "1009") {
                        $("#addressInfo").text("地址不能为空")
                    } else if (code == "1010") {
                        $("#nameInfo").text("收货人不能为空")
                    } else if (code == "1011") {
                        $("#telephoneInfo").text("电话不能为空")
                    }
				})				
			})
			$("#address,#name,#telephone").focus(function() {
				$("#addressInfo,#nameInfo,#telephoneInfo").text("")
			})
		})
		
		</script>
	</head>

	<body>
		<%@ include file="menu.jsp" %>

		<div class="container">
			<div class="row">

				<div style="margin:0 auto;margin-top:10px;width:950px;">
					<strong>订单详情</strong>
					<table class="table table-bordered">
						<tbody>
							<tr class="warning">
								<th colspan="5">订单编号:${ order.oid } <span id="oidInfo" style="color: red;"></span></th>
							</tr>
							<tr class="warning">
								<th>图片</th>
								<th>商品</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
							</tr>
							<c:forEach var="orderItem" items="${ order.orderItems }">
							<tr class="active">
								<td width="60" width="40%">
									<img src="${ pageContext.request.contextPath }/${ orderItem.product.pimage }" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank">${ orderItem.product.pname }</a>
								</td>
								<td width="20%">
									￥${ orderItem.product.shop_price }
								</td>
								<td width="10%">
									${ orderItem.count }
								</td>
								<td width="15%">
									<span class="subtotal">￥${ orderItem.subtotal }</span>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div style="text-align:right;margin-right:120px;">
					商品金额: <strong style="color:#ff6600;">￥${ order.total }元</strong>
				</div>

			</div>

			<div>
				<hr/>
				<form id="orderForm" action="${ pageContext.request.contextPath }/OrderServlet" class="form-horizontal" style="margin-top:5px;margin-left:150px;" method="post">
					<input type="hidden" name="method" value="payOrder"/>
					<input type="hidden" name="oid" value="${ order.oid }"/>
					<div class="form-group">
						<label for="address" class="col-sm-1 control-label">地址<span style="color: red">*</span></label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="address" id="address" placeholder="请输入收货地址">
						</div>
						<span id="addressInfo" style="color: red"></span>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">收货人<span style="color: red">*</span></label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="name" id="name" placeholder="请输收货人">
						</div>
						<span id="nameInfo" style="color: red"></span>
					</div>
					<div class="form-group">
						<label for="telephone" class="col-sm-1 control-label">电话<span style="color: red">*</span></label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="telephone" id="telephone" placeholder="请输入联系方式">
						</div>
						<span id="telephoneInfo" style="color: red"></span>
					</div>
				

				<hr/>

				<div style="margin-top:5px;margin-left:150px;">
					<strong>选择银行：</strong>
					<p>
						<br/>
						<input type="radio" name="pd_FrpId" value="ICBC-NET-B2C" checked="checked" />工商银行
						<img src="${pageContext.request.contextPath }/bank_img/icbc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_FrpId" value="BOC-NET-B2C" />中国银行
						<img src="${pageContext.request.contextPath }/bank_img/bc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_FrpId" value="ABC-NET-B2C" />农业银行
						<img src="${pageContext.request.contextPath }/bank_img/abc.bmp" align="middle" />
						<br/>
						<br/>
						<input type="radio" name="pd_FrpId" value="BOCO-NET-B2C" />交通银行
						<img src="${pageContext.request.contextPath }/bank_img/bcc.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_FrpId" value="PINGANBANK-NET" />平安银行
						<img src="${pageContext.request.contextPath }/bank_img/pingan.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_FrpId" value="CCB-NET-B2C" />建设银行
						<img src="${pageContext.request.contextPath }/bank_img/ccb.bmp" align="middle" />
						<br/>
						<br/>
						<input type="radio" name="pd_FrpId" value="CEB-NET-B2C" />光大银行
						<img src="${pageContext.request.contextPath }/bank_img/guangda.bmp" align="middle" />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="pd_FrpId" value="CMBCHINA-NET-B2C" />招商银行
						<img src="${pageContext.request.contextPath }/bank_img/cmb.bmp" align="middle" />
						<input type="radio" name="pd_FrpId" value="BCCB-NET-B2C" />北京银行
						<img src="${pageContext.request.contextPath }/bank_img/bj.bmp" align="middle" />

					</p>
					<hr/>
					<p style="text-align:right;margin-right:100px;">
					    <span id="payOrderBtn">
					        <img src="${pageContext.request.contextPath }/images/finalbutton.gif" width="204" height="51" border="0" />
					    </span>
						<!-- <a href="javascript:document.getElementById('orderForm').submit();">
							
						</a> -->
					</p>
					<hr/>
				</form>
				</div>
			</div>

		</div>

		<div style="margin-top:50px;">
			<img src="${pageContext.request.contextPath }/image/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势" />
		</div>

		<div style="text-align: center;margin-top: 5px;">
			<ul class="list-inline">
				<li><a>关于我们</a></li>
				<li><a>联系我们</a></li>
				<li><a>招贤纳士</a></li>
				<li><a>法律声明</a></li>
				<li><a>友情链接</a></li>
				<li><a target="_blank">支付方式</a></li>
				<li><a target="_blank">配送方式</a></li>
				<li><a>服务声明</a></li>
				<li><a>广告声明</a></li>
			</ul>
		</div>
		<div style="text-align: center;margin-top: 5px;margin-bottom:20px;">
		Copyright &copy; 20202-2021 桃宝商城 版权所有</div>

	</body>

</html>