package com.todo.todo_api.dto.paginated;

import com.todo.todo_api.dto.response.ResponseTaskDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedTaskDto {
    private List<ResponseTaskDto> taskDtoList;
    private long dataCount;
}
