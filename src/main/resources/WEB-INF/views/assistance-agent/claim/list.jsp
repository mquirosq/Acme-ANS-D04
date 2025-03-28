<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<jstl:if test = "${_command == 'list-completed'}">
	<acme:print code = "assistance-agent.claim.list.completed.subtitle"/>
</jstl:if>

<jstl:if test = "${_command == 'list-pending'}">
	<acme:print code = "assistance-agent.claim.list.pending.subtitle"/>
</jstl:if>

<acme:list>
	<acme:list-column code = "assistance-agent.claim.list.label.passengerEmail" path = "passengerEmail"/>
	<acme:list-column code = "assistance-agent.claim.list.label.type" path = "type"/>
	<acme:list-column code = "assistance-agent.claim.list.label.registrationMoment" path = "registrationMoment"/>
	<acme:list-payload path = "payload"/>
</acme:list>