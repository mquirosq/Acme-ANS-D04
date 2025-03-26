<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airline.form.label.IATACode" path="IATACode"/>	
	<acme:input-url code="administrator.airline.form.label.website" path="website"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices="${types}"/>
	<acme:input-moment code="administrator.airline.form.label.foundation" path="foundation"/>
	<acme:input-textbox code="administrator.airline.form.label.email" path="email"/>	
	<acme:input-textbox code="administrator.airline.form.label.phoneNumber" path="phoneNumber"/>
</acme:form>