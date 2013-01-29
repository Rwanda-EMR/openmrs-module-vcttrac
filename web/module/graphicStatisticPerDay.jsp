<%@ include file="/WEB-INF/template/include.jsp"%>

<br/><span style="font-size: 2.2em; font-weight: bold; text-decoration: underline;"><spring:message code="@MODULE_ID@.graph.statistic.perday"/></span>

<table width="96%" cellspacing="25">
	<!-- <tr>
		<td></td>
		<td style="text-align: right;">
			<span>
				<table>
					<tr>
						<td>Day</td>
						<td>><input name="todayDate" type="text" size="11" onclick="showCalendar(this)" value="${todayDate}" /></td>
						<td><input name="refresh" type="button" value="<spring:message code='@MODULE_ID@.tablelist.refresh'/>" /></td>
					</tr>
				</table>
			</span>
		</td>
	</tr> -->
	<tr>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Today vs Yesterday" src="chart.htm?chart=barChartView&type=todayAndYesterday&width=300&height=300" width="300" height="300" /></div></td>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Counseling individuel vs Couples" src="chart.htm?chart=pieChartView&type=counselingType&width=350&height=300" width="350" height="300" /></div></td>
	</tr>
	<tr>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="PIT vs VCT" src="chart.htm?chart=pieChartView&type=vctVsPit&width=350&height=320" width="350" height="320" /></div></td>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Masculin vs Feminin" src="chart.htm?chart=pieChartView&type=gender&width=350&height=300" width="350" height="300" /></div></td>
	</tr>
	<!-- <tr>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Positive vs Negative" src="chart.htm?chart=vctResultOfHivTest&width=300&height=200" width="300" height="200" /></div></td>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Masculin vs Feminin" src="chart.htm?chart=pieChartView&type=gender&width=300&height=200" width="300" height="200" /></div></td>
	</tr>
	<tr>
		<td><br/><br/><div style="border: 1px solid #DEDEDE;"><img title="Age" src="chart.htm?chart=vctResultOfHivTest&width=300&height=200" width="300" height="200" /></div></td>
		<td></td>
	</tr> -->
</table>