package com.techmeridian.stockmanagement.journal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface PhyJournalRepository extends CrudRepository<PhyJournal, Integer> {

	PhyJournal findByPhyJournalIdAndCompany(Integer phyJournalId, Company company);

	PhyJournal findByName(String name);

	PhyJournal findByNameAndCompany(String name, Company company);

	List<PhyJournal> findByCompany(Company company);

	List<PhyJournal> findByJournalTemplateNameAndLocationCodeAndCompany(String journalTemplateName, String locationCode,
			Company company);
}
