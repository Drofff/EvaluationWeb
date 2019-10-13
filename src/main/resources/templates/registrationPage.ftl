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
    <a class="item disabled">
        Schedule
    </a>
    <a class="item disabled">
        Tests
    </a>
    <a class="item disabled">
        Profile
    </a>
<div class="right menu">
    <div class="item">
        <a class="ui primary button" href="/logout">Log out</a>
    </div>
</div>
</div>

<#if error_message??>
<div class="ui negative message" style="width: 30%; margin-left: 33%;">
    <div class="header">
        ${error_message}
    </div>
</div>
</#if>

<form class="ui form" action="/registration" method="post" enctype="multipart/form-data" style="margin-left: 33%; width: 30%; margin-top: 10%;">
    <div class="field">
        <label>First Name</label>
        <input type="text" name="firstName" placeholder="First Name">
    </div>
    <div class="field">
        <label>Last Name</label>
        <input type="text" name="lastName" placeholder="Last Name">
    </div>
    <div class="field">
        <input type="file" accept="image/*" name="photo">
    </div>
    <button class="ui button" type="submit">Submit</button>
</form>

</body>
</html>