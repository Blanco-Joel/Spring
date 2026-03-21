package com.example.library.integration.controller;

import com.example.library.application.model.member.MemberRequest;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import com.example.library.infrastructure.persistence.repository.MemberRepository;
import com.example.library.testsupport.SqliteMvcIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqliteMvcIntegrationTest
class MemberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void createMemberCreatesAndReturnsMember() throws Exception {
        MemberRequest request = MemberRequest.builder()
                .fullName("Joel")
                .email("joel@test.dev")
                .build();

        mockMvc.perform(post("/member/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Joel"))
                .andExpect(jsonPath("$.email").value("joel@test.dev"))
                .andExpect(jsonPath("$.loans").doesNotExist());
    }

    @Test
    void getMembersReturnsPersistedMembers() throws Exception {
        memberRepository.save(new MemberEntity(null, "Joel", "joel@test.dev", null));

        mockMvc.perform(get("/member/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName").value("Joel"))
                .andExpect(jsonPath("$[0].email").value("joel@test.dev"));
    }

    @Test
    void getMemberByIdReturnsPersistedMember() throws Exception {
        MemberEntity savedMember = memberRepository.save(new MemberEntity(null, "Ana", "ana@test.dev", null));

        mockMvc.perform(get("/member/{id}", savedMember.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@test.dev"));
    }

    @Test
    void updateMemberUpdatesPersistedData() throws Exception {
        MemberEntity savedMember = memberRepository.save(new MemberEntity(null, "Original", "original@test.dev", null));
        MemberRequest request = MemberRequest.builder()
                .fullName("Actualizado")
                .email("actualizado@test.dev")
                .build();

        mockMvc.perform(put("/member/{id}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Actualizado"))
                .andExpect(jsonPath("$.email").value("actualizado@test.dev"));
    }

    @Test
    void deleteMemberRemovesMemberFromDatabase() throws Exception {
        MemberEntity savedMember = memberRepository.save(new MemberEntity(null, "Borrable", "borrable@test.dev", null));

        mockMvc.perform(delete("/member/{id}", savedMember.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/member/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getMembersByFilterReturnsMatchingMembers() throws Exception {
        memberRepository.save(new MemberEntity(null, "Filtrado", "filtrado@test.dev", null));

        mockMvc.perform(get("/member/byFilter")
                        .param("email", "filtrado@test.dev")
                        .param("fullName", "Filtrado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName").value("Filtrado"))
                .andExpect(jsonPath("$[0].email").value("filtrado@test.dev"));
    }
}
