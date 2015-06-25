$(function(){
    var $listGrid = $("#listGrid");
    
    $("#btnRefresh").click(function () {
        $.messager.progress({
            title:"请稍后",
            msg:"请求加载中"
        });

        $.ajax({
            cache: false,
            url: contextPath + "client/servlet/SvnPackServlet?method=refreshDownload",
            type: "POST",
            dataType: "json",
            complete: function(){
                $.messager.progress("close");
            },
            success: function(res){
                if(res.code == 1){
                    $listGrid.datagrid('reload');
                }
                $.messager.alert("提示", res.msg);
            }
        })
    });
})