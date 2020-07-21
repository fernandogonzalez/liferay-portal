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

package com.liferay.translation.service.impl;

import com.liferay.info.item.InfoItemClassPKReference;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.petra.io.StreamUtil;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.translation.exporter.TranslationInfoItemFieldValuesExporter;
import com.liferay.translation.model.TranslationEntry;
import com.liferay.translation.service.base.TranslationEntryLocalServiceBaseImpl;

import java.io.IOException;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the translation entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.translation.service.TranslationEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TranslationEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.translation.model.TranslationEntry",
	service = AopService.class
)
public class TranslationEntryLocalServiceImpl
	extends TranslationEntryLocalServiceBaseImpl {

	public TranslationEntry addOrUpdateTranslationEntry(
			long groupId, InfoItemFieldValues infoItemFieldValues,
			Locale targetLocale, ServiceContext serviceContext)
		throws IOException {

		InfoItemClassPKReference infoItemClassPKReference =
			infoItemFieldValues.getInfoItemClassPKReference();

		TranslationEntry translationEntry =
			translationEntryPersistence.fetchByG_S(
				_portal.getClassNameId(infoItemClassPKReference.getClassName()),
				infoItemClassPKReference.getClassPK(),
				LocaleUtil.toLanguageId(targetLocale));

		if (translationEntry == null) {
			translationEntry = translationEntryPersistence.create(
				counterLocalService.increment());

			translationEntry.setUuid(serviceContext.getUuid());

			translationEntry.setGroupId(groupId);

			translationEntry.setCompanyId(serviceContext.getCompanyId());
			translationEntry.setUserId(serviceContext.getUserId());
			translationEntry.setClassName(
				infoItemClassPKReference.getClassName());
			translationEntry.setClassPK(infoItemClassPKReference.getClassPK());
			translationEntry.setLanguageId(
				LocaleUtil.toLanguageId(targetLocale));
		}

		translationEntry.setContent(
			StreamUtil.toString(
				_xliffTranslationInfoItemFieldValuesExporter.
					exportInfoItemFieldValues(
						infoItemFieldValues, LocaleUtil.getDefault(),
						targetLocale)));
		translationEntry.setContentType(
			_xliffTranslationInfoItemFieldValuesExporter.getMimeType());

		return translationEntryPersistence.update(translationEntry);
	}

	public TranslationEntry fetchTranslationEntry(
		InfoItemClassPKReference infoItemClassPKReference, Locale locale) {

		return translationEntryPersistence.fetchByG_S(
			_portal.getClassNameId(infoItemClassPKReference.getClassName()),
			infoItemClassPKReference.getClassPK(),
			LocaleUtil.toLanguageId(locale));
	}

	@Reference
	private Portal _portal;

	@Reference(target = "(content.type=application/xliff+xml)")
	private TranslationInfoItemFieldValuesExporter
		_xliffTranslationInfoItemFieldValuesExporter;

}