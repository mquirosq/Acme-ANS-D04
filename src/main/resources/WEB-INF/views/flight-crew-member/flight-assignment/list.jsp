<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.list.label.duty" path="duty"/>
	<acme:list-column code="flightCrewMember.list.label.moment" path="moment"/>
	<acme:list-column code="flightCrewMember.list.label.currentStatus" path="currentStatus"/>
</acme:list>