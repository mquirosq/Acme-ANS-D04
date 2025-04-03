<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${_command != 'create'}">
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-select code="technician.task-record.form.label.technician"
		path="technician" choices="${technicians}"/>
	<acme:input-select code="technician.task-record.form.label.type"
		path="type" choices="${types}"/>
	<acme:input-integer code="technician.task-record.form.label.priority"
		path="priority"/>
	<acme:input-integer code="technician.task-record.form.label.estimate"
		path="estimate"/>
	<acme:input-textarea code="technician.task-record.form.label.description"
		path="description"/>	
			<acme:submit code="technician.task-record.form.button.delete" action="/technician/task-record/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="technician.task-record.form.label.task"
		path="task" choices="${tasks}"/>
			<acme:submit code="technician.task-record.form.button.create" action="/technician/task-record/create?id=${id}" />
		</jstl:when>
	</jstl:choose>
</acme:form>