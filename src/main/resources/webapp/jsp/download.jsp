<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <script type="text/javascript" src="download.js"></script>
    <title> <%= systemName %> <%= systemVersion %> </title>
</head>
<body>

<div id="content">
    <%@include file="include/nav.jsp"%>
    <div id="wrapper">
        <table id="listGrid" class="easyui-datagrid" style="width:100%;height:450px"
              url="<%=contextPath%>client/servlet/SvnPackServlet?method=queryDownloadProgress"
               toolbar="#toolbar" pagination="true"
               rownumbers="false" fitColumns="true" singleSelect="true">
            <thead>
                <tr>
                    <th field="no" width="15" align="center">序号</th>
                    <th field="filename" width="30" align="center">文件名</th>
                    <th field="version" width="50" align="center">版本号</th>
                    <th field="size" width="30" align="center">大小(B)</th>
                    <th field="comment" width="45" align="center">备注</th>
                    <th field="progress" width="50" align="center">进度</th>
                    <th field="assesstime" width="50" align="center">预估更新时间</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar">
            <a id="btnRefresh" href="javascript:" class="easyui-linkbutton" iconCls="icon-reload" plain="true" >刷新并开启下载</a>
        </div>

    </div>
</div>

</body>
</html>