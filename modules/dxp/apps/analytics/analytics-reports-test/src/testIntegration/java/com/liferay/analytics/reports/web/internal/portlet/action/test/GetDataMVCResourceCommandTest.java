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

package com.liferay.analytics.reports.web.internal.portlet.action.test;

import com.liferay.analytics.reports.test.MockObject;
import com.liferay.analytics.reports.test.analytics.reports.info.item.MockAnalyticsReportsInfoItem;
import com.liferay.analytics.reports.test.info.item.provider.MockInfoItemFieldValuesProvider;
import com.liferay.analytics.reports.test.layout.display.page.MockLayoutDisplayPageProvider;
import com.liferay.analytics.reports.test.util.MockContextUtil;
import com.liferay.analytics.reports.web.internal.portlet.action.test.util.MockHttpUtil;
import com.liferay.analytics.reports.web.internal.portlet.action.test.util.MockThemeDisplayUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.info.item.InfoItemReference;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
import com.liferay.layout.display.page.constants.LayoutDisplayPageWebKeys;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceResponse;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PrefsPropsImpl;

import java.io.ByteArrayOutputStream;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Date;
import java.util.Dictionary;
import java.util.Objects;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class GetDataMVCResourceCommandTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addLayout(_group);
	}

	@Test
	public void testGetAuthorWithoutPortraitURL() throws Exception {
		MockContextUtil.testWithMockContext(
			new MockContextUtil.MockContext.Builder(
				_classNameLocalService
			).analyticsReportsInfoItem(
				MockAnalyticsReportsInfoItem.builder(
				).build()
			).infoItemFieldValuesProvider(
				MockInfoItemFieldValuesProvider.builder(
				).authorName(
					RandomTestUtil.randomString()
				).authorProfileImage(
					RandomTestUtil.randomString() + "?img_id=0"
				).build()
			).layoutDisplayPageProvider(
				MockLayoutDisplayPageProvider.builder(
					_classNameLocalService
				).build()
			).build(),
			() -> {
				MockLiferayResourceResponse mockLiferayResourceResponse =
					new MockLiferayResourceResponse();

				_mvcResourceCommand.serveResource(
					_getMockLiferayResourceRequest(),
					mockLiferayResourceResponse);

				ByteArrayOutputStream byteArrayOutputStream =
					(ByteArrayOutputStream)
						mockLiferayResourceResponse.getPortletOutputStream();

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					new String(byteArrayOutputStream.toByteArray()));

				JSONObject contextJSONObject = jsonObject.getJSONObject(
					"context");

				JSONObject authorJSONObject = contextJSONObject.getJSONObject(
					"author");

				Assert.assertNull(authorJSONObject.get("url"));
			});
	}

	@Test
	public void testGetContext() throws Exception {
		String authorName = RandomTestUtil.randomString();
		String authorProfileImage =
			RandomTestUtil.randomString() + "?img_id=10";
		Date publishDate = new Date();
		String title = RandomTestUtil.randomString();

		MockContextUtil.testWithMockContext(
			new MockContextUtil.MockContext.Builder(
				_classNameLocalService
			).analyticsReportsInfoItem(
				MockAnalyticsReportsInfoItem.builder(
				).publishDate(
					publishDate
				).build()
			).infoItemFieldValuesProvider(
				MockInfoItemFieldValuesProvider.builder(
				).authorName(
					authorName
				).authorProfileImage(
					authorProfileImage
				).build()
			).layoutDisplayPageProvider(
				MockLayoutDisplayPageProvider.builder(
					_classNameLocalService
				).title(
					title
				).build()
			).build(),
			() -> {
				MockLiferayResourceResponse mockLiferayResourceResponse =
					new MockLiferayResourceResponse();

				_mvcResourceCommand.serveResource(
					_getMockLiferayResourceRequest(),
					mockLiferayResourceResponse);

				ByteArrayOutputStream byteArrayOutputStream =
					(ByteArrayOutputStream)
						mockLiferayResourceResponse.getPortletOutputStream();

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					new String(byteArrayOutputStream.toByteArray()));

				JSONObject contextJSONObject = jsonObject.getJSONObject(
					"context");

				JSONObject authorJSONObject = contextJSONObject.getJSONObject(
					"author");

				Assert.assertEquals(authorName, authorJSONObject.get("alt"));
				Assert.assertEquals(
					authorProfileImage, authorJSONObject.get("url"));

				Instant instant = publishDate.toInstant();

				ZonedDateTime zonedDateTime = instant.atZone(
					ZoneId.systemDefault());

				LocalDate localDate = LocalDate.from(
					DateTimeFormatter.ISO_DATE.parse(
						contextJSONObject.getString("publishDate")));

				Assert.assertEquals(zonedDateTime.toLocalDate(), localDate);

				Assert.assertEquals(
					title, contextJSONObject.getString("title"));

				JSONArray jsonArray = contextJSONObject.getJSONArray(
					"viewURLs");

				Assert.assertEquals(
					String.valueOf(jsonArray), jsonArray.length(), 1);

				JSONObject viewURLJSONObject = jsonArray.getJSONObject(0);

				Assert.assertEquals(
					Boolean.TRUE, viewURLJSONObject.getBoolean("default"));

				Assert.assertEquals(
					LocaleUtil.toBCP47LanguageId(LocaleUtil.getDefault()),
					viewURLJSONObject.getString("languageId"));

				String viewURL = viewURLJSONObject.getString("viewURL");

				Assert.assertTrue(
					viewURL.contains(
						"param_languageId=" +
							LocaleUtil.toLanguageId(LocaleUtil.getDefault())));
			});
	}

	@Test
	public void testGetTrafficSources() throws Exception {
		PrefsProps prefsProps = PrefsPropsUtil.getPrefsProps();

		ValidPrefsPropsWrapper validPrefsPropsWrapper =
			new ValidPrefsPropsWrapper(prefsProps);

		ReflectionTestUtil.setFieldValue(
			PrefsPropsUtil.class, "_prefsProps", validPrefsPropsWrapper);

		String dataSourceId = validPrefsPropsWrapper.getString(
			RandomTestUtil.nextLong(), "liferayAnalyticsDataSourceId");

		ReflectionTestUtil.setFieldValue(
			_mvcResourceCommand, "_http",
			MockHttpUtil.geHttp(
				HashMapBuilder.<String, UnsafeSupplier<String, Exception>>put(
					"/api/1.0/data-sources/" + dataSourceId,
					() -> StringPool.BLANK
				).put(
					"/api/seo/1.0/traffic-sources",
					() -> JSONUtil.put(
						JSONUtil.put(
							"countryKeywords",
							JSONUtil.put(
								JSONUtil.put(
									"countryCode", "us"
								).put(
									"countryName", "United States"
								).put(
									"keywords",
									JSONUtil.put(
										JSONUtil.put(
											"keyword", "liferay"
										).put(
											"position", 1
										).put(
											"searchVolume", 3600
										).put(
											"traffic", 2880L
										))
								))
						).put(
							"name", "organic"
						).put(
							"trafficAmount", 3192L
						).put(
							"trafficShare", 93.93D
						)
					).toString()
				).build()));

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("trafficSourcesEnabled", true);

		try {
			MockContextUtil.testWithMockContext(
				MockContextUtil.MockContext.builder(
					_classNameLocalService
				).build(),
				() -> {
					MockLiferayResourceResponse mockLiferayResourceResponse =
						new MockLiferayResourceResponse();

					_mvcResourceCommand.serveResource(
						_getMockLiferayResourceRequest(),
						mockLiferayResourceResponse);

					ByteArrayOutputStream byteArrayOutputStream =
						(ByteArrayOutputStream)
							mockLiferayResourceResponse.
								getPortletOutputStream();

					JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
						new String(byteArrayOutputStream.toByteArray()));

					JSONObject contextJSONObject = jsonObject.getJSONObject(
						"context");

					JSONArray jsonArray = contextJSONObject.getJSONArray(
						"trafficSources");

					Assert.assertEquals(2, jsonArray.length());

					JSONObject jsonObject1 = jsonArray.getJSONObject(0);

					Assert.assertEquals("paid", jsonObject1.get("name"));

					Assert.assertNull(jsonObject1.get("value"));

					JSONObject jsonObject2 = jsonArray.getJSONObject(1);

					Assert.assertEquals("organic", jsonObject2.get("name"));

					Assert.assertEquals(93.93D, jsonObject2.get("share"));

					Assert.assertEquals(3192, jsonObject2.get("value"));

					JSONArray countryKeywordsJSONArray =
						(JSONArray)jsonObject2.get("countryKeywords");

					Assert.assertEquals(
						JSONUtil.put(
							JSONUtil.put(
								"countryCode", "us"
							).put(
								"countryName", "United States"
							).put(
								"keywords",
								JSONUtil.put(
									JSONUtil.put(
										"keyword", "liferay"
									).put(
										"position", 1
									).put(
										"searchVolume", 3600
									).put(
										"traffic", 2880
									))
							)
						).toString(),
						countryKeywordsJSONArray.toString());
				});
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				PrefsPropsUtil.class, "_prefsProps", prefsProps);

			ReflectionTestUtil.setFieldValue(
				_mvcResourceCommand, "_http", _http);
		}
	}

	@Test
	public void testGetTrafficSourcesWithInvalidConnection() throws Exception {
		PrefsProps prefsProps = PrefsPropsUtil.getPrefsProps();

		InvalidPropsWrapper invalidPropsWrapper = new InvalidPropsWrapper(
			prefsProps);

		ReflectionTestUtil.setFieldValue(
			PrefsPropsUtil.class, "_prefsProps", invalidPropsWrapper);

		try {
			MockContextUtil.testWithMockContext(
				MockContextUtil.MockContext.builder(
					_classNameLocalService
				).build(),
				() -> {
					MockLiferayResourceResponse mockLiferayResourceResponse =
						new MockLiferayResourceResponse();

					_mvcResourceCommand.serveResource(
						_getMockLiferayResourceRequest(),
						mockLiferayResourceResponse);

					ByteArrayOutputStream byteArrayOutputStream =
						(ByteArrayOutputStream)
							mockLiferayResourceResponse.
								getPortletOutputStream();

					JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
						new String(byteArrayOutputStream.toByteArray()));

					JSONObject contextJSONObject = jsonObject.getJSONObject(
						"context");

					JSONArray jsonArray = contextJSONObject.getJSONArray(
						"trafficSources");

					ResourceBundle resourceBundle =
						ResourceBundleUtil.getBundle(
							LocaleUtil.getDefault(),
							_mvcResourceCommand.getClass());

					Assert.assertEquals(
						JSONUtil.putAll(
							JSONUtil.put(
								"helpMessage",
								ResourceBundleUtil.getString(
									resourceBundle,
									"this-number-refers-to-the-volume-of-" +
										"people-that-find-your-page-through-" +
											"paid-keywords")
							).put(
								"name", "paid"
							).put(
								"title",
								ResourceBundleUtil.getString(
									resourceBundle, "paid")
							),
							JSONUtil.put(
								"helpMessage",
								ResourceBundleUtil.getString(
									resourceBundle,
									"this-number-refers-to-the-volume-of-" +
										"people-that-find-your-page-through-" +
											"a-search-engine")
							).put(
								"name", "organic"
							).put(
								"title",
								ResourceBundleUtil.getString(
									resourceBundle, "organic")
							)
						).toJSONString(),
						jsonArray.toJSONString());
				});
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				PrefsPropsUtil.class, "_prefsProps", prefsProps);

			ReflectionTestUtil.setFieldValue(
				_mvcResourceCommand, "_http", _http);
		}
	}

	@Test
	public void testGetViewURLs() throws Exception {
		MockContextUtil.testWithMockContext(
			MockContextUtil.MockContext.builder(
				_classNameLocalService
			).build(),
			() -> {
				MockLiferayResourceResponse mockLiferayResourceResponse =
					new MockLiferayResourceResponse();

				_mvcResourceCommand.serveResource(
					_getMockLiferayResourceRequest(),
					mockLiferayResourceResponse);

				ByteArrayOutputStream byteArrayOutputStream =
					(ByteArrayOutputStream)
						mockLiferayResourceResponse.getPortletOutputStream();

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					new String(byteArrayOutputStream.toByteArray()));

				JSONObject contextJSONObject = jsonObject.getJSONObject(
					"context");

				JSONArray jsonArray = contextJSONObject.getJSONArray(
					"viewURLs");

				Assert.assertEquals(
					String.valueOf(jsonArray), jsonArray.length(), 1);

				JSONObject viewURLJSONObject = jsonArray.getJSONObject(0);

				Assert.assertEquals(
					Boolean.TRUE, viewURLJSONObject.getBoolean("default"));

				Assert.assertEquals(
					LocaleUtil.toBCP47LanguageId(LocaleUtil.getDefault()),
					viewURLJSONObject.getString("languageId"));

				String viewURL = viewURLJSONObject.getString("viewURL");

				Assert.assertTrue(
					viewURL.contains(
						"param_languageId=" +
							LocaleUtil.toLanguageId(LocaleUtil.getDefault())));
			});
	}

	@Test
	public void testGetViewURLsWithMultipleLocales() throws Exception {
		MockContextUtil.testWithMockContext(
			MockContextUtil.MockContext.builder(
				_classNameLocalService
			).analyticsReportsInfoItem(
				MockAnalyticsReportsInfoItem.builder(
				).locales(
					Arrays.asList(LocaleUtil.SPAIN, LocaleUtil.US)
				).build()
			).build(),
			() -> {
				MockLiferayResourceResponse mockLiferayResourceResponse =
					new MockLiferayResourceResponse();

				_mvcResourceCommand.serveResource(
					_getMockLiferayResourceRequest(),
					mockLiferayResourceResponse);

				ByteArrayOutputStream byteArrayOutputStream =
					(ByteArrayOutputStream)
						mockLiferayResourceResponse.getPortletOutputStream();

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					new String(byteArrayOutputStream.toByteArray()));

				JSONObject contextJSONObject = jsonObject.getJSONObject(
					"context");

				JSONArray jsonArray = contextJSONObject.getJSONArray(
					"viewURLs");

				Assert.assertEquals(
					String.valueOf(jsonArray), jsonArray.length(), 2);

				JSONObject viewURLJSONObject1 = jsonArray.getJSONObject(0);

				Assert.assertEquals(
					Boolean.TRUE, viewURLJSONObject1.getBoolean("default"));
				Assert.assertEquals(
					LocaleUtil.toBCP47LanguageId(LocaleUtil.SPAIN),
					viewURLJSONObject1.getString("languageId"));

				String viewURL1 = viewURLJSONObject1.getString("viewURL");

				Assert.assertTrue(
					viewURL1.contains(
						"param_languageId=" +
							LocaleUtil.toLanguageId(LocaleUtil.SPAIN)));

				JSONObject viewURLJSONObject2 = jsonArray.getJSONObject(1);

				Assert.assertEquals(
					Boolean.FALSE, viewURLJSONObject2.getBoolean("default"));
				Assert.assertEquals(
					LocaleUtil.toBCP47LanguageId(LocaleUtil.US),
					viewURLJSONObject2.getString("languageId"));

				String viewURL2 = viewURLJSONObject2.getString("viewURL");

				Assert.assertTrue(
					viewURL2.contains(
						"param_languageId=" +
							LocaleUtil.toLanguageId(LocaleUtil.US)));
			});
	}

	private MockLiferayResourceRequest _getMockLiferayResourceRequest() {
		MockLiferayResourceRequest mockLiferayResourceRequest =
			new MockLiferayResourceRequest();

		try {
			LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
				_layoutDisplayPageProviderTracker.
					getLayoutDisplayPageProviderByClassName(
						MockObject.class.getName());

			mockLiferayResourceRequest.setAttribute(
				LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER,
				layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
					new InfoItemReference(MockObject.class.getName(), 0)));

			mockLiferayResourceRequest.setAttribute(
				WebKeys.THEME_DISPLAY,
				MockThemeDisplayUtil.getThemeDisplay(
					_companyLocalService.getCompany(
						TestPropsValues.getCompanyId()),
					_group, _layout,
					_layoutSetLocalService.getLayoutSet(
						_group.getGroupId(), false)));

			return mockLiferayResourceRequest;
		}
		catch (PortalException portalException) {
			throw new AssertionError(portalException);
		}
	}

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private Http _http;

	@Inject
	private Language _language;

	private Layout _layout;

	@Inject
	private LayoutDisplayPageProviderTracker _layoutDisplayPageProviderTracker;

	@Inject
	private LayoutSetLocalService _layoutSetLocalService;

	@Inject(filter = "mvc.command.name=/analytics_reports/get_data")
	private MVCResourceCommand _mvcResourceCommand;

	@Inject
	private Portal _portal;

	private class InvalidPropsWrapper extends PrefsPropsImpl {

		public InvalidPropsWrapper(PrefsProps prefsProps) {
			_prefsProps = prefsProps;
		}

		@Override
		public String getString(long companyId, String name) {
			if (Objects.equals("liferayAnalyticsDataSourceId", name) ||
				Objects.equals(
					name, "liferayAnalyticsFaroBackendSecuritySignature") ||
				Objects.equals("liferayAnalyticsFaroBackendURL", name)) {

				return null;
			}

			return _prefsProps.getString(companyId, name);
		}

		private final PrefsProps _prefsProps;

	}

	private class ValidPrefsPropsWrapper extends PrefsPropsImpl {

		public ValidPrefsPropsWrapper(PrefsProps prefsProps) {
			_prefsProps = prefsProps;
		}

		@Override
		public String getString(long companyId, String name) {
			if (Objects.equals("liferayAnalyticsDataSourceId", name) ||
				Objects.equals(
					name, "liferayAnalyticsFaroBackendSecuritySignature") ||
				Objects.equals("liferayAnalyticsFaroBackendURL", name)) {

				return "test";
			}

			return _prefsProps.getString(companyId, name);
		}

		private final PrefsProps _prefsProps;

	}

}