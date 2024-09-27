package taco.klkl.domain.member.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByNameAndTag(final String name, final String tag);

	Optional<Member> findByUuid(final UUID uuid);

	Optional<Member> findByProviderAndProviderId(final String provider, final String providerId);
}
