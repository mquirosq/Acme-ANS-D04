<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.resolutionPercentage" path = "resolutionPercentage"/>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.status" path = "status"/>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.isPublished" path = "isPublished"/>
	<acme:list-payload path = "payload"/>
</acme:list>

<jstl:if test = "${canCreate}">
	<acme:button code = "assistance-agent.tracking-log.list.button.create" action = "/assistance-agent/tracking-log/create?masterId=${masterId}"/>
</jstl:if>