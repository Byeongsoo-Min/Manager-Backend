package Manager.spring.repository;

import Manager.spring.domain.Card;
import Manager.spring.domain.Chat;
import Manager.spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    List<Member> findAll();

    Optional<Chat> findChat(Long id);

    List<Card> findCardList(Member member);
}
