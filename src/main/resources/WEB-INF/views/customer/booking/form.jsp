
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly = "${readonly}"> 
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>	
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-textbox code="customer.booking.form.label.lastCardNibble" path="lastCardNibble" placeholder = "customer.booking.form.placeholder.lastCardNibble"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-money code="customer.booking.form.label.price" path="price" readonly="True"/>
		<jstl:if test="${systemPrice != null}">
			<acme:input-money code="customer.booking.form.label.price-exchange" path="systemPrice" readonly="True"/>
		</jstl:if>
		<acme:input-moment code="customer.booking.form.label.purchasedAt" path="purchasedAt" readonly = "${true}"/>
		<acme:input-checkbox code = "customer.booking.form.label.draft" path = "draftMode" readonly = "${true}"/>
	</jstl:if>
	
	<jstl:if test="${acme:anyOf(_command, 'show') && !updateable && draftMode}">
		<p>
			<acme:print code="customer.booking.form.inform.pastDeparture"/>
		<p>
	</jstl:if>
				
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show || publish') && draftMode}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?masterId=${id}"/>	
			<jstl:if test="${updateable}">
				<acme:button code="customer.booking.form.button.edit" action="/customer/booking/update?id=${id}"/>
				<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish?id=${id}"/>	
			</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
