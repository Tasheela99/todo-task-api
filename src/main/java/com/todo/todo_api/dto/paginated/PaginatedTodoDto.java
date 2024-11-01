package com.todo.todo_api.dto.paginated;

import com.todo.todo_api.dto.response.ResponseTodoDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedTodoDto {
    private List<ResponseTodoDto> taskDtoList;
    private long dataCount;
}
