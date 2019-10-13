<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script>
        $(function() {
            $("#create_user").click(function() {
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
    <a class="item" id="create_user">Create user</a>
    <a class="item" href="/logout">Logout</a>
    </div>

    <div class="ui secondary vertical pointing menu" style="position: absolute; top: 30">
        <a class="active item">
            Manage Users
        </a>
        <a class="item" href="/admin/groups">
            Manage Groups
        </a>
    </div>


    <div class="ui modal">
        <i class="close icon"></i>
        <div class="header">
            Create user
        </div>
        <div class="image content">
            <form class="ui form" method="post" action="/admin/users" style="margin-left:15%; width:60%">
                <div class="field">
                    <label>Email</label>
                    <input type="text" name="email" placeholder="user@email.com">
                </div>
                <div class="field">
                    <label>Group</label>
                    <div class="ui selection dropdown">
                        <input type="hidden" name="groupId">
                        <i class="dropdown icon"></i>
                        <div class="default text">Group</div>
                        <div class="menu">
                        <#list groups as group>
                            <div class="item" data-value="${group.id}">${group.name}</div>
                        </#list>
                    </div>
                </div>
        </div>
        <div class="field">
            <label>Position</label>
            <input type="text" name="position" placeholder="Example: Student, Programming teacher">
        </div>
        <div class="field">
            <div class="ui checkbox">
                <input type="checkbox" name="teacher">
                <label>Teacher</label>
            </div>
        </div>
        <button class="ui button" type="submit">Submit</button>
        </form>
    </div>
    <div class="actions">
        <div class="ui black deny button">
            Nope
        </div>
    </div>
    </div>

    <div style="margin-left: 25%; margin-top: 5%;">
        <form action="/admin/users" class="ui form" method="get">

            <div class="two fields">
                <div class="field" style="width: 25%; margin-left: 5%;">
                    <select name="groups" multiple="" class="ui fluid dropdown">
                        <option value="">Groups</option>
                        <#list groups as group>
                        <option <#if group.selected?? && group.selected> selected </#if> value="${group.name}">${group.name}</option>
                        </#list>
                    </select>
                </div>

                <div class="field">
                    <input type="text" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="User's full name">
                </div>
            </div>

            <button class="ui button" style="margin-left: 55%;" type="submit">Search</button>

        </form>

        <div class="ui very relaxed list" style="margin-top: 5%;">
            <#list users as user>
                <#if user.photoUrl??>
                    <div class="item">
                        <img class="ui avatar image" src="${user.photoUrl}">
                        <div class="content">
                            <a class="header">${user.firstName} ${user.lastName}</a>
                            <div class="description">Email: <b>${user.email}</b></div>
                            <div class="description">Group: <b>${user.groupName}</b></div>
                            <div class="description">Position: <b>${user.position}</b></div>
                            <a>Change group</a>
                        </div>
                    </div>
                <#else>
                    <div class="item">
                        <div class="content">
                            <a class="header">${user.email}</a>
                        <div class="description">Group: <b>${user.groupName}</b></div>
                        <div class="description">Position: <b>${user.position}</b></div>
                    </div>
                    </div>
                </#if>
            </#list>
        </div>

    </div>

</body>