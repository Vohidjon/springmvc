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
    <%@ include file="../layout/head.jsp" %>
    <style>
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }

        .fitem input {
            width: 160px;
        }
    </style>
</head>
<body>
<div id="su-body" class="easyui-layout" style="min-height: 658px;">
    <div data-options="region:'north'" style="height:70px; border: none;">
        <%@ include file="../layout/header.jsp" %>
    </div>
    <%--<div data-options="region:'east',split:true" title="East" style="width:100px;"></div>--%>
    <div data-options="region:'west',split:true" title="Navigation  " style="width:250px;">
        <%@ include file="../layout/sidebar.jsp" %>
    </div>
    <div data-options="region:'center',title:'Products',iconCls:'icon-ok'">
        <table id="p_datagrid" class="easyui-datagrid" data-options="
               url:'/products/', method:'get', border:false, pagination:true,
               singleSelect:true, fit:true, fitColumns:true, toolbar: toolbar">
            <thead>
            <tr>
                <th data-options="field:'name'" width="100">Name</th>
                <th data-options="field:'category', formatter: categoryFormatter" width="80">Category</th>
                <th data-options="field:'description'" width="80">Description</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="su-footer" data-options="region:'south',split:true" style="height:70px;">
        <%@ include file="../layout/footer.jsp" %>
    </div>
</div>
<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <div class="ftitle">Product Information</div>
    <form id="fm" method="post" novalidate>
        <div class="fitem">
            <label>Name:</label>
            <input name="name" class="easyui-textbox" required="true">
        </div>
        <div class="fitem">
            <label>Category:</label>
            <input class="easyui-combobox" id="category" name="category" required="true"
                   data-options="valueField:'id',textField:'name', url:'/product-categories/', method:'get'">
        </div>
        <div class="fitem">
            <label>Description:</label>
            <input name="description" class="easyui-textbox">
        </div>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveProduct()"
       style="width:90px">Save</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close')"
       style="width:90px">Cancel</a>
</div>
</body>
<script>
    var toolbar = [{
        text: 'Add',
        iconCls: 'icon-add',
        handler: prepareForm
    }, {
        text: 'Edit',
        iconCls: 'icon-edit',
        handler: editProduct
    }, {
        text: 'Delete',
        iconCls: 'icon-remove',
        handler: deleteProduct
    }];
    var url;
    function categoryFormatter(value, row, index){
        return '<a href="/">' + value.name + '</a>';
    }
    function prepareForm () {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'New Product');
        $('#fm').form('clear');
        $('#category').combobox('reload');
        url = '/products/';
    }
    function saveProduct() {
        $('#fm').form('submit', {
            url: url,
            onSubmit: function () {
                $('#dlg').dialog('close');        // close the dialog
                return $(this).form('validate');
            },
            success: function (response) {
                if (response !== 'success') {
                    $.messager.show({
                        title: 'Error',
                        msg: "Unexpected Error Occurred!!!"
                    });
                } else {
                    $('#p_datagrid').datagrid('reload');    // reload the user data
                }
            }
        });
    }
    function editProduct() {
        var row = $('#p_datagrid').datagrid('getSelected');
        $('#category').combobox('reload');
        if (row) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Product');
            $('#fm').form('load', row);
            url = '/products/' + row.id;
            $('#category').combobox('select', row.category.id);
        }
    }
    function deleteProduct() {
        var row = $('#p_datagrid').datagrid('getSelected');
        if (row) {
            $.messager
                    .confirm(
                            'Confirm',
                            'Are you sure you want to delete ' + row.name + '?',
                            function (result) {
                                if (result) {
                                    $.ajax({
                                        url: '/products/' + row.id,
                                        method: 'delete',
                                        success: function (result) {
                                            if (result === 'success') {
                                                $('#p_datagrid').datagrid('reload');
                                            } else {
                                                $.messager.show({    // show error message
                                                    title: 'Error',
                                                    msg: "Unexpected Error Occurred"
                                                });
                                            }
                                        },
                                        error: function (error) {
                                            console.log(error);
                                            $.messager.show({    // show error message
                                                title: 'Error',
                                                msg: "Unexpected Error Occurred"
                                            });
                                        }
                                    });
                                }
                            });
        }
    }
</script>
</html>