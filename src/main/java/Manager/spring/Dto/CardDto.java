package Manager.spring.Dto;


import Manager.spring.domain.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDto {
    private Long memberId;
    private String companyName;
    private String companyNumber;
    private String companyImageUrl;
    private Long cardId;

    public CardDto() {

    }

    @JsonCreator
    public CardDto(
            @JsonProperty("companyName") String companyName,
            @JsonProperty("companyNumber") String companyNumber,
            @JsonProperty("companyImageUrl") String companyImageUrl,
            @JsonProperty("memberId") Long memberId) {
        this.companyName = companyName;
        this.companyNumber = companyNumber;
        this.companyImageUrl = companyImageUrl;
        this.memberId = memberId;
    }

    public static CardDto fromEntity(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setCompanyName(card.getCompanyName());
        cardDto.setCompanyNumber(card.getCompanyNumber());
        cardDto.setCompanyImageUrl(card.getCompanyImageUrl());
        cardDto.setMemberId(card.getMember().getId());
        cardDto.setCardId(card.getId());
        return cardDto;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

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

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
