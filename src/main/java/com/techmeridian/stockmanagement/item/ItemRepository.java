package com.techmeridian.stockmanagement.item;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface ItemRepository extends CrudRepository<Item, Integer> {

	Item findByItemNo(String itemNo);

	Item findByItemNoAndCompany(String itemNo, Company company);

	Item findByPartNoAndCompany(String partNo, Company company);

	List<Item> findByCompany(Company company);

	@Query("SELECT i FROM Item i WHERE i.company.id=?1 AND (i.itemNo=?2 OR i.partNo=?2)")
	Item findByCompanyAndItemNoOrPartNo(Integer companyId, String noOrPartNo);
}
