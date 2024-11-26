package Manager.spring.controller;

import Manager.spring.Dto.CardDto;
import Manager.spring.domain.Card;
import Manager.spring.domain.Member;
import Manager.spring.service.CardService;
import Manager.spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
