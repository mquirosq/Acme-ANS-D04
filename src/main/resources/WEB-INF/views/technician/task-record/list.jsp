<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.task-record.list.label.type" path="task.type"/>
	<acme:list-column code="technician.task-record.list.label.technician" path="task.technician"/>
	<acme:list-column code="technician.task-record.list.label.priority" path="task.priority"/>
	<acme:list-payload path="payload"/>
</acme:list>
	<acme:button code="technician.task-record.list.button.create" action="/technician/task-record/create?id=${maintenanceRecordId}"/>
