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

package com.liferay.commerce.inventory.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.TimelineModel;
import com.liferay.commerce.inventory.model.CommerceInventoryAudit;
import com.liferay.commerce.inventory.service.CommerceInventoryAuditService;
import com.liferay.commerce.inventory.type.CommerceInventoryAuditType;
import com.liferay.commerce.inventory.type.CommerceInventoryAuditTypeRegistry;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.DateFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_AUDIT,
	service = CommerceDataSetDataProvider.class
)
public class CommerceInventoryAuditDataSetDataProvider
	implements CommerceDataSetDataProvider<TimelineModel> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		String sku = ParamUtil.getString(httpServletRequest, "sku");

		return _commerceInventoryAuditService.getCommerceInventoryAuditsCount(
			_portal.getCompanyId(httpServletRequest), sku);
	}

	@Override
	public List<TimelineModel> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<TimelineModel> timelineModels = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Format dateTimeFormat = FastDateFormatFactoryUtil.getDateTime(
			DateFormat.MEDIUM, DateFormat.MEDIUM, themeDisplay.getLocale(),
			themeDisplay.getTimeZone());

		String sku = ParamUtil.getString(httpServletRequest, "sku");

		List<CommerceInventoryAudit> commerceInventoryAudits =
			_commerceInventoryAuditService.getCommerceInventoryAudits(
				_portal.getCompanyId(httpServletRequest), sku,
				pagination.getStartPosition(), pagination.getEndPosition());

		for (CommerceInventoryAudit commerceInventoryAudit :
				commerceInventoryAudits) {

			StringBundler titleSB = new StringBundler(1);

			try {
				CommerceInventoryAuditType commerceInventoryAuditType =
					_commerceInventoryAuditTypeRegistry.
						getCommerceInventoryAuditType(
							commerceInventoryAudit.getLogType());

				titleSB.append(
					commerceInventoryAuditType.formatLog(
						commerceInventoryAudit.getUserId(),
						commerceInventoryAudit.getLogTypeSettings(),
						_portal.getLocale(httpServletRequest)));
			}
			catch (Exception e) {
				throw new PortalException(e.getMessage(), e);
			}

			timelineModels.add(
				new TimelineModel(
					commerceInventoryAudit.getCommerceInventoryAuditId(),
					dateTimeFormat.format(
						commerceInventoryAudit.getCreateDate()),
					String.valueOf(commerceInventoryAudit.getQuantity()),
					titleSB.toString()));
		}

		return timelineModels;
	}

	@Reference
	private CommerceInventoryAuditService _commerceInventoryAuditService;

	@Reference
	private CommerceInventoryAuditTypeRegistry
		_commerceInventoryAuditTypeRegistry;

	@Reference
	private Portal _portal;

}