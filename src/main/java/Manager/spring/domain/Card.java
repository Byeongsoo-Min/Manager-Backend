package Manager.spring.domain;

import Manager.spring.Dto.CardDto;
import jakarta.persistence.*;

@Entity
public class Card {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "companyName")
    private String companyName;
    @Column(name = "companyNumber")
    private String companyNumber;
    @Column(name = "companyImageUrl")
    private String companyImageUrl;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public void setCompanyImageUrl(String companyImageUrl) {
        this.companyImageUrl = companyImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setCardEntity(CardDto cardDto, Member member) {
        this.companyImageUrl = cardDto.getCompanyImageUrl();
        this.companyName = cardDto.getCompanyName();
        this.companyNumber = cardDto.getCompanyNumber();
        this.member = member;
    }
}
