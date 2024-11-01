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

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping(path = "/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> create(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestTodoDto dto
    ) {
        todoService.createTodo(token,dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Todo Created",
                        null
                ), HttpStatus.CREATED
        );
    }

    @PutMapping(path = "/user/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> update(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @RequestBody RequestTodoDto dto
    ) {
        boolean isUpdated = todoService.updateTodo(token,id,dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Todo Updated",
                        isUpdated
                ), HttpStatus.OK
        );
    }

    @PutMapping(path = "/user/update-state/{id}/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> updateState(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @PathVariable(value = "status") boolean status
    ) {
        boolean isUpdated = todoService.updateTodoState(token,id,status);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Todo State Updated",
                        isUpdated
                ), HttpStatus.OK
        );
    }

    @GetMapping(path = "/user/get-by-id/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getTodoById(
            @RequestHeader("Authorization") String token,
            @PathVariable String id
    ) {
        ResponseTodoDto dto  = todoService.getTodoById(token,id);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Todo "+id,
                        dto
                ), HttpStatus.OK
        );
    }

    @GetMapping(path = "/user/get-all", params = {"page", "size", "searchText"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAll(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "searchText",required = false) String searchText
    ) {
        PaginatedTodoDto dto = todoService.getAllTodos(token,page, size, searchText);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Todos",
                        dto
                ), HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/user/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id) {
        boolean isDeleted = todoService.deleteTodo(token,id);
        return new ResponseEntity<>(
                new StandardResponse(
                        204,
                        "Todo Deleted",
                        isDeleted),
                HttpStatus.NO_CONTENT
        );
    }

}
