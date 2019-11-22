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
	<a class="item" href="/mail/my">
		MailBox
	</a>
    <#if isTeacher?? && isTeacher>
        <a class="item" href="/teacher/manager">
            Lessons Manager
        </a>
	    <a class="item" href="/success/manage-students">
		    SS Manager
	    </a>
	    <a class="item" href="/subjects">
		    Subjects Manager
	    </a>
        <a class="item" href="/test/create">
            Tests Manager
        </a>
        <a class="item" href="/teacher/storage">
            My Storage
        </a>
    </#if>
    <#if isAdmin?? && isAdmin>
        <a class="item" href="/admin/users">
            Admin Panel
        </a>
    </#if>
    <div class="right menu">
        <#if isTeacher?? && isTeacher>
            <a  class="ui search-link" href="/students/all">
                <i class="search icon search-icon"></i>
            </a>
        </#if>
        <div class="item">
            <a class="ui primary button" href="/logout">Log out</a>
        </div>
    </div>

</div>