<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="customer.dashboard.label.lastFiveDestinations"/>
</h2>
<jstl:choose>
	<jstl:when test="${not empty lastFiveDestinations}">
		<ol>
			<jstl:forEach var="destination" items="${lastFiveDestinations}">
				<li><acme:print value="${destination}"/></li>
			</jstl:forEach>
		</ol>
	</jstl:when>
	<jstl:otherwise>
		<acme:print code="customer.dashboard.form.no-bookings-message"/>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="customer.dashboard.form.title.money-spent-last-year"/>
</h2>

<jstl:choose>
	<jstl:when test="${not empty moneySpentInBookingsDuringLastYear}">
		<table class="table table-sm">
		    <jstl:forEach items="${moneySpentInBookingsDuringLastYear}" var="money">
		        <tr>
		            <th scope="row">
		                <acme:print value="${money.currency}"/>
		            </th>
		            <td>
		                <acme:print value="${money.amount}"/>
		            </td>
		        </tr>
		    </jstl:forEach>
		</table>
	</jstl:when>
	<jstl:otherwise>
		<acme:print code="customer.dashboard.form.no-bookings-message"/>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="customer.dashboard.form.title.booking-costs"/>
</h2>

<jstl:choose>
	<jstl:when test="${not empty totalCostOfBookings}">
		<table class="table table-sm">
			<tr>
		        <th scope="row"></th>
		        <jstl:forEach items="${totalCostOfBookings}" var="money">
		            <th><acme:print value="${money.currency}"/></th>
		        </jstl:forEach>
		    </tr>
		    <tr>
		        <th scope="row">
		            <acme:print code="customer.dashboard.form.label.total-cost"/>
		        </th>
		    <jstl:forEach items="${totalCostOfBookings}" var="money">
		        <td>
		            <jstl:choose>
		                <jstl:when test="${money.amount != null}">
		                    <acme:print value="${money.amount}"/>
		                </jstl:when>
		                <jstl:otherwise>-</jstl:otherwise>
		            </jstl:choose>
		        </td>
		    </jstl:forEach>
		    </tr>
		    <tr>
		        <th scope="row">
		            <acme:print code="customer.dashboard.form.label.average-cost"/>
		        </th>
			    <jstl:forEach items="${averageCostOfBookings}" var="money">
			        <td>
			            <jstl:choose>
			                <jstl:when test="${money.amount != null}">
			                    <acme:print value="${money.amount}"/>
			                </jstl:when>
			                <jstl:otherwise>-</jstl:otherwise>
			            </jstl:choose>
			        </td>
			    </jstl:forEach>
		    </tr>
		    <tr>
		        <th scope="row">
		            <acme:print code="customer.dashboard.form.label.minimum-cost"/>
		        </th>
			    <jstl:forEach items="${minimumCostOfBookings}" var="money">
			        <td>
			            <jstl:choose>
			                <jstl:when test="${money.amount != null}">
			                    <acme:print value="${money.amount}"/>
			                </jstl:when>
			                <jstl:otherwise>-</jstl:otherwise>
			            </jstl:choose>
			        </td>
			    </jstl:forEach>
		    </tr>
		    <tr>
		        <th scope="row">
		            <acme:print code="customer.dashboard.form.label.maximum-cost"/>
		        </th>
			    <jstl:forEach items="${maximumCostOfBookings}" var="money">
			        <td>
			            <jstl:choose>
			                <jstl:when test="${money.amount != null}">
			                    <acme:print value="${money.amount}"/>
			                </jstl:when>
			                <jstl:otherwise>-</jstl:otherwise>
			            </jstl:choose>
			        </td>
			    </jstl:forEach>
		    </tr>
		    <tr>
		        <th scope="row">
		            <acme:print code="customer.dashboard.form.label.deviation-cost"/>
		        </th>
			    <jstl:forEach items="${standardDeviationOfCostOfBookings}" var="money">
			        <td>
			            <jstl:choose>
			                <jstl:when test="${money.amount != null}">
			                    <acme:print value="${money.amount}"/>
			                </jstl:when>
			                <jstl:otherwise>-</jstl:otherwise>
			            </jstl:choose>
			        </td>
			    </jstl:forEach>
		    </tr>
		</table>
	</jstl:when>
	<jstl:otherwise>
		<acme:print code="customer.dashboard.form.no-bookings-message"/>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="customer.dashboard.form.title.passengers-statistics"/>
</h2>

<table class="table table-sm">
    <tr>
      	<th scope="row">
            <acme:print code="customer.dashboard.form.label.total-passengers"/>
        </th>
        <td>
            <acme:print value="${totalNumberOfPassengersInBookings}"/>
        </td>
    </tr> 
    <tr>
	    <th scope="row">
	        <acme:print code="customer.dashboard.form.label.average-passengers"/>
	    </th>
	    <td>
	        <jstl:choose>
	            <jstl:when test="${averageNumberOfPassengerInBookings != null}">
	                <acme:print value="${averageNumberOfPassengerInBookings}"/>
	            </jstl:when>
	            <jstl:otherwise>-</jstl:otherwise>
	        </jstl:choose>
	    </td>
	</tr>
	<tr>
	    <th scope="row">
	        <acme:print code="customer.dashboard.form.label.minimum-passengers"/>
	    </th>
	    <td>
	        <jstl:choose>
	            <jstl:when test="${minimumNumberOfPassengersInBookings != null}">
	                <acme:print value="${minimumNumberOfPassengersInBookings}"/>
	            </jstl:when>
	            <jstl:otherwise>-</jstl:otherwise>
	        </jstl:choose>
	    </td>
	</tr>
	<tr>
	    <th scope="row">
	        <acme:print code="customer.dashboard.form.label.maximum-passengers"/>
	    </th>
	    <td>
	        <jstl:choose>
	            <jstl:when test="${maximumNumberOfPassengersInBookings != null}">
	                <acme:print value="${maximumNumberOfPassengersInBookings}"/>
	            </jstl:when>
	            <jstl:otherwise>-</jstl:otherwise>
	        </jstl:choose>
	    </td>
	</tr>
	<tr>
	    <th scope="row">
	        <acme:print code="customer.dashboard.form.label.deviation-passengers"/>
	    </th>
	    <td>
	        <jstl:choose>
	            <jstl:when test="${standardDeviationOfPassengersInBookings != null && standardDeviationOfPassengersInBookings != 'NaN'}">
	                <acme:print value="${standardDeviationOfPassengersInBookings}"/>
	            </jstl:when>
	            <jstl:otherwise>-</jstl:otherwise>
	        </jstl:choose>
	    </td>
	</tr>
</table>

<h2>
    <acme:print code="customer.dashboard.form.title.bookings-by-travel-class"/>
</h2>

<jstl:if test="${not empty numberOfBookingsByTravelClass}">
	<div>
    	<canvas id="travel-class-chart"></canvas>
	</div>

    <script type="text/javascript">
        $(document).ready(function() {
            var data = {
                labels: [
                    <jstl:forEach items="${numberOfBookingsByTravelClass}" var="entry" varStatus="loop">
                        "${entry.key}"<jstl:if test="${!loop.last}">,</jstl:if>
                    </jstl:forEach>
                ],
                datasets: [
                    {
                        data: [
                            <jstl:forEach items="${numberOfBookingsByTravelClass}" var="entry" varStatus="loop">
                                ${entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
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
            
            var canvas = document.getElementById("travel-class-chart");
            var context = canvas.getContext("2d");
            new Chart(context, {
                type: "pie",
                data: data,
                options: options
            });
        });
    </script>
</jstl:if>
<jstl:if test="${empty numberOfBookingsByTravelClass}">
    <acme:print code="customer.dashboard.form.no-bookings-message"/>
</jstl:if>

<acme:return/>