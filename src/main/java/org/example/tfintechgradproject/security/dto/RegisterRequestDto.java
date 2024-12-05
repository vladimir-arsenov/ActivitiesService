package org.example.tfintechgradproject.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

  private String email;
  private String password;
  private String nickname;
  private boolean rememberMe;
}