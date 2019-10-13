<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script>
        $(function() {
            $("#create_group").click(function() {
                $('.ui.modal')
                      .modal('show')
                    ;
            });
            $(".ui.selection.dropdown").dropdown();
        });
    </script>
</head>
<body>

<div class="ui top fixed menu">
    <div class="item">
        <b>EvaWeb <b style="color : #FFD700">Admin</b></b>
    </div>
    <a class="item" id="create_group">Create group</a>
    <a class="item" href="/">Continue as user</a>
    <a class="item" href="/logout">Logout</a>
</div>

<div class="ui secondary vertical pointing menu" style="position: absolute; top: 30">
    <a class="item" href="/admin/users">
        Manage Users
    </a>
    <a class="active item" href="/admin/groups">
        Manage Groups
    </a>
</div>


<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">
        Create group
    </div>
    <div class="image content">
        <form class="ui form" method="post" action="/admin/groups" style="margin-left:15%; width:60%">
            <div class="field">
                <label>Name</label>
                <input type="text" name="name" placeholder="Group-IFTKN.123">
            </div>
            <button class="ui button" type="submit">Create</button>
    </form>
</div>
<div class="actions">
    <div class="ui black deny button">
        Nope
    </div>
</div>
</div>

<div style="margin-left: 25%; margin-top: 5%;">
    <form action="/admin/groups" class="ui form" method="get">
        <div class="field">
            <input type="text" style="width: 30%;" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="Group's name">
            <button class="ui button" style="margin-left: 10%;" type="submit">Search</button>
        </div>
</form>

<div class="ui very relaxed list" style="margin-top: 5%;">
    <#list groups as group>

    <div class="item">
        <div class="content">
            <a class="header">${group.name}</a>
            <a href="/admin/manage/groups/${group.id}">Manage group</a>
        </div>
    </div>

</#list>
</div>

</div>

</body>