<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:form readonly = "${readonly}">
	<acme:input-textbox code = "customer.passenger.form.label.fullName" path = "fullName"/>	
	<acme:input-textbox code = "customer.passenger.form.label.email" path = "email" placeholder = "customer.passenger.form.placeholder.email"/>
	<acme:input-textbox code = "customer.passenger.form.label.passportNumber" path = "passportNumber" placeholder = "customer.passenger.form.placeholder.passportNumber"/>
	<acme:input-moment code = "customer.passenger.form.label.birthDate" path = "birthDate"/>
	<acme:input-textarea code = "customer.passenger.form.label.specialNeeds" path = "specialNeeds"/>
	<acme:input-checkbox code = "customer.passenger.form.label.draft" path = "draftMode"/>
 </acme:form>