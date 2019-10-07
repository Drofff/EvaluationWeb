<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script>
        $(function() {
            $(".ui.fluid.dropdown").dropdown();
        });
    </script>
</head>
<body>
<div class="ui menu">
    <div class="header item">
        Evaluation Web
    </div>
    <a class="item active">
        Schedule
    </a>
    <a class="item" href="/test">
        Tests
    </a>
    <a class="item" href="/profile">
        Profile
    </a>
    <#if isTeacher?? && isTeacher>
    <a class="item" href="/teacher/manager">
        Lessons Manager
    </a>
    <a class="item" href="/teacher/storage">
        My Storage
    </a>
</#if>
<div class="right menu">
    <div class="item">
        <a class="ui primary button" href="/logout">Log out</a>
    </div>
</div>

</div>
<div class="ui segment">

    <h4 class="header" style="margin-top: 10px; margin-left: 10px;">My Students</h4>

    <br/>

    <form action="/students/search" class="ui form" method="get">

        <div style="width: 25%; margin-left: 5%;">
            <select name="group_id" multiple="" class="ui fluid dropdown">
                <option value="">Groups</option>
                <#list groups as group>
                    <option <#if group.selected?? && group.selected> selected </#if> value="${group.id}">${group.name}</option>
                </#list>
            </select>
        </div>

        <div class="ui input">
            <input type="text" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="Student's name">
        </div>

        <button class="ui button" type="submit">Search</button>

    </form>

    <div class="ui items" style="margin-left: 10%; margin-right: 10%; margin-top: 5%; margin-bottom: 5%;">
        <#list students as student>
            <div class="item">
                <img class="ui medium circular image" style="width: 100px; height: 100px; margin-right: 3%;" src="${student.photoUrl}">
                <div class="content">
                    <a class="header">${student.firstName} ${student.lastName}</a>
                    <div class="meta">
                        <span>${student.group.name}</span>
                    </div>
                    <div class="extra">
                        <button class="ui primary button">
                            Send Email
                        </button>
                    </div>
                </div>
            </div>
        </#list>
    </div>

</div>
</body>

</html>