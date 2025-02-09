<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommercePaymentMethodGroupRelsDisplayContext commercePaymentMethodGroupRelsDisplayContext = (CommercePaymentMethodGroupRelsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

long commerceChannelId = commercePaymentMethodGroupRelsDisplayContext.getCommerceChannelId();
%>

<portlet:actionURL name="editCommercePaymentMethodAddressRestriction" var="editCommercePaymentMethodAddressRestrictionActionURL" />

<aui:form action="<%= editCommercePaymentMethodAddressRestrictionActionURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="commerceChannelId" type="hidden" value="<%= commerceChannelId %>" />

	<clay:data-set-display
		contextParams='<%=
			HashMapBuilder.<String, String>put(
				"commerceChannelId", String.valueOf(commerceChannelId)
			).build()
		%>'
		dataProviderKey="<%= CommercePaymentRestrictionsPageClayTable.NAME %>"
		formId="fm"
		id="<%= CommercePaymentRestrictionsPageClayTable.NAME %>"
		itemsPerPage="<%= commercePaymentMethodGroupRelsDisplayContext.getCommerceCountriesCount() %>"
		namespace="<%= liferayPortletResponse.getNamespace() %>"
		pageNumber="<%= 1 %>"
		portletURL="<%= currentURLObj %>"
		selectedItemsKey="commerceCountryId"
		showPagination="<%= false %>"
	/>
</aui:form>