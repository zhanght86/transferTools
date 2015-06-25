<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@include file="jsp/include/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link  href="<%=contextPath%>css/index.css" rel="stylesheet">
    <script type="text/javascript" src="index.js"></script>
    <title> 版本更新系统 V1.0 </title>
</head>
<body>
<div id="content">
    <table>
        <tr><td width="950">
            <%@include file="jsp/include/nav.jsp"%>
        </td></tr>
        <tr><td>
            <div style="padding: 10px 10px;float:left;">

                <table width="950">
                    <tr>
                        <td>文件已更新到序列号 </td>
                        <td id="updateNo" style="color:red">>正在获取...</td>
                    </tr>
                    <tr>
                        <td>节点发布的最大文件序列号 </td>
                        <td id="releaseNo" style="color:red">>正在获取...</td>
                    </tr>
                    <tr>
                        <td>当前已发布或下载完成的增量包数量 </td>
                        <td id="releaseNum" style="color:red">>正在获取...</td>
                    </tr>
                    <tr>
                        <td>当前正在更新的增量包数量 </td>
                        <td id="updateNum" style="color:red">>正在获取...</td>
                    </tr>
                    <tr>
                        <td>当前系统 </td>
                        <td id="busy" style="color:red">>正在获取...</td>
                    </tr>
                </table>
            </div>
        </td></tr>

    </table>

    <br>

</div>
</body>
</html>