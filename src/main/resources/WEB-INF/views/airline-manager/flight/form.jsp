
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="airline-manager.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.requiresSelfTransfer" path="requiresSelfTransfer"/>	
	<acme:input-money code="airline-manager.flight.form.label.cost" path="cost"/>
	<acme:input-textbox code="airline-manager.flight.form.label.description" path="description"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.draftMode" path="draftMode"/>
	<acme:input-select code="airline-manager.flight.form.label.manager" path="manager" choices="${managers}"/>
	<acme:input-moment code="airline-manager.flight.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.flight.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-textbox code="airline-manager.flight.form.label.originCity" path="originCity"/>
	<acme:input-textbox code="airline-manager.flight.form.label.destinationCity" path="destinationCity"/>
	<acme:input-double code="airline-manager.flight.form.label.numberOfLayovers" path="numberOfLayovers"/>
</acme:form>
