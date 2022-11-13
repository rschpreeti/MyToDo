package com.todo.AdvanceToDo.Services;

import com.todo.AdvanceToDo.Entities.Task;
import com.todo.AdvanceToDo.Entities.UserInfo;
import com.todo.AdvanceToDo.Reposiitry.TaskRepo;
import com.todo.AdvanceToDo.Reposiitry.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class TaskService {
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    UserRepo userRepo;

    @Autowired EmailSenderService emailSenderService;

    public void addTask(Task newTask) {
        taskRepo.save(newTask);
    }

    public List<Task> getAllTask(int userId) {
        return taskRepo.findByUserId(userId);
    }

    public void deleteTask(int id) {
        taskRepo.deleteById(id);
    }

    public void updateTask(int id, String newStatus) {
        Optional<Task> task= taskRepo.findById(id);
        if(task.isPresent()){
            task.get().setStatus(newStatus);
            taskRepo.save(task.get());
        }
    }
//cron(ss min hh dayOfMonth month dayOfWeek year)
    //This is scheduled for every day @ 12 AM night.

    @Scheduled(cron = "0 0 0 * * ?")
    public void TaskNotification() throws MessagingException {
        String Subject="My-ToDos Task reminder for ";
       List<UserInfo> userInfo =userRepo.findAll();
        for(UserInfo user:userInfo){
            String toEmail=user.getEmailId();
            List<Task> task=taskRepo.findByUserId(user.getUserId()).stream().filter(x->x.getStatus()=="Pending").collect(Collectors.toList());
            String body="Hi "+user.getName()+",<br>" +
                    "Greeting of the day...<br>" ;
            String bodyIfTaskDoNotExist="There is no task for the day, Enjoy... <br>" +
                    "Want to add something as task ? <a href=\"URL\">click here</a><br><br>"+
            "Regards <br>" +
            "Team : My-ToDos<br><br>" +
            "<b>Note: <b> This is automated email, pls do not reply as this mailbox is not monitored";;
             String bodyIfTaskExist=   "Today's Date is "+LocalDate.now()+" and the task for the day is as below. <a href=\"URL\">Visit </a> to update if already completed <br><br>"+
                "<table style=\"border-collapse: collapse;border: 3px solid;text-align:center\">" +
                "<caption>Task for " + LocalDate.now()+"</caption>" +
                    "<tr style=\"background-color:#ffccee; color:black\">" +
                        "<th style=\"padding:8px\">Task</th>" +
                        "<th style=\"padding:8px\">Start date</th>" +
                        "<th style=\"padding:8px\">Due date</th>" +
                     "<th style=\"padding:8px\">Severity</th>" +
                    "<tr>";

            System.out.println("Task for the users are -->"+task.size());
            //finding todays date
            Date today=new Date(new java.util.Date().getTime());

            for(Task task1:task){
                if(task1.getStartDate().toString().equals(today.toString())){
                    bodyIfTaskExist+="<tr>" +
                            "<td style=\"padding:8px\">"+task1.getTask()+"</td>" +
                            "<td style=\"padding:8px\">"+task1.getStartDate()+"</td>" +
                            "<td style=\"padding:8px\">"+task1.getDueDate()+"</td>" +
                            "<td style=\"padding:8px\">"+task1.getSeverity()+"</td>"+
                            "</tr>";
                }
            }
            bodyIfTaskExist+="</table> <br><br>" +
                    "Regards <br>" +
                    "Team : My-ToDos<br><br>" +
                    "<b>Note: <b> This is automated email, pls do not reply as this mailbox is not monitored";

            Subject+=LocalDate.now();

            if(task.size()==0){
                body+=bodyIfTaskDoNotExist;
            }else {
                body+=bodyIfTaskExist;
            }

            //emailSenderService.dailyTaskNotification(user.getEmailId(),body,Subject);
        }


    }
}
