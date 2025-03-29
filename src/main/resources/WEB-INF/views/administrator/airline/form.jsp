<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airline.form.label.IATACode" path="IATACode" placeholder="administrator.airline.form.placeholder.IATACode"/>	
	<acme:input-url code="administrator.airline.form.label.website" path="website"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices="${types}"/>
	<acme:input-moment code="administrator.airline.form.label.foundation" path="foundation"/>
	<acme:input-textbox code="administrator.airline.form.label.email" path="email" placeholder="administrator.airline.form.placeholder.email"/>	
	<acme:input-textbox code="administrator.airline.form.label.phoneNumber" path="phoneNumber" placeholder="administrator.airline.form.placeholder.phoneNumber"/>
	
	<jstl:if test="${!readonly}">
		<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="administrator.airline.form.button.edit" action="/administrator/airline/update?id=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>