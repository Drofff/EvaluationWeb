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
<#include "parts/navbar.ftl">
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
<div class="segment">

	<div class="ui grid">
		<div class="two wide column">
			<div class="ui vertical menu">
				<a class="teal item" href="/mail/my">
					Inbox
				</a>
				<a class="active item">
					Send mail
				</a>
			</div>
		</div>
		<div class="eleven wide column" style="margin-left: 3%;">
			<form class="ui form" id="message_form" method="post" action="/mail/send" style="width: 60%; margin-top: 5%; margin-left: 20%;">

				<div class="field">
					<label>Title</label>
					<input type="text" required name="title" <#if oldTitle??>value="${oldTitle}"</#if> placeholder="Title">
				</div>
				<div class="field" style="margin-top: 3%;">
					<label>Text</label>
					<textarea maxlength="255" id="message_text" name="text" form="message_form" minlength="1" placeholder="Please, provide your message here"></textarea>
				</div>

				<div class="field" style="width: 510px; margin-top: 3%;">
					<label>Receivers</label>
					<select name="receivers" multiple="" id="receivers_selector" class="ui fluid search dropdown">
						<option value="">Select users</option>
                        <#list my_students as student>
							<option <#if selectedReceiversIds?? && selectedReceiversIds?seq_contains(student.userId.id)>selected</#if> value="${student.userId.id}">${student.firstName} ${student.lastName} (${student.userId.username})</option>
                        </#list>
					</select>
				</div>

				<button class="ui primary button" type="submit">
					Send
				</button>

			</form>
		</div>
	</div>

</div>
<script>
	$("#receivers_selector").dropdown();
    <#if oldText??>
		var old_text = '${oldText}';
		$("#message_text").val(old_text);
	</#if>
</script>
<script src="/resources/main.js"></script>
</body>

</html>
