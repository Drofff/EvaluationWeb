<html>
<head>
    <title>Evaluation Web - Lessons Manager</title>
    <link rel="stylesheet" href="/datepicker.css"/>
    <link href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script
            src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"
            integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/teacherNavbar.ftl">
<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error_message}</p>
    </div>
</#if>
<div class="ui tabular menu">
    <a class="item" href="/teacher/manager">
        Add lessons
    </a>
    <a class="item active">
        Edit/Delete lessons
    </a>
</div>
<#if message??>
<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${message}
    </div>
</div>
</#if>
<#if msgErr??>
<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${msgErr}
    </div>
</div>
</#if>
<div class="ui grid" style="margin-top : 1%;">
    <table class="ui celled table">
        <thead>
        <tr><th>Subject</th>
            <th>Group</th>
            <th>Date</th>
            <th></th>
            <th></th>
        </tr></thead>
        <tbody>
        <#list lessons as date, lesson>
            <tr>
                <td data-label="Subject">${lesson.title}</td>
                <td data-label="Group">${lesson.groupId.name}</td>
                <td data-label="Date">${date}</td>
                <td>
                    <a href="/teacher/edit/${lesson.id}">
                        <div class="ui animated button" tabindex="0">
                            <div class="visible content">Edit</div>
                            <div class="hidden content">
                                <i class="pencil alternate icon"></i>
                            </div>
                        </div>
                    </a>
                </td>
                <td>
                    <form action="/teacher/delete" method="post">
                        <input type="hidden" name="lessonId" value="${lesson.id}">

                            <button class="ui vertical animated button" type="submit" tabindex="0">
                                <div class="hidden content">Delete</div>
                                <div class="visible content">
                                    <i class="trash alternate icon"></i>
                                </div>
                            </button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>