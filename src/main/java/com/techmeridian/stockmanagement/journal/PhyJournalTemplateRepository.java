package com.techmeridian.stockmanagement.journal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.warehouse.WareHouse;

public interface PhyJournalTemplateRepository extends CrudRepository<PhyJournalTemplate, Integer> {

	List<PhyJournalTemplate> findByWareHouse(WareHouse wareHouse);

	PhyJournalTemplate findByNameAndWareHouseAndCompany(String name, WareHouse wareHouse, Company company);

	List<PhyJournalTemplate> findByCompany(Company company);
}
