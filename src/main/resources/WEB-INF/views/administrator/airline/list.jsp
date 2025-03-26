<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.announcement.list.label.IATACode" path="IATACode" width="10%"/>
	<acme:list-column code="administrator.announcement.list.label.type" path="type" width="20%"/>
	<acme:list-column code="administrator.airline.list.label.name" path="name" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="administrator.company.list.button.create" action="/administrator/company/create"/>
