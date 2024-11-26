package Manager.spring.service;

import Manager.spring.ChatGptConfig;
import Manager.spring.Dto.ChatCompletionDto;
import Manager.spring.Dto.ChatRequestMsgDto;
import Manager.spring.domain.Chat;
import Manager.spring.domain.GptChat;
import Manager.spring.domain.Member;
import Manager.spring.domain.UserChat;
import Manager.spring.repository.ChatRepository;
import Manager.spring.repository.GptChatRepository;
import Manager.spring.repository.MemberRepository;
import Manager.spring.repository.UserChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ChatGptServiceImpl implements ChatGptService{

    private final ChatGptConfig chatGptConfig;
    private final ChatRepository chatRepository;
    private final GptChatRepository gptChatRepository;
    private final UserChatRepository userChatRepository;
    private final MemberRepository memberRepository;
    public ChatGptServiceImpl(ChatGptConfig chatGptConfig, ChatRepository chatRepository, GptChatRepository gptChatRepository, MemberRepository memberRepository, UserChatRepository userChatRepository) {
        this.chatGptConfig = chatGptConfig;
        this.chatRepository = chatRepository;
        this.gptChatRepository = gptChatRepository;
        this.memberRepository = memberRepository;
        this.userChatRepository = userChatRepository;
    }


//    public ChatGptServiceImpl(ChatGptConfig chatGptConfig) {
//        this.chatGptConfig = chatGptConfig;
//    }

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Override
    public List<Map<String, Object>> modelList() {
        return null;
    }

    @Override
    public Map<String, Object> prompt(ChatCompletionDto chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");
        System.out.println("chatCompletionDto = " + chatCompletionDto);
        Map<String, Object> resultMap = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGptConfig.httpHeaders();

        // [STEP2] DTO 해석해서 멤버 아이디, Chat, User_Chat 찾기
        Long member_id = chatCompletionDto.getMember_id();
        Chat chat = memberRepository.findChat(member_id).get();
        UserChat userChat = new UserChat();
        userChat.setMessage(chatCompletionDto.getMessages().stream()
                .filter(msg -> "user".equals(msg.getRole()))
                .map(ChatRequestMsgDto::getContent)
                .collect(Collectors.joining(", ")));
        System.out.println("userChat = " + userChat.getMessage());
        // UserChat 저장
        userChatRepository.save(userChat);
        // [STEP3] 유저가 생성한 지난 대화기록 불러오기
        List<GptChat> previous_gpt_message = gptChatRepository.findByChatId(chat.getId());
        List<UserChat> previous_user_message = userChatRepository.findByChatId(chat.getId());
        // [STEP4] 요청에 대화기록 추가하기
        List<ChatRequestMsgDto> newChatRequestMsgDto = new ArrayList<>();
        previous_gpt_message.stream().forEach(item -> {
            ChatRequestMsgDto messageDto = new ChatRequestMsgDto("assistant", item.getMessage());
            newChatRequestMsgDto.add(messageDto);
        });
        previous_user_message.stream().forEach(item-> {
            ChatRequestMsgDto messageDto = new ChatRequestMsgDto("user", item.getMessage());
            newChatRequestMsgDto.add(messageDto);
        });
        ChatCompletionDto newCompletionDto = new ChatCompletionDto(chatCompletionDto.getModel(), newChatRequestMsgDto, chatCompletionDto.getMember_id());

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(newCompletionDto, headers);

        ResponseEntity<String> response = chatGptConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
//            System.out.println("JsonMappingException = " + e.getMessage());
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
//            System.out.println("RuntimeException" + e.getMessage());
            log.debug("RuntimeException :: " + e.getMessage());
        }
        storeChat(resultMap, chatCompletionDto.getMember_id());
        return resultMap;
    }

    // 함수 삭제 혹은 파라미터 수정 필요
    @Override
    public String isValidChat(String refusal, String content) {
        return content;
    }

    @Override
    public String storeChat(Map<String, Object> response, Long member_id) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        GptChat gptChat = new GptChat();
        Chat chat = memberRepository.findChat(member_id).get();

        if (message.get("refusal") == null) {
            gptChat.setMessage(message.get("content").toString());
            gptChat.setChat(chat);
            chat.addGptMessage(gptChat);
            gptChatRepository.save(gptChat);
            return isValidChat("null",message.get("content").toString());
        } else {
            return "Please Try again";
        }

    }

    @Override
    public String summaryChat(List<ChatRequestMsgDto> chatRequestMsgDtoList) {
        int count = 0;
//        chatRequestMsgDtoList.stream().forEach(item-> {
//            if count <
//
//
//        });
        return "";
    }
}
