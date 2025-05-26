<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code = "flightCrewMember.activity-log.form.label.registrationMoment" path = "registrationMoment" readonly = "${true}"/>	
	<acme:input-textbox code = "flightCrewMember.activity-log.form.label.typeOfIncident" path = "typeOfIncident"/>
	<acme:input-textbox code = "flightCrewMember.activity-log.form.label.description" path = "description"/>
	<acme:input-integer code = "flightCrewMember.activity-log.form.label.severityLevel" path = "severityLevel"/>
	<acme:input-checkbox code = "flightCrewMember.activity-log.form.label.isPublished" path = "published" readonly = "${true}"/>

	<jstl:choose> 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
		</jstl:when>		
	</jstl:choose>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && canBePublished == true && published == false}">
		<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
	</jstl:if>
</acme:form>