<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.bookingRecord.list.label.passenger" path="passenger"/>
	<acme:list-column code="customer.bookingRecord.list.label.passport" path="passport"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${draft}">
	<acme:button code="customer.bookingRecord.list.button.create" action="/customer/booking-record/create?masterId=${bookingId}"/>
</jstl:if>


