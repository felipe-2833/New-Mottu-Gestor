package br.com.fiap.newmottugestor.oracle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Data
@Table(name = "mottuser")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    public User(OAuth2User principal, String email) {
        this.name = principal.getAttributes().get("name").toString();
        this.email = email;
        this.avatarUrl = principal.getAttributes().get("avatar_url").toString();
    }
}
