<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code = "flightCrewMember.activityLog.form.label.registrationMoment" path = "registrationMoment" readonly = "${true}"/>	
	<acme:input-textbox code = "flightCrewMember.activityLog.form.label.typeOfIncident" path = "typeOfIncident"/>
	<acme:input-textbox code = "flightCrewMember.activityLog.form.label.description" path = "description"/>
	<acme:input-integer code = "flightCrewMember.activityLog.form.label.severityLevel" path = "severityLevel"/>
	<jstl:if test="${acme:anyOf(_command, 'show')}">
		<acme:input-checkbox code = "flightCrewMember.flightAssignment.form.label.isPublished" path = "published" readonly = "${true}"/>
	</jstl:if>
</acme:form>