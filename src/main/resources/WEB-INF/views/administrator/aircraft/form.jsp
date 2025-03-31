<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form readonly = "${readonly}">
	<acme:input-textbox code = "administrator.aircraft.form.label.model" path = "model"/>	
	<acme:input-textbox code = "administrator.aircraft.form.label.registrationNumber" path = "registrationNumber"/>
	<acme:input-integer code = "administrator.aircraft.form.label.capacity" path = "capacity" placeholder = "administrator.aircraft.form.placeholder.capacity"/>
	<acme:input-integer code = "administrator.aircraft.form.label.cargoWeight" path = "cargoWeight" placeholder = "administrator.aircraft.form.placeholder.cargoWeight"/>
	<acme:input-select code = "administrator.aircraft.form.label.status" path = "status" choices = "${statuses}"/>
	<acme:input-textarea code = "administrator.aircraft.form.label.details" path = "details"/>
	<acme:input-select code = "administrator.aircraft.form.label.airline" path = "airline" choices = "${airlines}"/>
	
	<jstl:if test = "${!readonly}">
		<acme:input-checkbox code = "administrator.aircraft.form.label.confirmation" path = "confirmation"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test = "${_command == 'show'}">
			<acme:button code = "administrator.aircraft.form.button.edit" action = "/administrator/aircraft/update?id=${id}"/>
		</jstl:when>
		<jstl:when test = "${acme:anyOf(_command, 'update|disable')}">
			<acme:submit code = "administrator.aircraft.form.button.edit" action = "/administrator/aircraft/update"/>			
			
			<jstl:if test = "${status != 'UNDER_MAINTENANCE'}">
				<acme:submit code = "administrator.aircraft.form.button.disable" action = "/administrator/aircraft/disable"/>
			</jstl:if>
		</jstl:when>
		<jstl:when test = "${_command == 'create'}">
			<acme:submit code = "administrator.aircraft.form.button.create" action = "/administrator/aircraft/create"/>
		</jstl:when>			
	</jstl:choose>	
</acme:form>