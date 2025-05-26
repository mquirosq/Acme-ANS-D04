<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.bannedPassenger.list.label.fullName" path="fullName" width="50%"/>
	<acme:list-column code="administrator.bannedPassenger.list.label.passportNumber" path="passportNumber" width="10%"/>
	<acme:list-column code="administrator.bannedPassenger.list.label.issuedAt" path="issuedAt" width="20%"/>
	<acme:list-column code="administrator.bannedPassenger.list.label.liftedAt" path="liftedAt" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>