<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code = "flightCrewMember.activityLog.form.label.registrationMoment" path = "registrationMoment" readonly = "${true}"/>	
	<acme:input-textbox code = "flightCrewMember.activityLog.form.label.typeOfIncident" path = "typeOfIncident" readonly = "${false}"/>
	<acme:input-textbox code = "flightCrewMember.activityLog.form.label.description" path = "description" readonly = "${false}"/>
	<acme:input-integer code = "flightCrewMember.activityLog.form.label.severityLevel" path = "severityLevel" readonly = "${false}"/>
	<jstl:if test="${acme:anyOf(_command, 'show')}">
		<acme:input-checkbox code = "flightCrewMember.activityLog.form.label.isPublished" path = "published" readonly = "${true}"/>
	</jstl:if>
		 <jstl:choose> 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/activity-logt/update?id=${id}"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>