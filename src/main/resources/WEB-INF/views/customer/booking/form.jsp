
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>	
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-textbox code="customer.booking.form.label.lastCardNibble" path="lastCardNibble" placeholder = "customer.booking.form.placeholder.lastCardNibble"/>
	<acme:input-money code="customer.booking.form.label.price" path="price" readonly="True"/>
	<acme:input-moment code="customer.booking.form.label.purchasedAt" path="purchasedAt" readonly="True"/>
	<acme:input-checkbox code = "customer.booking.form.label.draft" path = "draftMode" readonly = "${true}"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?id=${id}"/>	
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>		
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?id=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'update')}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?id=${id}"/>
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
