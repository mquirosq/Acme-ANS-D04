<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft" width="34%"/>
	<acme:list-column code="technician.maintenance-record.list.label.maintenanceDate" path="maintenanceDate" width="33%"/>
	<acme:list-column code="technician.maintenance-record.list.label.inspectionDue" path="inspectionDue" width="33%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>	