package kr.gojumangte.management.accounts.exception;

import java.util.Collections;
import java.util.List;
import kr.gojumangte.management.exception.NotFoundException;

/**
 * 회원정보가 존재하지 않는 경우.
 */
public class AccountNotFoundException extends NotFoundException {

  private String loginId;

  /**
   * Constructor.
   * @param loginId : loginId
   */
  public AccountNotFoundException(String loginId) {
    this.loginId = loginId;
  }

  @Override
  public String getMessage() {
    return "Account not found - loginId=" + loginId;
  }

  @Override
  public String getCode() {
    return "not-found.account";
  }


  @Override
  public List<String> getErrors() {
    return Collections.singletonList("account : not found");
  }
}