package com.todo.todo_api.dto.request;

import com.todo.todo_api.entity.enums.PriorityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTodoDto {
    private String title;
    private String description;
    private PriorityType priorityType;
    private Date startDate;
    private Date endDate;
}
