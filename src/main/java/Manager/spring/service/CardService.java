package Manager.spring.service;


import Manager.spring.domain.Card;
import Manager.spring.repository.CardRepository;
import Manager.spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CardService(CardRepository cardRepository, MemberRepository memberRepository) {
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    public List<Card> getCardByName(String name) {
        return cardRepository.findByName(name);
    }

    public List<Card> getCardByMemberId(Long id) {
        return cardRepository.findByMemberId(id);
    }



}
