package com.todo.todo_api.api;

import com.todo.todo_api.dto.paginated.PaginatedTodoDto;
import com.todo.todo_api.dto.request.RequestTodoDto;
import com.todo.todo_api.dto.response.ResponseTodoDto;
import com.todo.todo_api.service.TodoService;
import com.todo.todo_api.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
    private final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);
    private final TodoService todoService;

    @PostMapping(path = "/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> create(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestTodoDto dto
    ) {
        LOGGER.info("Received request to create a todo for user with token: {}", token);
        todoService.createTodo(token, dto);
        LOGGER.info("Todo created successfully for user with token: {}", token);
        return new ResponseEntity<>(
                new StandardResponse(201, "Todo Created", null),
                HttpStatus.CREATED
        );
    }

    @PutMapping(path = "/user/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> update(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @RequestBody RequestTodoDto dto
    ) {
        LOGGER.info("Received request to update todo with ID: {} for user with token: {}", id, token);
        boolean isUpdated = todoService.updateTodo(token, id, dto);
        if (isUpdated) {
            LOGGER.info("Todo with ID: {} updated successfully", id);
        } else {
            LOGGER.warn("Failed to update todo with ID: {}", id);
        }
        return new ResponseEntity<>(
                new StandardResponse(200, "Todo Updated", isUpdated),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/user/update-state/{id}/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> updateState(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @PathVariable(value = "status") boolean status
    ) {
        LOGGER.info("Received request to update state of todo with ID: {} to {} for user with token: {}", id, status, token);
        boolean isUpdated = todoService.updateTodoState(token, id, status);
        LOGGER.info("Todo state with ID: {} updated to {}: {}", id, status, isUpdated);
        return new ResponseEntity<>(
                new StandardResponse(200, "Todo State Updated", isUpdated),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/user/get-by-id/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getTodoById(
            @RequestHeader("Authorization") String token,
            @PathVariable String id
    ) {
        LOGGER.info("Received request to fetch todo with ID: {} for user with token: {}", id, token);
        ResponseTodoDto dto = todoService.getTodoById(token, id);
        LOGGER.info("Todo with ID: {} fetched successfully", id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Todo " + id, dto),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/user/get-all", params = {"page", "size", "searchText"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAll(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "searchText", required = false) String searchText
    ) {
        LOGGER.info("Received request to fetch all todos for user with token: {}, page: {}, size: {}, searchText: {}", token, page, size, searchText);
        PaginatedTodoDto dto = todoService.getAllTodos(token, page, size, searchText);
        LOGGER.info("Todos fetched successfully for user with token: {}, total count: {}", token, dto.getTaskDtoList().size());
        return new ResponseEntity<>(
                new StandardResponse(200, "All Todos", dto),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/user/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id) {
        LOGGER.info("Received request to delete todo with ID: {} for user with token: {}", id, token);
        boolean isDeleted = todoService.deleteTodo(token, id);
        if (isDeleted) {
            LOGGER.info("Todo with ID: {} deleted successfully", id);
        } else {
            LOGGER.warn("Failed to delete todo with ID: {}", id);
        }
        return new ResponseEntity<>(
                new StandardResponse(204, "Todo Deleted", isDeleted),
                HttpStatus.NO_CONTENT
        );
    }
}
