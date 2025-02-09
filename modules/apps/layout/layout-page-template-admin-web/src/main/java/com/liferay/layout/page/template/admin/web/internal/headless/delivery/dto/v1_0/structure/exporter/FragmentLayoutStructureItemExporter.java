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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.exporter;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.converter.PageFragmentInstanceDefinitionDTOConverter;
import com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.converter.PageWidgetInstanceDefinitionDTOConverter;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(service = LayoutStructureItemExporter.class)
public class FragmentLayoutStructureItemExporter
	implements LayoutStructureItemExporter {

	@Override
	public String getClassName() {
		return FragmentStyledLayoutStructureItem.class.getName();
	}

	@Override
	public PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean saveInlineContent, boolean saveMappingConfiguration) {

		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem =
			(FragmentStyledLayoutStructureItem)layoutStructureItem;

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());

		if (fragmentEntryLink == null) {
			return null;
		}

		JSONObject editableValuesJSONObject = null;

		try {
			editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
				fragmentEntryLink.getEditableValues());
		}
		catch (JSONException jsonException) {
			return null;
		}

		String portletId = editableValuesJSONObject.getString("portletId");

		if (Validator.isNull(portletId)) {
			return new PageElement() {
				{
					definition =
						_pageFragmentInstanceDefinitionDTOConverter.toDTO(
							fragmentStyledLayoutStructureItem,
							saveInlineContent, saveMappingConfiguration);
					type = PageElement.Type.FRAGMENT;
				}
			};
		}

		String instanceId = editableValuesJSONObject.getString("instanceId");

		return new PageElement() {
			{
				definition = _pageWidgetInstanceDefinitionDTOConverter.toDTO(
					fragmentEntryLink,
					PortletIdCodec.encode(portletId, instanceId));
				type = PageElement.Type.WIDGET;
			}
		};
	}

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private PageFragmentInstanceDefinitionDTOConverter
		_pageFragmentInstanceDefinitionDTOConverter;

	@Reference
	private PageWidgetInstanceDefinitionDTOConverter
		_pageWidgetInstanceDefinitionDTOConverter;

}