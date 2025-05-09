<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.duty" path = "duty" choices="${duties}"/>	
	<acme:input-moment code = "flightCrewMember.flightAssignment.form.label.moment" path = "moment" readonly = "${true}"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.currentStatus" path = "currentStatus" choices="${statusChoices}"/>
	<acme:input-textbox code = "flightCrewMember.flightAssignment.form.label.remarks" path = "remarks"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.allocatedFlightCrewMember" path = "allocatedFlightCrewMember" choices="${flightCrewMembers}"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.leg" path = "leg" choices="${legs}"/>
	<jstl:if test="${acme:anyOf(_command, 'show')}">
		<acme:input-checkbox code = "flightCrewMember.flightAssignment.form.label.isPublished" path = "published" readonly = "${true}"/>
	</jstl:if>
	<acme:button code = "assistance-agent.claim.form.button.trackingLogs" action = "/flight-crew-member/activity-log/list?masterId=${id}"/>
	 <jstl:choose> 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update?id=${id}"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
