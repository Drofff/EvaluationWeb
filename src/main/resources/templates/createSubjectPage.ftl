<html>
<head>
	<title>Evaluation Web</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
	<link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/teacherNavbar.ftl">
<#if error_message??>
	<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
		<div class="header">
            ${error_message}
		</div>
	</div>
</#if>
<#if message??>
	<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
		<div class="header">
            ${message}
		</div>
	</div>
</#if>
<div class="ui tabular menu">
	<a class="item active">
		Create Subject
	</a>
	<a class="item" href="/subjects">
		Delete Subjects
	</a>
</div>
<div style="margin-top: 5%; margin-left: 5%;">

	<form class="ui form" method="post" action="/subjects/create" style="width: 50%;">
		<div class="field">
			<label>Subject name</label>
			<input type="text" name="name" placeholder="Example: Mathematics for Programming, OOP languages">
		</div>
		<button class="ui button" type="submit">Create</button>
	</form>

</div>
<script src="/resources/main.js"></script>
</body>

</html>
