<%@ page contentType="text/html;charset=UTF-8" language="java"%>



<%@include file="/WEB-INF/inc/tag.jsp"%>



<!DOCTYPE html>



<html lang="en">



<head>



<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>

<c:import url="/WEB-INF/inc/link.jsp" />
</head>



<body>



	<c:import url="/WEB-INF/inc/header3.jsp">



		<c:param name="menu" value="about" />



	</c:import>



	<div class="container">



		<div class="row">


			<p>
				<br />
			</p>
			<p class="MsoNormal" style="text-align: center;">
				<strong>藏以致用，以技證道——上海圖書館家譜知識服務平台Beta版</strong></p>
			<p class="MsoNormal" style="text-align: center;">
				<strong>發佈說明</strong>
			</p>
			<p class="MsoNormal" style="text-align: center;">&nbsp;</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				家譜中記錄了大量的人物、經濟、移民、文化、民俗、教育、人口等資料，對歷史學、經濟學、社會學、教育學、民俗學、人口學、遺傳學等人文社會科學、自然科學的研究，有望起到有力的推動作用。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				上海圖書館的家譜專家們在長期的整理研究過程中，催生了一大批具有業界影響力的成果：主持編纂並出版了《上海圖書館館藏家譜提要》、《中國家譜總目》、《中國家譜通論》、《中國家譜資料選編》等。尤其是集中了全球家譜專家的努力和智慧的《中國家譜總目》，不僅是一部中國家譜的聯合目錄，還是一部家譜知識的百科全書。但可惜的是，作為印本書的傳播範圍有限，需要藉助現代信息技術使之煥發新的生命。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				上海圖書館的技術研發團隊試圖利用新的技術手段來重新組織和利用已有的館藏資源和研究成果，實現針對普通大眾的常識普及和智慧尋根服務，針對人文研究學者的知識發現和知識挖掘服務，以及針對圖書館的書目控制和知識增值功能。“上海圖書館家譜知識服務平台”就是這種嘗試的一個階段性的成果，我們以《中國家譜總目》收錄的、包含608個姓氏的，來自港、奧、台、日、韓、北美、德國等地的收藏機構所藏的5萬4千餘種家譜目錄為基礎，析出姓氏608個，先祖名人7萬余個，譜籍地名1600餘個，堂號3萬餘個，以知識組織的方法和關聯數據技術，重構上海圖書館的家譜服務。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">&nbsp;</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				對於圖書館來說，該平台主要滿足三方面的需求：</p>
			<p class="MsoNormal" style="text-indent: 21.1000pt;">
				<strong>第一：建立全球家谱联合目录，促进数据重用和共享。</strong>
			</p>
			<p class="MsoNormal" style="text-indent: 21.0000pt;">
				讓用戶直觀地了解某一家譜在全球各個收藏機構的收藏情況。在<a class="btn-primary"
					href="http://data.library.sh.cn/" target="_blank">上海圖書館開放數據平台</a>上以關聯開放數據（<span>LOD</span><span>）的方式公開發佈整理的規範數據，如中國歷史紀年表、地理名詞表、機構名錄等，促進數據的重用和共享。</span>
			</p>
			<p class="MsoNormal" style="text-indent: 21.1pt;">
				<strong>第二：基于万维网的规范控制。</strong>
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				利用關聯數據技術，使用<span>HTTP&nbsp;URI</span><span>作為規範數據的唯一標識符，實現基於萬維網的唯一標識和統一定位。</span>
			</p>
			<p class="MsoNormal" style="text-indent: 21.1pt;">
				<strong>第三：支持书目控制的可持续性发展。</strong>
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				不僅是一個展示系統，還是一個可寫的、支持眾包的平台。跟蹤家譜書目變化，定期更新；發現數據衝突和錯漏，實時修改，保證家譜書目控制的可持續性發展。</p>
			<p class="MsoNormal" style="text-indent: 21pt;">&nbsp;</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				對於用戶來說，試圖滿足多種不同層次的用戶需求。</p>
			<p class="MsoNormal" style="text-indent: 21.1pt;">
				<strong>第一：吸引大眾了解家譜，認識家譜。</strong>
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				進入<a class="btn-primary" href="https://jpv1.library.sh.cn/"
					target="_blank">首頁</a>，通過滑動屏幕左側的姓氏，觀察每個姓氏的家譜最早纂修年份，該姓家譜文獻和先祖名人數量，點選感興趣的姓氏，在屏幕右側觀看先祖名人和家譜文獻以及姓氏知識的詳細信息。
			</p>
			<p class="NewStyle16" style="text-indent: 21.1000pt;">
				<strong>第二：基于有限已知信息的寻根问祖。</strong>
			</p>
			<p class="NewStyle16">
				支持基于概念及概念间关系的匹配的檢索，准确地定位到读者想要的结果，不仅要提供方便的文献获取途径，还直接提供读者想要的内容。可使用<a class="btn-primary" href="https://jpv1.library.sh.cn/jp/service/search"
					target="_blank">簡單檢索</a>實現方便的查詢。
			</p>
			<p class="NewStyle16" style="text-indent: 21.1000pt;">
				<strong>第三：面向特定研究主题的知识发现。</strong>
			</p>
			<p class="NewStyle16">
				不僅檢索文獻，還可發現数据、事实和知识。可使用<a class="btn-primary"
					href="https://jpv1.library.sh.cn/jp/service" target="_blank">高級檢索</a>，<a
					class="btn-primary" href="https://jpv1.library.sh.cn/jp/service"
					target="_blank">基於時空的瀏覽</a>，<a class="btn-primary"
					href="https://jpv1.library.sh.cn/jp/service/map" target="_blank">地圖畫圈瀏覽</a>等功能。
			</p>
			<p class="NewStyle16" style="text-indent: 21.1000pt;">
				<strong>第四：基于</strong><span><strong>UGC</strong></span><span><strong>（用户贡献内容）的知识进化和积累。</strong></span>
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				允許并吸引研究專家、學生、民間團體貢獻知識。需登錄知識服務平台，通過撰寫反饋與不同人士交流互動。經過認證的專家登錄系統后，可直接修改數據，經審核通過后發佈，系統會記錄每一次修改。提供知識交流的平台，使得數據在使用過程中實現增殖。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
			    本平台的實現完全基於關聯數據技術：我們設計了<a class="btn-primary"
					href="http://ontology.library.sh.cn/" target="_blank">基於BIBFRAME的本體</a>，提取姓氏、人、地、時、機構等實體并賦予HTTP URI，將CNMARC/RDB數據轉換成BIBFRAME/RDF格式，採用RDF Store存儲數據，利用了Apache Jena、語義可视化技术、GIS技術作为开发框架。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
			    于2015年3月完成的<a class="btn-primary"
					href="http://gen.library.sh.cn/" target="_blank">“上海圖書館家譜知識庫”</a>是本平台的Demo系統，是前期研究成果。在此基礎上，本平台在資源總量（從2.1余萬種到5.4余萬種）、資源組織（從BIBFRAME到BIBFRAEM2.0）、系統功能（增加了用戶管理、反饋交流、數據維護流程）、用戶體驗（界面設計區分大眾和專家）這四個方面都有了較大的改進。另外還推出了<a class="btn-primary"
					href="http://weijp.library.sh.cn" target="_blank">“姓氏尋根”上圖家譜微服務網站</a>，作為平台的有力補充，將書目控制擴展到移動服務領域，在功能設計和內容組織上增加了趣味性，主要滿足普通大眾尋根問祖的需求。
			</p>
			<p class="MsoNormal" style="text-indent: 21pt;">
				特別說明的是，本平台的數據以《中國家譜總目》為基礎，對姓氏、人、地、時、機構等數據進行了進一步的規範和補充，歡迎各界人士提出寶貴意見，我們將繼續積極探索，持續改進。
			</p>
		</div>









	</div>



	<c:import url="/WEB-INF/inc/footer.jsp" />
</body>



</html>