package Manager.spring.repository;

import Manager.spring.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository {
    Chat save(Chat chat);

    Optional<Chat> findById(Long id);

    List<Chat> findAll();

}
