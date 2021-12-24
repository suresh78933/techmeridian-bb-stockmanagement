package com.techmeridian.stockmanagement.nav.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.uom.UnitOfMeasure;
import com.techmeridian.stockmanagement.uom.UnitOfMeasureRepository;

@Service
public class NavUnitOfMeasureService {
	private Logger logger = Logger.getLogger(NavUnitOfMeasureService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	public String getUOMList() {
		logger.info("Fetching uom list from nav services ... ");
		try {
			logger.info("Nav Props: [Namespace: " + navProperties.getNavUnitOfMeasureNamespace() + ", SoapURL: "
					+ navProperties.getNavUnitOfMeasureSoapURL() + ", " + "SoapAction: "
					+ navProperties.getNavUnitOfMeasureSoapAction() + ", MethodName: "
					+ navProperties.getNavUnitOfMeasureMethodName() + ", UserName: " + navProperties.getNavUserName()
					+ ", Password: " + navProperties.getNavPassword());

			SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
					navProperties.getNavUnitOfMeasureNamespace(), navProperties.getNavUnitOfMeasureSoapURL(),
					navProperties.getNavUnitOfMeasureSoapAction(), navProperties.getNavUnitOfMeasureMethodName(),
					navProperties.getNavUserName(), navProperties.getNavPassword(), navProperties.getNavDomain(),
					navProperties.getNavWorkstation());

			Set<UnitOfMeasure> navUnitOfMeasure = new HashSet<UnitOfMeasure>();
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject soapObject = (SoapObject) response.getProperty(j);

				UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
				unitOfMeasure.setCode(ServiceUtil.getValue("Code", soapObject));
				unitOfMeasure.setDescription(ServiceUtil.getValue("Description", soapObject));
				unitOfMeasure
						.setInternationalStandardCode(ServiceUtil.getValue("International_Standard_Code", soapObject));
				unitOfMeasure.setETag(ServiceUtil.getValue("ETag", soapObject));

				navUnitOfMeasure.add(unitOfMeasure);
			}

			Map<String, UnitOfMeasure> persistedUOMList = new HashMap<String, UnitOfMeasure>();
			for (UnitOfMeasure unitOfMeasure : unitOfMeasureRepository.findAll()) {
				persistedUOMList.put(unitOfMeasure.getCode(), unitOfMeasure);
			}

			Set<UnitOfMeasure> uomToPersist = new HashSet<UnitOfMeasure>();
			for (UnitOfMeasure navUOM : navUnitOfMeasure) {
				if (persistedUOMList.containsKey(navUOM.getCode())) {
					navUOM.setUnitOfMeasureId(persistedUOMList.get(navUOM.getCode()).getUnitOfMeasureId());
					logger.info(navUOM.getCode() + " exists in db, will be updated.");
				} else {
					logger.info("Found new item " + navUOM.getCode() + ", will be persisted.");
				}
				uomToPersist.add(navUOM);
			}
			unitOfMeasureRepository.save(uomToPersist);
			logger.info("Fetching uom list from nav services, Success.");
			return "OK";
		} catch (Exception exception) {
			logger.error("Error fetching uom list", exception);
		}
		return "Error.";
	}
}
