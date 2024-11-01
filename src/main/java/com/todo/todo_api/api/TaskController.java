package com.todo.todo_api.api;

import com.todo.todo_api.dto.paginated.PaginatedTaskDto;
import com.todo.todo_api.dto.request.RequestTaskDto;
import com.todo.todo_api.dto.response.ResponseTaskDto;
import com.todo.todo_api.service.TaskService;
import com.todo.todo_api.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping(path = "/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> create(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestTaskDto dto
    ) {
        taskService.createTask(token,dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Task Created",
                        null
                ), HttpStatus.CREATED
        );
    }

    @PutMapping(path = "/user/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> update(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @RequestBody RequestTaskDto dto
    ) {
        boolean isUpdated = taskService.updateTask(token,id,dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Task Updated",
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
        boolean isUpdated = taskService.updateTaskState(token,id,status);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Task State Updated",
                        isUpdated
                ), HttpStatus.OK
        );
    }

    @GetMapping(path = "/user/get-by-id/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getTaskById(
            @RequestHeader("Authorization") String token,
            @PathVariable String id
    ) {
        ResponseTaskDto dto  = taskService.getTaskById(token,id);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Task "+id,
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
        PaginatedTaskDto dto = taskService.getAllTasks(token,page, size, searchText);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Tasks",
                        dto
                ), HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/user/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id) {
        boolean isDeleted = taskService.deleteTask(token,id);
        return new ResponseEntity<>(
                new StandardResponse(
                        204,
                        "Task Deleted",
                        isDeleted),
                HttpStatus.NO_CONTENT
        );
    }

}
