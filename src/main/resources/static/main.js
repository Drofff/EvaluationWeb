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

$(function() {
    $("#groups_selector").dropdown();
});

function add_question() {
    question_index++;
    answer_index = 1;
    $("#new_question").prepend(get_question_block());
}

function add_answer(q_index) {
    answer_index++;
    var button_id = "new_answ_q" + q_index;
    $("#" + button_id).prepend(get_answer_block(q_index));
}

function get_answer_block(q_index) {
    return "<div class='field testAnswer'>" +
                "<input type='text' name='a" + answer_index + "q" + q_index + "' required>" +
           "</div>" +
           "<div class='ui checkbox testCheckbox' style='left: 5px'>" +
                "<input type='checkbox' name='a" + answer_index + "q" + q_index + "r' title='Check right answer'>"+
                "<label></label>" +
            "</div>";
}

function get_question_block() {
    return  "<div class='field' style='width: 510px;'>" +
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
            "<div class='ui divider'></div>"
}

//adminUsers.ftl
$(function() {
    $("#create_user").click(function() {
        $('.ui.modal')
            .modal('show')
        ;
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