package Manager.spring.service;

import Manager.spring.domain.Card;
import Manager.spring.domain.Chat;
import Manager.spring.domain.Member;
import Manager.spring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findOneByName(String managerName) {
        return memberRepository.findByName(managerName); }

    public Optional<Chat> findChat(Long chatId) {
        return memberRepository.findChat(chatId);
    }

    public List<Card> findCardList(Member member) {
        return memberRepository.findCardList(member);
    }

}
