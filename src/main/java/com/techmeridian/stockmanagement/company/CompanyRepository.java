package com.techmeridian.stockmanagement.company;

import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Integer> {

	Company findByName(String name);
}
