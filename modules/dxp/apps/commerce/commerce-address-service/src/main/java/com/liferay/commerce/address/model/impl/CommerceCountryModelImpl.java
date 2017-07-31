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

package com.liferay.commerce.address.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.address.model.CommerceCountry;
import com.liferay.commerce.address.model.CommerceCountryModel;
import com.liferay.commerce.address.model.CommerceCountrySoap;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the CommerceCountry service. Represents a row in the &quot;CommerceCountry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link CommerceCountryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CommerceCountryImpl}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommerceCountryImpl
 * @see CommerceCountry
 * @see CommerceCountryModel
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class CommerceCountryModelImpl extends BaseModelImpl<CommerceCountry>
	implements CommerceCountryModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a commerce country model instance should use the {@link CommerceCountry} interface instead.
	 */
	public static final String TABLE_NAME = "CommerceCountry";
	public static final Object[][] TABLE_COLUMNS = {
			{ "commerceCountryId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "name", Types.VARCHAR },
			{ "allowsBilling", Types.BOOLEAN },
			{ "allowsShipping", Types.BOOLEAN },
			{ "twoLettersISOCode", Types.VARCHAR },
			{ "threeLettersISOCode", Types.VARCHAR },
			{ "numericISOCode", Types.INTEGER },
			{ "priority", Types.INTEGER },
			{ "published", Types.BOOLEAN }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("commerceCountryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("allowsBilling", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("allowsShipping", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("twoLettersISOCode", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("threeLettersISOCode", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("numericISOCode", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("priority", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("published", Types.BOOLEAN);
	}

	public static final String TABLE_SQL_CREATE = "create table CommerceCountry (commerceCountryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,allowsBilling BOOLEAN,allowsShipping BOOLEAN,twoLettersISOCode VARCHAR(75) null,threeLettersISOCode VARCHAR(75) null,numericISOCode INTEGER,priority INTEGER,published BOOLEAN)";
	public static final String TABLE_SQL_DROP = "drop table CommerceCountry";
	public static final String ORDER_BY_JPQL = " ORDER BY commerceCountry.name ASC, commerceCountry.priority ASC";
	public static final String ORDER_BY_SQL = " ORDER BY CommerceCountry.name ASC, CommerceCountry.priority ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.commerce.address.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.commerce.address.model.CommerceCountry"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.commerce.address.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.commerce.address.model.CommerceCountry"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = false;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static CommerceCountry toModel(CommerceCountrySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		CommerceCountry model = new CommerceCountryImpl();

		model.setCommerceCountryId(soapModel.getCommerceCountryId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setName(soapModel.getName());
		model.setAllowsBilling(soapModel.getAllowsBilling());
		model.setAllowsShipping(soapModel.getAllowsShipping());
		model.setTwoLettersISOCode(soapModel.getTwoLettersISOCode());
		model.setThreeLettersISOCode(soapModel.getThreeLettersISOCode());
		model.setNumericISOCode(soapModel.getNumericISOCode());
		model.setPriority(soapModel.getPriority());
		model.setPublished(soapModel.getPublished());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<CommerceCountry> toModels(
		CommerceCountrySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<CommerceCountry> models = new ArrayList<CommerceCountry>(soapModels.length);

		for (CommerceCountrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.commerce.address.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.commerce.address.model.CommerceCountry"));

	public CommerceCountryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _commerceCountryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setCommerceCountryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commerceCountryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return CommerceCountry.class;
	}

	@Override
	public String getModelClassName() {
		return CommerceCountry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("commerceCountryId", getCommerceCountryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("allowsBilling", getAllowsBilling());
		attributes.put("allowsShipping", getAllowsShipping());
		attributes.put("twoLettersISOCode", getTwoLettersISOCode());
		attributes.put("threeLettersISOCode", getThreeLettersISOCode());
		attributes.put("numericISOCode", getNumericISOCode());
		attributes.put("priority", getPriority());
		attributes.put("published", getPublished());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long commerceCountryId = (Long)attributes.get("commerceCountryId");

		if (commerceCountryId != null) {
			setCommerceCountryId(commerceCountryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		Boolean allowsBilling = (Boolean)attributes.get("allowsBilling");

		if (allowsBilling != null) {
			setAllowsBilling(allowsBilling);
		}

		Boolean allowsShipping = (Boolean)attributes.get("allowsShipping");

		if (allowsShipping != null) {
			setAllowsShipping(allowsShipping);
		}

		String twoLettersISOCode = (String)attributes.get("twoLettersISOCode");

		if (twoLettersISOCode != null) {
			setTwoLettersISOCode(twoLettersISOCode);
		}

		String threeLettersISOCode = (String)attributes.get(
				"threeLettersISOCode");

		if (threeLettersISOCode != null) {
			setThreeLettersISOCode(threeLettersISOCode);
		}

		Integer numericISOCode = (Integer)attributes.get("numericISOCode");

		if (numericISOCode != null) {
			setNumericISOCode(numericISOCode);
		}

		Integer priority = (Integer)attributes.get("priority");

		if (priority != null) {
			setPriority(priority);
		}

		Boolean published = (Boolean)attributes.get("published");

		if (published != null) {
			setPublished(published);
		}
	}

	@JSON
	@Override
	public long getCommerceCountryId() {
		return _commerceCountryId;
	}

	@Override
	public void setCommerceCountryId(long commerceCountryId) {
		_commerceCountryId = commerceCountryId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@JSON
	@Override
	public boolean getAllowsBilling() {
		return _allowsBilling;
	}

	@JSON
	@Override
	public boolean isAllowsBilling() {
		return _allowsBilling;
	}

	@Override
	public void setAllowsBilling(boolean allowsBilling) {
		_allowsBilling = allowsBilling;
	}

	@JSON
	@Override
	public boolean getAllowsShipping() {
		return _allowsShipping;
	}

	@JSON
	@Override
	public boolean isAllowsShipping() {
		return _allowsShipping;
	}

	@Override
	public void setAllowsShipping(boolean allowsShipping) {
		_allowsShipping = allowsShipping;
	}

	@JSON
	@Override
	public String getTwoLettersISOCode() {
		if (_twoLettersISOCode == null) {
			return StringPool.BLANK;
		}
		else {
			return _twoLettersISOCode;
		}
	}

	@Override
	public void setTwoLettersISOCode(String twoLettersISOCode) {
		_twoLettersISOCode = twoLettersISOCode;
	}

	@JSON
	@Override
	public String getThreeLettersISOCode() {
		if (_threeLettersISOCode == null) {
			return StringPool.BLANK;
		}
		else {
			return _threeLettersISOCode;
		}
	}

	@Override
	public void setThreeLettersISOCode(String threeLettersISOCode) {
		_threeLettersISOCode = threeLettersISOCode;
	}

	@JSON
	@Override
	public int getNumericISOCode() {
		return _numericISOCode;
	}

	@Override
	public void setNumericISOCode(int numericISOCode) {
		_numericISOCode = numericISOCode;
	}

	@JSON
	@Override
	public int getPriority() {
		return _priority;
	}

	@Override
	public void setPriority(int priority) {
		_priority = priority;
	}

	@JSON
	@Override
	public boolean getPublished() {
		return _published;
	}

	@JSON
	@Override
	public boolean isPublished() {
		return _published;
	}

	@Override
	public void setPublished(boolean published) {
		_published = published;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			CommerceCountry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public CommerceCountry toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (CommerceCountry)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		CommerceCountryImpl commerceCountryImpl = new CommerceCountryImpl();

		commerceCountryImpl.setCommerceCountryId(getCommerceCountryId());
		commerceCountryImpl.setGroupId(getGroupId());
		commerceCountryImpl.setCompanyId(getCompanyId());
		commerceCountryImpl.setUserId(getUserId());
		commerceCountryImpl.setUserName(getUserName());
		commerceCountryImpl.setCreateDate(getCreateDate());
		commerceCountryImpl.setModifiedDate(getModifiedDate());
		commerceCountryImpl.setName(getName());
		commerceCountryImpl.setAllowsBilling(getAllowsBilling());
		commerceCountryImpl.setAllowsShipping(getAllowsShipping());
		commerceCountryImpl.setTwoLettersISOCode(getTwoLettersISOCode());
		commerceCountryImpl.setThreeLettersISOCode(getThreeLettersISOCode());
		commerceCountryImpl.setNumericISOCode(getNumericISOCode());
		commerceCountryImpl.setPriority(getPriority());
		commerceCountryImpl.setPublished(getPublished());

		commerceCountryImpl.resetOriginalValues();

		return commerceCountryImpl;
	}

	@Override
	public int compareTo(CommerceCountry commerceCountry) {
		int value = 0;

		value = getName().compareTo(commerceCountry.getName());

		if (value != 0) {
			return value;
		}

		if (getPriority() < commerceCountry.getPriority()) {
			value = -1;
		}
		else if (getPriority() > commerceCountry.getPriority()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommerceCountry)) {
			return false;
		}

		CommerceCountry commerceCountry = (CommerceCountry)obj;

		long primaryKey = commerceCountry.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		CommerceCountryModelImpl commerceCountryModelImpl = this;

		commerceCountryModelImpl._setModifiedDate = false;
	}

	@Override
	public CacheModel<CommerceCountry> toCacheModel() {
		CommerceCountryCacheModel commerceCountryCacheModel = new CommerceCountryCacheModel();

		commerceCountryCacheModel.commerceCountryId = getCommerceCountryId();

		commerceCountryCacheModel.groupId = getGroupId();

		commerceCountryCacheModel.companyId = getCompanyId();

		commerceCountryCacheModel.userId = getUserId();

		commerceCountryCacheModel.userName = getUserName();

		String userName = commerceCountryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			commerceCountryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			commerceCountryCacheModel.createDate = createDate.getTime();
		}
		else {
			commerceCountryCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			commerceCountryCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			commerceCountryCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		commerceCountryCacheModel.name = getName();

		String name = commerceCountryCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			commerceCountryCacheModel.name = null;
		}

		commerceCountryCacheModel.allowsBilling = getAllowsBilling();

		commerceCountryCacheModel.allowsShipping = getAllowsShipping();

		commerceCountryCacheModel.twoLettersISOCode = getTwoLettersISOCode();

		String twoLettersISOCode = commerceCountryCacheModel.twoLettersISOCode;

		if ((twoLettersISOCode != null) && (twoLettersISOCode.length() == 0)) {
			commerceCountryCacheModel.twoLettersISOCode = null;
		}

		commerceCountryCacheModel.threeLettersISOCode = getThreeLettersISOCode();

		String threeLettersISOCode = commerceCountryCacheModel.threeLettersISOCode;

		if ((threeLettersISOCode != null) &&
				(threeLettersISOCode.length() == 0)) {
			commerceCountryCacheModel.threeLettersISOCode = null;
		}

		commerceCountryCacheModel.numericISOCode = getNumericISOCode();

		commerceCountryCacheModel.priority = getPriority();

		commerceCountryCacheModel.published = getPublished();

		return commerceCountryCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(31);

		sb.append("{commerceCountryId=");
		sb.append(getCommerceCountryId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", allowsBilling=");
		sb.append(getAllowsBilling());
		sb.append(", allowsShipping=");
		sb.append(getAllowsShipping());
		sb.append(", twoLettersISOCode=");
		sb.append(getTwoLettersISOCode());
		sb.append(", threeLettersISOCode=");
		sb.append(getThreeLettersISOCode());
		sb.append(", numericISOCode=");
		sb.append(getNumericISOCode());
		sb.append(", priority=");
		sb.append(getPriority());
		sb.append(", published=");
		sb.append(getPublished());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(49);

		sb.append("<model><model-name>");
		sb.append("com.liferay.commerce.address.model.CommerceCountry");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>commerceCountryId</column-name><column-value><![CDATA[");
		sb.append(getCommerceCountryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>allowsBilling</column-name><column-value><![CDATA[");
		sb.append(getAllowsBilling());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>allowsShipping</column-name><column-value><![CDATA[");
		sb.append(getAllowsShipping());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>twoLettersISOCode</column-name><column-value><![CDATA[");
		sb.append(getTwoLettersISOCode());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>threeLettersISOCode</column-name><column-value><![CDATA[");
		sb.append(getThreeLettersISOCode());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>numericISOCode</column-name><column-value><![CDATA[");
		sb.append(getNumericISOCode());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>priority</column-name><column-value><![CDATA[");
		sb.append(getPriority());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>published</column-name><column-value><![CDATA[");
		sb.append(getPublished());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = CommerceCountry.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			CommerceCountry.class
		};
	private long _commerceCountryId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _name;
	private boolean _allowsBilling;
	private boolean _allowsShipping;
	private String _twoLettersISOCode;
	private String _threeLettersISOCode;
	private int _numericISOCode;
	private int _priority;
	private boolean _published;
	private CommerceCountry _escapedModel;
}