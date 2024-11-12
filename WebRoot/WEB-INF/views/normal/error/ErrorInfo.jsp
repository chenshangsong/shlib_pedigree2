<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8" />
        <title>错误报告</title>
        <%
		out.print("<center><font size =5>对不起，您访问过于频繁！2分钟后访问本网站！</font></center>");//被拒绝服务器Ip显示信息
	%>
	<br>
	<br>
	<br>
	    <input  id='r1'  value="${stoptime}"  type="hidden"  />
        <!-- Our CSS stylesheet file -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300" />
        <link rel="stylesheet" href="${ctx}/res/assets/css/styles.css" />
        <link rel="stylesheet" href="${ctx}/res/assets/countdown/jquery.countdown.css" />
        
        <!--[if lt IE 9]>
          <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>
    
    <body>

		<div id="countdown"></div>

	<!--	<p id="note"></p> -->

      <!--  <footer>
	        <h2><i>Tutorial:</i> A jQuery Countdown Timer</h2>
            <a class="tzine" href="http://tutorialzine.com/2011/12/countdown-jquery/">Head on to <i>Tutorial<b>zine</b></i> to download this example</a>
        </footer>
        -->
        <!-- JavaScript includes -->
		<!-- <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script> -->
		<script  type="text/javascript" src="${ctx}/res/sysmanager/vendors/jquery.js"></script>
		<script src="${ctx}/res/assets/countdown/jquery.countdown.js"></script>
		<script src="${ctx}/res/assets/js/script.js"></script>
  
    </body>
</html>