package kr.gojumangte.management.accounts.model;

import kr.gojumangte.management.common.model.EnumModel;

/**
 * 계정 상태.
 */
public enum AccountStatus implements EnumModel {
  I("INITIALIZED"),
  A("ACTIVE"),
  L("LOCKED"),
  W("WITHDRAW"),
  ;

  private String value;
  AccountStatus(String value) {
    this.value = value;
  }

  @Override
  public String getKey() {
    return this.name();
  }

  @Override
  public String getValue() {
    return this.value;
  }
}
