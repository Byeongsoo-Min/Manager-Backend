package Manager.spring.repository;

import Manager.spring.domain.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {

    Card save(Card card);

    Optional<Card> findById(Long id);

    List<Card> findByName(String name);

    List<Card> findByMemberId(Long memberId);

}
