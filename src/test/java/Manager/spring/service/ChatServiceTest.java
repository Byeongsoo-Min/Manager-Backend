package Manager.spring.service;

import Manager.spring.ManagerBackendApplication;
import Manager.spring.domain.Chat;
import Manager.spring.domain.GptChat;
import Manager.spring.domain.UserChat;
import Manager.spring.repository.ChatRepository;
import Manager.spring.repository.GptChatRepository;
import Manager.spring.repository.UserChatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = ManagerBackendApplication.class)
public class ChatServiceTest {
    @Autowired
    ChatService chatService;

    @Autowired
    GptChatRepository gptChatRepository;

    @Autowired
    UserChatRepository userChatRepository;

    @Test
    void create() {
        Chat chat = new Chat();

        GptChat gpt = new GptChat();
        UserChat user = new UserChat();

        gpt.setMessage("안녕하세요. 좋은 아침입니다.");
        user.setMessage("안녕");

        gpt.setChat(chat);
        user.setChat(chat);

        gptChatRepository.save(gpt);
        userChatRepository.save(user);

        chatService.createChat(chat);

        String message = chatService.getGptMessages(chat.getId()).get(0).getMessage();
        String userMessage = chatService.getUserMessages(chat.getId()).get(0).getMessage();
        System.out.println("gpt = " + message);
        System.out.println("userMessage = " + userMessage);

        Assertions.assertThat(chatService.getGptMessages(chat.getId())).isEqualTo(gptChatRepository.findByChatId(chat.getId()));
        Assertions.assertThat(chatService.getUserMessages(chat.getId())).isEqualTo(userChatRepository.findByChatId(chat.getId()));
    }

    @Test
    void multipleChatting() {
        Chat chat = new Chat();

        GptChat gpt1 = new GptChat();
        GptChat gpt2 = new GptChat();
        GptChat gpt3 = new GptChat();
        GptChat gpt4 = new GptChat();

        UserChat user1 = new UserChat();
        UserChat user2 = new UserChat();
        UserChat user3 = new UserChat();
        UserChat user4 = new UserChat();

        gpt1.setMessage("안녕하세요. 좋은 아침입니다.");
        user1.setMessage("안녕");

        gpt1.setChat(chat);
        user1.setChat(chat);

        gpt2.setMessage("무엇을 도와드릴까요");
        user2.setMessage("너랑 대화할");

        gpt2.setChat(chat);
        user2.setChat(chat);

        gpt3.setMessage("좋습니다!");
        user3.setMessage("너 이름이 뭐야");

        gpt3.setChat(chat);
        user3.setChat(chat);

        gpt4.setMessage("전 챗 지피티입니다.");
        user4.setMessage("좋아!");

        gpt4.setChat(chat);
        user4.setChat(chat);

        List<GptChat> gptChats = new ArrayList<>();
        gptChats.add(gpt1);
        gptChats.add(gpt2);
        gptChats.add(gpt3);
        gptChats.add(gpt4);

        List<UserChat> userChats = new ArrayList<>();
        userChats.add(user1);
        userChats.add(user2);
        userChats.add(user3);
        userChats.add(user4);

        for(int i = 0; i<4; i++) {
            gptChatRepository.save(gptChats.get(i));
            userChatRepository.save(userChats.get(i));
        }



        chatService.createChat(chat);

        String message = chatService.getGptMessages(chat.getId()).get(2).getMessage();
        String userMessage = chatService.getUserMessages(chat.getId()).get(2).getMessage();
        System.out.println("gpt = " + message);
        System.out.println("userMessage = " + userMessage);

        Assertions.assertThat(chatService.getGptMessages(chat.getId())).isEqualTo(gptChatRepository.findByChatId(chat.getId()));
        Assertions.assertThat(chatService.getUserMessages(chat.getId())).isEqualTo(userChatRepository.findByChatId(chat.getId()));
    }
}
