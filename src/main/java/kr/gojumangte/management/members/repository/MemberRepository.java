package kr.gojumangte.management.members.repository;

import kr.gojumangte.management.members.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
