<html>
<head>
	<title>Evaluation Web | My Marks</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
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
		<form action="/success/my" method="get">
			<div class="ui selection dropdown">
				<input type="hidden" name="subjectId">
				<i class="dropdown icon"></i>
				<div class="default text">Subject</div>
				<div class="menu">
					<#list subjects as subject>
						<div class="item" data-value="${subject.id}">${subject.name}</div>
					</#list>
				</div>
			</div>
			<button type="submit" class="ui primary button">
				Show
			</button>
		</form>

		<#if marks?? && marks?size gt 0>
			<table class="ui celled table">
				<thead>
				<tr><th>Teacher</th>
					<th>Mark</th>
					<th>Description</th>
					<th>Datetime</th>
				</tr></thead>
				<tbody>
	                <#list marks as mark>
		                <tr>
			                <td data-label="Teacher">
				                <a href="/profile/view/${mark.subject.teacher.profile.id}">
					                ${mark.subject.teacher.profile.firstName} ${mark.subject.teacher.profile.lastName}
				                </a>
			                </td>
			                <td data-label="Mark">${mark.mark}</td>
			                <td data-label="Description">${mark.description}</td>
			                <td data-label="Datetime">${mark.localDateTime}</td>
		                </tr>
	                </#list>
				</tbody>
			</table>
		<#else>
			<h2 class="ui header">Select subject</h2>
		</#if>

	</div>

</div>
<script>
	$(function() {
        <#if selected_subject_id??>
			var selected = "${selected_subject_id}";
            $(".ui.selection.dropdown").dropdown('set selected', selected)
        <#else>
            $(".ui.selection.dropdown").dropdown();
		</#if>
	});
</script>
</body>

</html>