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

package com.liferay.trash.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.trash.model.TrashEntry;
import com.liferay.trash.model.TrashEntryModel;
import com.liferay.trash.model.TrashEntrySoap;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the TrashEntry service. Represents a row in the &quot;TrashEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>TrashEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link TrashEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TrashEntryImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class TrashEntryModelImpl
	extends BaseModelImpl<TrashEntry> implements TrashEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a trash entry model instance should use the <code>TrashEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "TrashEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"entryId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"classNameId", Types.BIGINT}, {"classPK", Types.BIGINT},
		{"systemEventSetKey", Types.BIGINT}, {"typeSettings", Types.CLOB},
		{"status", Types.INTEGER}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("entryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("systemEventSetKey", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("typeSettings", Types.CLOB);
		TABLE_COLUMNS_MAP.put("status", Types.INTEGER);
	}

	public static final String TABLE_SQL_CREATE =
		"create table TrashEntry (entryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,classNameId LONG,classPK LONG,systemEventSetKey LONG,typeSettings TEXT null,status INTEGER)";

	public static final String TABLE_SQL_DROP = "drop table TrashEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY trashEntry.createDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY TrashEntry.createDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final long CLASSNAMEID_COLUMN_BITMASK = 1L;

	public static final long CLASSPK_COLUMN_BITMASK = 2L;

	public static final long COMPANYID_COLUMN_BITMASK = 4L;

	public static final long CREATEDATE_COLUMN_BITMASK = 8L;

	public static final long GROUPID_COLUMN_BITMASK = 16L;

	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
		_entityCacheEnabled = entityCacheEnabled;
	}

	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
		_finderCacheEnabled = finderCacheEnabled;
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static TrashEntry toModel(TrashEntrySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		TrashEntry model = new TrashEntryImpl();

		model.setEntryId(soapModel.getEntryId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setSystemEventSetKey(soapModel.getSystemEventSetKey());
		model.setTypeSettings(soapModel.getTypeSettings());
		model.setStatus(soapModel.getStatus());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<TrashEntry> toModels(TrashEntrySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<TrashEntry> models = new ArrayList<TrashEntry>(soapModels.length);

		for (TrashEntrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public TrashEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _entryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _entryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return TrashEntry.class;
	}

	@Override
	public String getModelClassName() {
		return TrashEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<TrashEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<TrashEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<TrashEntry, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((TrashEntry)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<TrashEntry, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<TrashEntry, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(TrashEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<TrashEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<TrashEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, TrashEntry>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			TrashEntry.class.getClassLoader(), TrashEntry.class,
			ModelWrapper.class);

		try {
			Constructor<TrashEntry> constructor =
				(Constructor<TrashEntry>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException roe) {
					throw new InternalError(roe);
				}
			};
		}
		catch (NoSuchMethodException nsme) {
			throw new InternalError(nsme);
		}
	}

	private static final Map<String, Function<TrashEntry, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<TrashEntry, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<TrashEntry, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<TrashEntry, Object>>();
		Map<String, BiConsumer<TrashEntry, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<TrashEntry, ?>>();

		attributeGetterFunctions.put("entryId", TrashEntry::getEntryId);
		attributeSetterBiConsumers.put(
			"entryId", (BiConsumer<TrashEntry, Long>)TrashEntry::setEntryId);
		attributeGetterFunctions.put("groupId", TrashEntry::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId", (BiConsumer<TrashEntry, Long>)TrashEntry::setGroupId);
		attributeGetterFunctions.put("companyId", TrashEntry::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<TrashEntry, Long>)TrashEntry::setCompanyId);
		attributeGetterFunctions.put("userId", TrashEntry::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<TrashEntry, Long>)TrashEntry::setUserId);
		attributeGetterFunctions.put("userName", TrashEntry::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<TrashEntry, String>)TrashEntry::setUserName);
		attributeGetterFunctions.put("createDate", TrashEntry::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<TrashEntry, Date>)TrashEntry::setCreateDate);
		attributeGetterFunctions.put("classNameId", TrashEntry::getClassNameId);
		attributeSetterBiConsumers.put(
			"classNameId",
			(BiConsumer<TrashEntry, Long>)TrashEntry::setClassNameId);
		attributeGetterFunctions.put("classPK", TrashEntry::getClassPK);
		attributeSetterBiConsumers.put(
			"classPK", (BiConsumer<TrashEntry, Long>)TrashEntry::setClassPK);
		attributeGetterFunctions.put(
			"systemEventSetKey", TrashEntry::getSystemEventSetKey);
		attributeSetterBiConsumers.put(
			"systemEventSetKey",
			(BiConsumer<TrashEntry, Long>)TrashEntry::setSystemEventSetKey);
		attributeGetterFunctions.put(
			"typeSettings", TrashEntry::getTypeSettings);
		attributeSetterBiConsumers.put(
			"typeSettings",
			(BiConsumer<TrashEntry, String>)TrashEntry::setTypeSettings);
		attributeGetterFunctions.put("status", TrashEntry::getStatus);
		attributeSetterBiConsumers.put(
			"status", (BiConsumer<TrashEntry, Integer>)TrashEntry::setStatus);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getEntryId() {
		return _entryId;
	}

	@Override
	public void setEntryId(long entryId) {
		_entryId = entryId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
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
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
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
		_columnBitmask = -1L;

		if (_originalCreateDate == null) {
			_originalCreateDate = _createDate;
		}

		_createDate = createDate;
	}

	public Date getOriginalCreateDate() {
		return _originalCreateDate;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return "";
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@JSON
	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@JSON
	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

	@JSON
	@Override
	public long getSystemEventSetKey() {
		return _systemEventSetKey;
	}

	@Override
	public void setSystemEventSetKey(long systemEventSetKey) {
		_systemEventSetKey = systemEventSetKey;
	}

	@JSON
	@Override
	public String getTypeSettings() {
		if (_typeSettings == null) {
			return "";
		}
		else {
			return _typeSettings;
		}
	}

	@Override
	public void setTypeSettings(String typeSettings) {
		_typeSettings = typeSettings;
	}

	@JSON
	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setStatus(int status) {
		_status = status;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), TrashEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public TrashEntry toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, TrashEntry>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		TrashEntryImpl trashEntryImpl = new TrashEntryImpl();

		trashEntryImpl.setEntryId(getEntryId());
		trashEntryImpl.setGroupId(getGroupId());
		trashEntryImpl.setCompanyId(getCompanyId());
		trashEntryImpl.setUserId(getUserId());
		trashEntryImpl.setUserName(getUserName());
		trashEntryImpl.setCreateDate(getCreateDate());
		trashEntryImpl.setClassNameId(getClassNameId());
		trashEntryImpl.setClassPK(getClassPK());
		trashEntryImpl.setSystemEventSetKey(getSystemEventSetKey());
		trashEntryImpl.setTypeSettings(getTypeSettings());
		trashEntryImpl.setStatus(getStatus());

		trashEntryImpl.resetOriginalValues();

		return trashEntryImpl;
	}

	@Override
	public int compareTo(TrashEntry trashEntry) {
		int value = 0;

		value = DateUtil.compareTo(getCreateDate(), trashEntry.getCreateDate());

		value = value * -1;

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

		if (!(obj instanceof TrashEntry)) {
			return false;
		}

		TrashEntry trashEntry = (TrashEntry)obj;

		long primaryKey = trashEntry.getPrimaryKey();

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
		return _entityCacheEnabled;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	@Override
	public void resetOriginalValues() {
		TrashEntryModelImpl trashEntryModelImpl = this;

		trashEntryModelImpl._originalGroupId = trashEntryModelImpl._groupId;

		trashEntryModelImpl._setOriginalGroupId = false;

		trashEntryModelImpl._originalCompanyId = trashEntryModelImpl._companyId;

		trashEntryModelImpl._setOriginalCompanyId = false;

		trashEntryModelImpl._originalCreateDate =
			trashEntryModelImpl._createDate;

		trashEntryModelImpl._originalClassNameId =
			trashEntryModelImpl._classNameId;

		trashEntryModelImpl._setOriginalClassNameId = false;

		trashEntryModelImpl._originalClassPK = trashEntryModelImpl._classPK;

		trashEntryModelImpl._setOriginalClassPK = false;

		trashEntryModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<TrashEntry> toCacheModel() {
		TrashEntryCacheModel trashEntryCacheModel = new TrashEntryCacheModel();

		trashEntryCacheModel.entryId = getEntryId();

		trashEntryCacheModel.groupId = getGroupId();

		trashEntryCacheModel.companyId = getCompanyId();

		trashEntryCacheModel.userId = getUserId();

		trashEntryCacheModel.userName = getUserName();

		String userName = trashEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			trashEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			trashEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			trashEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		trashEntryCacheModel.classNameId = getClassNameId();

		trashEntryCacheModel.classPK = getClassPK();

		trashEntryCacheModel.systemEventSetKey = getSystemEventSetKey();

		trashEntryCacheModel.typeSettings = getTypeSettings();

		String typeSettings = trashEntryCacheModel.typeSettings;

		if ((typeSettings != null) && (typeSettings.length() == 0)) {
			trashEntryCacheModel.typeSettings = null;
		}

		trashEntryCacheModel.status = getStatus();

		return trashEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<TrashEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<TrashEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<TrashEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((TrashEntry)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<TrashEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<TrashEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<TrashEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((TrashEntry)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, TrashEntry>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private static boolean _entityCacheEnabled;
	private static boolean _finderCacheEnabled;

	private long _entryId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _originalCreateDate;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private long _systemEventSetKey;
	private String _typeSettings;
	private int _status;
	private long _columnBitmask;
	private TrashEntry _escapedModel;

}