<%@ include file="template/overviewHeader.jsp"%>

<openmrs:require privilege="View VCT Overview" otherwise="/login.htm" redirect="/module/vcttrac/vctOverview.htm" />

<div style="width: 95%; margin: auto;">
	<div class="box" style="float: left; width: 28%;">
		<ul>
			<li><a id="dayStat" href="#"><spring:message code="vcttrac.graph.statistic.perday"/></a></li>
			<li><a onclick="loadGraph('registrationEvolutionPerDay','550','250',1,6);" href="#"><spring:message code="vcttrac.graph.evolution.client.received"/></a></li>
			<!-- <li><a onclick="loadGraph('vctResultOfHivTest');" href="#"><spring:message code="vcttrac.statistic.graph.resultoftest"/></a></li> -->
		</ul>
	</div>
	
	<div style="float: right; width: 70%;">
		<div class="box" style="text-align: center;" id="chartHolder">
						
		</div>
	</div>
	
	<div style="clear: both;"></div>
</div>
<br/><br/>

<script type="text/javascript">
	function loadGraph(id,width,height,boldIndex,graphCategory){
		var bts="<ul id='menu'><li class='first "+((boldIndex==1)?"active":"")+"'><a href='#' onclick=loadGraph('registrationEvolutionPerDay','550','250',1,1)>This Week</a></li><li class='"+((boldIndex==2)?"active":"")+"'><a href='#' onclick=loadGraph('registrationEvolutionPerDay','750','250',2,2)>This Month</a></li><li class='"+((boldIndex==3)?"active":"")+"'><a href='#' onclick=loadGraph('barChartView','650','250',3,3)>This Year</a></li><li class='"+((boldIndex==4)?"active":"")+"'><a href='#' onclick=loadGraph('barChartView','650','250',4,4)>Years</a></li></ul><br/>";
		//chart.htm?chart=barChartView&type=todayAndYesterday&width=300&height=300

		if(graphCategory<3){
			jQuery("#chartHolder").html(
				bts+"<img src='chart.htm?chart="+id+"&width="+width+"&height="+height+"&graphCategory="+graphCategory+"' width='"+width+"' height='"+height+"' />");
			//alert("this is it AAAAA");
		}
		if(graphCategory>2 && graphCategory<4){
			jQuery("#chartHolder").html(
					bts+"<img src='chart.htm?chart="+id+"&width="+width+"&height="+height+"&graphCategory="+graphCategory+"&type=monthInYear' width='"+width+"' height='"+height+"' />");
			//alert("this is it BBBBBBB");
		}
		if(graphCategory>=4){
			jQuery("#chartHolder").html(
					bts+"<img src='chart.htm?chart="+id+"&width="+width+"&height="+height+"&graphCategory="+graphCategory+"&type=years' width='"+width+"' height='"+height+"' />");
			//alert("this is it BBBBBBB");
		}
	}

	jQuery(document).ready(function(){
		jQuery("#dayStat").click(function(){
			jQuery("#chartHolder").load("vctTodayGraphicalStatistic.htm");
		});

		jQuery("#dayStat").click();
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>