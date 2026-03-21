package com.example.library.application.controller;

import com.example.library.application.model.member.MemberRequest;
import com.example.library.application.model.member.MemberResponse;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.utils.mappers.BookResponseMappers;
import com.example.library.application.utils.mappers.MemberResponseMappers;
import com.example.library.domain.model.Book;
import com.example.library.domain.model.Member;
import com.example.library.domain.service.MemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;
    private final MemberResponseMappers memberResponseMappers;

    @GetMapping("/all")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<Member> response = memberService.findAll();

        return new ResponseEntity<>(memberResponseMappers.responseListMapper(response), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponse> createBook(@RequestBody MemberRequest request) {
        Member response = memberService.createMember(request);

        return new ResponseEntity<>(memberResponseMappers.responseMapperWithoutLoan(response), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getBookById(@PathVariable("id") Long id) throws BookNotFound {
        Member response = memberService.findById(id);

        return new ResponseEntity<>(memberResponseMappers.responseMapper(response), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<MemberResponse> updateBook(@PathVariable("id") Long id, @RequestBody MemberRequest request) {
        Member response = memberService.updateMember(id, request);

        return new ResponseEntity<>(memberResponseMappers.responseMapper(response), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") Long id) {
        memberService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/byFilter")
    public ResponseEntity<List<MemberResponse>> getBooksByFilter(@RequestParam(required = false) String  email,
                                                               @RequestParam(required = false) String  fullName) {
        List<Member> response = memberService.findByFilter(email,fullName);
        return new ResponseEntity<>(memberResponseMappers.responseListMapper(response), HttpStatus.OK);

    }
}
