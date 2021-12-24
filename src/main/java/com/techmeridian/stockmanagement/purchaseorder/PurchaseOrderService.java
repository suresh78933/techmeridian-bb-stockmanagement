package com.techmeridian.stockmanagement.purchaseorder;

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
import com.techmeridian.stockmanagement.nav.service.NavPurchaseOrderLinesService;
import com.techmeridian.stockmanagement.user.User;
import com.techmeridian.stockmanagement.user.UserRepository;
import com.techmeridian.stockmanagement.utils.Utility;

@Service("purchaseOrderService")
@Transactional
public class PurchaseOrderService {

	private Logger logger = Logger.getLogger(PurchaseOrderService.class);

	@Autowired
	private NavPurchaseOrderLinesService navPurchaseOrderLinesService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderLinesRepository purchaseOrderLinesRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public List<PurchaseOrder> getPurchaseOrders(Integer companyId) throws Exception {
		Company company = getCompany(companyId);
		return StreamSupport.stream(purchaseOrderRepository.findByCompany(company).spliterator(), true)
				.collect(Collectors.toList());
	}

	public PurchaseOrder getPurchaseOrder(Integer companyId, String purchaseOrderNo) throws Exception {
		Company company = getCompany(companyId);
		return purchaseOrderRepository.findByNumberAndCompany(purchaseOrderNo, company);
	}

	public PurchaseOrder getPurchaseOrder(Integer companyId, Integer poId) throws Exception {
		Company company = getCompany(companyId);
		return purchaseOrderRepository.findByPurchaseOrderIdAndCompany(poId, company);
	}

	public synchronized List<PurchaseOrderLines> addItems(Integer companyId, AddItemReq addItemReq, User user)
			throws Exception {
		Company company = getCompany(companyId);
		PurchaseOrder purchaseOrder = purchaseOrderRepository
				.findByPurchaseOrderIdAndCompany(addItemReq.getPurchaseOrderId(), company);
		if (purchaseOrder == null) {
			throw new Exception("Invalid purchase order");
		}
		// Item item =
		// itemRepository.findByItemNoAndCompany(addItemReq.getItem(), company);
		Item item = itemRepository.findByCompanyAndItemNoOrPartNo(company.getId(), addItemReq.getItem());
		if (item == null) {
			throw new Exception("Invalid item");
		}

		PurchaseOrderLines persistedPOL = purchaseOrderLinesRepository.findByItemAndPurchaseOrder(item, purchaseOrder);
		if (persistedPOL == null) {
			throw new Exception("Could not find a purchase order with POId " + addItemReq.getPurchaseOrderId()
					+ ", item " + addItemReq.getItem()
					+ ", server will have fresh data in some time, please try back in some time.");
		}

		if ((addItemReq.getQuantity() + Utility.getInt(persistedPOL.getQuantityMobileNavReceieved())
				+ Utility.getInt(persistedPOL.getQuantityReceived())) > Utility.getInt(persistedPOL.getQuantity())) {
			logger.error("Recived now: " + addItemReq.getQuantity() + ", Nav Quantity: "
					+ Utility.getInt(persistedPOL.getQuantityMobileNavReceieved()) + ", Allready received: "
					+ Utility.getInt(persistedPOL.getQuantityReceived()) + ", Quantity: "
					+ Utility.getInt(persistedPOL.getQuantity()) + ", Received cannot be greater than total quantity.("
					+ (Utility.getInt(addItemReq.getQuantity())
							+ Utility.getInt(persistedPOL.getQuantityMobileNavReceieved())
							+ Utility.getInt(persistedPOL.getQuantityReceived()))
					+ ")>" + Utility.getInt(persistedPOL.getQuantity()));
			throw new Exception("Received quantity cannot be greater than total quantity.");
		}

		if ((addItemReq.getQuantity() + Utility.getInt(persistedPOL.getQuantityMobileNavReceieved())) < 0) {
			logger.error("Recived now: " + Utility.getInt(addItemReq.getQuantity()) + ", Nav Quantity: "
					+ Utility.getInt(persistedPOL.getQuantityMobileNavReceieved())
					+ ", Scanned quantity is becoming negative.("
					+ (addItemReq.getQuantity() + Utility.getInt(persistedPOL.getQuantityMobileNavReceieved()))
					+ ")<0");
			throw new Exception("Scanned quantity is becoming negative.");
		}

		persistedPOL.setQuantityMobileNavReceieved(Utility.getInt(addItemReq.getQuantity())
				+ Utility.getInt(persistedPOL.getQuantityMobileNavReceieved()));

		int quantityToReceive = Utility.getInt(persistedPOL.getQuantity())
				- (persistedPOL.getQuantityMobileNavReceieved() + persistedPOL.getQuantityReceived());
		// Received cannot be greater than quantity

		persistedPOL.setQuantityToReceive(quantityToReceive);
		persistedPOL.setUpdatedBy(userRepository.findOne(user.getUserId()));
		persistedPOL.setPushToNav(true);
		purchaseOrderLinesRepository.save(persistedPOL);

		return purchaseOrderLinesRepository.findByPurchaseOrder(purchaseOrder);
	}

	private Company getCompany(Integer companyId) throws Exception {
		Company company = companyRepository.findOne(companyId);
		if (company == null) {
			throw new Exception("Company with id " + companyId + " not found.");
		}
		return company;
	}

	public String pushToNav(Integer companyId, PurchaseOrder purchaseOrder, User user) throws Exception {
		Company company = getCompany(companyId);
		logger.info("Pushing purchaseOrder to Nav, " + purchaseOrder + ", initiated by " + user);
		purchaseOrder.setCompany(company);
		return navPurchaseOrderLinesService.pushToNav(purchaseOrder);
	}

	public List<PurchaseOrderLines> reloadPurchaseOrderAndReturnItems(Integer companyId, Integer poId)
			throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(poId);
		if (purchaseOrder == null) {
			throw new Exception("Invalid purchase order id.");
		}
		return reloadPurchaseOrderAndReturnItems(purchaseOrder);

	}

	public List<PurchaseOrderLines> reloadPurchaseOrderAndReturnItems(Integer companyId, String purchaseOrderNo)
			throws Exception {
		Company company = getCompany(companyId);
		if (company == null) {
			throw new Exception("Invalid company id.");
		}
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findByNumberAndCompany(purchaseOrderNo, company);
		if (purchaseOrder == null) {
			throw new Exception("Invalid purchase order number.");
		}
		return reloadPurchaseOrderAndReturnItems(purchaseOrder);
	}

	public List<PurchaseOrderLines> reloadPurchaseOrderAndReturnItems(PurchaseOrder purchaseOrder) throws Exception {
		navPurchaseOrderLinesService.reloadPurchaseOrderLines(purchaseOrder);
		return getPurchaseOrderItems(purchaseOrder.getCompany().getId(), purchaseOrder.getPurchaseOrderId());
	}

	public List<PurchaseOrderLines> getPurchaseOrderItems(Integer companyId, Integer poId) throws Exception {
		Company company = getCompany(companyId);
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderIdAndCompany(poId, company);
		if (purchaseOrder == null) {
			throw new Exception("Invalid id");
		}
		return purchaseOrderLinesRepository.findByPurchaseOrder(purchaseOrder);
	}

	public List<PurchaseOrderLines> getPurchaseOrderItems(Integer companyId, String poNo) throws Exception {
		Company company = getCompany(companyId);
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findByNumberAndCompany(poNo, company);
		if (purchaseOrder == null) {
			throw new Exception("Invalid po number");
		}
		return purchaseOrderLinesRepository.findByPurchaseOrder(purchaseOrder);
	}

}
