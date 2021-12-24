package com.techmeridian.stockmanagement.scheduler;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techmeridian.stockmanagement.nav.service.NavCompanyService;
import com.techmeridian.stockmanagement.nav.service.NavItemService;
import com.techmeridian.stockmanagement.nav.service.NavLocationService;
import com.techmeridian.stockmanagement.nav.service.NavPhyInvJournalService;
import com.techmeridian.stockmanagement.nav.service.NavPurchaseOrderLinesService;
import com.techmeridian.stockmanagement.nav.service.NavPurchaseOrderService;
import com.techmeridian.stockmanagement.nav.service.NavStockIssueLinesService;
import com.techmeridian.stockmanagement.nav.service.NavStockIssueService;
import com.techmeridian.stockmanagement.nav.service.NavUnitOfMeasureService;
import com.techmeridian.stockmanagement.nav.service.NavUserService;
import com.techmeridian.stockmanagement.nav.service.NavVendorService;

@Component("NavScheduler")
public class NavScheduler {

	private Logger logger = Logger.getLogger(NavScheduler.class);

	@Value("${nav.scheduler.startin}")
	private Long startIn;

	@Value("${nav.scheduler.refreshin}")
	private Long refreshIn;

	@Autowired
	private NavLocationService navLocationService;

	@Autowired
	private NavCompanyService navCompanyService;

	@Autowired
	private NavPhyInvJournalService navPhyInvJournalService;

	@Autowired
	private NavVendorService navVendorService;

	@Autowired
	private NavItemService navItemService;

	@Autowired
	private NavUnitOfMeasureService navUnitOfMeasureService;

	@Autowired
	private NavPurchaseOrderService navPurchaseOrderService;

	@Autowired
	private NavPurchaseOrderLinesService navPurchaseOrderLinesService;

	@Autowired
	private NavStockIssueService navStockIssueService;

	@Autowired
	private NavStockIssueLinesService navStockIssueLinesService;

	@Autowired
	private NavUserService navUserService;

	private static final long START_IN = 5 * 60 * 1000;
	private static final long REFRESH_IN = 30 * 60 * 1000;

	public NavScheduler() {
	}

	public void start() {
		TimerTask navTimerTask = new TimerTask() {

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				logger.info("Nav refresh scheduler starting up . . . ");

				loadUnitOfMeasureList();

				loadCompanies();

				loadUsers();

				loadVendorList();

				loadItemList();

				loadLocations();

				loadJournals();
				loadJournalItems();
				loadJournalItemsForKeys();

				loadPurchaseOrders();
				loadPurchaseOrdersLines();
				loadPurchaseOrdersLinesKeys();

				loadStockIssues();
				loadStockIssueLines();
				loadStockIssuesLinesKeys();

				logger.info("Done refreshing data from NAV, Time taken: " + (System.currentTimeMillis() - start));
			}
		};

		if (startIn == null) {
			startIn = START_IN;
		}

		if (refreshIn == null || refreshIn <= 0) {
			refreshIn = REFRESH_IN;
		}

		Timer timer = new Timer();
		timer.schedule(navTimerTask, startIn, refreshIn);
		System.out.println("************************************************************************************");
		System.out.println("Nav refresh timer is successfully set. Will have first run in " + (startIn / 60000)
				+ " mins and repeat every " + (refreshIn / 60000) + " mins.");
		System.out.println("************************************************************************************");
	}

	private void loadUnitOfMeasureList() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching UOM list .... ");
			String response = navUnitOfMeasureService.getUOMList();
			logger.info("Fetching UOM list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching UOM list ", exception);
		}
	}

	private void loadCompanies() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching company list .... ");
			String response = navCompanyService.getCompanyList();
			logger.info("Fetching company list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching company list ", exception);
		}
	}

	private void loadUsers() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching user list .... ");
			String response = navUserService.fetchUsers();
			logger.info("Fetching user list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching user list ", exception);
		}
	}

	private void loadVendorList() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching vendor list .... ");
			String response = navVendorService.getVendorList();
			logger.info("Fetching vendor list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching vendor list ", exception);
		}
	}

	private void loadItemList() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching item list .... ");
			String response = navItemService.getItemList();
			logger.info("Fetching item list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching item list ", exception);
		}
	}

	private void loadLocations() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching location list .... ");
			String response = navLocationService.getLocationList();
			logger.info("Fetching location list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching location list ", exception);
		}
	}

	private void loadJournals() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching journal list .... ");
			String response = navPhyInvJournalService.getJournals();
			logger.info("Fetching journal list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching journal list ", exception);
		}
	}

	private void loadJournalItems() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching journal item list .... ");
			String response = navPhyInvJournalService.getJournalItems();
			logger.info("Fetching journal item list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching journal item list ", exception);
		}
	}

	private void loadJournalItemsForKeys() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching journal item list for keys .... ");
			String response = navPhyInvJournalService.getJournalItemKeys();
			logger.info("Fetching journal item list for keys, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching journal item list for keys ", exception);
		}
	}

	private void loadPurchaseOrders() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching purchase order list .... ");
			String response = navPurchaseOrderService.getPurchaseOrderList();
			logger.info("Fetching purchase order list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching purchase order list ", exception);
		}
	}

	private void loadPurchaseOrdersLines() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching purchase order lines list .... ");
			String response = navPurchaseOrderLinesService.getPurchaseOrderLinesList();
			logger.info("Fetching purchase order lines list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching purchase order lines list ", exception);
		}
	}

	private void loadPurchaseOrdersLinesKeys() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching purchase order lines keys .... ");
			String response = navPurchaseOrderLinesService.getPurchaseOrderLinesKeys();
			logger.info("Fetching purchase order lines keys, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching purchase order lines keys ", exception);
		}
	}

	private void loadStockIssues() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching stock issue list .... ");
			String response = navStockIssueService.getStockIssueList();
			logger.info("Fetching stock issue list, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching stock issue list ", exception);
		}
	}

	private void loadStockIssueLines() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching stock issue lines .... ");
			String response = navStockIssueLinesService.getStockIssueLines();
			logger.info("Fetching stock issue lines, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching stock issue lines ", exception);
		}
	}

	private void loadStockIssuesLinesKeys() {
		try {
			long start = System.currentTimeMillis();
			logger.info("Fetching stock issue lines keys .... ");
			String response = navStockIssueLinesService.getStockIssueLineKeys();
			logger.info("Fetching stock issue lines keys, Completed. Response: " + response + ", Time taken: "
					+ (System.currentTimeMillis() - start));
		} catch (Exception exception) {
			logger.error("Error fetching stock issue lines keys ", exception);
		}
	}

	private Timer timer = null;

	public void stop() {
		timer.cancel();
	}
}
