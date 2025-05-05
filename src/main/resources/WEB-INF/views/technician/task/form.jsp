<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="technician.task.form.label.type" path="type" choices="${types}"/>
	<acme:input-integer code="technician.task.form.label.priority" path="priority"/>
	<acme:input-integer code="technician.task.form.label.hour-estimate" path ="hourEstimate"/>
	<acme:input-textarea code="technician.task.form.label.description" path="description"/>
	
		<jstl:choose>			
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && isDraft == true}">
            <acme:submit code="technician.task.form.button.publish" action="/technician/task/publish?id=${id}"/>
            <acme:submit code="technician.task.form.button.update" action="/technician/task/update?id=${id}"/>
            <acme:submit code="technician.task.form.button.delete" action="/technician/task/delete?id=${id}"/>
            
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="technician.task.form.button.create" action="/technician/task/create"/>
        </jstl:when>  	
	</jstl:choose>
</acme:form>