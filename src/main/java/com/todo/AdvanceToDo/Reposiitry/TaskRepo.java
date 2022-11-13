package com.todo.AdvanceToDo.Reposiitry;

import com.todo.AdvanceToDo.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Integer> {
    List<Task> findByUserId(int userId);
}
