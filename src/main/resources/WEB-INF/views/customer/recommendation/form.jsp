<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="customer.recommendation.form.label.name" path="name"/>
	<acme:input-textbox code="customer.recommendation.form.label.city" path="city"/>
	<acme:input-textbox code="customer.recommendation.form.label.country" path="country"/>	
	<acme:input-url code="customer.recommendation.form.label.website" path="website"/>
</acme:form>