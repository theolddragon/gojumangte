package kr.gojumangte.management.accounts.service;

import kr.gojumangte.management.accounts.exception.AccountNotFoundException;
import kr.gojumangte.management.accounts.model.Account;
import kr.gojumangte.management.accounts.model.AccountStatus;
import kr.gojumangte.management.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("loadUserByUsername({})", username);
    try {
      Account account = accountService.getAccountByIdentify(username);
      if (account.isEnabled()) {
        return account;
      }
    } catch(AccountNotFoundException accountNotFoundException) {
      throw new UsernameNotFoundException("account not found by username.");
    }

    throw new UsernameNotFoundException("account not activated by username.");
  }
}
