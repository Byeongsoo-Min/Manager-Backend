package Manager.spring.service;


import Manager.spring.ManagerBackendApplication;
import Manager.spring.domain.Card;
import Manager.spring.domain.Member;
import Manager.spring.repository.CardRepository;
import Manager.spring.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = ManagerBackendApplication.class)
public class CardServiceTest {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    MemberRepository memberRepository;

//    @Test
//    void create() {
//        Card card = new Card();
//        Member member = new Member();
//
//        card.setCompanyImageUrl("MBSOO");
//        card.setCompanyName("ASDF");
//        card.setCompanyNumber("010-2222-222");
//        card.setMember(member);
//
//        memberRepository.save(member);
//        cardRepository.save(card);
//
//        System.out.println("companyNumber = " + memberRepository.findCardList(member).get(0).getCompanyNumber());
//        Assertions.assertThat(card).isEqualTo(memberRepository.findCardList(member).get(0));
//    }
}
