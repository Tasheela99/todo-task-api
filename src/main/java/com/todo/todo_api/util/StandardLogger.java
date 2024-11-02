package com.todo.todo_api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class StandardLogger {
    private String type;
    private String message;
}
