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
    <div class="ui left fixed vertical menu" style="margin-top:5%;">
        <div class="item">
            <img class="ui tiny circular image" src="${photoUrl}" style="margin-left: 22%;">
            <h4 class="ui header">${name}</h4>
        </div>
        <a class="item" href="/test">My Tests</a>
        <a class="item" href="/test/grades">Grades</a>
        <a class="item">Teachers</a>
    </div>
    <div style="margin-left : 20%; height: 1200px; overflow: auto">

        <div class="ui special cards">
            <div class="card">
                <div class="blurring dimmable image">
                    <div class="ui dimmer">
                        <div class="content">
                            <div class="center">
                                <a href="/profile/view/${teacherId}" class="ui inverted button">View profile</a>
                            </div>
                        </div>
                    </div>
                    <img src="https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940">
                </div>
                <div class="content">
                    <a class="header">${TeachersNmae}</a>
                    <div class="meta">
                        <span class="date">${WhenJoinedToUniversity}</span><br>
                    </div>
                </div>
                <div class="extra content">
                    <a>
                        <i class="users icon"></i>
                        ${numberOfClasses}
                    </a>
                </div>
            </div>
        </div>
        <div class="ui buttons" style="margin-left: 28%; margin-top: 3%;">
            <#if prevURL??>
                <a class="ui labeled icon button" href="${prevURL}">
                    <i class="left chevron icon"></i>
                    Previous
                </a>
            <#else>
                <a class="ui labeled icon button disabled">
                    <i class="left chevron icon"></i>
                    Previous
                </a>
            </#if>
            <#if nextURL??>
                <a class="ui right labeled icon button" href="${nextURL}">
                    Next
                    <i class="right chevron icon"></i>
                </a>
            <#else>
                <a class="ui right labeled icon button disabled">
                    Next
                    <i class="right chevron icon"></i>
                </a>
            </#if>
        </div>

    </div>
</div>
<script src="/resources/main.js"></script>
</body>
</html>
