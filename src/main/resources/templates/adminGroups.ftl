<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>


<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error_message}</p>
    </div>
</#if>
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
                <input type="text" name="name" placeholder="Group-IFTKN.123" pattern="[A-Za-zА-Яа-яЁё-іІїЇєЄ._%+-]+\.[0-9]{3}$" required>
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
            <input type="text" style="width: 40%;" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="Group's name">
            <button class="ui button" style="margin-left: 1%;width: 8.8%" type="submit">Search</button>
        </div>
    </form>



    <table class="ui compact table" style="width: 50%">
        <thead>
        <tr>
            <th>Group</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list groups as group>
            <tr>
                <td><a>${group.name}</a></td>
                <td><a href="/admin/manage/groups/${group.id}">Manage group</a></td>
            </tr>

        </#list>
        </tbody>
    </table>
    <div class="ui buttons" style="margin-left: 14  %; ">
        <#if prevURL??>
            <a class="ui labeled icon button" href="${prevURL}">
                <i class="left chevron icon"></i>
                Previous
            </a>
        <#else>
            <a class="ui labeled icon button disabled">
                <i class="left chevron icon"></i>
                Previous
            </a>
        </#if>
        <#if nextURL??>
            <a class="ui right labeled icon button" href="${nextURL}">
                Next
                <i class="right chevron icon"></i>
            </a>
        <#else>
            <a class="ui right labeled icon button disabled">
                Next
                <i class="right chevron icon"></i>
            </a>
        </#if>
    </div>

</div>
<script src="/resources/main.js"></script>
</body>
</html>