package com.project.shopapp.dtos.responses;

import com.project.shopapp.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private User user;
}
