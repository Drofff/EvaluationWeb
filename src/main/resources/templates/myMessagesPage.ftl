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
				<a class="active item">
					Inbox
				</a>
				<a class="teal item"  href="/mail/send">
					Send mail
				</a>
			</div>
		</div>
		<div class="eleven wide column" style="margin-left: 7%;">

			<div class="ui grid">
				<div class="eight wide column">
					<h2 class="header" style="margin-bottom: 5%;">Income</h2>
					<div class="ui styled accordion">
						<#list to_me_messages as tmm>
						    <div class="title">
							<i class="dropdown icon"></i>
							    ${tmm.topic}
							</div>
							<div class="content">
								<p class="transition hidden">${tmm.text}</p>
								<small class="secondary">From ${tmm.sender.profile.firstName} ${tmm.sender.profile.lastName} at ${tmm.dateTimeFormatted}</small>
							</div>
						</#list>
					</div>
		            <#if to_me_messages?size == 0>
						<h4 class="header" style="margin-top: 10%;">Income mail box is empty</h4>
		            </#if>
				</div>
				<div class="eight wide column">
					<h2 class="header" style="margin-bottom: 5%;">Outcome</h2>
					<div class="ui styled accordion">
		                <#list from_me_messages as fmm>
							<div class="title">
								<i class="dropdown icon"></i>
		                        ${fmm.topic}
							</div>
							<div class="content">
								<p class="transition hidden" style="width">${fmm.text}</p>
								<small class="secondary">To <#list fmm.receivers as receiver>
										${receiver.profile.firstName} ${receiver.profile.lastName},
								</#list> at ${fmm.dateTimeFormatted}</small>
							</div>
		                </#list>
					</div>
		            <#if from_me_messages?size == 0>
						<h4 class="header" style="margin-top: 10%;">Outcome mail box is empty</h4>
		            </#if>
				</div>
			</div>
		</div>

</div>
<script>
    $('.ui.accordion')
        .accordion()
    ;
</script>
<script src="/resources/main.js"></script>
</body>

</html>
