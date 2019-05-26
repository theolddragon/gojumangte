package kr.gojumangte.management.members.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import kr.gojumangte.management.accounts.model.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 고주망테 회원정보 관리.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_index")
  private long index;

  // 이름
  private String name;

  // 성별
  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  // 생년월일
  @Column(name = "birth_date")
  private LocalDate birthDate;

  // 가입일
  @Column(name = "join_date")
  private LocalDate joinDate;

  // 구력 년단위
  @Column(name = "term_year")
  private int termYear;

  // 구력 월단위
  @Column(name = "term_month")
  private int termMonth;

  // 참석 횟수
  private int attendance;

  @OneToOne
  @JoinColumn(name = "account_index", referencedColumnName = "index")
  private Account account;
}
