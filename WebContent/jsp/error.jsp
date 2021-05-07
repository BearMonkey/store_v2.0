<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
    <head></head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>错误</title>
        <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/bootstrap.min.css" type="text/css" />
        <script src="${ pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
        <script src="${ pageContext.request.contextPath }/js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/style.css" type="text/css"/>

<style>
  body{
   margin-top:20px;
   margin:0 auto;
 }
 .container .row div{ 
     /* position:relative;
     float:left; */
 }
 </style>
    <script type="text/javascript">
    </script>
</head>
<body>


<%@ include file="menu.jsp" %>

<div class="container" style="width:100%;background:url('${ pageContext.request.contextPath }/image/regist_bg.jpg');">
<div class="row"> 
    ${ pageContext.request.contextPath }  
</div>

</body></html>




