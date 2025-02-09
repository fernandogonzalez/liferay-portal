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

package com.liferay.headless.commerce.admin.account.internal.resource.v1_0;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.exception.NoSuchAccountException;
import com.liferay.commerce.account.exception.NoSuchAccountGroupException;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.model.CommerceAccountGroupCommerceAccountRel;
import com.liferay.commerce.account.model.CommerceAccountOrganizationRel;
import com.liferay.commerce.account.model.CommerceAccountUserRel;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelService;
import com.liferay.commerce.account.service.CommerceAccountGroupService;
import com.liferay.commerce.account.service.CommerceAccountOrganizationRelService;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.service.CommerceAccountUserRelService;
import com.liferay.commerce.account.service.persistence.CommerceAccountOrganizationRelPK;
import com.liferay.commerce.account.service.persistence.CommerceAccountUserRelPK;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.model.CommerceRegion;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceCountryService;
import com.liferay.commerce.service.CommerceRegionLocalService;
import com.liferay.headless.commerce.admin.account.dto.v1_0.Account;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountAddress;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountMember;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountOrganization;
import com.liferay.headless.commerce.admin.account.internal.dto.v1_0.converter.AccountDTOConverter;
import com.liferay.headless.commerce.admin.account.internal.odata.entity.v1_0.AccountEntityModel;
import com.liferay.headless.commerce.admin.account.internal.util.v1_0.AccountMemberUtil;
import com.liferay.headless.commerce.admin.account.internal.util.v1_0.AccountOrganizationUtil;
import com.liferay.headless.commerce.admin.account.resource.v1_0.AccountResource;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/account.properties",
	scope = ServiceScope.PROTOTYPE, service = AccountResource.class
)
public class AccountResourceImpl
	extends BaseAccountResourceImpl implements EntityModelResource {

	@Override
	public Response deleteAccount(Long id) throws Exception {
		_commerceAccountService.deleteCommerceAccount(id);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response deleteAccountByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with externalReferenceCode: " +
					externalReferenceCode);
		}

		_commerceAccountService.deleteCommerceAccount(
			commerceAccount.getCommerceAccountId());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response deleteAccountGroupByExternalReferenceCodeAccount(
			@NotNull String accountExternalReferenceCode,
			@NotNull String externalReferenceCode)
		throws Exception {

		CommerceAccountGroup commerceAccountGroup =
			_commerceAccountGroupService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccountGroup == null) {
			throw new NoSuchAccountGroupException(
				"Unable to find AccountGroup with externalReferenceCode: " +
					externalReferenceCode);
		}

		CommerceAccount commerceAccount =
			_commerceAccountService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), accountExternalReferenceCode);

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with external reference code: " +
					accountExternalReferenceCode);
		}

		CommerceAccountGroupCommerceAccountRel
			commerceAccountGroupCommerceAccountRel =
				_commerceAccountGroupCommerceAccountRelService.
					getCommerceAccountGroupCommerceAccountRel(
						commerceAccountGroup.getCommerceAccountGroupId(),
						commerceAccount.getCommerceAccountId());

		_commerceAccountGroupCommerceAccountRelService.
			deleteCommerceAccountGroupCommerceAccountRel(
				commerceAccountGroupCommerceAccountRel.
					getCommerceAccountGroupCommerceAccountRelId());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Account getAccount(Long id) throws Exception {
		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				GetterUtil.getLong(id),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Account getAccountByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with externalReferenceCode: " +
					externalReferenceCode);
		}

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				commerceAccount.getCommerceAccountId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Page<Account> getAccountsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			booleanQuery -> booleanQuery.getPreBooleanFilter(), filter,
			CommerceAccount.class, search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			new UnsafeConsumer() {

				public void accept(Object object) throws Exception {
					SearchContext searchContext = (SearchContext)object;

					searchContext.setCompanyId(contextCompany.getCompanyId());
				}

			},
			document -> _toAccount(
				_commerceAccountService.getCommerceAccount(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))),
			sorts);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public Response patchAccount(Long id, Account account) throws Exception {
		_updateAccount(id, account);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response patchAccountByExternalReferenceCode(
			String externalReferenceCode, Account account)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with externalReferenceCode: " +
					externalReferenceCode);
		}

		_updateAccount(commerceAccount.getCommerceAccountId(), account);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Account postAccount(Account account) throws Exception {
		CommerceAccount commerceAccount =
			_commerceAccountService.upsertCommerceAccount(
				account.getName(),
				CommerceAccountConstants.DEFAULT_PARENT_ACCOUNT_ID, true, null,
				_getEmailAddress(account, null), account.getTaxId(),
				GetterUtil.get(
					account.getType(),
					CommerceAccountConstants.ACCOUNT_TYPE_PERSONAL),
				true, account.getExternalReferenceCode(),
				_serviceContextHelper.getServiceContext());

		// Expando

		Map<String, ?> customFields = account.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				contextCompany.getCompanyId(), CommerceAccount.class,
				commerceAccount.getPrimaryKey(), customFields);
		}

		// Update nested resources

		_updateNestedResources(
			account, commerceAccount,
			_serviceContextHelper.getServiceContext());

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				commerceAccount.getCommerceAccountId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Response postAccountByExternalReferenceCodeLogo(
			String externalReferenceCode, MultipartBody multipartBody)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with externalReferenceCode: " +
					externalReferenceCode);
		}

		updateAccountLogo(commerceAccount, multipartBody);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response postAccountGroupByExternalReferenceCodeAccount(
			@NotNull String externalReferenceCode, Account account)
		throws Exception {

		CommerceAccountGroup commerceAccountGroup =
			_commerceAccountGroupService.fetchByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (commerceAccountGroup == null) {
			throw new NoSuchAccountGroupException(
				"Unable to find AccountGroup with externalReferenceCode: " +
					externalReferenceCode);
		}

		CommerceAccount commerceAccount = null;

		if (account.getId() != null) {
			commerceAccount = _commerceAccountService.fetchCommerceAccount(
				account.getId());
		}
		else if (account.getExternalReferenceCode() != null) {
			commerceAccount =
				_commerceAccountService.fetchByExternalReferenceCode(
					contextCompany.getCompanyId(),
					account.getExternalReferenceCode());
		}

		if (commerceAccount == null) {
			throw new NoSuchAccountException(
				"Unable to find Account with external reference code: " +
					account.getExternalReferenceCode());
		}

		_commerceAccountGroupCommerceAccountRelService.
			addCommerceAccountGroupCommerceAccountRel(
				commerceAccountGroup.getCommerceAccountGroupId(),
				commerceAccount.getCommerceAccountId(),
				_serviceContextHelper.getServiceContext());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response postAccountLogo(Long id, MultipartBody multipartBody)
		throws Exception {

		updateAccountLogo(
			_commerceAccountService.getCommerceAccount(id), multipartBody);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	public void updateAccountLogo(
			CommerceAccount commerceAccount, MultipartBody multipartBody)
		throws IOException, PortalException {

		_commerceAccountService.updateCommerceAccount(
			commerceAccount.getCommerceAccountId(), commerceAccount.getName(),
			true, multipartBody.getBinaryFileAsBytes("logo"),
			commerceAccount.getEmail(), commerceAccount.getTaxId(),
			commerceAccount.isActive(),
			_serviceContextHelper.getServiceContext(
				commerceAccount.getCommerceAccountGroupId()));
	}

	private long _getCommerceRegionId(
			CommerceCountry commerceCountry, AccountAddress accountAddress)
		throws Exception {

		if (Validator.isNull(accountAddress.getRegionISOCode()) ||
			(commerceCountry == null)) {

			return 0;
		}

		CommerceRegion commerceRegion =
			_commerceRegionLocalService.getCommerceRegion(
				commerceCountry.getCommerceCountryId(),
				accountAddress.getRegionISOCode());

		return commerceRegion.getCommerceRegionId();
	}

	private String _getEmailAddress(
		Account account, CommerceAccount commerceAccount) {

		String[] emailAddresses = new String[0];

		if (account.getEmailAddresses() != null) {
			emailAddresses = account.getEmailAddresses();
		}

		if (emailAddresses.length > 0) {
			return emailAddresses[0];
		}

		if (commerceAccount == null) {
			return "";
		}

		return commerceAccount.getEmail();
	}

	private Account _toAccount(CommerceAccount commerceAccount)
		throws Exception {

		if (commerceAccount == null) {
			return null;
		}

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				commerceAccount.getCommerceAccountId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	private CommerceAccount _updateAccount(Long id, Account account)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountService.getCommerceAccount(id);

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceAccount.getCommerceAccountGroupId());

		commerceAccount = _commerceAccountService.updateCommerceAccount(
			commerceAccount.getCommerceAccountId(), account.getName(), true,
			null, _getEmailAddress(account, commerceAccount),
			GetterUtil.get(account.getTaxId(), commerceAccount.getTaxId()),
			commerceAccount.isActive(),
			commerceAccount.getDefaultBillingAddressId(),
			commerceAccount.getDefaultShippingAddressId(),
			account.getExternalReferenceCode(), serviceContext);

		// Expando

		Map<String, ?> customFields = account.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), CommerceAccount.class,
				commerceAccount.getPrimaryKey(), customFields);
		}

		// Update nested resources

		_updateNestedResources(account, commerceAccount, serviceContext);

		return commerceAccount;
	}

	private CommerceAccount _updateNestedResources(
			Account account, CommerceAccount commerceAccount,
			ServiceContext serviceContext)
		throws Exception {

		// Account addresses

		AccountAddress[] accountAddresses = account.getAccountAddresses();

		if (accountAddresses != null) {
			List<CommerceAddress> commerceAddresses =
				_commerceAddressService.getCommerceAddresses(
					commerceAccount.getModelClassName(),
					commerceAccount.getCommerceAccountId(), QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			for (CommerceAddress commerceAddress : commerceAddresses) {
				_commerceAddressService.deleteCommerceAddress(
					commerceAddress.getCommerceAddressId());
			}

			for (AccountAddress accountAddress : accountAddresses) {
				CommerceCountry commerceCountry =
					_commerceCountryService.getCommerceCountry(
						commerceAccount.getCompanyId(),
						accountAddress.getCountryISOCode());

				CommerceAddress commerceAddress =
					_commerceAddressService.addCommerceAddress(
						commerceAccount.getModelClassName(),
						commerceAccount.getCommerceAccountId(),
						accountAddress.getName(),
						accountAddress.getDescription(),
						accountAddress.getStreet1(),
						accountAddress.getStreet2(),
						accountAddress.getStreet3(), accountAddress.getCity(),
						accountAddress.getZip(),
						_getCommerceRegionId(commerceCountry, accountAddress),
						commerceCountry.getCommerceCountryId(),
						accountAddress.getPhoneNumber(),
						GetterUtil.get(
							accountAddress.getDefaultBilling(), false),
						GetterUtil.get(
							accountAddress.getDefaultShipping(), false),
						serviceContext);

				if (GetterUtil.get(accountAddress.getDefaultBilling(), false)) {
					_commerceAccountService.updateDefaultBillingAddress(
						commerceAccount.getCommerceAccountId(),
						commerceAddress.getCommerceAddressId());
				}

				if (GetterUtil.get(
						accountAddress.getDefaultShipping(), false)) {

					_commerceAccountService.updateDefaultShippingAddress(
						commerceAccount.getCommerceAccountId(),
						commerceAddress.getCommerceAddressId());
				}
			}
		}

		// Account members

		AccountMember[] accountMembers = account.getAccountMembers();

		if (accountMembers != null) {
			for (AccountMember accountMember : accountMembers) {
				User user = AccountMemberUtil.getUser(
					_userLocalService, accountMember,
					contextCompany.getCompanyId());

				CommerceAccountUserRel commerceAccountUserRel =
					_commerceAccountUserRelService.fetchCommerceAccountUserRel(
						new CommerceAccountUserRelPK(
							commerceAccount.getCommerceAccountId(),
							user.getUserId()));

				if (commerceAccountUserRel != null) {
					continue;
				}

				AccountMemberUtil.addCommerceAccountUserRel(
					_commerceAccountUserRelService, accountMember,
					commerceAccount, user, serviceContext);
			}
		}

		// Account organizations

		AccountOrganization[] accountOrganizations =
			account.getAccountOrganizations();

		if (accountOrganizations != null) {
			for (AccountOrganization accountOrganization :
					accountOrganizations) {

				long organizationId = AccountOrganizationUtil.getOrganizationId(
					_organizationLocalService, accountOrganization,
					contextCompany.getCompanyId());

				CommerceAccountOrganizationRel commerceAccountOrganizationRel =
					_commerceAccountOrganizationRelService.
						fetchCommerceAccountOrganizationRel(
							new CommerceAccountOrganizationRelPK(
								commerceAccount.getCommerceAccountId(),
								organizationId));

				if (commerceAccountOrganizationRel != null) {
					continue;
				}

				_commerceAccountOrganizationRelService.
					addCommerceAccountOrganizationRel(
						commerceAccount.getCommerceAccountId(), organizationId,
						serviceContext);
			}
		}

		return commerceAccount;
	}

	private static final EntityModel _entityModel = new AccountEntityModel();

	@Reference
	private AccountDTOConverter _accountDTOConverter;

	@Reference
	private CommerceAccountGroupCommerceAccountRelService
		_commerceAccountGroupCommerceAccountRelService;

	@Reference
	private CommerceAccountGroupService _commerceAccountGroupService;

	@Reference
	private CommerceAccountOrganizationRelService
		_commerceAccountOrganizationRelService;

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private CommerceAccountUserRelService _commerceAccountUserRelService;

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CommerceCountryService _commerceCountryService;

	@Reference
	private CommerceRegionLocalService _commerceRegionLocalService;

	@Reference
	private OrganizationLocalService _organizationLocalService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UserLocalService _userLocalService;

}