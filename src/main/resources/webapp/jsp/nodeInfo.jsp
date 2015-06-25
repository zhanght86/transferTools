<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <link  href="<%=contextPath%>css/register.css" rel="stylesheet">
    <script type="text/javascript" src="nodeInfo.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <div id="tt" class="easyui-tabs" data-options="tabWidth:120" style="width:100%;height:350px;padding-right:1px;">
            <div title="父节点配置"  style="padding:10px;">
                <form id="parentInfoForm" method="post">
                    <table>
                        <tr style="display: none;">
                            <td>服务器Id: </td>
                            <td>
                                <input id="parentid" name="parentid" class="easyui-validatebox"  data-options="required:false" />
                            </td>
                        </tr>
                        <tr>
                            <td>服务器Ip: </td>
                            <td>
                                <input id="parentip" name="parentip" class="easyui-validatebox" data-options="required:true,validType:'length[7,30]',missingMessage:'请输入上级ip地址或者域名'"/>
                            </td>
                        </tr>
                        <tr>
                            <td>服务器端口: </td>
                            <td>
                                <input id="parentport" name="parentport" value="80" class="easyui-validatebox" data-options="required:true"/>
                            </td>
                        </tr>
                        <!-- <tr>
                            <td>邀请码: </td>
                            <td>
                                <input id="invitecode" name="invitecode" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入上级服务器生成的有效邀请码'"/>
                            </td>
                        </tr> -->
                    </table>
                </form>

                <div style="text-align:left;padding:5px 0;">
                    <a id="btnSaveParent" href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-ok'">保存</a>
                    <a id="btnTestConnect" href="javascript:" class="easyui-linkbutton" style="width:120px" data-options="iconCls:'icon-reload'">测试连接</a>
                </div>
            </div>
            <div title="本机信息" style="padding:10px;">
                <form id="selfInfoForm" method="post">
                    <table>
                        <tr>
                            <td>ID: </td>
                            <td>
                                <input id="nodeid" name="nodeid" class="easyui-validatebox" data-options="required:false"  disabled="true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>名称: </td>
                            <td>
                                <input id="name" name="name" class="easyui-validatebox" data-options="required:true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>节点IP: </td>
                            <td>
                                <input id="ip" name="ip" class="easyui-validatebox"  disabled="true"/>
                            </td>
                        </tr>
                       <!--
                       <tr>
                            <td>端口: </td>
                            <td>
                                <input id="port" name="port" class="easyui-validatebox"/>
                            </td>
                        </tr>
                        -->
                        <tr>
                            <td>节点类型: </td>
                            <td>
                                <select id="nodetype" name="nodetype" class="easyui-combobox" style="width:173px;" >
                                    <option value="center">中心</option>
                                    <option value="relay">传输</option>
                                    <option value="deploy">部署</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>部署类型: </td>
                            <td>
                                <select id="deploytype" name="deploytype" class="easyui-combobox" style="width:173px;">
                                    <!-- <option value="self">全手动</option> -->
                                    <option value="semi-auto">手动挡</option>
                                    <option value="auto">自动挡</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>区域: </td>
                            <td>
                                <input id="area" name="area" class="easyui-validatebox" data-options="required:true"/>
                                <%--<select id="area" name="area"  class="easyui-combobox" style="width:173px;">--%>
                                    <%--<option value="常州">常州</option>--%>
                                    <%--<option value="南京鼓楼">南京鼓楼</option>--%>
                                    <%--<option value="南京江宁">南京江宁</option>--%>
                                    <%--<option value="南京雨花">南京雨花</option>--%>
                                    <%--<option value="徐州">徐州</option>--%>
                                    <%--<option value="南京">南京</option>--%>
                                <%--</select>--%>
                            </td>
                        </tr>
                        <tr>
                            <td>操作系统: </td>
                            <td>
                                <input id="osname" name="osname" class="easyui-validatebox" disabled="true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>计算机名: </td>
                            <td>
                                <input id="hostname" name="hostname" class="easyui-validatebox" disabled="true"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2"></td>
                        </tr>
                    </table>
                </form>

                <div style="text-align:left;padding:5px 0;">
                    <a id="btnUpdateSelf" href="javascript:" class="easyui-linkbutton" style="width:200px" data-options="iconCls:'icon-ok'">保存</a>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>