<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.flight-leg.list.label.flightNumber" path="flightNumber"/>
	<acme:list-column code="airline-manager.flight-leg.list.label.draftMode" path="draftMode"/>
	<acme:list-column code="airline-manager.flight-leg.list.label.departureAirport" path="departureAirport"/>
	<acme:list-column code="airline-manager.flight-leg.list.label.arrivalAirport" path="arrivalAirport"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="airline-manager.flight-leg.list.button.create" action="/airline-manager/flight-leg/create"/>