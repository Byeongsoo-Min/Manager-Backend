package Manager.spring;


import Manager.spring.repository.*;
import Manager.spring.service.CardService;
import Manager.spring.service.ChatService;
import Manager.spring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private EntityManager em;
    private final GptChatRepository gptChatRepository;
    private final UserChatRepository userChatRepository;


    @Autowired
    public SpringConfig(EntityManager em, GptChatRepository gpt, UserChatRepository user) {
        this.em = em;
        this.gptChatRepository = gpt;
        this.userChatRepository = user;
    }

    @Bean
    public CardService cardService() {
        return new CardService(cardRepository(), memberRepository());
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public ChatService chatService() {
        return new ChatService(chatRepository(), gptChatRepository, userChatRepository);
    }

    @Bean
    public CardRepository cardRepository() {
        return new JPACardRepository(em);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JPAChatRepository(em);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JPAMemberRepository(em);
    }
}
