package Manager.spring.repository;

import Manager.spring.domain.Card;
import Manager.spring.domain.Chat;
import Manager.spring.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class JPAMemberRepository implements MemberRepository {
    private final EntityManager em;

    public JPAMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public Optional<Chat> findChat(Long chatId) {
        Chat chat = em.find(Chat.class, chatId);
        return Optional.ofNullable(chat);
    }

    @Override
    public List<Card> findCardList(Member member) {
        List<Card> list = em.createQuery("select c from Card c where c.member.id = :memberId", Card.class)
                .setParameter("memberId", member.getId())
                .getResultList();
        return list;
    }

}
