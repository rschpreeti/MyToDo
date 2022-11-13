package com.todo.AdvanceToDo.Reposiitry;

import com.todo.AdvanceToDo.Entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserInfo,Integer> {
    UserInfo findByEmailIdAndPassword(String emailId, String password);
    UserInfo findByEmailId(String emailId);
}
