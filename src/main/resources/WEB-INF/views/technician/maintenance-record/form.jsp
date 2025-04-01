
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="technician.maintenance-record.form.label.maintenance-date" path="maintenance-date"/>
	<acme:input-select code="technician.maintenance-record.form.label.task" path="task" choices="${tasks}"/>	
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-moment code="technician.maintenance-record.form.label.inspection-due" path="inspection-due"/>
	<acme:input-money code="technician.maintenance-record.form.label.cost" path="cost"/>
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:button code="technician.maintenance-record.form.button.duties" action="/technician/maintenance-record/list?masterId=${id}"/>			
		</jstl:when>		
	</jstl:choose>
</acme:form>
