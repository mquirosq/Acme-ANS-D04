
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 	
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${statuses}" readonly="true"/>
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-moment code="technician.maintenance-record.form.label.maintenance-date" path="maintenanceDate"/>
	<acme:input-moment code="technician.maintenance-record.form.label.inspection-due" path="inspectionDue"/>
	<acme:input-money code="technician.maintenance-record.form.label.cost" path="cost"/>
		<jstl:if test="${systemPrice != null}">
			<acme:input-money code="technician.maintenance-record.form.label.price-exchange" path="systemPrice" readonly="true"/>
		</jstl:if>
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task-record/list?id=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true && publishable == true}">
            <acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish?id=${id}"/>
            <acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update?id=${id}"/>
            <acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete?id=${id}"/>
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task-record/list?id=${id}"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true && publishable == false}">
            <acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update?id=${id}"/>
            <acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete?id=${id}"/>
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task-record/list?id=${id}"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
        </jstl:when>  	
	</jstl:choose>
</acme:form>
