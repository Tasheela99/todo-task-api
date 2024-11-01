package com.todo.todo_api.dto.response;

import com.todo.todo_api.entity.enums.PriorityType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTaskDto {
    private String taskId;
    private String title;
    private String description;
    private PriorityType priorityType;
    private String startDate;
    private String endDate;
    private String createdAt;
    private boolean completedState;
    private String user;
}
