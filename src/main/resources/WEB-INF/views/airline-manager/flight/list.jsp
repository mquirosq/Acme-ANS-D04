<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.flight.list.label.tag" path="tag"/>
	<acme:list-column code="airline-manager.flight.list.label.draftMode" path="draftMode"/>
	<acme:list-column code="airline-manager.flight.list.label.identifierCode" path="identifierCode"/>
	<acme:list-column code="airline-manager.flight.list.label.numberOfLayovers" path="numberOfLayovers"/>
	<acme:list-payload path="payload"/>
</acme:list>