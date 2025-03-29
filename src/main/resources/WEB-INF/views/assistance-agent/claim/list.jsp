<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "assistance-agent.claim.list.label.passengerEmail" path = "passengerEmail"/>
	<acme:list-column code = "assistance-agent.claim.list.label.type" path = "type"/>
	<acme:list-column code = "assistance-agent.claim.list.label.registrationMoment" path = "registrationMoment"/>
	<acme:list-column code = "assistance-agent.claim.list.label.status" path = "status"/>
	<acme:list-payload path = "payload"/>
</acme:list>