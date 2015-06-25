$(function(){
    debugger;
    var $parentInfoForm = $("#parentInfoForm");
    var $parentip = $("#parentip");
    var $parentport = $("#parentport");
    //var $invitecode = $("#invitecode");

    var $nodeid = $("#nodeid");
    var $name = $("#name");
    var $ip = $("#ip");
    //var $port = $('#port');
    var $nodetype = $('#nodetype');
    var $deploytype = $('#deploytype');
    var $area = $('#area');
    var $osname = $('#osname');
    var $hostname = $('#hostname');

    var $btnSaveParent = $("#btnSaveParent");
    var $btnSave = $("#btnSave");

    // ======================== 第一个tab页， 父节点信息修改及连接测试 ======================
    $parentInfoForm.form({
        url: contextPath + "client/servlet/SelfServlet?method=updateParent",
        onSubmit: function(){
            return true;
            //进行表单验证
            //如果返回false阻止提交
        },
        success:function(res) {
            var resObj = eval("(" + res + ")");
            $.messager.alert('提示框', resObj.msg);

            // 只能保存一次父节点ip
            //$btnSaveParent.linkbutton('disable');
            //$btnSaveParent.unbind( "click" );
        }
    });

    $btnSaveParent.click(function(){
        $parentInfoForm.submit();
    });

    $("#btnTestConnect").click(function(){
        $.messager.progress({
            title:"请稍后",
            msg:"连接父节点中"
        });
        $.ajax({
            cache: false,
            url:  contextPath + "client/servlet/SelfServlet?method=test",
            type: "POST",
            dataType: "json",
            data: $parentInfoForm.serialize(),
            //async: true,
            complete:function(){
                $.messager.progress("close");
            },
            success: function(res){
                $.messager.alert('提示框', res.msg)
            }
        })
    });

    // ======================== 第二个tab页， 节点信息修改 ======================
    $('#selfInfoForm').form({
        url:  contextPath + "client/servlet/SelfServlet?method=updateSelf",
        onSubmit: function(){
            //进行表单验证
            //如果返回false阻止提交
        },
        success:function(res){
            var resObj = eval("(" + res + ")");

            $.messager.alert('提示框', resObj.msg)
        }
    });

    $("#btnUpdateSelf").click(function(){
        $('#selfInfoForm').submit();
    })

// ======================== 查询注册及节点信息 ======================
    function query(){
        $.ajax({
            cache: false,
            url:  contextPath + "client/servlet/SelfServlet?method=query",
            type: "POST",
            dataType: "json",
            async: false,
            success: function(self){
                debugger;
                if(!self || !self.nodeid){
                    // set default
                    $nodetype.combobox('setValue', "deploy");
                    $deploytype.combobox('setValue', "semi-auto");
                    //$port.val("80");
                    return;
                }else{
                    loadSelf(self);
                }
            }
        })
    }

    function loadSelf(node){
        debugger;
        // 第一个tab页：父节点信息
        //if(node.parentip){
        //    $btnSaveParent.linkbutton('disable');
        //    $btnSaveParent.unbind( "click" );
        //}
        $parentip.val(node.parentip);
        $parentport.val(node.parentport);
        //$invitecode.val(node.invitecode);

        $parentip.validatebox("validate");
        $parentport.validatebox("validate");
        //$invitecode.validatebox("validate");

        // 第二个tab页，自身信息
        $nodeid.val(node.nodeid);
        $name.val(node.name);
        $ip.val(node.ip);
        //$port.val(node.port);
        $nodetype.combobox('setValue', node.nodetype);
        $deploytype.combobox('setValue', node.deploytype);
        $area.val(node.area);
        $osname.val(node.osname);
        $hostname.val(node.hostname);

        $name.validatebox("validate");

    }
    query();

})