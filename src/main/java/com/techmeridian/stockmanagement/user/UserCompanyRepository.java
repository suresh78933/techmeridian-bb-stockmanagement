package com.techmeridian.stockmanagement.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface UserCompanyRepository extends CrudRepository<UserCompany, Integer> {

	List<UserCompany> findByUser(User user);

	List<UserCompany> findByCompany(Company company);

	UserCompany findByCompanyAndUser(Company company, User user);
}
