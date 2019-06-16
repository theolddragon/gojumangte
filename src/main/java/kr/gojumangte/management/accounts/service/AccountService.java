package kr.gojumangte.management.accounts.service;

import kr.gojumangte.management.accounts.exception.AccountNotFoundException;
import kr.gojumangte.management.accounts.model.Account;
import kr.gojumangte.management.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

  private final AccountRepository accountRepository;

  /**
   * identify(ID)로 계정 정보 조회하기.
   *
   * @param identify
   * @return Account
   */
  public Account getAccountByIdentify(String identify) {
    log.info("getAccountById({})", identify);
    return accountRepository.findByIdentify(identify)
        .orElseThrow(() -> new AccountNotFoundException(identify));
  }
}
