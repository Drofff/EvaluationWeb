<html>
<head>
    <title>Evaluation Web - My Tests</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
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
        <a class="item" href="/test">My Tests</a>
        <a class="item" href="/test/grades">Grades</a>
    </div>
    <div style="margin-left : 20%; margin-top: 7%; margin-bottom : 5%;">
            <div class="ui huge header">${test.name}</div>
        <div class="ui disabled header">
            Till: ${deadline}
        </div>
        <div class="ui disabled header">
            Duration: ${test.duration} minutes
        </div>
        <div class="ui horizontal list" style="margin-top : 10%; margin-bottom : 15%;">
            <div class="item">
                <img class="ui mini circular image" src="${teacher.photoUrl}">
                <div class="content">
                    <div class="ui sub header">${teacher.firstName} ${teacher.lastName}</div>
                    Teacher
                </div>
            </div>
        </div>
        <#if otherTest?? && otherTest>
            <h3 class="ui header red">To start this test you have to finish previous one</h3>
        <#else>
        <form action="/test" method="post" style="margin-top: 5%;">
            <input type="hidden" name="testId" value="${test.id}">
            <button type="submit" class="ui primary button">
                Start test
            </button>
        </form>
        </#if>
    </div>
</div>
</div>
</html>