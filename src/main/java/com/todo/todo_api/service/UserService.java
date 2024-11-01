package com.todo.todo_api.service;

import com.todo.todo_api.dto.request.RequestUserDto;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.util.CommonResponseDto;

public interface UserService {
    CommonResponseDto registerUser(RequestUserDto dto);
    User getUserByToken(String token);

}
