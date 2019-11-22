<html>
<head>
	<title>Evaluation Web | Students Success</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
	<link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/navbar.ftl">
<#if error_message??>
	<div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
		<p>${error_message}</p>
	</div>
</#if>

<div class="ui segment">

	<div style="margin-left: 10%; margin-top: 2%; margin-bottom: 5%; margin-right: 10%;">
		<form action="/success/manage-students" method="get">
			<div id="subject_selector" class="ui selection dropdown">
				<input type="hidden" name="subjectId">
				<i class="dropdown icon"></i>
				<div class="default text">Subject</div>
				<div class="menu">
                    <#list subjects as subject>
						<div class="item" data-value="${subject.id}">${subject.name}</div>
                    </#list>
				</div>
			</div>
			<div id="student_selector" class="ui selection dropdown">
				<input type="hidden" name="studentId">
				<i class="dropdown icon"></i>
				<div class="default text">Student</div>
				<div class="menu">
                    <#list students as student>
						<div class="item" data-value="${student.id}">${student.firstName} ${student.lastName} (${student.userId.username})</div>
                    </#list>
				</div>
			</div>
			<button type="submit" class="ui primary button">
				Show
			</button>
			<#if selected_student_id?? && selected_subject_id??>
				<a id="eval_button" class="ui inverted primary button">Evaluate</a>
			</#if>
		</form>

        <#if marks?? && marks?size gt 0>
			<table class="ui celled table">
				<thead>
				<tr><th>Mark</th>
					<th>Description</th>
					<th>Datetime</th>
				</tr></thead>
				<tbody>
                <#list marks as mark>
					<tr>
						<td data-label="Mark">${mark.mark}</td>
						<td data-label="Description">${mark.description}</td>
						<td data-label="Datetime">${mark.localDateTime}</td>
					</tr>
                </#list>
				</tbody>
			</table>
        <#else>
			<h2 class="ui header">Select subject and student</h2>
        </#if>

	</div>

</div>
<div class="ui modal">
	<i class="close icon"></i>
	<div class="header">
		Evaluation
	</div>
	<div class="image content">
		<div class="description">
			<form class="ui form" action="/success/evaluate" method="post">
				<div class="field">
					<div id="subject_selector_eval" class="ui selection disabled dropdown">
						<input type="hidden" name="subjectId">
						<i class="dropdown icon"></i>
						<div class="default text">Subject</div>
						<div class="menu">
	                        <#list subjects as subject>
								<div class="item" data-value="${subject.id}">${subject.name}</div>
	                        </#list>
						</div>
					</div>
				</div>
				<div class="field">
					<div id="student_selector_eval" class="ui selection disabled dropdown">
						<input type="hidden" name="studentId">
						<i class="dropdown icon"></i>
						<div class="default text">Student</div>
						<div class="menu">
	                        <#list students as student>
								<div class="item" data-value="${student.id}">${student.firstName} ${student.lastName} (${student.userId.username})</div>
	                        </#list>
						</div>
					</div>
				</div>
				<div class="field">
					<label>Mark</label>
					<input type="number" name="mark" placeholder="0">
				</div>
				<div class="field">
					<label>Description</label>
					<input type="text" name="description" placeholder="Example: Exam result, test result, class work reward">
				</div>
				<button class="ui button" type="submit">Submit</button>
			</form>
		</div>
	</div>
	<div class="actions">
		<div class="ui black deny button">
			Cancel
		</div>
	</div>
</div>
<script>
    $(function() {
        $(".ui.selection.dropdown").dropdown();
        $(".ui.selection.disabled.dropdown").dropdown();
        <#if selected_subject_id??>
            var selected_subject = "${selected_subject_id}";
            var selected_student = "${selected_student_id}";
            $("#student_selector").dropdown('set selected', selected_student);
            $("#subject_selector").dropdown('set selected', selected_subject);
        </#if>
	    $("#eval_button").click(function() {
            $('.ui.modal').modal('show');
            var subject = $("#subject_selector").dropdown('get value');
            var student = $("#student_selector").dropdown('get value');
            $("#student_selector_eval").dropdown('set selected', student);
            $("#subject_selector_eval").dropdown('set selected', subject);
	    });
    });
</script>
</body>

</html>