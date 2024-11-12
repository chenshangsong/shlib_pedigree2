<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<hr>
<footer class="footer ">
	<div class="container">
		<div class="row footer-bottom">
			<ul class="list-inline text-center">
				<li>Copyright © 上海图书馆 </li>
				<li>非特别注明，本站遵循cc2.0协议（ 署名-非商业性使用-相同方式共享）</li>
				<li><a href="${ctx}/page/about" target= _blank>About（关于本站）</a></li>
				<li><a href="${ctx}/page/contact" target= _blank>Contact（联系我们）</a></li>
				<li><a id="wwz" href="javascript:void(0)"><span class="glyphicon glyphicon-qrcode" ></span>&nbsp;关注“姓氏寻根”微网站</li>
		</div>
	</div>
</footer>
<div class="modal fade" id="modal-loading">
	<div class="modal-dialog modal-loading">
		<div class="modal-content" style="margin-top: -50px;">
			<div class="modal-body text-center">
				<img src="${ctx}/res/images/loading.gif" /> &nbsp;&nbsp;<span
					class="modal-info">Loading...</span>
			</div>
		</div>
	</div>
</div>
<%-- <script src="${ctx}/res/jquery/jquery.min.js"></script> --%>
<script src="${ctx}/res/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx}/res/icheck/icheck.js"></script>
<script src="${ctx}/res/jquery/jquery.pin.js"></script>
<script src="${ctx}/res/jquery/jquery.xhashchange.min.js"></script>
<script src="${ctx}/res/js/normal/common.js"></script>
<script src="${ctx}/res/js/normal/dialog.js"></script>
<script src="${ctx}/res/commJS/layer/layer.js"></script>
<script>
	$(function() {
		$("#wwz").click(function() {
			layer.open({
				type: 2,
			    title: false,
			    area: ['290px', '290px'],
			    shade: 0.8,
			    closeBtn: 1,
			    shadeClose: true,
			    content: '${ctx}/res/wwz.png'
			});
			
		});
		if ($("#float_bar").length > 0) {
			var searchTop = $("#float_bar").offset().top;
			$(window).scroll(function() {
				var position = $("#float_bar").css("position");
				var scrollTop = $(window).scrollTop();
				if (position != "fixed" && scrollTop > searchTop) {
					$("#float_bar").css({
						position : "fixed",
						top : 20,
						"z-index" : 10
					});
				}
				if (position == "fixed" && (scrollTop < searchTop)) {
					$("#float_bar").css({
						position : "static",
						"z-index" : 0
					});
				}
			});
		}
	})
</script>