
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="airline-manager.flight.form.label.tag" path="tag"/>
	<acme:input-money code="airline-manager.flight.form.label.cost" path="cost"/>
	<jstl:if test="${systemCost != null}">
		<acme:input-money code="airline-manager.flight.form.label.cost-exchange" path="systemCost" readonly = "${true}"/>
	</jstl:if>
	<acme:input-textbox code="airline-manager.flight.form.label.description" path="description"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.requiresSelfTransfer" path="requiresSelfTransfer"/>	
	<jstl:if test="${_command != 'create'}">
		<acme:input-moment code="airline-manager.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly = "${true}"/>
		<acme:input-moment code="airline-manager.flight.form.label.scheduledArrival" path="scheduledArrival" readonly = "${true}"/>
		<acme:input-textbox code="airline-manager.flight.form.label.originCity" path="originCity" readonly = "${true}"/>
		<acme:input-textbox code="airline-manager.flight.form.label.destinationCity" path="destinationCity" readonly = "${true}"/>
		<acme:input-double code="airline-manager.flight.form.label.numberOfLayovers" path="numberOfLayovers" readonly = "${true}"/>
		<acme:input-checkbox code="airline-manager.flight.form.label.draftMode" path="draftMode" readonly = "${true}"/>
	</jstl:if>
		<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode}">
			<acme:button code="airline-manager.flight.form.button.flight-legs" action="/airline-manager/flight-leg/list?parentId=${id}"/>	
			<acme:submit code="airline-manager.flight.form.button.update" action="/airline-manager/flight/update"/>
			<acme:submit code="airline-manager.flight.form.button.publish" action="/airline-manager/flight/publish"/>	
			<acme:submit code="airline-manager.flight.form.button.delete" action = "/airline-manager/flight/delete"/>				
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="airline-manager.flight.form.button.flight-legs" action="/airline-manager/flight-leg/list?parentId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'update || publish || delete')}">
			<acme:button code="airline-manager.flight.form.button.flight-legs" action="/airline-manager/flight-leg/list?parentId=${id}"/>
			<acme:submit code="airline-manager.flight.form.button.update" action="/airline-manager/flight/update"/>
			<acme:submit code="airline-manager.flight.form.button.publish" action="/airline-manager/flight/publish"/>
			<acme:submit code="airline-manager.flight.form.button.delete" action = "/airline-manager/flight/delete"/>				
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="airline-manager.flight.form.button.create" action="/airline-manager/flight/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
