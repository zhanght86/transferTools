<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <script type="text/javascript" src="svnpack.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <table id="listGrid" class="easyui-datagrid" style="width:100%;height:450px"
              url="<%=contextPath%>client/servlet/SvnPackServlet?method=queryPage"
               toolbar="#toolbar" pagination="true"
               rownumbers="false" fitColumns="true" singleSelect="true">
            <thead>
                <tr>
                    <th field="no" width="15" align="center">序号</th>
                    <th field="version" width="50" align="center">版本号</th>
                    <th field="size" width="30" align="center">大小(B)</th>
                    <th field="comment" width="45" align="center">备注</th>
                    <th field="createtime" width="50" align="center">创建时间</th>
                    <th field="releasetime" width="50" align="center">发布时间</th>
                    <th field="assesstime" width="50" align="center">预估更新时间</th>
                    <th field="releasenum" width="20" align="center">发布数</th>
                    <th field="updatenum" width="20" align="center">已更新数</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar">
            <!-- <a id="btnQuery" href="javascript:" class="easyui-linkbutton" iconCls="icon-search" plain="true" >查询</a> -->
            <a id="btnCreate" href="javascript:" class="easyui-linkbutton" iconCls="icon-add" plain="true" >上传文件</a>
            <!-- <a id="btnEdit" href="javascript:" class="easyui-linkbutton" iconCls="icon-edit" plain="true" >修改文件</a> -->
            <a id="btnDelete" href="javascript:" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" >删除文件</a>
            <a id="btnRelease" href="javascript:" class="easyui-linkbutton" iconCls="icon-save" plain="true" >发布文件</a>
            <a id="btnInfo" href="javascript:" class="easyui-linkbutton" iconCls="icon-start" plain="true" >跟踪文件</a>
            <a id="btnDeploy" href="javascript:" class="easyui-linkbutton" iconCls="icon-bug" plain="true" >部署</a>
        </div>


        <!-- ========================= 新增窗口 =========================  -->
        <div id="createWin" class="easyui-window" data-options="closed:true, title:'创建增量包',iconCls:'icon-save'" style="width:500px;height:300px;padding:5px;">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'center',border:false" style="padding:10px;background:#fff;border:1px solid #ccc;">
                    <form id="createForm" method="post"  enctype="multipart/form-data">
                        <table>
                            <tr>
                                <td>文件: </td>
                                <td><input name="zipfile" class="easyui-filebox" data-options="buttonText:'选择'" ></td>
                            </tr>
                            <tr>
                                <td>版本号: </td>
                                <td><input name="version" class="easyui-validatebox" data-options="required:true,validType:'length[1,50]'"></td>
                            </tr>
                            <tr>
                                <td>备注: </td>
                                <td><input name="comment" class="easyui-validatebox" data-options="required:false,validType:'length[1,100]'"></td>
                            </tr>
                            <tr>
                                <td>预估更新时间: </td>
                                <td><input name="assesstime" class="easyui-datebox" data-options="required:false"></td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
                    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="javascript:$('#createForm').submit();">保存</a>
                    <a id="createWinClose" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="javascript:$('#createWin').window('close');">取消</a>
                </div>
            </div>
        </div>


        <!-- ========================= 发布窗口 =========================  -->
        <div id="releaseWin" class="easyui-window" data-options="closed:true, title:'创建增量包',iconCls:'icon-save'" style="width:500px;height:300px;padding:5px;">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'center',border:false" style="padding:10px;background:#fff;border:1px solid #ccc;">
                    <ul id="childrenTree" class="easyui-tree" data-options="url:'<%=contextPath%>client/servlet/SvnPackServlet?method=queryChildren'"></ul>
                </div>
                <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
                    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="">保存</a>
                    <a id="releaseWinClose" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="javascript:$('#releaseWin').window('close');">取消</a>
                </div>
            </div>
        </div>



    </div>
</div>

</body>
</html>