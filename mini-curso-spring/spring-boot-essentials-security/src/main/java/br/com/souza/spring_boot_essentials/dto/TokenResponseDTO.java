package br.com.souza.spring_boot_essentials.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TokenResponseDTO {

    private String token;
    private String type;
    private long expiration;
}

