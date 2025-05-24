<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form readonly = "${readonly}">
	<acme:input-textbox code = "customer.passenger.form.label.fullName" path = "fullName"/>	
	<acme:input-textbox code = "customer.passenger.form.label.email" path = "email" placeholder = "customer.passenger.form.placeholder.email"/>
	<acme:input-textbox code = "customer.passenger.form.label.passportNumber" path = "passportNumber" placeholder = "customer.passenger.form.placeholder.passportNumber"/>
	<acme:input-moment code = "customer.passenger.form.label.birthDate" path = "birthDate"/>
	<acme:input-textarea code = "customer.passenger.form.label.specialNeeds" path = "specialNeeds"/>
	<jstl:if test = "${_command != 'create'}">
		<acme:input-checkbox code = "customer.passenger.form.label.draft" path = "draftMode" readonly = "${true}"/>
 	</jstl:if>
 	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|publish') && draftMode}">
			<acme:button code="customer.passenger.form.button.edit" action="/customer/passenger/update?id=${id}"/>
			<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish?id=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.button.save" action="/customer/passenger/create"/>
		</jstl:when>		
	</jstl:choose>	
 </acme:form>
 
 