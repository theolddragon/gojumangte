package kr.gojumangte.management.accounts.model;


import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kr.gojumangte.management.members.model.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 계정 Model.
 */
@Entity
@Table(name = "TB_ACCOUNT")
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class Account implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_index")
  private Integer index;

  @Column(name = "identify", unique = true, length = 20, nullable = false)
  private String identify;

  @Column(name = "password", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 1, nullable = false)
  private AccountStatus status;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "TB_ACCOUNT_ROLES",
      joinColumns = @JoinColumn(name = "account_index", referencedColumnName = "account_index")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 6)
  private Set<AccountRole> roles;

  @OneToOne(mappedBy = "account")
  private Member member;

  // 비밀번호 오류 횟수
  @Column(name = "password_error_count", nullable = false)
  private int passwordErrorCount = 0;

  // 계정 관리용 메모
  @Column(name = "memo")
  private String memo;

  public Account(String identify) {
    this.identify = identify;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(roles -> new SimpleGrantedAuthority("ROLE_" + roles.name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return this.identify;
  }

  /**
   * 계정이 만료되지 않았는지를 리턴.
   * @return <code>true</code> if the user's account is valid (ie non-expired),
   * <code>false</code> if no longer valid (ie expired)
   */
  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  /**
   * 계정이 잠겨있지 않은지를 리턴.
   * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return this.status != AccountStatus.L;
  }

  /**
   * 계정의 패스워드가 만료되지 않았는지를 리턴.
   *
   * @return <code>true</code> if the user's credentials are valid (ie non-expired),
   * <code>false</code> if no longer valid (ie expired)
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }
  /**
   * 계정인 탈퇴한 상태가 아닌지를 리턴.
   * @return boolean
   */
  private boolean isNonWithdraw() {
    return this.status != AccountStatus.W;
  }

  /**
   * 계정이 사용가능한 계정인지를 리턴.
   *
   * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
   */
  @Override
  public boolean isEnabled() {
    return isAccountNonLocked() && isNonWithdraw();
  }

  /**
   * 비밀번호 오류 수량 증가.
   *
   * @return Account
   */
  public Account incrementErrorCount() {
    this.passwordErrorCount++;
    return this;
  }

  /**
   * 비밀번호 오류 수량 초기화.
   *
   * @return Account
   */
  public Account initializeErrorCount() {
    this.passwordErrorCount = 0;
    return this;
  }
}

