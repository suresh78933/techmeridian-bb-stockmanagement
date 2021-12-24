package com.techmeridian.stockmanagement.company;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("companyService")
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	public List<Company> getCompanies() {
		return StreamSupport.stream(companyRepository.findAll().spliterator(), true).collect(Collectors.toList());
	}
}
