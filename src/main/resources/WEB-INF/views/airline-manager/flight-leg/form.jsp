
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="airline-manager.flight-leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="airline-manager.flight-leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.flight-leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-select code = "airline-manager.flight-leg.form.label.status" path = "status" choices = "${statuses}"/>
	<acme:input-select code = "airline-manager.flight-leg.form.label.departureAirport" path = "departureAirport" choices = "${departureAirports}"/>
	<acme:input-select code = "airline-manager.flight-leg.form.label.arrivalAirport" path = "arrivalAirport" choices = "${arrivalAirports}"/>
	<acme:input-select code = "airline-manager.flight-leg.form.label.deployedAircraft" path = "deployedAircraft" choices = "${deployedAircrafts}"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-select code = "airline-manager.flight-leg.form.label.parentFlight" path = "parentFlight" choices = "${parentFlights}"/>
		<acme:input-checkbox code="airline-manager.flight-leg.form.label.draftMode" path="draftMode" readonly = "${true}"/>
	</jstl:if>
		<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode}">
			<acme:submit code="airline-manager.flight-leg.form.button.update" action="/airline-manager/flight-leg/update"/>
			<acme:submit code="airline-manager.flight-leg.form.button.publish" action="/airline-manager/flight-leg/publish"/>		
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'update || publish')}">
			<acme:submit code="airline-manager.flight-leg.form.button.update" action="/airline-manager/flight-leg/update"/>
			<acme:submit code="airline-manager.flight-leg.form.button.publish" action="/airline-manager/flight-leg/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="airline-manager.flight-leg.form.button.create" action="/airline-manager/flight-leg/create?parentId=${parentId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
