package Manager.spring.repository;

import Manager.spring.domain.Chat;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JPAChatRepository implements ChatRepository{


    private final EntityManager em;

    public JPAChatRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Chat save(Chat chat) {
        em.persist(chat);
        return chat;
    }

    @Override
    public Optional<Chat> findById(Long id) {
        Chat chat = em.find(Chat.class, id);
        return Optional.ofNullable(chat);
    }

    @Override
    public List<Chat> findAll() {
        return em.createQuery("select c from Chat c", Chat.class).getResultList();
    }
}
