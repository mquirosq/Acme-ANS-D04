<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.duty" path="duty"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.moment" path="moment"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.currentStatus" path="currentStatus"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.leg" path="leg.flightNumber"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
