package com.techmeridian.stockmanagement.nav.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.stockissue.StockIssue;
import com.techmeridian.stockmanagement.stockissue.StockIssueRepository;

@Service
public class NavStockIssueService {
	private Logger logger = Logger.getLogger(NavStockIssueService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	StockIssueRepository stockIssueRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getStockIssueList() {
		logger.info("Fetching stock issue list from nav services ... ");
		String[] methodResponse = new String[] { "OK" };

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching ware house list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavStockIssueListSoapURL(),
						company.getName().replaceAll(" ", "%20"));
				SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
						navProperties.getNavStockIssueListNamespace(), url,
						navProperties.getNavStockIssueListSoapAction(), navProperties.getNavStockIssueListMethodName(),
						navProperties.getNavUserName(), navProperties.getNavPassword(), navProperties.getNavDomain(),
						navProperties.getNavWorkstation());

				Set<StockIssue> navStockIssueList = new HashSet<StockIssue>();
				for (int j = 0; j < response.getPropertyCount(); j++) {
					SoapObject soapObject = (SoapObject) response.getProperty(j);

					StockIssue stockIssue = new StockIssue();
					stockIssue.setIssueNo(ServiceUtil.getValue("Issue_No", soapObject));
					stockIssue.setIssueDescription(ServiceUtil.getValue("Issue_Description", soapObject));
					stockIssue.setIssueRefNo(ServiceUtil.getValue("Issue_Ref_No", soapObject));
					stockIssue.setStatus(ServiceUtil.getValue("Status", soapObject));
					stockIssue.setIssueRaiseBy(ServiceUtil.getValue("Issue_Raise_by", soapObject));
					stockIssue.setIssueRaisedOn(ServiceUtil.getValue("Issue_Raised_On", soapObject));
					stockIssue.setIssuedBy(ServiceUtil.getValue("Issued_by", soapObject));
					stockIssue.setIssuedOn(ServiceUtil.getValue("Issued_On", soapObject));
					stockIssue.setIssuedTo(ServiceUtil.getValue("Issued_to", soapObject));
					stockIssue.setGlobalDimension1Code(ServiceUtil.getValue("Global_Dimension_1_Code", soapObject));
					stockIssue.setGlobalDimension2Code(ServiceUtil.getValue("Global_Dimension_2_Code", soapObject));
					stockIssue.setLocation(ServiceUtil.getValue("Location", soapObject));
					stockIssue.setCompany(company);

					navStockIssueList.add(stockIssue);
				}

				Map<String, StockIssue> persistedStockIssueList = new HashMap<String, StockIssue>();
				stockIssueRepository.findByCompany(company).stream()
						.forEach(stockIssue -> persistedStockIssueList.put(stockIssue.getIssueNo(), stockIssue));

				Set<StockIssue> uomToPersist = new HashSet<StockIssue>();
				for (StockIssue navStockIssue : navStockIssueList) {
					if (persistedStockIssueList.containsKey(navStockIssue.getIssueNo())) {
						navStockIssue.setStockIssueId(
								persistedStockIssueList.get(navStockIssue.getIssueNo()).getStockIssueId());
						logger.info(navStockIssue.getIssueNo() + " exists in db, will be updated.");
					} else {
						logger.info("Found new item " + navStockIssue.getIssueNo() + ", will be persisted.");
					}
					uomToPersist.add(navStockIssue);
				}
				stockIssueRepository.save(uomToPersist);
				logger.info("Fetching stock issue list from nav services, Success.");
			} catch (Exception exception) {
				logger.error("Error fetching stock issue list ", exception);
				methodResponse[0] = "Error";
			}
		});
		return methodResponse[0];
	}
}
