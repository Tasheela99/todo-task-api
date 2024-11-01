package com.todo.todo_api.service.impl;

import com.todo.todo_api.dto.paginated.PaginatedTaskDto;
import com.todo.todo_api.dto.request.RequestTaskDto;
import com.todo.todo_api.dto.response.ResponseTaskDto;
import com.todo.todo_api.entity.Task;
import com.todo.todo_api.entity.User;
import com.todo.todo_api.exception.EntryDuplicateException;
import com.todo.todo_api.exception.EntryNotFoundException;
import com.todo.todo_api.exception.UnAuthorizedException;
import com.todo.todo_api.repo.TaskRepo;
import com.todo.todo_api.repo.UserRepo;
import com.todo.todo_api.service.TaskService;
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
public class TaskServiceImpl implements TaskService {

    private final UserService userService;
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    @Override
    public void createTask(String token, RequestTaskDto dto) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        Task task = Task.builder()
                .taskId(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priorityType(dto.getPriorityType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdAt(new Date())
                .completedState(false)
                .user(user)
                .build();
        if (!taskRepo.existsById(task.getTaskId())) {
            taskRepo.save(task);
        } else {
            throw new EntryDuplicateException("Task already exists");
        }
    }

    @Override
    public boolean updateTask(String token, String id, RequestTaskDto dto) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }

        Task existingTask = taskRepo.findById(id).orElseThrow(() ->
                new EntryNotFoundException("Task Not Found"));

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setPriorityType(dto.getPriorityType());
        existingTask.setStartDate(dto.getStartDate());
        existingTask.setEndDate(dto.getEndDate());
        taskRepo.save(existingTask);
        return true;
    }


    @Override
    public ResponseTaskDto getTaskById(String token, String id) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        Optional<Task> task = taskRepo.findById(id);
        if (task.isEmpty()) {
            throw new EntryNotFoundException("Task Not Found");
        }
        return converter(task.get());
    }

    @Override
    public PaginatedTaskDto getAllTasks(String token, int page, int size, String searchText) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }
        List<ResponseTaskDto> list = taskRepo.findAllTasks(searchText, PageRequest.of(page, size)).stream().map(
                this::converter
        ).collect(Collectors.toList());
        long count = taskRepo.countAllTasks(searchText);
        return PaginatedTaskDto.builder().taskDtoList(list).dataCount(count).build();
    }

    @Override
    public boolean deleteTask(String token, String id) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new EntryNotFoundException("User Not Found");
        }
        Optional<Task> task = taskRepo.findById(id);
        if (task.isEmpty()) {
            throw new EntryNotFoundException("Task Not Found");
        }
        if (!task.get().isCompletedState()){
            throw new UnAuthorizedException("Task Needs to be completed");
        }
        taskRepo.delete(task.get());
        return true;
    }

    @Override
    public boolean updateTaskState(String token, String id, boolean status) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            throw new UnAuthorizedException("User Not Found");
        }

        Task existingTask = taskRepo.findById(id).orElseThrow(() ->
                new EntryNotFoundException("Task Not Found"));

        existingTask.setCompletedState(status);
        taskRepo.save(existingTask);
        return true;
    }

    private ResponseTaskDto converter(Task task) {
        return ResponseTaskDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priorityType(task.getPriorityType())
                .startDate(task.getStartDate().toString())
                .endDate(task.getEndDate().toString())
                .createdAt(task.getCreatedAt().toString())
                .completedState(task.isCompletedState())
                .user(task.getUser().getEmail())
                .build();
    }
}
