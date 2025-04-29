<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.duty" path = "duty" choices="${duties}"/>	
	<acme:input-moment code = "flightCrewMember.flightAssignment.form.label.moment" path = "moment"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.currentStatus" path = "currentStatus" choices="${statusChoices}"/>
	<acme:input-textbox code = "flightCrewMember.flightAssignment.form.label.remarks" path = "remarks"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.allocatedFlightCrewMember" path = "allocatedFlightCrewMember" choices="${flightCrewMembers}"/>
	<acme:input-select code = "flightCrewMember.flightAssignment.form.label.leg" path = "leg" choices="${legs}"/>
	<acme:input-checkbox code = "flightCrewMember.flightAssignment.form.label.isPublished" path = "published" readonly = "${true}"/>
	
	 <jstl:choose>	 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
		<jstl:when test="${_command == 'show' && !published }">
			<acme:button code = "flight-crew-member.flight-assignment.form.button.update" action = "/flight-crew-member/flight-assignment/update?id=${id}"/>
			<acme:submit code = "flight-crew-member.flight-assignment.form.button.publish" action = "/flight-crew-member/flight-assignment/publish"/>
			<acme:submit code = "flight-crew-member.flight-assignment.form.button.delete" action = "/flight-crew-member/flight-assignment/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
