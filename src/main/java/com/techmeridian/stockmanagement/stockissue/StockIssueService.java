package com.techmeridian.stockmanagement.stockissue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.item.Item;
import com.techmeridian.stockmanagement.item.ItemRepository;
import com.techmeridian.stockmanagement.journal.AddItemReq;
import com.techmeridian.stockmanagement.nav.service.NavStockIssueLinesService;
import com.techmeridian.stockmanagement.user.User;
import com.techmeridian.stockmanagement.utils.Utility;

@Service("stockIssueService")
@Transactional
public class StockIssueService {

	private Logger logger = Logger.getLogger(StockIssueService.class);

	@Autowired
	private StockIssueRepository stockIssueRepository;

	@Autowired
	private StockIssueLinesRepository stockIssueLinesRepository;

	@Autowired
	private NavStockIssueLinesService navStockIssueLinesService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public List<StockIssue> getStockIssues(Integer companyId) throws Exception {
		Company company = getCompany(companyId);
		return StreamSupport.stream(stockIssueRepository.findByCompany(company).spliterator(), true)
				.collect(Collectors.toList());
	}

	public StockIssue getStockIssue(Integer companyId, String stockIssueNo) throws Exception {
		Company company = getCompany(companyId);
		return stockIssueRepository.findByIssueNoAndCompany(stockIssueNo, company);
	}

	public StockIssue getStockIssue(Integer companyId, Integer stockIssueId) throws Exception {
		Company company = getCompany(companyId);
		return stockIssueRepository.findByStockIssueIdAndCompany(stockIssueId, company);
	}

	public synchronized List<StockIssueLines> addItems(Integer companyId, AddItemReq addItemReq, User user)
			throws Exception {
		Company company = getCompany(companyId);
		StockIssue stockIssue = stockIssueRepository.findByStockIssueIdAndCompany(addItemReq.getStockIssueId(),
				company);
		if (stockIssue == null) {
			throw new Exception("Invalid stock issue");
		}
		// Item item =
		// itemRepository.findByItemNoAndCompany(addItemReq.getItem(), company);
		Item item = itemRepository.findByCompanyAndItemNoOrPartNo(company.getId(), addItemReq.getItem());
		if (item == null) {
			throw new Exception("Invalid item");
		}

		StockIssueLines persistedSIL = stockIssueLinesRepository.findByNoAndStockIssue(addItemReq.getItem(),
				stockIssue);
		if (persistedSIL == null) {
			throw new Exception("Could not find a stock issue with SIId " + addItemReq.getStockIssueId() + ", item "
					+ addItemReq.getItem()
					+ ", server will have fresh data in some time, please try back in some time.");
		}

		if ((addItemReq.getQuantity() + Utility.getInt(persistedSIL.getMobileNavQuantity())
				+ Utility.getIntFromString(persistedSIL.getQuantityIssued())) > Utility
						.getInt(persistedSIL.getQuantity())) {
			logger.error("Recived now: " + addItemReq.getQuantity() + ", Nav Quantity: "
					+ persistedSIL.getMobileNavQuantity() + ", Allready received: " + persistedSIL.getQuantityIssued()
					+ ", Quantity: " + persistedSIL.getQuantity() + ", Received cannot be greater than total quantity.("
					+ (Utility.getInt(addItemReq.getQuantity()) + Utility.getInt(persistedSIL.getMobileNavQuantity())
							+ Utility.getIntFromString(persistedSIL.getQuantityIssued()))
					+ ")>" + persistedSIL.getQuantity());
			throw new Exception("Received quantity cannot be greater than total quantity.");
		}

		if ((Utility.getInt(addItemReq.getQuantity()) + Utility.getInt(persistedSIL.getMobileNavQuantity())) < 0) {
			logger.error("Recived now: " + addItemReq.getQuantity() + ", Nav Quantity: "
					+ persistedSIL.getMobileNavQuantity() + ", Scanned quantity is becoming negative.("
					+ (addItemReq.getQuantity() + persistedSIL.getMobileNavQuantity()) + ")<0");
			throw new Exception("Scanned quantity is becoming negative.");
		}

		persistedSIL.setMobileNavQuantity(
				Utility.getInt(addItemReq.getQuantity()) + Utility.getInt(persistedSIL.getMobileNavQuantity()));

		int stockToBeIssued = Utility.getInt(persistedSIL.getQuantity())
				- (Utility.getInt(persistedSIL.getMobileNavQuantity())
						+ Utility.getIntFromString(persistedSIL.getQuantityIssued()));
		persistedSIL.setQuantityToIssue(stockToBeIssued + "");
		persistedSIL.setPushToNav(true);

		stockIssueLinesRepository.save(persistedSIL);

		return stockIssueLinesRepository.findByStockIssue(stockIssue);
	}

	public String pushToNav(Integer companyId, StockIssue stockIssue, User user) throws Exception {
		Company company = getCompany(companyId);
		stockIssue.setCompany(company);
		logger.info("Pushing stock issue to Nav, " + stockIssue + ", initiated by " + user);
		return navStockIssueLinesService.pushToNav(stockIssue);
	}

	private Company getCompany(Integer companyId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return company;
	}

	public List<StockIssueLines> reloadStockIssueAndReturnItems(Integer companyId, Integer siId) throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		StockIssue stockIssue = stockIssueRepository.findOne(siId);
		if (stockIssue == null) {
			throw new Exception("Invalid stock issue id.");
		}
		return reloadStockIssueAndReturnItems(stockIssue);
	}

	public List<StockIssueLines> reloadStockIssueAndReturnItems(Integer companyId, String stockIssueNo)
			throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		StockIssue stockIssue = stockIssueRepository.findByIssueNoAndCompany(stockIssueNo, company);
		if (stockIssue == null) {
			throw new Exception("Invalid stock issue number.");
		}
		return reloadStockIssueAndReturnItems(stockIssue);
	}

	public List<StockIssueLines> reloadStockIssueAndReturnItems(StockIssue stockIssue) throws Exception {
		navStockIssueLinesService.reloadStockIssueLines(stockIssue);
		return getStockIssueLines(stockIssue.getCompany().getId(), stockIssue.getStockIssueId());
	}

	public List<StockIssueLines> getStockIssueLines(Integer companyId, Integer siId) throws Exception {
		Company company = getCompany(companyId);
		StockIssue stockIssue = stockIssueRepository.findByStockIssueIdAndCompany(siId, company);
		if (stockIssue == null) {
			throw new Exception("Invalid id");
		}
		return stockIssueLinesRepository.findByStockIssue(stockIssue);
	}

	public synchronized List<StockIssueLines> getStockIssueLines(Integer companyId, String siNo) throws Exception {
		Company company = getCompany(companyId);
		StockIssue stockIssue = stockIssueRepository.findByIssueNoAndCompany(siNo, company);
		if (stockIssue == null) {
			throw new Exception("Invalid po number");
		}
		return stockIssueLinesRepository.findByStockIssue(stockIssue);
	}

}
