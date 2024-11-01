package com.todo.todo_api.api;

import com.todo.todo_api.dto.request.RequestUserDto;
import com.todo.todo_api.service.UserService;
import com.todo.todo_api.util.CommonResponseDto;
import com.todo.todo_api.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping(path = {"/register"})
    public ResponseEntity<StandardResponse> registerUser(
            @RequestBody RequestUserDto dto) throws IOException, SQLException {
        CommonResponseDto registeredStateData = userService.registerUser(dto);
        return new ResponseEntity<>(new StandardResponse(registeredStateData.getCode(),
                registeredStateData.getMessage(), registeredStateData.getData()),
                registeredStateData.getCode() == 201 ? HttpStatus.CREATED :
                        registeredStateData.getCode() == 409 ? HttpStatus.CONFLICT :
                                registeredStateData.getCode() == 423 ? HttpStatus.LOCKED : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
