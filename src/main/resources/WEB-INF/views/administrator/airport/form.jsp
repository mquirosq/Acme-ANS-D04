<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.airport.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airport.form.label.IATACode" path="IATACode" placeholder="administrator.airport.form.placeholder.IATACode"/>	
	<acme:input-textbox code="administrator.airport.form.label.city" path="city" placeholder="administrator.airport.form.placeholder.city"/>	
	<acme:input-textbox code="administrator.airport.form.label.country" path="country" placeholder="administrator.airport.form.placeholder.country"/>	
	<acme:input-url code="administrator.airport.form.label.website" path="website"/>
	<acme:input-select code="administrator.airport.form.label.scope" path="scope" choices="${scopes}"/>
	<acme:input-textbox code="administrator.airport.form.label.email" path="email" placeholder="administrator.airport.form.placeholder.email"/>	
	<acme:input-textbox code="administrator.airport.form.label.phoneNumber" path="phoneNumber" placeholder="administrator.airport.form.placeholder.phoneNumber"/>
</acme:form>