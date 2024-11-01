package com.todo.todo_api.service;

import com.todo.todo_api.dto.paginated.PaginatedTaskDto;
import com.todo.todo_api.dto.request.RequestTaskDto;
import com.todo.todo_api.dto.response.ResponseTaskDto;

public interface TaskService {
    void createTask(String token,RequestTaskDto dto);

    boolean updateTask(String token,String id,RequestTaskDto dto);

    ResponseTaskDto getTaskById(String token,String id);

    PaginatedTaskDto getAllTasks(String token,int page, int size, String searchText);

    boolean deleteTask(String token,String id);

    boolean updateTaskState(String token, String id, boolean status);
}
