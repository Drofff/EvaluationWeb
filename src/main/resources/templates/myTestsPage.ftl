<html>
<head>
    <title>Evaluation Web - My Tests</title>
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
    <div class="ui left fixed vertical menu" style="margin-top:5%;">
        <div class="item">
            <img class="ui tiny circular image" src="${photoUrl}" style="margin-left: 22%;">
            <h4 class="ui header">${name}</h4>
        </div>
        <a class="item">My Tests</a>
        <a class="item" href="/test/grades">Grades</a>
        <a class="item" href="#">Teachers</a>
    </div>
    <div style="margin-left : 20%;">
        <#if tests?? && tests?size gt 0>
        <div class="ui cards" style="margin-bottom : 40%;">
            <#if currentTest??>
            <div class="card" style="margin-top: 10%; margin-bottom : 10%;">
                <div class="content">
                    <div class="header">${currentTest.name} (Active)</div>
                    ${timeLeft} minutes left
                </div>
                <a href="/test/currentTest">
                    <div class="ui bottom attached button">
                        <i class="arrow right icon"></i>
                        Continue
                    </div>
                </a>
            </div>
            </#if>
            <#list tests as test, deadline>
                <div class="card" style="margin-top: 10%; margin-bottom : 10%;">
                    <div class="content">
                        <div class="header">${test.name}</div>
                        <div class="description">
                            Deadline: ${deadline}
                        </div>
                    </div>
                    <a href="/test/${test.id}">
                    <div class="ui bottom attached button">
                        <i class="arrow right icon"></i>
                        Start test
                    </div>
                    </a>
                </div>
            </#list>
        </div>
        <#else>
            <h2 class="ui header" style="margin-top:10%; margin-bottom: 20%;">
                <div class="content">
                    You haven't any tests yet
                </div>
            </h2>
        </#if>
    </div>
</div>
</html>