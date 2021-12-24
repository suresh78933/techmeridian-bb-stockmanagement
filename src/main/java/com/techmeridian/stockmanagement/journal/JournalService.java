package com.techmeridian.stockmanagement.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.item.Item;
import com.techmeridian.stockmanagement.item.ItemRepository;
import com.techmeridian.stockmanagement.nav.service.NavPhyInvJournalService;
import com.techmeridian.stockmanagement.user.User;
import com.techmeridian.stockmanagement.utils.Utility;
import com.techmeridian.stockmanagement.warehouse.WareHouse;
import com.techmeridian.stockmanagement.warehouse.WareHouseRepository;

@Service("journalService")
@Transactional
public class JournalService {

	private Logger logger = Logger.getLogger(JournalService.class);

	@Autowired
	private PhyJournalRepository phyJournalRepository;

	@Autowired
	private PhyJournalItemRepository phyJournalItemRepository;

	@Autowired
	private PhyJournalTemplateRepository phyJournalTemplateRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private WareHouseRepository wareHouseRepository;

	@Autowired
	private NavPhyInvJournalService navPhyInvJournalService;

	@Autowired
	private CompanyRepository companyRepository;

	public PhyJournal getPhyJournal(Integer companyId, Integer phyJournalId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return phyJournalRepository.findByPhyJournalIdAndCompany(phyJournalId, company);
	}

	public List<PhyJournal> getJournalForAWareHouse(Integer companyId, String wareHouseCode) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		WareHouse wareHouse = wareHouseRepository.findByCodeAndCompany(wareHouseCode, company);
		if (wareHouse == null) {
			throw new Exception("No ware house with code " + wareHouseCode);
		}
		List<PhyJournalTemplate> phyJournalTemplates = phyJournalTemplateRepository.findByWareHouse(wareHouse);

		final List<PhyJournal> phyJournals = new ArrayList<PhyJournal>();
		phyJournalTemplates.stream().forEach(phyJournalTemplate -> {
			phyJournals.addAll(phyJournalRepository
					.findByJournalTemplateNameAndLocationCodeAndCompany(phyJournalTemplate.getName(),
							wareHouse.getCode(), company)
					.stream().collect(Collectors.toList()));
		});
		return phyJournals;
	}

	public List<PhyJournalItem> getPhyJournalItems(Integer companyId, Integer phyJournalId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		PhyJournal phyJournal = phyJournalRepository.findByPhyJournalIdAndCompany(phyJournalId, company);
		return phyJournalItemRepository.findByPhyJournalAndCompany(phyJournal, company).stream()
				.collect(Collectors.toList());
	}

	public List<PhyJournalItem> getPhyJournalItems(Company company, Integer phyJournalId) throws Exception {
		PhyJournal phyJournal = phyJournalRepository.findByPhyJournalIdAndCompany(phyJournalId, company);
		return phyJournalItemRepository.findByPhyJournalAndCompany(phyJournal, company).stream()
				.collect(Collectors.toList());
	}

	public synchronized List<PhyJournalItem> addItem(Integer companyId, AddItemReq addItemReq, User user)
			throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		PhyJournal phyJournal = phyJournalRepository.findByPhyJournalIdAndCompany(addItemReq.getJournalId(), company);

		Item item = itemRepository.findByCompanyAndItemNoOrPartNo(company.getId(), addItemReq.getItem());
		if (item == null) {
			throw new Exception("Item with no/partNo " + addItemReq.getItem()
					+ " not found, please try with valid item no/partNo.");
		}
		PhyJournalItem existingItem = phyJournalItemRepository.findByItemNoAndJournalBatchNameAndLocationCodeAndCompany(
				item.getItemNo(), phyJournal.getName(), addItemReq.getWareHouseCode(), company);

		if (existingItem != null) {
			existingItem.setMobileNavQuantity(
					Utility.getInt(existingItem.getMobileNavQuantity()) + Utility.getInt(addItemReq.getQuantity()));

			phyJournalItemRepository.save(existingItem);
		} else {
			PhyJournalItem phyJournalItem = new PhyJournalItem();
			phyJournalItem.setItem(item);
			phyJournalItem.setItemNo(item.getItemNo());
			phyJournalItem.setItemDescription(item.getItemDescription());
			phyJournalItem.setPhyJournal(phyJournal);
			phyJournalItem.setJournalTemplateName(phyJournal.getJournalTemplateName());
			phyJournalItem.setJournalBatchName(phyJournal.getName());
			phyJournalItem.setMobileNavQuantity(addItemReq.getQuantity());
			phyJournalItem.setLocationCode(addItemReq.getWareHouseCode());
			phyJournalItem.setUnitOfMeasureCode(item.getBaseUnitOfMeasure());
			phyJournalItem.setLineNo(getMaxLineNo());
			phyJournalItem.setEntryType("Positive_Adjmt");
			phyJournalItem.setCompany(company);

			phyJournalItemRepository.save(phyJournalItem);
		}
		return getPhyJournalItems(company, phyJournal.getPhyJournalId());
	}

	public Integer getMaxLineNo() {
		Integer lineNo = phyJournalItemRepository.findMaxLineNo();
		return (lineNo == null || lineNo <= 0) ? 10000 : ++lineNo;
	}

	public String pushToNav(Integer companyId, PhyJournal journal, User user) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		logger.info("Pushing journal to Nav, " + journal + ", initiated by " + user);
		journal.setCompany(company);
		return navPhyInvJournalService.pushToNav(journal);
	}
}
