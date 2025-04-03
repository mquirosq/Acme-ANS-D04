<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="technician.task-record.form.label.task"
		path="task" choices="${tasks}"
		readonly="${_command != 'create'}" />

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:submit code="technician.task-record.form.button.delete" action="/technician/task-record/delete" />

		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.task-record.form.button.create" action="/technician/task-record/create?id=${maintenanceRecordId}" />

		</jstl:when>
	</jstl:choose>
</acme:form>