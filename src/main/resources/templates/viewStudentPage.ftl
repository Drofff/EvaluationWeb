<html>
<head>
	<title>Evaluation Web</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
	<link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/teacherNavbar.ftl">

<#if error_message??>
	<div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
		<p>${error_message}</p>
	</div>
</#if>

<div class="ui segment">

	<img class="ui medium circular image" src="${student.photoUrl}" style="margin-left: auto; margin-right: auto; width: 250px; height: 250px; margin-top: 10px;">
	<div style="text-align : center; margin-top: 2%;">
		<h1 style="margin-left: auto; margin-right: auto; width:30%">${student.firstName} ${student.lastName}</h1>
	</div>
	<h4 style="margin-left: 35%;">
		<#if student.status == 'Online'>
			<i style="color: green" class="circle icon"></i>
		<#else>
			<i style="color: green" class="circle outline icon"></i>
		</#if>
		Status: ${student.status}</h4>
	<h4 style="margin-left: 35%;"><i class="address card icon"></i> Position: ${student.position}</h4>
	<h4 style="margin-left: 35%;"><i class="users icon"></i> Group: ${student.groupName}</h4>
	<h4 style="margin-left: 35%;"><i class="users icon"></i>Email: ${student.email}</h4>
	<h4 style="margin-left: 35%;">
		<a class="ui primary button" href="/mail/send?receiverId=${student.id}">
			Send Mail
		</a>
		<#if subject??>
			<a class="ui primary button" href="/success/manage-students?subjectId=${subject.id}&studentId=${student.id}">
				Evaluate
			</a>
        </#if>
	</h4>
</div>
</body>
</html>