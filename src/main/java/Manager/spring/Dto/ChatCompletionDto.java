package Manager.spring.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatCompletionDto {
    private String model;

    private List<ChatRequestMsgDto> messages;

    private int max_tokens = 200;

    private int frequency_penalty = 2;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long member_id;

    @Builder
    public ChatCompletionDto(String model, List<ChatRequestMsgDto> messages, Long member_id) {
        this.model = model;
        this.messages = messages;
        this.member_id = member_id;
    }
}
