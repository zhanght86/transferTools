$(function(){
    $("#btnCreate").click(function(){
        $.ajax({
            cache: false,
            url: contextPath + "client/servlet/InviteServlet?method=createInviteCode",
            type: "POST",
            dataType: "json",
            success: function(){
                $('#dg').datagrid('reload');
            }
        })
    });

})