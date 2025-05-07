<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.recommendation.form.label.city" path="city"/>
	<acme:input-textbox code="administrator.recommendation.form.label.country" path="country"/>

	<acme:input-textarea code="administrator.recommendation.form.label.placeId" path="placeId" readonly="true"/>	
	<acme:input-textarea code="administrator.recommendation.form.label.name" path="name" readonly="true"/>
	<acme:input-url code="administrator.recommendation.form.label.website" path="website" readonly="true"/>
		
	<acme:submit code="administrator.recommendation.form.button.perform" action="/administrator/recommendation/perform"/>
</acme:form>
