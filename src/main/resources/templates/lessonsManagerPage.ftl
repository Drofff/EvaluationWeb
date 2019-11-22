<html>
<head>
    <title>Evaluation Web - Lessons Manager</title>
    <link rel="stylesheet" href="/datepicker.css"/>
    <link href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script
            src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"
            integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/style.css">
</head>
<script>

    var select = false;

    $(function() {

        var dates = "";

        $("#picker").datepicker({ showOn : 'none' });

        $("#picker").change(function() {

            var new_date = $("#picker").val();

            if (new_date.length == 10 && select) {


                select = false;

                if ( dates.length > 4 ) {
                    dates += ", ";
                 }

                dates += new_date;

            } else {

                dates = new_date;

            }

            $("#picker").val(dates);

        });
    });

    function show_picker() {

        $("#picker").datepicker('show');
        select = true;

    }

</script>
<body>
<#include "parts/teacherNavbar.ftl">
<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error_message}</p>
    </div>
</#if>
<div class="ui tabular menu">
    <a class="item active">
        Add lessons
    </a>
    <a class="item" href="/teacher/manager/edit">
        Edit/Delete lessons
    </a>
</div>
<#if message??>
<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${message}
    </div>
</div>
</#if>
<#if msgErr??>
<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${msgErr}
    </div>
</div>
</#if>
<div class="ui grid">

    <div class="ui form" style="margin-top: 5%; margin-left: 10%; margin-bottom : 5%;">

        <form action="/teacher/manager/add" method="post">

    <div class="two fields">
        <div class="field">
            <label>Subject</label>
            <select class="ui fluid dropdown" name="title">
                <#list subjects as sub>
                    <option value="${sub.name}">${sub.name}</option>
                </#list>
            </select>
        </div>
        <div class="field">
            <label>Group</label>
            <select class="ui fluid dropdown" name="groupId">
                <#list groups as group>
                    <option value="${group.id}">${group.name}</option>
                </#list>
            </select>
        </div>
    </div>

    <div class="field" style="margin-top : 10%;">
        <label>Room</label>
        <input type="text" name="room" placeholder="Audience" required>
    </div>

    <div class="field" style="margin-top : 10%;">
        <label>Select dates for lessons</label>
    </div>

    <div class="two fields">
        <div class="field" style="width : 300px;">
            <input type="text" id="picker" name="dates" required>
        </div>
        <div class="field">
            <i class="ui secondary button" onclick="show_picker();">
                Add
            </i>
        </div>
    </div>

    <div class="field" style="margin-top : 10%;">
        <label>Time</label>
        <input type="time" min="7" max="20" name="time" required>
    </div>

    <div class="field" style="margin-top : 10%;">
        <label>Duration</label>
        <div class="ui right labeled input">
            <input type="number" name="duration" placeholder="Maximum : 160" required>
            <div class="ui basic label">Minutes</div>
        </div>
    </div>

        <button class="ui primary button" type="submit" style="margin-top : 10%;">
            Save
        </button>
        </form>

</div>
</body>

</html>