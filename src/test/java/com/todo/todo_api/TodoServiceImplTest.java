package com.todo.todo_api;

import com.todo.todo_api.dto.request.RequestTodoDto;
import com.todo.todo_api.entity.Todo;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.entity.enums.PriorityType;
import com.todo.todo_api.exception.UnAuthorizedException;
import com.todo.todo_api.repo.TodoRepo;
import com.todo.todo_api.service.UserService;
import com.todo.todo_api.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TodoRepo todoRepo;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTodo_Success() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXNoZWVsYWpheTE5OTlAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzMwNTI3ODczLCJleHAiOjE3MzU2Njk4MDB9.7XzOoX0-PcSBW9db8e-nO1fy9xNyPLYc_mziSh8HSx4";
        RequestTodoDto dto = new RequestTodoDto("Task 1", "Description", PriorityType.HIGH, new Date(), new Date());

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());

        when(userService.getUserByToken(token)).thenReturn(user);
        when(todoRepo.existsById(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> todoService.createTodo(token, dto));
        verify(todoRepo, times(1)).save(any(Todo.class));
    }

    @Test
    void createTodo_UserNotFound_ShouldThrowUnAuthorizedException() {
        String token = "yJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXNoZWVsYWpheTE5OTlAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzMwNTI3ODczLCJleHAiOjE3MzU2Njk4MDB9.7XzOoX0-PcSBW9db8e-nO1fy9xNyPLYc_mziSh8HSx4";

        RequestTodoDto dto = new RequestTodoDto("Task 1", "Description", PriorityType.HIGH, new Date(), new Date());

        when(userService.getUserByToken(token)).thenReturn(null);

        assertThrows(UnAuthorizedException.class, () -> todoService.createTodo(token, dto));
    }

    @Test
    void updateTodo_Success() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXNoZWVsYWpheTE5OTlAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzMwNTI3ODczLCJleHAiOjE3MzU2Njk4MDB9.7XzOoX0-PcSBW9db8e-nO1fy9xNyPLYc_mziSh8HSx4";
        String id = "be76daa5-e68e-44e6-b73d-ade9427a7ff7";
        RequestTodoDto dto = new RequestTodoDto("Updated Task", "Updated Description", PriorityType.MEDIUM, new Date(), new Date());

        User user = new User();
        Todo todo = new Todo();
        todo.setTodoId(id);

        when(userService.getUserByToken(token)).thenReturn(user);
        when(todoRepo.findById(id)).thenReturn(Optional.of(todo));

        boolean result = todoService.updateTodo(token, id, dto);
        assertTrue(result);
        verify(todoRepo, times(1)).save(todo);
    }

    @Test
    void deleteTodo_TodoNotCompleted_ShouldThrowUnAuthorizedException() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXNoZWVsYWpheTE5OTlAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzMwNTI3ODczLCJleHAiOjE3MzU2Njk4MDB9.7XzOoX0-PcSBW9db8e-nO1fy9xNyPLYc_mziSh8HSx4";
        String id = "be76daa5-e68e-44e6-b73d-ade9427a7ff7";

        User user = new User();
        Todo todo = new Todo();
        todo.setTodoId(id);
        todo.setCompletedState(false);

        when(userService.getUserByToken(token)).thenReturn(user);
        when(todoRepo.findById(id)).thenReturn(Optional.of(todo));

        assertThrows(UnAuthorizedException.class, () -> todoService.deleteTodo(token, id));
    }

    @Test
    void updateTodoState_Success() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXNoZWVsYWpheTE5OTlAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzMwNTI3ODczLCJleHAiOjE3MzU2Njk4MDB9.7XzOoX0-PcSBW9db8e-nO1fy9xNyPLYc_mziSh8HSx4";
        String id = "be76daa5-e68e-44e6-b73d-ade9427a7ff7";
        boolean newStatus = true;

        User user = new User();
        Todo todo = new Todo();
        todo.setTodoId(id);

        when(userService.getUserByToken(token)).thenReturn(user);
        when(todoRepo.findById(id)).thenReturn(Optional.of(todo));

        boolean result = todoService.updateTodoState(token, id, newStatus);
        assertTrue(result);
        assertEquals(newStatus, todo.isCompletedState());
        verify(todoRepo, times(1)).save(todo);
    }
}
