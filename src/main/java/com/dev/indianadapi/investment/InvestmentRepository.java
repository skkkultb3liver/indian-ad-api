package com.dev.indianadapi.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    List<Investment> findByUserAccountId(Long userAccountId);

    Optional<Investment> findByUserAccountIdAndFilmAdId(Long userAccountId, Long filmAdId);
}
