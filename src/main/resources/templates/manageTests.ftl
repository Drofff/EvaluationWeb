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
	<a class="item" href="/test/create">
		Create Test
	</a>
	<a class="active item">
		Edit/Delete Tests
	</a>
</div>
<div style="margin-top: 5%;">



</div>
<script src="/resources/main.js"></script>
</body>

</html>
