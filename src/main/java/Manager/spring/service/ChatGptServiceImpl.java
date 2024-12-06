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
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.concurrent.atomic.AtomicInteger;
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
        // [STEP3] 유저가 생성한 지난 대화기록 불러오기
        List<GptChat> previous_gpt_message = gptChatRepository.findByChatId(chat.getId());
        List<UserChat> previous_user_message = userChatRepository.findByChatId(chat.getId());
        System.out.println("previous_user_message = " + previous_user_message);
        // UserChat 저장 (유저가 지금 입력한 질문에 답을 요청하기 위해 이전 응답을 불러온 후 저장함)
        chat.addUserMessage(userChat);
        userChatRepository.save(userChat);
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
        // summary 알고리즘 사용안 할 때 테스트 코드
        ChatRequestMsgDto user_request = new ChatRequestMsgDto("user", userChat.getMessage());
        newChatRequestMsgDto.add(user_request);
        // summary 알고리즘 사용할 때.
//        List<ChatRequestMsgDto> summarized_message = summaryChat(newChatRequestMsgDto);
//        ChatRequestMsgDto user_request = new ChatRequestMsgDto("user", userChat.getMessage());
//        summarized_message.add(user_request);
//        ChatCompletionDto newCompletionDto = new ChatCompletionDto(chatCompletionDto.getModel(), summarized_message, chatCompletionDto.getMember_id());
        ChatCompletionDto newCompletionDto = new ChatCompletionDto(chatCompletionDto.getModel(), newChatRequestMsgDto, chatCompletionDto.getMember_id());

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(newCompletionDto, headers);
        System.out.println("requestEntity = " + requestEntity);
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
    public List<ChatRequestMsgDto> summaryChat(List<ChatRequestMsgDto> chatRequestMsgDtoList) {
        // 이전 대화 5개, 두 번째 문장까지만 가져옴
        // User거를 써머리할 필요가 있나?
        StringBuilder joined_user_message = new StringBuilder();
        StringBuilder joined_gpt_message = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        List<ChatRequestMsgDto> summarizeList = new ArrayList<>();
        chatRequestMsgDtoList.stream()
                .takeWhile(item -> counter.incrementAndGet() <= 30)
                .forEach(item -> {
                    int firstDot = item.getContent().indexOf(".");
                    int secondDot = item.getContent().indexOf(".", firstDot + 1);
                    System.out.println("item = " + item);
                    if(secondDot == -1 ){
                        if (item.getRole() == "user") {
                            joined_user_message.append(item.getContent()+"/ ");
                        } else {
                            joined_gpt_message.append(item.getContent() + "/ ");
                        }
                    } else {
                        String subString = item.getContent().substring(0, secondDot + 1);
                        if (item.getRole() == "user") {
                            joined_user_message.append(subString + "/ ");
                        } else {
                            joined_gpt_message.append(subString + "/ ");
                        }
                    }
                });
        ChatRequestMsgDto summarizeUserDto = new ChatRequestMsgDto("user", joined_user_message.toString());
        ChatRequestMsgDto summarizeGptDto = new ChatRequestMsgDto("assistant", joined_gpt_message.toString());
        summarizeList.add(summarizeUserDto);
        summarizeList.add(summarizeGptDto);
        return summarizeList;
    }

    @Override
    public Map<String, Object> getMessageFromChat(Map<String, Object> response) {
        try {
            // Jackson ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writeValueAsString(response);
            // JSON 문자열을 JsonNode로 변환
            JsonNode rootNode = objectMapper.readTree(json);

            // content 값 추출
            String content = rootNode
                    .path("choices")       // choices 배열
                    .get(0)               // 첫 번째 요소
                    .path("message")      // message 객체
                    .path("content")      // content 필드
                    .asText();            // 값 가져오기

            // Map 생성 및 데이터 저장
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("content", content);

            // 결과 출력
            return responseMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
