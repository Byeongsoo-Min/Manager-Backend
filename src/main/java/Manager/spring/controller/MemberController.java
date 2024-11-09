package Manager.spring.controller;


import Manager.spring.Dto.MemberDto;
import Manager.spring.domain.Card;
import Manager.spring.domain.Member;
import Manager.spring.service.CardService;
import Manager.spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> create(@RequestBody MemberDto memberDto) {
        Member member = new Member();
        String manager_name = memberDto.getManager_name();
        member.setManagerName(manager_name);
        try {
            Long memberId = memberService.join(member);
            return new ResponseEntity<>(createSuccessResponse(memberId, manager_name), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("e = " + e);
            return new ResponseEntity<>(createFailureResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //성공 응답 반환
    //추후 JWT 토큰 발급 해야함
    private Map<String, Object> createSuccessResponse(Long member_id, String manager_name) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "User Created Successfully");
        response.put("UserID", member_id);
        response.put("Manager_name", manager_name);

        return response;
    }

    private Map<String, Object> createFailureResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);

        return response;
    }
}
