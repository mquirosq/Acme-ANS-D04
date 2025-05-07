
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="any.review.form.label.alias" path="alias"/>
	<acme:input-moment code="any.review.form.label.moment" path="moment" readonly="true"/>
	<acme:input-textbox code="any.review.form.label.subject" path="subject"/>
	<acme:input-textarea code="any.review.form.label.text" path="text"/>
	<acme:input-double code="any.review.form.label.score" path="score"/>
	<acme:input-checkbox code="any.review.form.label.recommended" path="recommended"/>
	
	<jstl:if test = "${!readonly}">
		<acme:input-checkbox code = "any.review.form.label.confirmation" path = "confirmation"/>
	</jstl:if>
	
	<jstl:choose>
			<jstl:when test = "${_command == 'create'}">
				<acme:submit code = "any.review.form.button.create" action = "/any/review/create"/>
			</jstl:when>
	</jstl:choose>
</acme:form>

