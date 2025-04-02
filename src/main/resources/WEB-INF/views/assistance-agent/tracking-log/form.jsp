<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code = "assistance-agent.tracking-log.form.label.lastUpdateMoment" path = "lastUpdateMoment" readonly = "true"/>	
	<acme:input-moment code = "assistance-agent.tracking-log.form.label.creationMoment" path = "creationMoment" readonly = "true"/>	
	<acme:input-textbox code = "assistance-agent.tracking-log.form.label.step" path = "step"/>
	<acme:input-double code = "assistance-agent.tracking-log.form.label.resolutionPercentage" path = "resolutionPercentage" placeholder = "assistance-agent.tracking-log.form.placeholder.resolutionPercentage"/>
	<acme:input-textarea code = "assistance-agent.tracking-log.form.label.resolution" path = "resolution"/>
	<acme:input-select code = "assistance-agent.tracking-log.form.label.status" path = "status" choices = "${statuses}"/>
	<acme:input-select code = "assistance-agent.tracking-log.form.label.claim" path = "claim" choices = "${claims}"/>
	
	<jstl:choose>
		<jstl:when test = "${acme:anyOf(_command, 'show|update|delete|publish') && isPublished == false}">
			<acme:submit code = "assistance-agent.tracking-log.form.button.edit" action = "/assistance-agent/tracking-log/update"/>			
			<acme:submit code = "assistance-agent.tracking-log.form.button.delete" action = "/assistance-agent/tracking-log/delete"/>			
			
			<jstl:if test = "${isClaimPublished == true}">
				<acme:submit code = "assistance-agent.tracking-log.form.button.publish" action = "/assistance-agent/tracking-log/publish"/>
			</jstl:if>
		</jstl:when>
		<jstl:when test = "${_command == 'create'}">
			<acme:submit code = "assistance-agent.tracking-log.form.button.create" action = "/assistance-agent/tracking-log/create"/>
		</jstl:when>			
	</jstl:choose>	
</acme:form>