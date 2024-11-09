package Manager.spring.repository;

import Manager.spring.domain.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    List<UserChat> findByChatId(Long chatId);
}
