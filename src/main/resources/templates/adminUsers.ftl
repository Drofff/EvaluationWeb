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
    <a class="item" id="create_user">Create user</a>
    <a class="item" href="/">Continue as user</a>
    <a class="item" href="/logout">Logout</a>
</div>
<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;margin-top: 5%">
        <p>${error_message}</p>
    </div>
</#if>

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
                <input type="email" name="email" placeholder="user@email.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" required>
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
                <input type="text" name="position" placeholder="Example: Student, Programming teacher" pattern="[A-Za-zА-Яа-яЁё-іІїЇєЄ,\D, [0-9]{5,}" required>
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

<div style="margin-left: 20%; margin-top: 5%;">
    <form action="/admin/users" class="ui form" method="get">

        <div class="two fields">
            <div class="field" style="display: inline-block; width: 30%">
                <select name="groups" multiple="" class="ui fluid dropdown">
                    <option value="">Groups</option>
                    <#list groups as group>
                        <option <#if group.selected?? && group.selected> selected </#if> value="${group.name}">${group.name}</option>
                    </#list>
                </select>
            </div>

            <div class="field" style="display: inline-block; width: 46.8%">
                <input type="text" name="name" <#if oldName??> value="${oldName}" </#if> placeholder="User's full name">
            </div>
        </div>

        <button class="ui button"  type="submit">Search</button>

    </form>
    <div class="ui segment" style="width: 76.5%">
        <div class="ui special cards" >
            <#list users as user>
                <#if user.photoUrl??>
                    <div class="ui card" style="width: 31.75%">
                        <div class="blurring dimmable image">
                            <div class="ui dimmer">
                                <div class="content">
                                    <div class="center">
                                        <#--<a href="/profile/view/${teacherId}" class="ui inverted button">View profile</a>-->
                                        <a href="#" class="ui inverted button">View profile</a>
                                    </div>
                                </div>
                            </div>
                            <img style="width: 100%; height: 350px" src="${user.photoUrl}">
                        </div>
                        <div class="content">
                            <a class="header">${user.firstName} ${user.lastName}</a>
                            <div class="meta">
                                <span class="date">${user.position}</span>
                            </div>
                            <div class="description">Member of group: <b>${user.groupName}</b></div>
                        </div>
                        <div class="extra content">
                            <a>
                                <i class="envelope icon"></i>
                                ${user.email}
                            </a>
                        </div>
                    </div>
                <#else>
                    <div class="ui card" style="width: 31.75%">
                        <div class="blurring dimmable image">
                            <div class="ui dimmer">
                                <div class="content">
                                    <div class="center">
                                        <#--<a href="/profile/view/${teacherId}" class="ui inverted button">View profile</a>-->
                                        <a href="#" class="ui inverted button">View profile</a>
                                    </div>
                                </div>
                            </div>
                            <img style="width: 100%; height: 350px" src="http://scma.com.ua/wp-content/uploads/2015/02/user.png">
                        </div>
                        <div class="content">
                            <a class="header">${user.email}</a>
                            <div class="meta">
                                <span class="date">${user.position}</span>
                            </div>
                            <div class="description">Member of group: <b>${user.groupName}</b></div>
                        </div>
                        <div class="extra content">
                            <a>
                                <i class="user icon"></i>
                                User is not active yet
                            </a>
                        </div>
                    </div>
                </#if>
            </#list>
        </div>
        <div class="ui buttons" style="margin: 1.5% 0 0 36.5%;">
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

</div>
<script src="/resources/main.js"></script>
</body>
</html>