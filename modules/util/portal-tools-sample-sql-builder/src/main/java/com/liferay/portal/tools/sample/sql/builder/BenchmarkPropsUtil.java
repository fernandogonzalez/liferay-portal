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

package com.liferay.portal.tools.sample.sql.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.io.FileReader;
import java.io.Reader;

import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

/**
 * @author Lily Chi
 */
public class BenchmarkPropsUtil {

	public static String get(String key) {
		return _properties.getProperty(key);
	}

	public static String getActualPropertiesContent() {
		List<String> propertyNames = new ArrayList<>(
			_properties.stringPropertyNames());

		propertyNames.sort(null);

		StringBundler sb = new StringBundler(propertyNames.size() * 4);

		for (String propertyName : propertyNames) {
			if (!propertyName.startsWith("sample.sql")) {
				continue;
			}

			sb.append(propertyName);
			sb.append(StringPool.EQUAL);
			sb.append(_properties.getProperty(propertyName));
			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	private static final Properties _properties;

	static {
		Properties properties = new Properties();

		try (Reader reader = new FileReader(
				System.getProperty("sample-sql-properties"))) {

			properties.load(reader);

			String timeZoneId = properties.getProperty(
				"sample.sql.db.time.zone");

			if (Validator.isNull(timeZoneId)) {
				TimeZone timeZone = TimeZone.getDefault();

				properties.setProperty(
					"sample.sql.db.time.zone", timeZone.getID());
			}
			else {
				TimeZone.setDefault(
					TimeZone.getTimeZone(ZoneId.of(timeZoneId)));
			}
		}
		catch (Exception exception) {
			throw new ExceptionInInitializerError(exception);
		}

		_properties = properties;
	}

}