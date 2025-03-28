<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form readonly = "${readonly}">
	<acme:input-moment code = "assistance-agent.claim.form.label.registrationMoment" path = "registrationMoment"/>	
	<acme:input-email code = "assistance-agent.claim.form.label.passengerEmail" path = "passengerEmail"/>	
	<acme:input-textarea code = "assistance-agent.claim.form.label.description" path = "description"/>
	<acme:input-checkbox code = "assistance-agent.claim.form.label.isPublished" path = "isPublished"/>
	<acme:input-select code = "assistance-agent.claim.form.label.type" path = "type" choices = "${types}"/>
	<acme:input-select code = "assistance-agent.claim.form.label.status" path = "status" choices = "${statuses}"/>	
	<acme:input-select code = "assistance-agent.claim.form.label.leg" path = "type" choices = "${legs}"/>
	<acme:input-select code = "assistance-agent.claim.form.label.agent" path = "assistanceAgent" choices = "${assistanceAgents}"/>	
</acme:form>