<%@page%>

<%@taglib prefix = "jstl" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "acme" uri = "http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.passenger.list.label.fullName" path = "fullName" width="80%"/>
	<acme:list-column code = "customer.passenger.list.label.passportNumber" path = "passportNumber" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:choose>
	<jstl:when test="${!draft}">
		<acme:button code="customer.list-passengers.mine" action="/customer/passenger/list?all=${true}"/>
	</jstl:when>
	<jstl:when test="${draft}">
		<acme:button code="customer.list-passengers.booking" action="/customer/passenger/list?all=${false}"/>
	</jstl:when>
</jstl:choose>	

<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
