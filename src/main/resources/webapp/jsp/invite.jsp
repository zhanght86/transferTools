<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <script type="text/javascript" src="invite.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <table id="dg"  class="easyui-datagrid" style="width:100%;height:450px"
               url="<%=contextPath%>client/servlet/InviteServlet?method=queryAll"
               toolbar="#toolbar"
               rownumbers="true" fitColumns="true" singleSelect="true">
            <thead>
                <tr>
                    <th field="invitecode" width="50">邀请码</th>
                    <th field="createtime" width="50">生成时间</th>
                    <th field="terminaltime" width="50">失效时间</th>
                    <th field="statusAttr" width="50">状态</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar">
            <a id="btnCreate" href="javascript:" class="easyui-linkbutton" iconCls="icon-add" plain="true" >生成邀请码</a>
        </div>
    </div>
</div>

</body>
</html>