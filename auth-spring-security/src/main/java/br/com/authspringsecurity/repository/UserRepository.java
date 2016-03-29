package br.com.authspringsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.authspringsecurity.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	User findByUserName(@Param("userName") String userName);
}
