<html>
<head>
    <title>Evaluation Web - My Grades</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/navbar.ftl">
<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error_message}</p>
    </div>
</#if>
<div class="ui segment grid">
    <div style="margin-left : 10%; margin-top: 3%; margin-bottom: 5%;">

        <div class="ui special cards">

	        <#if !teachers?? || teachers?size == 0>
	            <h3 class="header">You have not any teachers yet</h3>
	        </#if>

	        <#if teachers??>
		        <#list teachers as teacher>
	            <div class="card">
	                <div class="blurring dimmable image">
	                    <div class="ui dimmer">
	                        <div class="content">
	                            <div class="center">
	                                <a href="/profile/view/${teacher.id}" class="ui inverted button">View profile</a>
	                            </div>
	                        </div>
	                    </div>
	                    <img src="${teacher.photoUrl}">
	                </div>
	                <div class="content">
	                    <a class="header">${teacher.firstName} ${teacher.lastName}</a>
	                    <div class="meta">
	                        <span class="date">${teacher.position}</span><br>
	                    </div>
	                </div>
	                <div class="extra content">
	                    <a>
	                        <i class="users icon"></i>
	                        ${teacher.group.name}
	                    </a>
	                </div>
	            </div>
	            </#list>
            </#if>

        </div>

    </div>
</div>
<script src="/resources/main.js"></script>
</body>
</html>
