package kr.gojumangte.management.accounts.repository;

import kr.gojumangte.management.accounts.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
