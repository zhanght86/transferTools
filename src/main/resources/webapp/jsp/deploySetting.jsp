<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <link  href="<%=contextPath%>css/register.css" rel="stylesheet">
    <script type="text/javascript" src="deploySetting.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <div id="tt" class="easyui-tabs" data-options="tabWidth:120" style="width:100%;height:350px;padding-right:1px;">
            <div title="应用服务器配置"  style="padding:10px;">
                <form id="deploySettingForm" method="post">
                    <table style="width:100%";>
                        <tr>
                            <td style="border:1px solid #000;">
                                <table>
                                    <tr>
                                        <td>服务器信息</td>
                                        <td cols="2">10.20.17.214:8090</td>
                                    </tr>
                                    <tr>
                                        <td>服务器目录：</td>
                                        <td>/home/phis</td>
                                        <td>删除</td>
                                    </tr>

                                    <tr>
                                        <td colspan="3">
                                            <a href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-add'">增加新的目录</a>
                                            <a href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-remove'">删除该服务器</a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td style="border:1px solid #000;" >
                                <table>
                                    <tr>
                                        <td>服务器信息</td>
                                        <td cols="2">10.20.17.214:8090</td>
                                    </tr>
                                    <tr>
                                        <td>服务器目录：</td>
                                        <td>/home/phis</td>
                                        <td>删除</td>
                                    </tr>
                                    <tr>
                                        <td>服务器目录：</td>
                                        <td>/home/phis</td>
                                        <td>删除</td>
                                    </tr>

                                    <tr>
                                        <td colspan="3">
                                            <a href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-add'">增加新的目录</a>
                                            <a href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-remove'">删除该服务器</a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </form>

                <div style="text-align:left;padding:5px 0;">
                    <a id="btnAddNew" href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-add'">增加部署服务器</a>
                    <!--<a id="btnTest" href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-reload'">刷新状态</a>-->
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>