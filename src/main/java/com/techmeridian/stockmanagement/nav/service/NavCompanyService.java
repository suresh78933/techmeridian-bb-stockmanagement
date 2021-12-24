package com.techmeridian.stockmanagement.nav.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;

@Service
public class NavCompanyService {

	private Logger logger = Logger.getLogger(NavCompanyService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	private CompanyRepository companyRepository;

	public String getCompanyList() {
		logger.info("Fetching company list from nav services ... ");
		try {
			SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
					navProperties.getNavCompanyListNamespace(), navProperties.getNavCompanyListSoapURL(),
					navProperties.getNavCompanyListSoapAction(), navProperties.getNavCompanyListMethodName(),
					navProperties.getNavUserName(), navProperties.getNavPassword(), navProperties.getNavDomain(),
					navProperties.getNavWorkstation());

			Set<Company> navCompanyList = new HashSet<Company>();
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject soapObject = (SoapObject) response.getProperty(j);

				Company company = new Company();
				company.setName(ServiceUtil.getValue("Name", soapObject));
				navCompanyList.add(company);
			}

			navCompanyList.stream().forEach(company -> {
				logger.info("Checking for company " + company.getName() + " in DB.");
				Company existingCompany = companyRepository.findByName(company.getName());
				if (existingCompany == null) {
					logger.info("Saving company " + company.getName());
					existingCompany = companyRepository.save(company);
				}
			});

			logger.info("Fetching company list from nav services, Success.");
			return "OK";
		} catch (Exception exception) {
			logger.error("Error fetching company list", exception);
		}
		return "Error.";
	}
}
