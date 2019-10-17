<html>
<head>
    <title>Evaluation Web</title>
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

    <h4 class="header" style="margin-top: 10px; margin-left: 10px;">My Students</h4>

    <br/>

    <form action="/students/search" class="ui form" method="get">

        <div style="width: 25%; ">
            <select name="group_id" multiple="" class="ui fluid dropdown">
                <option value="">Groups</option>
                <#list groups as group>
                    <option <#if group.selected?? && group.selected> selected </#if> value="${group.id}">${group.name}</option>
                </#list>
            </select>
        </div>

        <div class="ui input" style="margin-top: 5px;">
            <input type="text" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="Student's name">
        </div>

        <button class="ui button" type="submit">Search</button>

    </form>
    <div class="ui divider"></div>
    <div class="ui cards" style="margin-top: 1%; height: 55%">
        <#list students as student>
            <#if student.photoUrl??>
                <div class="ui card" style="width: 15%">
                    <a class="image" href="#">
                        <img src="${student.photoUrl}" style="height: 300px">
                    </a>
                    <div class="content">
                        <a class="header">${student.firstName} ${student.lastName}</a>
                        <div class="meta">
                            <span>${student.group.name}</span>
                        </div>
                        <div class="extra content">
                            <button class="ui primary button">
                                Send Email
                            </button>
                        </div>
                    </div>
                </div>
            <#else>
                <div class="ui card" style="width: 15%">
                    <a class="image" href="#">
                        <img src="http://scma.com.ua/wp-content/uploads/2015/02/user.png" style="height: 300px">
                    </a>
                    <div class="content">
                        <a class="header">${student.userId.username}</a>
                        <div class="meta">
                            <span>${student.group.name}</span>
                        </div>
                        <div class="extra content">
                            <button class="ui primary button">
                                Send Email
                            </button>
                        </div>
                    </div>
                </div>
            </#if>
        </#list>
    </div>

</div>
<div class="ui buttons" style="margin-left: 45%; margin-bottom: 3%;">
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
<script src="/resources/main.js"></script>
</body>

</html>