package com.todo.todo_api.service.impl;

import com.todo.todo_api.dto.paginated.PaginatedTodoDto;
import com.todo.todo_api.dto.request.RequestTodoDto;
import com.todo.todo_api.dto.response.ResponseTodoDto;
import com.todo.todo_api.entity.Todo;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.exception.EntryDuplicateException;
import com.todo.todo_api.exception.EntryNotFoundException;
import com.todo.todo_api.exception.UnAuthorizedException;
import com.todo.todo_api.repo.TodoRepo;
import com.todo.todo_api.repo.UserRepo;
import com.todo.todo_api.service.TodoService;
import com.todo.todo_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final UserService userService;
    private final TodoRepo todoRepo;
    private final UserRepo userRepo;

    @Override
    public void createTodo(String token, RequestTodoDto dto) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        Todo todo = Todo.builder()
                .todoId(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priorityType(dto.getPriorityType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdAt(new Date())
                .completedState(false)
                .user(user)
                .build();
        if (!todoRepo.existsById(todo.getTodoId())) {
            todoRepo.save(todo);
        } else {
            throw new EntryDuplicateException("Todo already exists");
        }
    }

    @Override
    public boolean updateTodo(String token, String id, RequestTodoDto dto) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }

        Todo existingTodo = todoRepo.findById(id).orElseThrow(() ->
                new EntryNotFoundException("Todo Not Found"));

        existingTodo.setTitle(dto.getTitle());
        existingTodo.setDescription(dto.getDescription());
        existingTodo.setPriorityType(dto.getPriorityType());
        existingTodo.setStartDate(dto.getStartDate());
        existingTodo.setEndDate(dto.getEndDate());
        todoRepo.save(existingTodo);
        return true;
    }


    @Override
    public ResponseTodoDto getTodoById(String token, String id) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        Optional<Todo> task = todoRepo.findById(id);
        if (task.isEmpty()) {
            throw new EntryNotFoundException("Todo Not Found");
        }
        return converter(task.get());
    }

    @Override
    public PaginatedTodoDto getAllTodos(String token, int page, int size, String searchText) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        List<ResponseTodoDto> list = todoRepo.findAllTodos(searchText, PageRequest.of(page, size),user.getUserId()).stream().map(
                this::converter
        ).collect(Collectors.toList());
        long count = todoRepo.countAllTodos(searchText,user.getUserId());
        return PaginatedTodoDto.builder().taskDtoList(list).dataCount(count).build();
    }

    @Override
    public boolean deleteTodo(String token, String id) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new EntryNotFoundException("User Not Found");
        }
        Optional<Todo> task = todoRepo.findById(id);
        if (task.isEmpty()) {
            throw new EntryNotFoundException("Todo Not Found");
        }
        if (!task.get().isCompletedState()){
            throw new UnAuthorizedException("Todo Needs to be completed");
        }
        todoRepo.delete(task.get());
        return true;
    }

    @Override
    public boolean updateTodoState(String token, String id, boolean status) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }

        Todo existingTodo = todoRepo.findById(id).orElseThrow(() ->
                new EntryNotFoundException("Todo Not Found"));

        existingTodo.setCompletedState(status);
        todoRepo.save(existingTodo);
        return true;
    }

    private ResponseTodoDto converter(Todo todo) {
        return ResponseTodoDto.builder()
                .taskId(todo.getTodoId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .priorityType(todo.getPriorityType())
                .startDate(todo.getStartDate().toString())
                .endDate(todo.getEndDate().toString())
                .createdAt(todo.getCreatedAt().toString())
                .completedState(todo.isCompletedState())
                .user(todo.getUser().getEmail())
                .build();
    }
}
