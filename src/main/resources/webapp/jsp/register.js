$(function(){
    var $parentip = $("#parentip");
    var $parentport = $("#parentport");
    var $tipContainer = $("#tipContainer");
    var $linkStatus = $('#linkStatus');

    $("#btnRegister").click(register);
    $("#btnReload").click(reload);

    $.ajax({
        cache: false,
        url:  contextPath + "client/servlet/RegisterServlet?method=query",
        type: "POST",
        dataType: "json",
        async: false,
        success: function(registerInfo){
            debugger;
            if(!registerInfo.parentid){
                return;
            }
            $("#parentip").val(registerInfo.parentip);
            $("#parentport").val(registerInfo.parentport);
            $('#linkStatus').combobox('setValue', registerInfo.linkstatus);
            $("#btnRegister").linkbutton('disable');
            $("#btnRegister").unbind( "click" );
        }
    })



    reload();

    function reload(){
        $.ajax({
            cache: false,
            url:  contextPath + "client/servlet/RegisterServlet?method=test",
            type: "POST",
            data:{parentip:$parentip.val(), parentport: $parentport.val()},
            dataType: "json",
            async: true,
            success: function(json){
                debugger;
                if(json){
                    $linkStatus.combobox('setValue', json.linkstatus);
                    $tipContainer.html("刷新状态成功！" + new Date());
                }
            }
        })
    }
})