package com.todo.todo_api.entity;

import com.todo.todo_api.entity.enums.PriorityType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todos")
public class Todo {
    @Id
    @Column(name = "todo_id",length = 80)
    private String todoId;

    @Column(name = "title",length = 100)
    private String title;

    @Column(name = "description",length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_type")
    private PriorityType priorityType;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date",columnDefinition = "DATETIME")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date",columnDefinition = "DATETIME")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at",columnDefinition = "DATETIME")
    private Date createdAt;

    @Column(name = "completed_state",columnDefinition = "TINYINT")
    private boolean completedState;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;
}
