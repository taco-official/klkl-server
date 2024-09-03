package taco.klkl.domain.region.dao.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.currency.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
