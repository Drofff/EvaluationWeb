<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <script>
        $(function() {
            $("#photo").click(function() {
                $('.ui.modal').modal('show');
            });
            $("#cancel").click(function() {
                $('.ui.modal').modal('hide');
            });
        });
    </script>
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
    <a class="item active">
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

<div class="ui modal">
    <form class="ui form" action="/profile/edit/photo" enctype="multipart/form-data" method="post">
        <div class="image content" style="margin-top: 5%; margin-left: 5%;">
            <div class="description">
                <div class="ui header">Select file</div>
                <input type="file" style="width: 50%;" id="photo_field" name="photo">
            </div>
        </div>
        <div class="actions" style="margin-top: 5%; margin-left: 5%;">
            <div class="ui black deny button" id="cancel">
                Cancel
            </div>
            <button type="submit" class="ui positive right labeled icon button">
                Save
                <i class="checkmark icon"></i>
            </button>
        </div>
    </form>
</div>

<form class="ui form segment" action="/profile/edit" method="post">

    <#if profile??>

    <div data-inverted="" data-tooltip="Click to load new photo" data-position="right center" style="margin-left: auto; margin-right: auto; width: 250px; height: 250px; margin-top: 10px;">
        <img id="photo" class="ui medium circular image" src="${profile.photoUrl}" style="width: 250px; height: 250px;">
    </div>

    <#if PhotoError??>
        <p style="color: red">${PhotoError}</p>
    </#if>

    <div style="text-align : center; margin-top: 2%;">
        <div class="two fields" style="margin-left: auto; margin-right: auto; width:30%">
            <div <#if firstNameError??>class="error field"<#else>class="field"</#if> >
                <input type="text" name="firstName" value="${profile.firstName}" placeholder="First Name">
                <#if firstNameError??>
                    <p style="color: red">${firstNameError}</p>
                </#if>
            </div>
            <div <#if lastNameError??>class="error field"<#else>class="field"</#if> >
                <input type="text" name="lastName" value="${profile.lastName}" placeholder="Last Name">
                <#if lastNameError??>
                    <p style="color: red">${lastNameError}</p>
                </#if>
            </div>
        </div>
    </div>
    <h4 style="margin-left: 35%;"><i class="address card icon"></i> Position: ${profile.position}</h4>
    <h4 style="margin-left: 35%;"><i class="users icon"></i> Group: ${profile.groupName}</h4>
    <h4 style="margin-left: 35%;"><i class="users icon"></i>Email: ${profile.email}</h4>
    <h4 style="margin-left: 35%;">
        <button type="submit" class="ui primary button">
            Save
        </button>
    </h4>
</#if>
</form>

</body>

</html>