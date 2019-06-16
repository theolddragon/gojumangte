package kr.gojumangte.management.accounts.repository;

import java.util.Optional;
import kr.gojumangte.management.accounts.model.Account;
import kr.gojumangte.management.accounts.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 계정 Repository.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

  /**
   * 탈퇴하지 않은 계정을 ID로 조회.
   *
   * @param identify login Id
   * @return a Optional of Account
   */
  Optional<Account> findByIdentify(String identify);
}
