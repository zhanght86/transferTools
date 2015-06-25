$(function(){
    var $listGrid = $("#listGrid");
    var $createWin = $("#createWin");
    var $releaseWin = $("#releaseWin");

    //================ 新增窗口打开 =================//
    $("#btnCreate").click(function(){
        $createWin.window('open');
    });
    $('#createForm').form({
        url: contextPath + "client/servlet/SvnPackServlet?method=create",
        onSubmit: function(){
            //进行表单验证
            //如果返回false阻止提交
        },
        success:function(res){
            var resObj = eval("(" + res + ")");
            debugger;
            if(resObj.code == 1){
                $createWin.window('close');
                $listGrid.datagrid('reload');
            }
            $.messager.alert("提示", resObj.msg);
        }
    });

    //================ 删除增量包的功能 =================//
    $("#btnDelete").click(function(){
        var row = $listGrid.datagrid('getSelected');
        if(!row){
            $.messager.alert("提示", "选择要删除的增量包");
            return;
        }
        if(row.status > 0 ){
            $.messager.alert("提示", "只能删除本机创建且未发布的增量包");
            return;
        }

        $.messager.confirm("提示", "确认要删除序号：" + row.no + "的增量包吗？",
            function(data) {
                if(data){
                    $.ajax({
                        cache: false,
                        url: contextPath + "client/servlet/SvnPackServlet?method=delete",
                        type: "POST",
                        dataType: "json",
                        data: row,
                        success: function(res){
                            if(res.code == 1){
                                $listGrid.datagrid('reload');
                            }
                            $.messager.alert("提示", res.msg);
                        }
                    })
                }
            }
        )

    });

    //===================== 发布增量包的功能 ====================
    $("#btnRelease").click(function(){
        var row = $listGrid.datagrid('getSelected');
        if(!row){
            $.messager.alert("提示", "选择要发布的增量包");
            return;
        }

        if(row.status > 0){
            $.messager.alert("提示", "只能发布本机创建且未发布的增量包！");
            return;
        }

        $.messager.confirm("提示", "确认要发布序号：" + row.no + "的增量包吗？",
            function(data){
                if(data){
                    $.messager.progress({
                        title:"请稍后",
                        msg:"请求加载中"
                    });

                    $.ajax({
                        cache: false,
                        url: contextPath + "client/servlet/SvnPackServlet?method=release",
                        type: "POST",
                        dataType: "json",
                        data: row,
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
                }
            }
        )
    });

    //===================== 部署增量包的功能 ====================
    $("#btnDeploy").click(function(){
        var row = $listGrid.datagrid('getSelected');
        if(!row){
            alert("选择要部署的增量包");
            return;
        }
        if(!confirm("确认要部署序号：" + row.no + "的增量包吗？")){
            return;
        }
        if(row.status == 1 || row.status == 3){
            alert("文件正在下载或者已经部署！");
            return;
        }

        $.ajax({
            cache: false,
            url: contextPath + "client/servlet/SvnPackServlet?method=deploy",
            type: "POST",
            dataType: "json",
            data: row,
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


function formatStatus(value){
    switch(value){
        case 0 :
            return "未发布";
        case 1 :
            return "正在更新";
        case 2 :
            return "已发布";
        default:
            return "";
    }
}

function formatDeploy(value){
    if(value > 0){
        return "已部署";
    }
    return "未部署";
}