<%--
  Created by IntelliJ IDEA.
  User: vohidjon-linux
  Date: 1/10/16
  Time: 11:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="head.jsp" %>
</head>
<body>
<div id="su-body" class="easyui-layout" style="min-height: 658px;">
    <div data-options="region:'north'" style="height:70px; border: none;">
        <%@ include file="header.jsp" %>
    </div>
    <%--<div data-options="region:'east',split:true" title="East" style="width:100px;"></div>--%>
    <div data-options="region:'west',split:true" title="Navigation  " style="width:250px;">
        <%@ include file="sidebar.jsp" %>
    </div>
    <div data-options="region:'center',title:'Homepage',iconCls:'icon-ok'">
    </div>
    <div id="su-footer" data-options="region:'south',split:true" style="height:70px;">
        <%@ include file="footer.jsp" %>
    </div>
</div>
</body>
</html>