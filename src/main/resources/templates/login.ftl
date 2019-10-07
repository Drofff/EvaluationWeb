<html>
<head>
 <title>Evaluation Web - Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
<body>
<div class="ui menu">
    <div class="header item">
        <b>Evaluation Web</b>
    </div>
    <div class="right menu">
        <p class="item">
       Welcome, student!
        </p>
    </div>

</div>
<div class="ui segment">
    <#if messageError??>
    <div class="ui negative message" style="width:30%; margin-left: 35%; margin-right: 35%;">
        <div class="header">Invalid Credentials
        </div>
        <p>Error. You provided wrong email or password
        </p></div>
</#if>
    <div class="ui form" style="margin-left:35%; margin-right:10%; width:30%; margin-top:2%; margin-bottom:1%;">
        <form action="/login" method="post">
        <div class="field mt">
            <label>Email</label>
            <input type="text" name="username" required placeholder="example@mail.com">
        </div>
        <div class="field">
            <label>Password</label>
            <input type="password" name="password" required placeholder="*******">
        </div>
        <button class="ui submit button" type="submit">Log in</button>
            <div class="ui checkbox" style="margin-left:10%;">
                <input type="checkbox" name="remember-me">
                <label>Remember me</label>
            </div>
        </form>
    </div>
    <a href="/password/recover" style="margin-left: 35%; margin-top: 1%; margin-bottom: 1%;">Forgot password? Recover it!</a>
    <br/>
    <div class="ui message" style="width:40%; margin-left:30%; margin-bottom:3%;">
        <div class="header">
            Hello! To use our service you have to:
        </div>
        <ul class="list">
            <li>Make sure that your email works good</li>
            <li>Request your teacher for credentials (password)</li>
        </ul>
    </div>
</div>
</body>

</html>