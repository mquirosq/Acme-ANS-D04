<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<h2>
	<acme:print code = "administrator.dashboard.label.totalNumberOfAirportsByOperationalScope"/>
</h2>
<jstl:if test = "${not empty totalNumberOfAirportsByOperationalScope}">
	<div>
    	<canvas id = "operational-scope-chart"></canvas>
	</div>
    <script type = "text/javascript">
        $(document).ready(
        	function() {
            	var data = {
                	labels: [
                    	<jstl:forEach items = "${totalNumberOfAirportsByOperationalScope}" var = "entry" varStatus = "loop">
                        	"${entry.key}"<jstl:if test = "${!loop.last}">,</jstl:if>
                    	</jstl:forEach>
                	], datasets: [
                    	{
                        	data: [
                            	<jstl:forEach items = "${totalNumberOfAirportsByOperationalScope}" var = "entry" varStatus = "loop">
                                	${entry.value}<jstl:if test = "${!loop.last}">,</jstl:if>
                            	</jstl:forEach>
                        	],
                        	backgroundColor: [
                            	"#FF6384",
	                            "#36A2EB",
    	                        "#FFCE56"
        	                ]
            	        }
                	]
            	};
            
            	var options = {
                	legend: {
                    	display: true,
                    	position: 'right'
                	},
                	responsive: true,
                	maintainAspectRatio: true
            	};
            
		        var canvas = document.getElementById("operational-scope-chart");
            	var context = canvas.getContext("2d");
        	    new Chart(
        	    	context,
        	    	{
                		type: "pie",
                		data: data,
                		options: options
            		}
        	    );
        	}
        );
    </script>
</jstl:if>
<jstl:if test = "${empty totalNumberOfAirportsByOperationalScope}">
    <acme:print code = "administrator.dashboard.form.message.no-airports"/>
</jstl:if>

<h2>
	<acme:print code = "administrator.dashboard.label.numberOfAirlinesByType"/>
</h2>
<jstl:if test = "${not empty numberOfAirlinesByType}">
	<div>
    	<canvas id = "airline-type-chart"></canvas>
	</div>
    <script type = "text/javascript">
        $(document).ready(
        	function() {
            	var data = {
                	labels: [
                    	<jstl:forEach items = "${numberOfAirlinesByType}" var = "entry" varStatus = "loop">
                        	"${entry.key}"<jstl:if test = "${!loop.last}">,</jstl:if>
                    	</jstl:forEach>
                	], datasets: [
                    	{
                        	data: [
                            	<jstl:forEach items = "${numberOfAirlinesByType}" var = "entry" varStatus = "loop">
                                	${entry.value}<jstl:if test = "${!loop.last}">,</jstl:if>
                            	</jstl:forEach>
                        	],
                        	backgroundColor: [
                            	"#FF6384",
	                            "#36A2EB",
    	                        "#FFCE56"
        	                ]
            	        }
                	]
            	};
            
            	var options = {
                	legend: {
                    	display: true,
                    	position: 'right'
                	},
                	responsive: true,
                	maintainAspectRatio: true
            	};
            
		        var canvas = document.getElementById("airline-type-chart");
            	var context = canvas.getContext("2d");
        	    new Chart(
        	    	context,
        	    	{
                		type: "pie",
                		data: data,
                		options: options
            		}
        	    );
        	}
        );
    </script>
</jstl:if>
<jstl:if test = "${empty numberOfAirlinesByType}">
    <acme:print code = "administrator.dashboard.form.message.no-airlines"/>
</jstl:if>

<h2>
	<acme:print code = "administrator.dashboard.form.label.ratioOfAirlinesWithEmailAddressAndPhoneNumber"/>
</h2>
<div>
	<canvas id = "airline-ratio-chart"></canvas>
</div>
<script type = "text/javascript">
	$(document).ready(
		function() {
			var data = {
				labels : [
					"Email & Phone", "Only Email / Only Phone / None"
				], datasets : [
					{
						data : [
							<jstl:out value = "${ratioOfAirlinesWithEmailAddressAndPhoneNumber}"/>, 
							<jstl:out value = "1.0 - ${ratioOfAirlinesWithEmailAddressAndPhoneNumber}"/>, 
						]
					}
				]
			};
			
			var options = {
				scales : {
					yAxes : [
						{
							ticks : {
								suggestedMin : 0.0,
								suggestedMax : 1.0
							}
						}
					]
				}, legend : {
					display : false
				}
			};
			
			var canvas, context;
	
			canvas = document.getElementById("airline-ratio-chart");
			context = canvas.getContext("2d");
		
			new Chart(
				context,
				{
					type : "bar",
					data : data,
					options : options
				}
			);
		}
	);
</script>

<h2>
	<acme:print code = "administrator.dashboard.form.label.ratioOfActiveAircrafts"/>
</h2>
<div>
	<canvas id = "aircraft-ratio-chart"></canvas>
</div>
<script type = "text/javascript">
	$(document).ready(
		function() {
			var data = {
				labels : [
					"Active", "Non-Active"
				], datasets : [
					{
						data : [
							<jstl:out value = "${ratioOfActiveAircrafts}"/>, 
							<jstl:out value = "${ratioOfNonActiveAircrafts}"/>, 
						]
					}
				]
			};
			
			var options = {
				scales : {
					yAxes : [
						{
							ticks : {
								suggestedMin : 0.0,
								suggestedMax : 1.0
							}
						}
					]
				}, legend : {
					display : false
				}
			};
			
			var canvas, context;
	
			canvas = document.getElementById("aircraft-ratio-chart");
			context = canvas.getContext("2d");
		
			new Chart(
				context,
				{
					type : "bar",
					data : data,
					options : options
				}
			);
		}
	);
</script>

<h2>
	<acme:print code = "administrator.dashboard.form.label.ratioOfReviewsWithScoreAboveFive"/>
</h2>
<div>
	<canvas id = "review-ratio-chart"></canvas>
</div>
<script type = "text/javascript">
	$(document).ready(
		function() {
			var data = {
				labels : [
					"Score > 5", "Score <= 5"
				], datasets : [
					{
						data : [
							<jstl:out value = "${ratioOfReviewsWithScoreAboveFive}"/>, 
							<jstl:out value = "1.0 - ${ratioOfReviewsWithScoreAboveFive}"/>, 
						]
					}
				]
			};
			
			var options = {
				scales : {
					yAxes : [
						{
							ticks : {
								suggestedMin : 0.0,
								suggestedMax : 1.0
							}
						}
					]
				}, legend : {
					display : false
				}
			};
			
			var canvas, context;
	
			canvas = document.getElementById("review-ratio-chart");
			context = canvas.getContext("2d");
		
			new Chart(
				context,
				{
					type : "bar",
					data : data,
					options : options
				}
			);
		}
	);
</script>

<h2>
	<acme:print code = "administrator.dashboard.form.title.reviewsInLastTenWeeksIndicators"/>
</h2>
<table class = "table table-sm">
	<tr>
		<th scope = "row">
			<acme:print code = "administrator.dashboard.form.label.numberOfReviewsPostedInLastTenWeeks"/>
		</th>
		<td>
			<acme:print value = "${numberOfReviewsPostedInLastTenWeeks}"/>
		</td>
	</tr>
	<tr>
		<th scope = "row">
			<acme:print code = "administrator.dashboard.form.label.minimumNumberOfReviewsPostedInLastTenWeeks"/>
		</th>
		<td>
			<acme:print value = "${minimumNumberOfReviewsPostedInLastTenWeeks}"/>
		</td>
	</tr>
	<tr>
		<th scope = "row">
			<acme:print code = "administrator.dashboard.form.label.maximumNumberOfReviewsPostedInLastTenWeeks"/>
		</th>
		<td>
			<acme:print value = "${maximumNumberOfReviewsPostedInLastTenWeeks}"/>
		</td>
	</tr>
	<tr>
		<th scope = "row">
			<acme:print code = "administrator.dashboard.form.label.averageNumberOfReviewsPostedInLastTenWeeks"/>
		</th>
		<td>
			<acme:print value = "${averageNumberOfReviewsPostedInLastTenWeeks}"/>
		</td>
	</tr>
	<tr>
		<th scope = "row">
			<acme:print code = "administrator.dashboard.form.label.standardDeviationOfNumberOfReviewsPostedInLastTenWeeks"/>
		</th>
		<td>
			<acme:print value = "${standardDeviationOfNumberOfReviewsPostedInLastTenWeeks}"/>
		</td>
	</tr>
</table>
<acme:return/>