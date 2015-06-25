<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <link  href="<%=contextPath%>css/register.css" rel="stylesheet">
    <script type="text/javascript" src="register.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <table>
            <div class="demo-info" style="margin-bottom:10px">
                <div class="demo-tip icon-tip"></div>
                <div id="tipContainer">此界面提供向父节点注册的功能</div>
            </div>

            <form id="registerForm" method="post" action="<%=contextPath%>client/servlet/RegisterServlet?method=register">
                <tr>
                    <td>当前上级服务器ip: </td>
                    <td>
                        <input id="parentip"  name="parentip"  class="easyui-validatebox" disabled="true"/>
                    </td>
                </tr>
                <tr>
                    <td>当前上级服务器端口: </td>
                    <td>
                        <input id="parentport" name="parentport" class="easyui-validatebox" disabled="true"/>
                    </td>
                </tr>
                <tr>
                    <td>当前连接状态: </td>
                    <td>
                        <select id="linkStatus" name="linkStatus"  class="easyui-combobox" style="width:173px;" disabled="false">
                            <option value="0">无法连接</option>
                            <option value="1">连接正常</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>上级服务器Ip地址: </td>
                    <td>
                        <input id="parentipNew" name="parentipNew" class="easyui-validatebox" data-options="required:true,validType:'length[7,30]',missingMessage:'请输入上级ip地址或者域名'"/>
                    </td>
                </tr>
                <tr>
                    <td>上级服务器端口: </td>
                    <td>
                        <input id="parentportNew" name="parentportNew" value="80" class="easyui-validatebox" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>邀请码: </td>
                    <td>
                        <input id="inviteCode" name="inviteCode" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入上级服务器生成的有效邀请码'"/>
                    </td>
                </tr>
                <tr>
                    <td><a id="btnRegister" href="javascript:" class="easyui-linkbutton" style="width:173px" data-options="iconCls:'icon-ok'">注册</a></td>
                    <td><a id="btnReload" href="javascript:" class="easyui-linkbutton" style="width:173px" data-options="iconCls:'icon-reload'">刷新</a></td>
                </tr>
            </form>
        </table>
    </div>
</div>

</body>
</html>