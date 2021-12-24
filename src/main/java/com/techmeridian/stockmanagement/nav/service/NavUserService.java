package com.techmeridian.stockmanagement.nav.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.role.Role;
import com.techmeridian.stockmanagement.security.EncryptionUtils;
import com.techmeridian.stockmanagement.user.Credentials;
import com.techmeridian.stockmanagement.user.CredentialsRepository;
import com.techmeridian.stockmanagement.user.User;
import com.techmeridian.stockmanagement.user.UserCompany;
import com.techmeridian.stockmanagement.user.UserCompanyRepository;
import com.techmeridian.stockmanagement.user.UserRepository;
import com.techmeridian.stockmanagement.utils.Utility;

@Service
public class NavUserService {

	private Logger logger = Logger.getLogger(NavUserService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserCompanyRepository userCompanyRepository;

	public String fetchUsers() {
		logger.info("Fetching user list ... ");
		String[] methodResponse = new String[] { "OK" };

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching user list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavUserListSoapURL(),
						company.getName().replaceAll(" ", "%20"));
				SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
						navProperties.getNavUserListNamespace(), url, navProperties.getNavUserListSoapAction(),
						navProperties.getNavUserListMethodName(), navProperties.getNavUserName(),
						navProperties.getNavPassword(), navProperties.getNavDomain(),
						navProperties.getNavWorkstation());

				Set<User> navUserList = new HashSet<User>();
				for (int j = 0; j < response.getPropertyCount(); j++) {
					SoapObject soapObject = (SoapObject) response.getProperty(j);

					User user = new User();
					user.setUserSecurityId(ServiceUtil.getValue("User_Security_ID", soapObject));
					user.setUserName(Utility.replaceBackSlashWithHiphen(ServiceUtil.getValue("User_Name", soapObject)));
					user.setFullName(ServiceUtil.getValue("Full_Name", soapObject));
					user.setWinSecurityId(ServiceUtil.getValue("Windows_Security_ID", soapObject));
					user.setWinUserName(ServiceUtil.getValue("Windows_User_Name", soapObject));
					user.setLicenseType(ServiceUtil.getValue("License_Type", soapObject));
					user.setEmailAddress(ServiceUtil.getValue("Authentication_Email", soapObject));
					user.setRole(getUserRole());

					navUserList.add(user);
				}

				navUserList.stream().forEach(user -> {
					logger.info("Checking for user " + user.getUserName() + " in DB.");
					User existingUser = userRepository.findByUserName(user.getUserName());
					if (existingUser == null) {
						logger.info("Saving user " + user);
						existingUser = userRepository.save(user);
						createPassword(existingUser);
					} else {
						logger.info("Updating user " + user.getUserName());
						user.setUserId(existingUser.getUserId());
						user.setUpdatedOn(Calendar.getInstance().getTime());
						user.setCreatedOn(existingUser.getCreatedOn());
						user.setRole(existingUser.getRole());
						userRepository.save(user);
					}
					createUserCompany(existingUser, company);
				});

			} catch (Exception exception) {
				logger.error("Error fetching user list for company " + company.getName(), exception);
				methodResponse[0] = "Error";
			}
		});
		return methodResponse[0];
	}

	private Role getUserRole() {
		Role role = new Role();
		role.setRoleId(4);
		role.setRoleName("USER");
		return role;
	}

	private void createUserCompany(User user, Company company) {
		UserCompany userCompany = userCompanyRepository.findByCompanyAndUser(company, user);
		if (userCompany == null) {
			userCompany = new UserCompany();
			userCompany.setCompany(company);
			userCompany.setUser(user);
			userCompanyRepository.save(userCompany);
		}
	}

	private void createPassword(User user) {
		try {
			Credentials credentials = new Credentials();
			credentials.setPassword(EncryptionUtils.getSecurePassword("changemenow"));
			credentials.setUser(user);
			credentialsRepository.save(credentials);
		} catch (Exception exception) {
			logger.error("Error creating password for user " + user, exception);
		}
	}
}
