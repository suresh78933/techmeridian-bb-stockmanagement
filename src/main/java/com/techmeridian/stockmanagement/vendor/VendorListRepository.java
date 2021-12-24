package com.techmeridian.stockmanagement.vendor;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface VendorListRepository extends CrudRepository<VendorList, Serializable> {

	VendorList findByNumberAndCompany(String number, Company company);

	List<VendorList> findByCompany(Company company);
}
