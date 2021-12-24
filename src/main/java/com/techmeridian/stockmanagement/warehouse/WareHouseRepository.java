package com.techmeridian.stockmanagement.warehouse;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface WareHouseRepository extends CrudRepository<WareHouse, Integer> {

	WareHouse findByNameAndCompany(String name, Company company);

	List<WareHouse> findByCompany(Company company);

	WareHouse findByCodeAndCompany(String code, Company company);
}
