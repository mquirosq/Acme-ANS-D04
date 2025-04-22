
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-select code="administrator.booking.form.label.flight" path="flight" choices="${flights}"/>	
	<acme:input-select code="administrator.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-textbox code="administrator.booking.form.label.lastCardNibble" path="lastCardNibble" placeholder = "administrator.booking.form.placeholder.lastCardNibble"/>
	<acme:input-moment code="administrator.booking.form.label.purchasedAt" path="purchasedAt"/>
	<acme:input-select code="administrator.booking.form.label.customer" path = "customer" choices="${customers}"/>

	<acme:button code="administrator.booking.form.button.passengers" action="/administrator/booking-record/list?masterId=${id}"/>
</acme:form>
