
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="customer.bookingRecord.form.label.passenger"
		path="passenger" choices="${passengers}"
		readonly="${_command != 'create'}" />

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && draft}">
			<acme:submit code="customer.bookingRecord.form.button.delete" action="/customer/booking-record/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.bookingRecord.form.button.create" action="/customer/booking-record/create?masterId=${bookingId}" />
		</jstl:when>
	</jstl:choose>
</acme:form>

