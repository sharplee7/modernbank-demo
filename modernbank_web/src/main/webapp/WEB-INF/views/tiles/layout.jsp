<%@ page language ="java"  pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="images/favicon.ico" type="image/ico" />

    <title>AWS AnyBank Company</title>

    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/Default.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/xtree.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/thickbox.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/css.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/base.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/global_style.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/global_layout.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/page_style.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/tiles/css/page_layout.css" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/jquery-1.11.1.js"></script>
<!--     <script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script> -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/thickbox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/json2.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/tiles/js/common.js"></script>
	
  </head>

  <body class="nav-md">
  	<div class="container body">
      	<div class="main_container">
			<tiles:insertAttribute name="header" />
			<div class="wrap-loading display-none">
		    	<div><img src="<%=request.getContextPath()%>/resource/tiles/images/loading.gif"/></div>
			</div>   
		    <tiles:insertAttribute name="content" />
		    <tiles:insertAttribute name="footer" />
		</div>
	</div>
  </body>
</html>