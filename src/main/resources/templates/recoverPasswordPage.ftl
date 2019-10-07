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
<div class="right menu">
    <div class="item">
        <a class="ui primary button" href="/login">Login</a>
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

<div class="ui message" style="width: 60%; margin-left: 25%; margin-top: 5%; margin-bottom: 3%;">
    <div class="header">
        Password recovery
    </div>
    <p>Please, enter your email address, so we could verify your identity</p>
</div>

<form class="ui form segment" action="/password/recover" method="post" style="width: 40%; margin-left: 30%; margin-top: 5%; margin-bottom: 5%;">
    <div class="field <#if invalidEmail??>invalid</#if>">
        <label>Email</label>
        <input type="text" name="email" placeholder="example@gmail.com">
    </div>
    <button class="ui primary button" type="submit">
        Recover password
    </button>
</form>

</body>

</html>