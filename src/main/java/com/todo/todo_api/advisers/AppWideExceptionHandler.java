package com.todo.todo_api.advisers;

import com.todo.todo_api.exception.EntryDuplicateException;
import com.todo.todo_api.exception.EntryNotFoundException;
import com.todo.todo_api.exception.UnAuthorizedException;
import com.todo.todo_api.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(EntryDuplicateException.class)
    public ResponseEntity<StandardResponse> handleDuplicateRequestException(EntryDuplicateException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(409, e.getMessage(), e),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(EntryNotFoundException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(404, e.getMessage(), e),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<StandardResponse> handleUserUnAuthorizedException(UnAuthorizedException e) {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(401, e.getMessage(), e),
                HttpStatus.UNAUTHORIZED);
    }
}
