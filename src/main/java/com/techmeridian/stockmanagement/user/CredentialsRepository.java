package com.techmeridian.stockmanagement.user;

import org.springframework.data.repository.CrudRepository;

public interface CredentialsRepository extends CrudRepository<Credentials, Integer> {

	Credentials findByUserAndPassword(User user, String password);
	
	Credentials findByUser(User user);
}
