<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.bannedPassenger.form.label.fullName" path="fullName"/>	
	<acme:input-moment code="administrator.bannedPassenger.form.label.dateOfBirth" path="dateOfBirth"/>	
	<acme:input-textbox code="administrator.bannedPassenger.form.label.passportNumber" path="passportNumber"/>	
	<acme:input-textbox code="administrator.bannedPassenger.form.label.nationality" path="nationality"/>	
	<acme:input-textarea code="administrator.bannedPassenger.form.label.reason" path="reason"/>
	<acme:input-moment code="administrator.bannedPassenger.form.label.issuedAt" path="issuedAt"/>
	<acme:input-moment code="administrator.bannedPassenger.form.label.liftedAt" path="liftedAt"/>	
</acme:form>