package com.techmeridian.stockmanagement.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findByUserName(String userName);
}
