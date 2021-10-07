package com.megait.mymall.repository;

import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); //NonNull : null일 가능성 zero!!
    // Member findByEmail(String email); //Nullable : null일 가능성이 있다.
    // ** Optional : NullPointerException 을 방지하고 싶어서 나온 클래스
    Member getByIdAndEmailAndType(Long id, String email, MemberType type);
    // SELECT * FROM
}
