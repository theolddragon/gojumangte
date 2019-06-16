package kr.gojumangte.management.common.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Base Datetime.
 * 정보의 등록 일시 및 마지막 변경일시가 필요할 때 사용.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseDateTime {

  @CreatedDate
  @Column(name = "create_date", nullable = false, updatable = false)
  private LocalDateTime createDate = LocalDateTime.now(); // 등록 일시

  @LastModifiedDate
  @Column(name = "last_modify_date")
  private LocalDateTime lastModifyDate; // 마지막 변경 일시
}
