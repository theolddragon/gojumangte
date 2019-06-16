package kr.gojumangte.management.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import kr.gojumangte.management.accounts.model.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Base Auditing Entity.
 * 정보의 등록 및 변경일시가 필요하고, 어느 계정에서 실행했는지도 필요할 때 사용.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseAuditingEntity extends BaseDateTime{

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  private Account createdBy;    // 등록 계정정보

  @LastModifiedBy
  @Column(name = "last_modified_by")
  private Account lastModifiedBy; // 마지막 변경 계정 정보
}
