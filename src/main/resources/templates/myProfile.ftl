<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
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
    <#if profile??>
        <img class="ui medium circular image" src="${profile.photoUrl}" style="margin-left: auto; margin-right: auto; width: 250px; height: 250px; margin-top: 10px;">
        <div style="text-align : center; margin-top: 2%;">
            <h1 style="margin-left: auto; margin-right: auto; width:30%">${profile.firstName} ${profile.lastName}</h1>
        </div>
        <h4 style="margin-left: 35%;"><i class="address card icon"></i> Position: ${profile.position}</h4>
        <h4 style="margin-left: 35%;"><i class="users icon"></i> Group: ${profile.groupName}</h4>
        <h4 style="margin-left: 35%;"><i class="users icon"></i>Email: ${profile.email}</h4>
        <h4 style="margin-left: 35%;">
            <a class="ui primary button" href="/password/change">
                Change Password
            </a>
            <a class="ui button" href="/profile/edit">
                Edit
            </a>
        </h4>
    </#if>
</div>

</body>

</html>