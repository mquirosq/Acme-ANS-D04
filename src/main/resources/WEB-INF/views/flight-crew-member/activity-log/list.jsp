<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.activityLog.list.label.typeOfIncident" path="typeOfIncident"/>
	<acme:list-column code="flightCrewMember.activityLog.list.label.registrationMoment" path="registrationMoment"/>
	<acme:list-column code="flightCrewMember.activityLog.list.label.severityLevel" path="severityLevel"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create"/>