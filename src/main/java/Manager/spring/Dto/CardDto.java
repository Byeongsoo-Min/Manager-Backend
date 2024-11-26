package Manager.spring.Dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDto {
    private Long memberId;
    private String companyName;
    private String companyNumber;
    private String companyImageUrl;


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
}
