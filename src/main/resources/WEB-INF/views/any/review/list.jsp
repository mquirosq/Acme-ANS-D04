<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.review.list.label.alias" path="alias" width="10%"/>	
	<acme:list-column code="any.review.list.label.subject" path="subject" width="15%"/>
	<acme:list-column code="any.review.list.label.text" path="text" width="65%"/>
	<acme:list-column code="any.review.list.label.score" path="score" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>


<acme:button code = "any.review.list.button.create" action = "/any/review/create"/>