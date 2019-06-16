package kr.gojumangte.management.members.model;

import java.time.LocalDate;
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
import kr.gojumangte.management.accounts.model.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 고주망테 회원정보 관리.
 */
@Entity
@Table(name = "TB_MEMBER")
@Getter
@Setter
@EqualsAndHashCode(of = "index")
public class Member {

  // 고유번호
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_index")
  private long index;

  // 역할, 직책
  @Enumerated(EnumType.STRING)
  @Column(name = "member_role", length = 10)
  private MemberRole role;

  // 이름
  private String name;

  // 성별
  @Enumerated(EnumType.STRING)
  @Column(name = "gender", length = 6)
  private Gender gender;

  // 생년월일
  @Column(name = "birth_date")
  private LocalDate birthDate;

  // 가입일
  @Column(name = "join_date")
  private LocalDate joinDate;

  // 구력(개월)
  @Column(name = "term")
  private int term;

  // 회원 등급
  @Enumerated(EnumType.STRING)
  @Column(name = "rank", length = 1)
  private Rank rank;

  // 참석 횟수
  private int attendance;

  @OneToOne
  @JoinColumn(name = "account_index")
  private Account account;
}
