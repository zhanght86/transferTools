<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<div id="nav">
    <div id="title"><%= systemName %> <%= systemVersion %></div>
    <div id="list">
        <a href="<%=contextPath%>index.jsp">[首页]</a>
        <a href="<%=contextPath%>jsp/upload.jsp">[文件管理]</a>
        <a href="<%=contextPath%>jsp/download.jsp">[下载中...]</a>
        <!-- <a href="/jsp/nodeNet.jsp">节点网络</a> -->
        <a href="<%=contextPath%>jsp/nodeInfo.jsp">[节点配置]</a>
        <!--<a href="/jsp/invite.jsp">[邀请码]</a> -->
        <a href="<%=contextPath%>jsp/deploySetting.jsp">[部署配置]</a>
    </div>
</div>