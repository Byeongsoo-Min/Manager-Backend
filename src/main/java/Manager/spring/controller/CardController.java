package Manager.spring.controller;

import Manager.spring.Dto.CardDto;
import Manager.spring.domain.Card;
import Manager.spring.domain.Member;
import Manager.spring.service.CardService;
import Manager.spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/card")
@RestController
public class CardController {
    private final CardService cardService;
    private final MemberService memberService;
    @Autowired
    public CardController(CardService cardService, MemberService memberService) {
        this.cardService = cardService;
        this.memberService = memberService;
    }

    @PostMapping("/uploads")
    public ResponseEntity<Map<String, Object>> uploadCard(@RequestParam("image") MultipartFile image, @ModelAttribute CardDto cardDto) {
        Card card = new Card();
        Long memberId = cardDto.getMemberId();
        Member member = new Member();
        try {
            member = memberService.findOne(memberId).get();
        } catch (Exception e) {
            System.out.println("e = " + e);
            return new ResponseEntity<>(createFailureResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        card.setCardEntity(cardDto, member);

        cardService.createCard(card, image);

        try {
            Long cardId = card.getId();
            return new ResponseEntity<>(createSuccessResponse(cardId), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("e = " + e);
            return new ResponseEntity<>(createFailureResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/retrieve")
    public ResponseEntity<List<Map<String, Object>>> getCardsByMemberId(@RequestParam(name = "memberId") Long memberId) {
        List<Card> cards = cardService.getCardByMemberId(memberId);
        if (cards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        // Card -> CardDto 변환
        List<CardDto> cardDtos = cards.stream()
                .map(CardDto::fromEntity)
                .collect(Collectors.toList());
        List<Map<String, Object>> response = new ArrayList<>();
        cardDtos.stream()
                .forEach( item -> {
                    try {
                        // 이미지 파일 읽기
                        String actualSrc = "src/main/resources/static/uploads/card";
                        Integer fileNameIndex = item.getCompanyImageUrl().lastIndexOf("/");
                        String imagePath = actualSrc + item.getCompanyImageUrl().substring(fileNameIndex + 1);
                        File imageFile = new File(imagePath);

                        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                        // 메타데이터와 함께 JSON 생성
                        Map<String, Object> cardInfo = new HashMap<>();
                        cardInfo.put("companyName", item.getCompanyName());
                        cardInfo.put("companyNumber", item.getCompanyNumber());
                        cardInfo.put("cardId", item.getCardId());
                        cardInfo.put("image", base64Image); // Base64 인코딩된 이미지
                        response.add(cardInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createSuccessResponse(Long card_id) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "Card Created Successfully");
        response.put("cardId", card_id);

        return response;
    }

    private Map<String, Object> createFailureResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);

        return response;
    }
}
