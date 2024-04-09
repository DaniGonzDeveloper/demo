package com.stibodx.demo.utils;

import lombok.*;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAuth {
    String username;
    String email;
    List<String> authorities;
    List<Integer> authoritiesIds;
}
