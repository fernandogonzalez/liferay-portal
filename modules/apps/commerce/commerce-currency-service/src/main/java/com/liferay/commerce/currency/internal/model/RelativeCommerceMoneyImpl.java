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

package com.liferay.commerce.currency.internal.model;

import com.liferay.commerce.currency.util.CommercePriceFormatter;

import java.math.BigDecimal;

import java.util.Locale;

/**
 * @author Matija Petanjek
 */
public class RelativeCommerceMoneyImpl extends CommerceMoneyImpl {

	public RelativeCommerceMoneyImpl(
		CommercePriceFormatter commercePriceFormatter) {

		super(commercePriceFormatter);

		_commercePriceFormatter = commercePriceFormatter;
	}

	@Override
	public String format(Locale locale) {
		BigDecimal price = getPrice();

		if (price == null) {
			price = BigDecimal.ZERO;
		}

		return _commercePriceFormatter.formatAsRelative(
			getCommerceCurrency(), price, locale);
	}

	private final CommercePriceFormatter _commercePriceFormatter;

}