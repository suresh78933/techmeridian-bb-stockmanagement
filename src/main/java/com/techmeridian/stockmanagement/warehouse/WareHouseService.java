package com.techmeridian.stockmanagement.warehouse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;

@Service("wareHouseService")
@Transactional
public class WareHouseService {

	@Autowired
	private WareHouseRepository wareHouseRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public List<WareHouse> getWareHouses() {
		return StreamSupport.stream(wareHouseRepository.findAll().spliterator(), true).collect(Collectors.toList());
	}

	public List<WareHouse> getWareHousesForCompany(Integer companyId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return StreamSupport.stream(wareHouseRepository.findByCompany(company).spliterator(), true)
				.collect(Collectors.toList());
	}
}
