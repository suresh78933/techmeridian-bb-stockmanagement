package com.techmeridian.stockmanagement.journal;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface PhyJournalItemRepository extends CrudRepository<PhyJournalItem, Integer> {

	PhyJournalItem findByItemNoAndJournalBatchName(String itemNo, String journalBatchName);

	List<PhyJournalItem> findByPhyJournal(PhyJournal phyJournal);

	List<PhyJournalItem> findByPhyJournalAndCompany(PhyJournal phyJournal, Company company);

	List<PhyJournalItem> findByCompany(Company company);

	PhyJournalItem findByItemNoAndJournalBatchNameAndLocationCode(String itemNo, String journalBatchName,
			String locationCode);

	PhyJournalItem findByItemNoAndJournalBatchNameAndLocationCodeAndCompany(String itemNo, String journalBatchName,
			String locationCode, Company company);

	PhyJournalItem findByItemNoAndJournalBatchNameAndLocationCodeAndLineNoAndCompany(String itemNo,
			String journalBatchName, String locationCode, Integer lineNo, Company company);

	@Query("SELECT MAX(lineNo) FROM PhyJournalItem")
	Integer findMaxLineNo();
}
