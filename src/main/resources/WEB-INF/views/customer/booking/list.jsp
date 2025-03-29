<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.label.locatorCode" path="locatorCode" width="33%"/>
	<acme:list-column code="customer.booking.list.label.purchasedAt" path="purchasedAt" width="33%"/>
	<acme:list-column code="customer.booking.list.label.price" path="price" width="33%"/>
	<acme:list-payload path="payload"/>
</acme:list>