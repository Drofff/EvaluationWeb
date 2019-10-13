<html>
<head>
    <title>Evaluation Web - My Tests</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script><script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
<script>

    var total_test_time = ${totalTestTime};
    var time = ${timeToDeadline};

    $(function() {

        $('#prog').progress();

        var fun_do = setInterval(function() {

            $.post("/test/time", { duration : total_test_time }, function(data) {

                $("#timer").text(data['remaind_time'] + ' minutes');

                time = data['remaind_time'];

                if (data <= 0) {

                    alert('Sorry, time is out');

                    clearInterval(fun_do);

                }

            });
        }, 1000 * 10);

    });

</script>
<body>
<div class="ui menu">
    <div class="header item">
        Evaluation Web
    </div>
    <a class="item" href="/">
        Schedule
    </a>
    <a class="item active">
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
<#if isAdmin?? && isAdmin>
<a class="item" href="/admin/users">
    Admin Panel
</a>
</#if>
<div class="right menu">
    <div class="item">
        <a class="ui primary button" href="/logout">Log out</a>
    </div>
</div>
</div>
<div class="ui segment grid">
    <div class="ui left fixed vertical menu" style="margin-top:5%;">
        <div class="item">
            <img class="ui tiny circular image" src="${photoUrl}" style="margin-left: 22%;">
            <h4 class="ui header">${name}</h4>
        </div>
        Time left:
        <p id="timer">${timeToDeadline} minutes</p>
            <div class="ui progress">
                <div class="bar">
                    <div class="progress" id="prog" data-percent="${fullPercent}"></div>
                </div>
            </div>
        <a class="ui primary button" href="/test/finish" style="margin-left : 22%;">Finish</a>
    </div>
    <div style="margin-left: 20%; margin-top: 5%;">
        <form action="/test/answer" method="post">
            <input type="hidden" value="${questionNumber}" name="question"/>
        <h1 class="ui header">${questionNumber}. ${question.question}</h1>
            <div class="ui form" style="margin-top:10%; margin-bottom: 10%;">
        <#list answers as answer>
                <div class="field" style="margin-bottom: 5%;">
                    <div class="ui checkbox">
                        <input type="checkbox" <#if selections?? && selections?seq_contains(answer.id)>checked</#if> value="${answer.id}" name="answer">
                        <label>${answer.text}</label>
                    </div>
                </div>
        </#list>
            <button class="ui primary button" style="margin-left: 10%; margin-top: 10%;" type="submit">Answer</button>
        </form>
    </div>
<div style="margin-left: 30%; margin-top:20%; margin-bottom : 10%;">
    <div class="ui buttons" >
        <#list questions as q, r>
            <a href="/test/currentTest?question=${q}" class="ui button <#if r>green</#if> <#if q == questionNumber>active</#if>">${q}</a>
    </#list>
    </div>
</div>

</div>
</html>