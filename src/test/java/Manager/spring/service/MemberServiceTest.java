package Manager.spring.service;


import Manager.spring.ManagerBackendApplication;
import Manager.spring.domain.Card;
import Manager.spring.domain.Chat;
import Manager.spring.domain.GptChat;
import Manager.spring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = ManagerBackendApplication.class)
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    ChatService chatService;

    @Autowired
    CardService cardService;

    @Test
    void join() {
        Member member = new Member();
        Chat chat = new Chat();
        Card card = new Card();

        member.setManagerName("hello");
        card.setMember(member);
        card.setCompanyName("MBSOO");
        card.setCompanyImageUrl("www.asdf.asdf");


        Chat chat1 = chatService.createChat(chat);
        Card card1 = cardService.createCard(card);
        Long saveId = memberService.join(member);

        Member member1 = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getId()).isEqualTo(member1.getId());
        Assertions.assertThat(memberService.findChat(chat.getId()).get()).isEqualTo(chat1);
        Assertions.assertThat(memberService.findCardList(member1).get(0)).isEqualTo(card1);
    }
}
