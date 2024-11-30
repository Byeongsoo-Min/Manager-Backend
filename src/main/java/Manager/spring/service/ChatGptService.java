package Manager.spring.service;


import Manager.spring.Dto.ChatCompletionDto;
import Manager.spring.Dto.ChatRequestMsgDto;
import Manager.spring.domain.Chat;
import Manager.spring.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ChatGptService {
    List<Map<String, Object>> modelList();

//    Map<String, Object> isValidModel(String modelName);
//
//    Map<String, Object> legacyPrompt(CompletionDto completionDto);

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);

    String isValidChat(String refusal, String content);

    String storeChat(Map<String, Object> response, Long member_id);

    List<ChatRequestMsgDto> summaryChat(List<ChatRequestMsgDto> chatRequestMsgDtoList);
}
