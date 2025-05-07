<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.recommendation.list.label.name" path="name"  width="30%"/>
	<acme:list-column code="customer.recommendation.list.label.city" path="city"  width="20%"/>
	<acme:list-column code="customer.recommendation.list.label.country" path="country"  width="20%"/>
	<acme:list-column code="customer.recommendation.list.label.website" path="website"  width="30%"/>
</acme:list>