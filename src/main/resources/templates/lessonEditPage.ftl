<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>
<div class="ui menu">
    <div class="header item">
        Evaluation Web
    </div>
    <a class="item" href="/">
        Schedule
    </a>
    <a class="item" href="/test">
        Tests
    </a>
    <a class="item" href="/profile">
        Profile
    </a>
    <a class="item" href="/teacher/manager">
        Lessons Manager
    </a>
    <a class="item" href="/teacher/storage">
        My Storage
    </a>
    <div class="right menu">
        <div class="item">
            <a class="ui primary button" href="/logout">Log out</a>
        </div>
    </div>

</div>
<script>
    $(function() {

        $('.ui.checkbox').checkbox();

        <#if lesson.homeTask??>
        $("#home").text('${lesson.homeTask}');
        </#if>

        <#if lesson.description??>
        $("#desc").text('${lesson.description}');
        </#if>

        $("#true_home").val( $("#home").text() );
        $("#true_desc").val( $("#desc").text() );

        console.log('Initial text : ' + $("#home").text() );

        $("#home").keyup(function () {
            $("#true_home").val( $("#home").val() );
        });

        $("#desc").keyup(function() {
            $("#true_desc").val( $("#desc").val() );
        });

    });

</script>
<#if messageError??>
<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        Attention
    </div>
   ${messageError}
</div>
</#if>
<#if messageSuccess??>
<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        Saved
    </div>
    ${messageSuccess}
</div>
</#if>
<div class="ui segment">
    <div class="ui form" style="width: 70%; margin-top:6%;">
        <form action="/teacher/edit" method="post">
            <input type="hidden" id="true_home" name="homeTask" />
            <input type="hidden" id="true_desc" name="description" />
            <input type="hidden" name="id" value="${lesson.id}" />
            <div class="field <#if homeTaskError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
                <label>Home Task</label>
                <textarea rows="2" id="home"></textarea>
                <#if homeTaskError??>
                <h5 class="ui red header">${homeTaskError}</h5>
            </#if>
            </div>
            <div class="field <#if descriptionError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
                <label>Description</label>
                <textarea rows="2" id="desc"></textarea>
                <#if descriptionError??>
                    <h5 class="ui red header">${descriptionError}</h5>
            </#if>
            </div>
            <div class="field <#if roomError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
                <label>Room</label>
                <input type="text" name="room" value="${lesson.room}" placeholder="Audience">
                <#if roomError??>
                    <h5 class="ui red header">${roomError}</h5>
                </#if>
            </div>
            <div class="inline field <#if testError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
                <div class="ui toggle checkbox">
                    <input type="checkbox" name="test" <#if lesson.test?? && lesson.test>checked</#if> tabindex="0" class="hidden">
                    <label>Test</label>
                </div>
            <#if testError??>
                <h5 class="ui red header">${testError}</h5>
            </#if>
            </div>
            <div class="field <#if dateTimeError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
                <label>Date and time</label>
                <input type="datetime-local" value="${lesson.dateTime}" name="dateTime">
            <#if dateTimeError??>
                <h5 class="ui red header">${dateTimeError}</h5>
            </#if>
            </div>

            <div class="field <#if durationError??>error</#if>" style="margin-left : 10%; margin-bottom: 4%;">
                <label>Duration</label>
                <div class="ui right labeled input">
                    <input type="number" name="duration" value="${lesson.duration}" placeholder="Maximum : 160" required>
                    <div class="ui basic label">Minutes</div>
                </div>
            <#if durationError??>
                <h5 class="ui red header">${durationError}</h5>
            </#if>
            </div>

        <#if groups??>
        <div class="field <#if groupIdError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
            <label>Group</label>
            <select class="ui fluid dropdown" name="groupId">
                <#list groups as group>
                    <option value="${group.id}" <#if group.id == lesson.groupId.id>selected</#if> >${group.name}</option>
                </#list>
            </select>
            <#if groupIdError??>
                <h5 class="ui red header">${groupIdError}</h5>
            </#if>
            </div>
        </#if>
        <#if subjects??>
        <div class="field <#if titleError??>error</#if>" style="margin-left:10%; margin-bottom: 4%;">
            <label>Subject</label>
            <select class="ui fluid dropdown" name="title">
                <#list subjects as subject>
                <option value="${subject.name}" <#if subject.name == lesson.title>selected</#if> >${subject.name}</option>
                </#list>
            </select>
            <#if titleError??>
                <h5 class="ui red header">${titleError}</h5>
            </#if>
        </div>
        </#if>
        <div class="field" style="margin-left:10%; margin-bottom: 4%;">
            <button id="save_button" type="submit" class="ui button">Save</button>
        </div>
    </form>
    </div>
</div>
</body>

</html>