<html>
<head>
    <title>Evaluation Web</title>
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

<#if errorMessage??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${errorMessage}</p>
    </div>
</#if>

<#if message??>
<div class="ui positive message" style="width: 30%; margin-left: 35%; text-align: center;">
    <p>${message}</p>
</div>
</#if>

<form class="ui form segment" action="/password/change" method="post" style="width: 40%; margin-left: 30%; margin-top: 10%; margin-bottom: 5%;">
    <div class="field <#if errorMessage??>invalid</#if>">
        <label>New Password</label>
        <input type="password" name="password" placeholder="A-Za-z1-9!@#$ (minimum 6 symbols)">
    </div>
    <div class="field <#if errorMessage??>invalid</#if>">
        <label>Repeat New Password</label>
        <input type="password" name="repeatedPassword" placeholder="A-Za-z1-9!@#$ (minimum 6 symbols)">
    </div>
    <button class="ui primary button" type="submit">
        Request Change
    </button>
</form>

</body>

</html>