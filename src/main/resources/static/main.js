//editMyProfile.ftl
$(function() {
    $("#photo").click(function() {
        $('.ui.modal').modal('show');
    });
    $("#cancel").click(function() {
        $('.ui.modal').modal('hide');
    });
});
//studentsSearchPage.ftl//adminUsers.ftl
$(function() {
    $(".ui.fluid.dropdown").dropdown();
});

//myStorage
$(function() {

    $('#selector').dropdown();

    $('#selector_edit').dropdown();


    $("#upload-button").click(function() {
        $('#file_upload').modal('show');
    });

    $("#close-modal").click(function() {
        $('#file_upload').modal('hide');
    });

    // $('#dropdown').dropdown();
    $("#create_dir_button").click(function () {
        $("#create_dir").modal('show');
    });

    $("#edit_button").click(function() {
        $("#edit_permissions").modal('show');
    });

});

var $createDirectoryChck = $('#make_public');
var createDirectoryPrivacy = function (){
    if($createDirectoryChck.prop('checked')){
        $('.ui.dropdown').addClass("disabled");
        document.getElementById("selector").selectedIndex=-1;
    }else{
        $('.ui.dropdown').removeClass("disabled");
    }
};
$(createDirectoryPrivacy);
$('#make_public').change(createDirectoryPrivacy);

var $editPermissionChck = $('#make_public_permissions');
var editPermissions = function (){
    if($editPermissionChck.prop('checked')){
        $('.ui.dropdown').addClass("disabled");
        document.getElementById("selector_edit").selectedIndex=-1;
    }else{
        $('.ui.dropdown').removeClass("disabled");
    }
}
$(editPermissions);
$('#make_public_permissions').change(editPermissions);

//listOfTeachers//adminUsers
$('.special.cards .image').dimmer({
    on: 'hover'
});


//createTestPage
var question_index = 1;
var answer_index = 1;

function set_question_index(q_index) {
    question_index = q_index;
}

$(function() {
    $("#groups_selector").dropdown();
});

function add_question() {
    question_index++;
    answer_index = 1;
    $("#new_question").prepend(get_question_block());
}

function delete_question(answer) {
    if(confirm("Are you sure?")){
        answer.closest('.delete-block-question').remove();
    }
}

function add_pre_defined_question(question, answers) {
    question_index++;
    question_field = get_question_field_with_value(question);
    answer_fields =  "<div class='field' style='margin-left: 5%;'>" +
                                    "<label>Answers:</label>";
    answers.forEach(function(answ) {
        answer_fields += get_answer_block_with_value(answ['name'], answ['value'], answ['is_right']);
    });
    answer_fields += "<div id='new_answ_q" + question_index + "'>" +
        "<a class='ui icon button' onclick='add_answer(" + question_index + ")' style='display: block; width: 40px'>" +
                         "<i class='plus icon'></i> " +
                     "</a>" +
                 "</div>" +
             "</div>" +
             "<div class='ui divider'></div>"+
              "</div>";
     $("#new_question").prepend(question_field + answer_fields);
}

function add_answer(q_index) {
    answer_index++;
    var button_id = "new_answ_q" + q_index;
    $("#" + button_id).prepend(get_answer_block(q_index));
}

function add_answer_by_name(q_name) {
    answer_index++;
    var button_id = "new_answ_" + q_name;
    var q_index = q_name.replace('q', '');
    $("#" + button_id).prepend(get_answer_block(q_index));
}

function delete_answer(answer) {
    if(confirm("Are you sure?")){
        answer.closest('.delete-block-answer').remove();
    }
}

function get_answer_block_with_value(name, val, is_right, is_last) {
    var right_answer_checked = '';
    if(is_right) {
        right_answer_checked = 'checked';
    }
    return "<div class='delete-block-answer'>" +
        "<button type='button' class='ui mini icon red button answer-delete-button' name='" + name + "' onclick='delete_answer(this)'>" +
        "<i class='delete icon'></i>" +
        "</button>" +
        "<div class='field testAnswer'>" +
                    "<input type='text' value='" + val + "' name='" + name + "' required>" +
               "</div>" +
               "<div class='ui checkbox testCheckbox' style='left: 5px'>" +
                    "<input type='checkbox' name='" + name + "r' " + right_answer_checked + " title='Check right answer'>"+
                    "<label></label>" +
                "</div>" +
        "</div>";
}

function get_answer_block(q_index) {
    return "<div class='delete-block-answer'>" +
                "<button type='button' class='ui mini icon red button answer-delete-button' name='a" + answer_index + "q" + q_index + "' onclick='delete_answer(this)'>" +
                    "<i class='delete icon'></i>" +
                "</button>" +
                "<div class='field testAnswer'>" +
                    "<input type='text' name='a" + answer_index + "q" + q_index + "' required>" +
                "</div>" +
                "<div class='ui checkbox testCheckbox' style='left: 5px'>" +
                    "<input type='checkbox' name='a" + answer_index + "q" + q_index + "r' title='Check right answer'>"+
                "<label></label>" +
                 "</div>" +
            "</div>";
}

function get_question_field_with_value(val) {
    return "<div class='delete-block-question'>" +
        "<button type='button' class='ui mini icon red button right floated' name='q" + question_index + "' onclick='delete_question(this)'>" +
        "<i class='delete icon'></i>" +
        "</button>" +
           "<div class='field' style='width: 510px;'>" +
                           "<label>Question:</label>" +
                           "<div class='field'>" +
                               "<input type='text' value='" + val + "' name='q" + question_index + "' required>" +
                           "</div>" +
                       "</div>";
}

function get_question_block() {
    return  "<div class='delete-block-question'>" +
            "<button type='button' class='ui mini icon red button right floated' name='q" + question_index + "' onclick='delete_question(this)'>" +
                "<i class='delete icon'></i>" +
            "</button>" +
            "<div class='field' style='width: 510px;'>" +
                "<label>Question:</label>" +
                "<div class='field'>" +
                    "<input type='text' name='q" + question_index + "' required>" +
                "</div>" +
            "</div>" +
            "" +
            "<div class='field' style='margin-left: 5%;'>" +
                "<label>Answers:</label>" +
                "<div class='field testAnswer'>" +
                    "<input type='text' name='a1q" + question_index + "' required>" +
                "</div> " +
                "<div class='ui checkbox testCheckbox'>" +
                    "<input type='checkbox' name='a1q" + question_index +"r' title='Check right answer'>" +
                    "<label></label>" +
                "</div>"+
                "" +
                "<div id='new_answ_q" + question_index + "'>" +
                    "<a class='ui icon button' onclick='add_answer(" + question_index + ")' style='display: block; width: 40px'>" +
                        "<i class='plus icon'></i> " +
                    "</a>" +
                "</div>" +
            "</div>" +
            "<div class='ui divider'></div>" +
            "</div>"
}

//manageTest.ftl
$('.ui.accordion').accordion();

//adminUsers.ftl
$(function() {
    $("#create_user").click(function() {
        $('.ui.modal')
            .modal('show');
    });
    $(".ui.selection.dropdown").dropdown();
});

//adminGroups
$(function() {
    $("#create_group").click(function() {
        $('.ui.modal')
            .modal('show')
        ;
    });
});

//manageGroupPage.ftl
$(function() {
    $("#add_member").click(function() {
        $('#add_member_modal')
            .modal('show')
        ;
    });
    $("#add_teacher").click(function() {
        $('#add_teacher_modal')
            .modal('show')
        ;
    });
    $(".ui.selection.dropdown").dropdown();
});