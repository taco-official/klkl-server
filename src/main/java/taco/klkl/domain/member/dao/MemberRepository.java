package taco.klkl.domain.member.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByName(final String name);

	Optional<Member> findByName(final String name);

	Optional<Member> findByProviderAndProviderId(final String provider, final String providerId);
}
