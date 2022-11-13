package com.todo.AdvanceToDo.Controller;

import com.todo.AdvanceToDo.SecureData;
import com.todo.AdvanceToDo.Entities.Task;
import com.todo.AdvanceToDo.Entities.UserInfo;
import com.todo.AdvanceToDo.Services.EmailSenderService;
import com.todo.AdvanceToDo.Services.TaskService;
import com.todo.AdvanceToDo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;
import java.util.Objects;


@Controller
public class WebController {
    @Autowired TaskService taskService;
    @Autowired UserService userService;

    @Autowired EmailSenderService emailSenderService;
    @RequestMapping("/")
    public String login(){return  "login.html";}

    @RequestMapping("/myTask")
    public String homePage(){
        return "index.html";
    }

    @PostMapping("addTask/{task}/{severity}/{startDate}/{dueDate}/{userId}")
    @ResponseBody
    public void addTask(@PathVariable("task") String task,
                          @PathVariable("severity") String severity,
                          @PathVariable("startDate") Date startDate,
                          @PathVariable("dueDate") Date dueDate,
                        @PathVariable("userId") int userId){
        String status="Pending";
        try {
            Task newTask=new Task(task,severity,startDate,dueDate,status,userId);
            taskService.addTask(newTask);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    @GetMapping("getAllTask/{userId}")
    @ResponseBody
    public List<Task> getAllTask(@PathVariable("userId") int userId){
        return taskService.getAllTask(userId);
    }

    @PostMapping("addUser/{name}/{mobNo}/{email}/{pass}")
    @ResponseBody
    public String addUser(@PathVariable("name") String name,
                        @PathVariable("mobNo") String mobNo,
                        @PathVariable("email") String email,
                        @PathVariable("pass") String pass){

        String msg= "Something went wrong.\nTry again...";
    try{
        UserInfo newUser=new UserInfo(name,mobNo,email, SecureData.encrypt(pass));
        int flag=userService.addUser(newUser);
        if(flag==1){
            String emailBody= "Hi "+name+",<br><br>"+
                    "Welcome to My-ToDos, developed and maintained by Miss Preeti Chaturvedi<br><br>" +
                    "Thanks and Regards<br>" +
                    "Team My-ToDos<br>" +
                    "<b>Note:- <b> This is automated email, pls don't reply to it as this mailbox is not monitored";
                    ;
            emailSenderService.sendEmailMassege(email,"Registered Successfully on My-ToDo",emailBody);
            msg= "Registered successfully";
        } else if (flag==0) {
            msg= "User with this email already exist.";
        }
    }catch (Exception e){
        System.out.println(e);
    }
        return msg;
    }

    @PostMapping("validate/{userName}/{password}")
    @ResponseBody
    public UserInfo validateUser(@PathVariable("userName") String email,@PathVariable("password") String password){
        UserInfo user=userService.validateUser(email,password);

        System.out.print(user.getName());
        if(Objects.nonNull(user))
        {return user;} else {
            return user;}
    }


    @RequestMapping("/logout")
    public String logoutDo(HttpServletRequest request, HttpServletResponse response)
    {
        return "logout.html";
    }

    @PostMapping("deleteTask/{id}")
    @ResponseBody
    public void deleteTask(@PathVariable("id") int id){
        taskService.deleteTask(id);}

    @PostMapping("changeStatus/{id}/{newStatus}")
    @ResponseBody
    public void updateTask(@PathVariable("id") int id,@PathVariable("newStatus") String newStatus){
        taskService.updateTask(id,newStatus);
    }

}
