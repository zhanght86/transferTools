$(function(){
    var $listGrid = $("#listGrid");
    var $createWin = $("#createWin");
    var $releaseWin = $("#releaseWin");

    //================ 新增窗口打开 =================//
    $("#btnCreate").click(function(){
        $createWin.window('open');
    });
    $('#createForm').form({
        url:  contextPath + "client/servlet/SvnPackServlet?method=create",
        onSubmit: function(){
            //进行表单验证
            //如果返回false阻止提交
        },
        success:function(res){
            var resObj = eval("(" + res + ")");
            debugger;
            if(resObj.code == 1){
                alert("上传成功！");
                $createWin.window('close');
                $listGrid.datagrid('reload');
            }else{
                alert(resObj.msg);
            }
        }
    });

    //================ 删除增量包的功能 =================//
    $("#btnDelete").click(function(){
        var row = $listGrid.datagrid('getSelected');
        if(!row){
            alert("选择要删除的增量包");
            return;
        }
        if(!confirm("确认要删除序号：" + row.no + "的增量包吗？")){
            return;
        }
        if(row.status > 0 ){
            alert("只能删除本机创建且未发布的增量包！");
            return;
        }
        $.ajax({
            cache: true,
            url:  contextPath + "client/servlet/SvnPackServlet?method=delete",
            type: "POST",
            dataType: "json",
            data: row,
            success: function(res){
                if(res.code == 1){
                    alert("删除成功！");
                    $listGrid.datagrid('reload');
                }else{
                    alert(res.msg); // 删除失败
                }
            }
        })
    });

    //===================== 发布增量包的功能 ====================
    $("#btnRelease").click(function(){
        var row = $listGrid.datagrid('getSelected');
        if(!row){
            alert("选择要发布的增量包");
            return;
        }
        if(!confirm("确认要发布序号：" + row.no + "的增量包吗？")){
            return;
        }
        if(row.status > 0){
            alert("只能发布本机创建且未发布的增量包！");
            return;
        }

        $releaseWin.window('open');

    });



})