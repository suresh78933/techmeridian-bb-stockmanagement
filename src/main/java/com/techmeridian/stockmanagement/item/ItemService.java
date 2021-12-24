package com.techmeridian.stockmanagement.item;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.nav.service.NavItemService;

@Service("itemService")
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	NavItemService navItemListService;

	public List<Item> getItems() {
		return StreamSupport.stream(itemRepository.findAll().spliterator(), true).collect(Collectors.toList());
	}

	public List<Item> getItemsByCompany(Integer companyId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return StreamSupport.stream(itemRepository.findByCompany(company).spliterator(), true)
				.collect(Collectors.toList());
	}

	public Item getItem(Integer itemId) {
		return itemRepository.findOne(itemId);
	}

	private Company getCompany(Integer companyId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return company;
	}

	public Item reloadItem(Integer companyId, Integer itId) throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		Item item = itemRepository.findOne(itId);
		if (item == null) {
			throw new Exception("Invalid item id.");
		}
		return reloadItem(item);
	}

	public Item reloadItem(Integer companyId, String itNo) throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		Item item = itemRepository.findByItemNoAndCompany(itNo, company);
		if (item == null) {
			throw new Exception("Invalid item number.");
		}
		return reloadItem(item);
	}

	public Item reloadItem(Item item) throws Exception {
		navItemListService.reloadItem(item);
		return getItem(item.getItemId());
	}

	public Item getItemByNoOrPartNo(Integer companyId, String itNo) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return itemRepository.findByCompanyAndItemNoOrPartNo(company.getId(), itNo);
	}

}
