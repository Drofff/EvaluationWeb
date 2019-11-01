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
<#if messageError??>
	<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
		<div class="header">
            ${messageError}
		</div>
	</div>
</#if>
<#if messageSuccess??>
	<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
		<div class="header">
            ${messageSuccess}
		</div>
	</div>
</#if>
<div class="ui tabular menu">
	<a class="item" href="/subjects/create">
		Create Subject
	</a>
	<a class="active item">
		Delete Subjects
	</a>
</div>
<div style="margin-top: 5%; margin-left: 5%;">

	<#if !subjects?? || subjects?size == 0>
		<h3 class="header">You have not any subjects yet</h3>
	</#if>

	<div class="ui cards">
		<#list subjects as subject>
			<div class="card">
				<div class="content" style="text-align: center">
					<div class="header">${subject.name}</div>
				</div>
				<a class="ui red bottom attached button" href="/subjects/delete/${subject.id}">
					<i class="trash alternate icon"></i>
					Delete
				</a>
			</div>
        </#list>
	</div>

</div>
<script src="/resources/main.js"></script>
</body>

</html>
