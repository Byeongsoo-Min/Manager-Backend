package Manager.spring.repository;

import Manager.spring.domain.GptChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GptChatRepository extends JpaRepository<GptChat, Long> {
    List<GptChat> findByChatId(Long chatId);
}
