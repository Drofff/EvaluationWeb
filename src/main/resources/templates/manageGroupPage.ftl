<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>

<div class="ui top fixed menu">
    <div class="item">
        <b>EvaWeb <b style="color : #FFD700">Admin</b></b>
    </div>
    <a class="item" id="add_member">Add member</a>
    <a class="item" id="add_teacher">Add teacher</a>
    <a class="item" href="/">Continue as user</a>
    <a class="item" href="/logout">Logout</a>
</div>

<div class="ui secondary vertical pointing menu" style="position: absolute; top: 30">
    <a class="item" href="/admin/users">
        Manage Users
    </a>
    <a class="item" href="/admin/groups">
        Manage Groups
    </a>
</div>

<#if error_message??>
<div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
    <p>${error_message}</p>
</div>
</#if>

<#if group??>
<div class="ui segment" style="margin-left: 20%; top: 5%; width: 73%; height: 90%; ">
    <h1 class="ui header">${group.name}</h1>
    <div class="ui divider"></div>

<div class="ui segment" style="float: left;width: 48.8%; height: 87%; overflow: auto">
    <h1 class="ui header">Teachers</h1>
    <div class="ui divider"></div>
    <#list group.teachers as user>
        <#if user.photoUrl??>
            <div class="item">
                <img class="ui avatar image" src="${user.photoUrl}" style="float: left; width: 20%;height: 15%">
                <div class="content">
                    <a class="header">${user.firstName} ${user.lastName}</a>
                    <div class="description">Email: <b>${user.userId.username}</b></div>
                    <div class="description">Position: <b>${user.position}</b></div>
                    <a href="/admin/manage/${group.id}/remove-teachers/${user.id}">Remove</a>
                </div>
            </div>
            <div class="ui divider"></div>
        <#else>
            <div class="item">
                <div class="content">
                    <a class="header">${user.userId.username}</a>
                    <div class="description">Position: <b>${user.position}</b></div>
                </div>
            </div>
            <div class="ui divider"></div>
        </#if>
    </#list>
</div>
<div class="ui segment" style="float: left;width: 48.8%; margin-left: 2%; height: 87%;overflow: auto">
    <h1 class="ui header">Members</h1>
    <div class="ui divider"></div>
    <#list group.members as user>
        <#if user.photoUrl??>
            <div class="item">
                <img class="ui avatar image" src="${user.photoUrl}" style="float: left; width: 20%;height: 10%">
                <div class="content">
                    <a class="header">${user.firstName} ${user.lastName}</a>
                    <div class="description">Email: <b>${user.userId.username}</b></div>
                    <div class="description">Position: <b>${user.position}</b></div>
                </div>
            </div>
            <div class="ui divider"></div>
        <#else>
            <div class="item">
                <div class="content">
                    <a class="header">${user.userId.username}</a>
                    <div class="description">Position: <b>${user.position}</b></div>
                </div>
            </div>
            <div class="ui divider"></div>
        </#if>
    </#list>
</div>


</div>

<div class="ui modal" id="add_teacher_modal">
    <i class="close icon"></i>
    <div class="header">
        Add teacher
    </div>
    <div class="image content">
        <form class="ui form" method="post" action="/admin/manage/${group.id}/add-teachers" style="margin-left:15%; width:60%">
            <div class="field">
                <div class="ui fluid search selection dropdown">
                    <input type="hidden" name="id">
                    <i class="dropdown icon"></i>
                    <div class="default text">Select teacher</div>
                    <div class="menu">
                        <#list teachers as teacher>
                            <div class="item" data-value="${teacher.id}">${teacher.firstName} ${teacher.lastName} (${teacher.userId.username})</div>
                        </#list>
                    </div>
                </div>
            </div>
            <button class="ui button" type="submit">Add</button>
        </form>
    </div>
    <div class="actions">
        <div class="ui black deny button">
            Nope
        </div>
    </div>
</div>

<div class="ui modal" id="add_member_modal">
    <i class="close icon"></i>
    <div class="header">
        Add member
    </div>
    <div class="image content">
        <form class="ui form" method="post" action="/admin/manage/${group.id}/add-members" style="margin-left:15%; width:60%">
            <div class="field">
                <div class="ui fluid search selection dropdown">
                    <input type="hidden" name="id">
                    <i class="dropdown icon"></i>
                    <div class="default text">Select member</div>
                    <div class="menu">
                        <#list members as user>
                            <#if user.firstName??>
                                <div class="item" data-value="${user.id}">${user.firstName} ${user.lastName} (${user.userId.username})</div>
                            </#if>
                        </#list>
                </div>
            </div>
    </div>
    <button class="ui button" type="submit">Add</button>
    </form>
</div>
<div class="actions">
    <div class="ui black deny button">
        Nope
    </div>
</div>
</div>

</#if>

<script src="/resources/main.js"></script>
</body>
</html>