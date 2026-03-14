package com.example.library.infrastructure.persistence.entities;

import io.swagger.v3.oas.models.links.Link;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Table( name = "member")
@NoArgsConstructor

public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long memberId;
    private String fullName;
    private String email;

    @OneToMany(mappedBy = "members")
    private Set<LoanEntity> loans;
}
