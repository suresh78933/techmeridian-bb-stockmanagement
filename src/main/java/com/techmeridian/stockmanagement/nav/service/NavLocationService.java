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
import com.techmeridian.stockmanagement.journal.PhyJournalTemplate;
import com.techmeridian.stockmanagement.journal.PhyJournalTemplateRepository;
import com.techmeridian.stockmanagement.warehouse.WareHouse;
import com.techmeridian.stockmanagement.warehouse.WareHouseRepository;

@Service
public class NavLocationService {

	private Logger logger = Logger.getLogger(NavLocationService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	private WareHouseRepository wareHouseRepository;

	@Autowired
	private PhyJournalTemplateRepository phyJournalTemplateRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getLocationList() {
		logger.info("Fetching ware house list ... ");
		String[] methodResponse = new String[] { "OK" };

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching ware house list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavLocationListSoapURL(),
						company.getName().replaceAll(" ", "%20"));
				SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
						navProperties.getNavLocationListNamespace(), url, navProperties.getNavLocationListSoapAction(),
						navProperties.getNavLocationListMethodName(), navProperties.getNavUserName(),
						navProperties.getNavPassword(), navProperties.getNavDomain(),
						navProperties.getNavWorkstation());

				Set<WareHouse> navLocationList = new HashSet<WareHouse>();
				Map<String, Set<String>> locationJrmlTemplateMap = new HashMap<String, Set<String>>();
				for (int j = 0; j < response.getPropertyCount(); j++) {
					SoapObject soapObject = (SoapObject) response.getProperty(j);

					WareHouse wareHouse = new WareHouse();
					wareHouse.setCode(ServiceUtil.getValue("Code", soapObject));
					wareHouse.setName(ServiceUtil.getValue("Name", soapObject));
					if (!locationJrmlTemplateMap.containsKey(ServiceUtil.getValue("Name", soapObject))) {
						locationJrmlTemplateMap.put(ServiceUtil.getValue("Name", soapObject), new HashSet<String>());
					}
					wareHouse.setCompany(company);
					locationJrmlTemplateMap.get(ServiceUtil.getValue("Name", soapObject))
							.add(ServiceUtil.getValue("Journal_Template_Name", soapObject));
					navLocationList.add(wareHouse);
				}

				Map<String, WareHouse> dbWareHouses = new HashMap<String, WareHouse>();
				navLocationList.stream().forEach(location -> {
					logger.info("Checking for warehouse " + location.getName() + " in DB.");
					WareHouse existingWareHouse = wareHouseRepository.findByNameAndCompany(location.getName(), company);
					if (existingWareHouse == null) {
						logger.info("Saving ware house " + location.getName());
						existingWareHouse = wareHouseRepository.save(location);
					} else {
						existingWareHouse.setCode(location.getCode());
						existingWareHouse = wareHouseRepository.save(existingWareHouse);
						logger.info("Updating ware house " + location.getName());
					}
					dbWareHouses.put(location.getName(), existingWareHouse);
				});

				locationJrmlTemplateMap.keySet().stream().forEach(wareHouseName -> {
					locationJrmlTemplateMap.get(wareHouseName).stream().forEach(jrnlTemplateName -> {
						WareHouse wareHouse = dbWareHouses.get(wareHouseName);
						logger.info("Checking for template name " + jrnlTemplateName + " for ware house "
								+ wareHouse.getName());
						PhyJournalTemplate phyJournalTemplate = phyJournalTemplateRepository
								.findByNameAndWareHouseAndCompany(jrnlTemplateName, dbWareHouses.get(wareHouseName),
										company);
						if (phyJournalTemplate == null) {
							phyJournalTemplate = new PhyJournalTemplate();
							phyJournalTemplate.setName(jrnlTemplateName);
							phyJournalTemplate.setWareHouse(wareHouse);
							phyJournalTemplate.setCompany(company);
							logger.info("Saving new template " + jrnlTemplateName + " for ware house "
									+ wareHouse.getName());
							phyJournalTemplateRepository.save(phyJournalTemplate);
						}
					});
				});
				logger.info("Fetching ware house list for company " + company.getName() + ", Success.");
			} catch (Exception exception) {
				logger.error("Error fetching ware house list for company " + company.getName(), exception);
				methodResponse[0] = "Error";
			}
		});
		return methodResponse[0];
	}
}
