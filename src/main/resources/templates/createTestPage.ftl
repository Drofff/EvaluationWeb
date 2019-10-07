<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
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
    <a class="item" href="/profile">
        Profile
    </a>
    <a class="item" href="/teacher/manager">
        Lessons Manager
    </a>
    <a class="item" href="/test/create">
        Tests Manager
    </a>
    <a class="item" href="/teacher/storage">
        My Storage
    </a>
    <div class="right menu">
        <div class="item">
            <a class="ui primary button" href="/logout">Log out</a>
        </div>
    </div>

</div>
<script>
    var question_index = 1;

    $(function() {

    });

    function add_question() {
        question_index++;
        $("#new_question").prepend(get_question_block());
    }

    function add_answer(q_index) {
        var button_id = "new_answ_q" + q_index;
        $("#" + button_id).prepend(get_answer_block(q_index));
    }

    function get_answer_block(q_index) {
        return "<div class='ui checkbox'><input type='checkbox' name='aq" + q_index + "r' >" +
        "</div><div class='field'><input type='text' name='aq" + q_index + "'></div>";
    }

    function get_question_block() {
        return "<div class='field' style='margin-top: 7%;'><label>Question:</label>" +
        "<div class='field'><input type='text' name='q" + question_index + "'></div></div><div class='field' " +
         "style='margin-top: 7%; margin-left: 5%;'><label>Answers:</label><div class='ui checkbox'> " +
         "<input type='checkbox' name='aq" + question_index + "r' ></div><div class='field'> " +
         "<input type='text' name='aq" + question_index + "'></div><div id='new_answ_q" + question_index + "'>" +
         "<a class='ui icon button' onclick='add_answer(" + question_index + ")'><i class='plus icon'></i> " +
         "</a></div></div>";
    }

</script>
<#if messageError??>
<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${messageError}
    </div>
</div>
</#if>
<#if messageSuccess??>
<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${messageSuccess}
    </div>
</div>
</#if>
<div class="ui segment">

    <form class="ui form" action="/test/create" method="post" style="width: 60%; margin-left: 10%; margin-right:10%;">
        <h4 class="ui dividing header">Test Information</h4>
        <div class="field">
            <label>Name</label>
                <div class="field">
                    <input type="text" name="name" placeholder="Test name">
                </div>
        </div>
        <div class="field">
            <label>Duration</label>
            <div class="field">
                <input type="number" name="duration" placeholder="Duration"> Minutes
            </div>
        </div>
        <div class="field">
            <label>Deadline</label>
            <div class="field">
                <input type="datetime-local" name="deadLine">
            </div>
        </div>

        <div class="field" style="margin-top: 7%;">
            <label>Question:</label>
            <div class="field">
                <input type="text" name="q1">
            </div>
        </div>
        <div class="field" style="margin-top: 7%; margin-left: 5%;">
            <label>Answers:</label>
            <div class="ui checkbox">
                <input type="checkbox" name="aq1r">
            </div>
            <div class="field">
                <input type="text" name="aq1">
            </div>

            <div id="new_answ_q1">
                <a class="ui icon button" onclick="add_answer(1)">
                    <i class="plus icon"></i>
                </a>
            </div>

        </div>

        <div id="new_question">
            <a class="ui icon button" onclick="add_question()">
                <i class="plus icon"></i>
            </a>
        </div>


        <button class="ui primary button" type="submit">
            Create
        </button>

    </form>

</div>
</body>

</html>