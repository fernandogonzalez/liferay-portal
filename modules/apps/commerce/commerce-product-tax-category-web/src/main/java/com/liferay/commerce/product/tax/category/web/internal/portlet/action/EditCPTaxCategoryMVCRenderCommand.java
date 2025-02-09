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

package com.liferay.commerce.product.tax.category.web.internal.portlet.action;

import com.liferay.commerce.admin.constants.CommerceAdminPortletKeys;
import com.liferay.commerce.product.exception.NoSuchCPTaxCategoryException;
import com.liferay.commerce.product.service.CPTaxCategoryService;
import com.liferay.commerce.product.tax.category.web.internal.display.context.CPTaxCategoryDisplayContext;
import com.liferay.commerce.tax.service.CommerceTaxMethodService;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderConstants;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"javax.portlet.name=" + CommerceAdminPortletKeys.COMMERCE_ADMIN,
		"mvc.command.name=editCPTaxCategory"
	},
	service = MVCRenderCommand.class
)
public class EditCPTaxCategoryMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/edit_tax_category.jsp");

		try {
			CPTaxCategoryDisplayContext cpTaxCategoryDisplayContext =
				new CPTaxCategoryDisplayContext(
					_commerceTaxMethodService, _cpTaxCategoryService,
					renderRequest, renderResponse);

			renderRequest.setAttribute(
				WebKeys.PORTLET_DISPLAY_CONTEXT, cpTaxCategoryDisplayContext);

			requestDispatcher.include(
				_portal.getHttpServletRequest(renderRequest),
				_portal.getHttpServletResponse(renderResponse));
		}
		catch (Exception exception) {
			if (exception instanceof NoSuchCPTaxCategoryException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(renderRequest, exception.getClass());

				return "/error.jsp";
			}

			throw new PortletException(
				"Unable to include edit_tax_category.jsp", exception);
		}

		return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
	}

	@Reference
	private CommerceTaxMethodService _commerceTaxMethodService;

	@Reference
	private CPTaxCategoryService _cpTaxCategoryService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.product.tax.category.web)"
	)
	private ServletContext _servletContext;

}