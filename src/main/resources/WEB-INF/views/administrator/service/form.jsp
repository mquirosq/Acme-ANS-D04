<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.service.form.label.name" path="name"/>	
	<acme:input-url code="administrator.service.form.label.pictureLink" path="pictureLink"/>	
	<acme:input-double code="administrator.service.form.label.avgDwellTime" path="avgDwellTime"/>
	<acme:input-textbox code="administrator.service.form.label.promotionCode" path="promotionCode"/>
	<acme:input-double code="administrator.service.form.label.promotionDiscount" path="promotionDiscount"/>
	
	<jstl:choose>
		<jstl:when test = "${_command == 'create'}">
			<acme:submit code = "administrator.service.list.button.create" action = "/administrator/service/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code = "administrator.service.form.button.update" action = "/administrator/service/update"/>
			<acme:submit code = "administrator.service.form.button.delete" action = "/administrator/service/delete"/>
		</jstl:when>			
	</jstl:choose>
	
</acme:form>