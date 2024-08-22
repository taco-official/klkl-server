package taco.klkl.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findFirstByName(String name);

	boolean existsByName(String name);
}
