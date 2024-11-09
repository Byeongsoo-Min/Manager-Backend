package Manager.spring.repository;

import Manager.spring.domain.Card;
import Manager.spring.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JPACardRepository implements CardRepository{

    private final EntityManager em;

    public JPACardRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Card save(Card card) {
        em.persist(card);
        return card;
    }

    @Override
    public Optional<Card> findById(Long id) {
        Card card = em.find(Card.class, id);
        return Optional.ofNullable(card);
    }

    @Override
    public List<Card> findByName(String name) {
        List<Card> resultList = em.createQuery("select c from Card c where c.companyName = :name", Card.class)
                .setParameter("name", name)
                .getResultList();
        if (resultList.isEmpty()) {
            List<Card> emptyCard = new ArrayList<>();
            Card dummyCard = new Card();
            Member member = new Member();

            member.setManagerName("dummyManager");

            dummyCard.setCompanyName("dummy");
            dummyCard.setCompanyImageUrl("dummyImage");
            dummyCard.setMember(member);
            emptyCard.add(dummyCard);
            return emptyCard;
        }
        return resultList;
    }

    @Override
    public List<Card> findByMemberId(Long memberId) {
        List<Card> resultList = em.createQuery("select c from Card c where c.member.id = :memberId", Card.class)
                .setParameter("memberId", memberId)
                .getResultList();

        if (resultList.isEmpty()) {
            List<Card> emptyCard = new ArrayList<>();
            Card dummyCard = new Card();
            Member member = new Member();

            member.setManagerName("dummyManager");

            dummyCard.setCompanyName("dummy");
            dummyCard.setCompanyImageUrl("dummyImage");
            dummyCard.setMember(member);
            emptyCard.add(dummyCard);
            return emptyCard;
        }

        return resultList;
    }
}
