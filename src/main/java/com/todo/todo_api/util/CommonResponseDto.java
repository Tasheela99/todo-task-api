package com.todo.todo_api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonResponseDto {
    private int code;
    private String message;
    private Object data;
    private ArrayList<Object> records;
}
