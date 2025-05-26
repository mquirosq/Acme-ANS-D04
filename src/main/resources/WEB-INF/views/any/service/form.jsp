<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="any.service.form.label.name" path="name"/>	
	<acme:input-url code="any.service.form.label.pictureLink" path="pictureLink"/>	
	<acme:input-double code="any.service.form.label.avgDwellTime" path="avgDwellTime"/>
	<acme:input-textbox code="any.service.form.label.promotionCode" path="promotionCode"/>
	<acme:input-double code="any.service.form.label.promotionDiscount" path="promotionDiscount"/>
</acme:form>