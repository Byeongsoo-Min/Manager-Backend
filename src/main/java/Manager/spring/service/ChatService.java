package Manager.spring.service;

import Manager.spring.domain.Chat;
import Manager.spring.domain.GptChat;
import Manager.spring.domain.UserChat;
import Manager.spring.repository.ChatRepository;
import Manager.spring.repository.GptChatRepository;
import Manager.spring.repository.UserChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final GptChatRepository gptChatRepository;
    private final UserChatRepository userChatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, GptChatRepository gptChatRepository, UserChatRepository userChatRepository) {
        this.chatRepository = chatRepository;
        this.gptChatRepository = gptChatRepository;
        this.userChatRepository = userChatRepository;
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public List<GptChat> getGptMessages(Long chatId) {
        return gptChatRepository.findByChatId(chatId);
    }

    public List<UserChat> getUserMessages(Long chatId) {
        return userChatRepository.findByChatId(chatId);
    }

    public List<Chat> getAllChat() {
        return chatRepository.findAll();
    }
}
