<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.passenger.list.label.fullName" path = "fullName" width="80%"/>
	<acme:list-column code = "customer.passenger.list.label.passportNumber" path = "passportNumber" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${!draft}">
	<acme:button code="customer.list-passengers.draft" action="/customer/passenger/list?draft=${true}"/>
</jstl:if>
