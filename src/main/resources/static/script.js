$(document).ready( function () {
    $('#myData').DataTable();
    document.getElementById('userName').innerHTML= 'Hi '+sessionStorage.userName;
     getAllTask();
} );

$('#add').click(function(){
    $('#cover-spin').css('display','block');
var task=document.getElementById('task').value;
var severity=document.getElementById('severity').value;
var startDate=document.getElementById('startDate').value;
var dueDate=document.getElementById('dueDate').value;
if(validateTask(task,severity,startDate,dueDate)){
    //Calling AJAX to insert the data in Database
    $.ajax({
    url:'addTask/'+task+'/'+severity+'/'+startDate+'/'+dueDate+'/'+sessionStorage.userId,
    type:'POST',
    success:function(response){
    getAllTask();
    alert('Task added successfully');
    $('#cover-spin').css('display','none');
    },
    error:function(err){
    alert('Something went wrong..\nTask not added, please try again');
    console.log(e);
    $('#cover-spin').css('display','none');
    }
    })
}else{
    $('#cover-spin').css('display','none');
}

})

function getAllTask(){
$('#cover-spin').css('display','block');
var table=$('#myData').DataTable();
table.clear().draw();
dataSet=[];
$.ajax({
url:'getAllTask/'+sessionStorage.userId,
type:'GET',
success:function(response){
for(var i=0;i<response.length;i++){
console.log(response[i][0]);
table.row.add(['TSK'+response[i].taskId,
response[i].task,
response[i].severity,
response[i].startDate,
response[i].dueDate,
response[i].status,
'<a href="#" onclick="deleteTask('+response[i].taskId+')"><i class="fas fa-trash text-danger"></i></a>',
'<a href="#"onclick=changeStatusOfTask('+response[i].taskId+',"'+response[i].startDate+'","'+response[i].dueDate+'","'+ String(response[i].status)+'","In-Progress")><i class="fa fa-spinner text-primary"></i></a> &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; <a href="#"onclick=changeStatusOfTask('+response[i].taskId+',"'+response[i].startDate+'","'+response[i].dueDate+'","'+ String(response[i].status)+'","Completed")><i class="far fa-check-circle text-success"></i></a></a>']
).draw();
//table.row.add(dataSet).draw();
}
$('#cover-spin').css('display','none');
},
error:function(err){
console.log(err);
$('#cover-spin').css('display','none');
}
});

}

function validateTask(task,severity,startDate,dueDate){
var specialChars = '[`#%^&*_+-=[]{};\':"\\|<>/~]/';
var validationFlag='';
if(task==''||task===''){
alert('Task cannot be empty.\nPlease enter the task name to proceed');
validationFlag=false;
}
else if(severity==''||severity===''){
alert('Severity cannot be empty.\nPlease enter the severity to proceed');
validationFlag=false;
}
else if(startDate==''||startDate===''){
alert('Task start date cannot be empty.\nPlease enter the start date to proceed');
validationFlag=false;
}
else if(dueDate==''||dueDate===''){
alert('Task due date cannot be empty.\nPlease enter the due date to proceed');
validationFlag=false;
}
else if(specialChars.split('').some((specialChar) => task.includes(specialChar))){
alert('Special character not allowed');
validationFlag=false;
}else if(sessionStorage.userId=='-1')
{
alert("Who are you ?\nPlease login to identify yourself..");
window.location.assign("/");
}
else if (!checkDate(startDate,dueDate)){
alert("Start date cannot be greater then the due date. ");
validationFlag=false;
}
else{
validationFlag=true;
}
return validationFlag;
}

$('#Registered').click(function(){
$('#cover-spin').css('display','block');
var name=document.getElementById('registerName').value;
var mobNo=document.getElementById('registerMobNo').value;
var email=document.getElementById('registerEmail').value;
var pass=document.getElementById('registerPassword').value;
var rePass=document.getElementById('registerRepeatPassword').value;

    if(rePass.localeCompare(pass)==0){
    //Calling AJAX to insert the user data in Database
        $.ajax({
        url:'addUser/'+name+'/'+mobNo+'/'+email+'/'+pass,
        type:'POST',
        success:function(response){
        alert(response);
        location.reload();
        $('#cover-spin').css('display','none');
        },
        error:function(err){
        alert('Something went wrong... please try again');
        console.log(e);
        $('#cover-spin').css('display','none');
        }
        })
    }else{
    alert("Password is not matching");
    $('#cover-spin').css('display','none');
    }
});


$('#loginBtn').click(function(){
    $('#cover-spin').css('display','block');
var userName=document.getElementById('loginName').value;
var password=document.getElementById('loginPassword').value;

$.ajax({
        url:'validate/'+userName+'/'+password,
        type:'POST',
        success:function(response){
        console.log(response);
        sessionStorage.userName=response['name'];
        sessionStorage.userEmail=response['emailId'];
        sessionStorage.userId=response['userId'];
        console.log(sessionStorage.userName+'  '+sessionStorage.userEmail);
        window.location.href='/myTask';
        $('#cover-spin').css('display','none');
        },
        error:function(err){
        alert('Something went wrong... please try again');
        console.log(e);
        $('#cover-spin').css('display','none');
        }
     })
});


$('#logout').click(function(){
sessionStorage.userName=null;
sessionStorage.userEmail=null;
sessionStorage.userId=-1;
});

function deleteTask(id){
    $('#cover-spin').css('display','block');
    if(confirm("Are you sure, want to delete task with id -> TSK"+id)){
        $.ajax({
        url: "deleteTask/"+id,
        type:'POST',
        success:function(response){
        alert("Task deleted successfully..");
        getAllTask();
        $('#cover-spin').css('display','none');
        },
        error:function(err){
        alert("something went wrong, try again..");
        $('#cover-spin').css('display','none');
        }
        });
    }else{
        $('#cover-spin').css('display','none');
    }

}

function changeStatusOfTask(id,startDate,dueDate,currentStatus,newStatus){
    $('#cover-spin').css('display','none');

if(checkDate(startDate,new Date())){
if(confirm("Changing status of task from "+currentStatus+" to "+newStatus)){
        $.ajax({
        url: "changeStatus/"+id+"/"+newStatus,
        type:'POST',
        success:function(response){
        alert("Task updated successfully..");
        getAllTask();
        },
        error:function(err){
        alert("something went wrong, try again..");
        }
        });
    }else{$('#cover-spin').css('display','none');}
}else{
alert('Cannot change the status of this task as the task is yet not started.');
$('#cover-spin').css('display','none');
}
}

function checkDate(startDate,dueDate){
var start=new Date(startDate);
var today=new Date(dueDate);
if(today.getTime()>=start.getTime()){
return true
}else{
return false
}
}
