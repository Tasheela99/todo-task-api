package com.todo.todo_api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDataDto {
    private String userId;
    private String email;
    private String userName;
    private String roleName;
}
