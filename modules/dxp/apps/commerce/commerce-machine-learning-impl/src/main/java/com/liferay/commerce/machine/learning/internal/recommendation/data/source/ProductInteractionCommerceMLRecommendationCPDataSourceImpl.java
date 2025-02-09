/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.machine.learning.internal.recommendation.data.source;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.machine.learning.recommendation.model.ProductInteractionCommerceMLRecommendation;
import com.liferay.commerce.machine.learning.recommendation.service.ProductInteractionCommerceMLRecommendationService;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.constants.CPWebKeys;
import com.liferay.commerce.product.data.source.CPDataSource;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	property = "commerce.product.data.source.name=" + ProductInteractionCommerceMLRecommendationCPDataSourceImpl.NAME,
	service = CPDataSource.class
)
public class ProductInteractionCommerceMLRecommendationCPDataSourceImpl
	extends BaseCommerceMLRecommendationCPDataSource {

	public static final String NAME =
		"productInteractionCommerceMLRecommendationDataSource";

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			getResourceBundle(locale),
			"product-interaction-based-recommendations");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public CPDataSourceResult getResult(
			HttpServletRequest httpServletRequest, int start, int end)
		throws Exception {

		CommerceAccount commerceAccount =
			commerceAccountHelper.getCurrentCommerceAccount(httpServletRequest);

		if (commerceAccount == null) {
			return new CPDataSourceResult(Collections.emptyList(), 0);
		}

		CPCatalogEntry cpCatalogEntry =
			(CPCatalogEntry)httpServletRequest.getAttribute(
				CPWebKeys.CP_CATALOG_ENTRY);

		if (cpCatalogEntry == null) {
			return new CPDataSourceResult(Collections.emptyList(), 0);
		}

		List<ProductInteractionCommerceMLRecommendation>
			productInteractionCommerceMLRecommendations =
				_productInteractionCommerceMLRecommendationService.
					getProductInteractionCommerceMLRecommendations(
						portal.getCompanyId(httpServletRequest),
						cpCatalogEntry.getCPDefinitionId());

		if (productInteractionCommerceMLRecommendations.isEmpty()) {
			return new CPDataSourceResult(Collections.emptyList(), 0);
		}

		long groupId = portal.getScopeGroupId(httpServletRequest);

		List<CPCatalogEntry> cpCatalogEntries = new ArrayList<>();

		List<ProductInteractionCommerceMLRecommendation>
			productInteractionCommerceMLRecommendationList = ListUtil.subList(
				productInteractionCommerceMLRecommendations, start, end);

		for (ProductInteractionCommerceMLRecommendation
				productInteractionCommerceMLRecommendation :
					productInteractionCommerceMLRecommendationList) {

			long recommendedEntryClassPK =
				productInteractionCommerceMLRecommendation.
					getRecommendedEntryClassPK();

			if (_log.isTraceEnabled()) {
				StringBuilder sb = new StringBuilder();

				sb.append("Recommended item: ");
				sb.append(recommendedEntryClassPK);
				sb.append(" rank: ");
				sb.append(productInteractionCommerceMLRecommendation.getRank());
				sb.append(" score: ");
				sb.append(
					productInteractionCommerceMLRecommendation.getScore());

				_log.trace(sb.toString());
			}

			try {
				CPCatalogEntry recommendedCPCatalogEntry =
					cpDefinitionHelper.getCPCatalogEntry(
						commerceAccount.getCommerceAccountId(), groupId,
						recommendedEntryClassPK,
						portal.getLocale(httpServletRequest));

				cpCatalogEntries.add(recommendedCPCatalogEntry);
			}
			catch (PortalException portalException) {
				if (_log.isDebugEnabled()) {
					_log.debug(portalException, portalException);
				}
			}
		}

		return new CPDataSourceResult(
			cpCatalogEntries,
			productInteractionCommerceMLRecommendations.size());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProductInteractionCommerceMLRecommendationCPDataSourceImpl.class);

	@Reference(unbind = "-")
	private ProductInteractionCommerceMLRecommendationService
		_productInteractionCommerceMLRecommendationService;

}