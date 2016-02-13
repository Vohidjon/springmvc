<%--
  Created by IntelliJ IDEA.
  User: vohidjon-linux
  Date: 1/10/16
  Time: 12:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="../layout/head.jsp" %>
    <style>
        #fm, #fm_c {
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
    <div id="su-main-content" data-options="region:'center'">
        <div id="tab-set" data-options="border: false" class="easyui-tabs" style="height: 100%;">
            <div title="categories">
                <%--product categories--%>
                    <table id="pc_datagrid" class="easyui-datagrid" data-options="url:'/product-categories/',
                            method:'get', border:false, pagination:true,singleSelect:true,
                            fit:true, fitColumns:true, toolbar: toolbar, onLoadSuccess: customOnLoadSuccess">
                        <thead>
                        <tr>
                            <%--<th data-options="field:'id'" width="80">Id</th>--%>
                            <th data-options="field:'name', formatter:linkMaker" width="30">Name</th>
                            <th data-options="field:'description'" width="70">Description</th>
                        </tr>
                        </thead>
                </table>
                <%--product categories--%>
            </div>
        </div>
    </div>
    <div id="su-footer" data-options="region:'south',split:true" style="height:70px;">
        <%@ include file="../layout/footer.jsp" %>
    </div>
    <%--htmls elements for additional popups--%>
    <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
         closed="true" buttons="#dlg-buttons">
        <div class="ftitle">Category Information</div>
        <form id="fm" method="post" novalidate>
            <div class="fitem">
                <label>Name:</label>
                <input name="name" class="easyui-textbox" required="true">
            </div>
            <div class="fitem">
                <label>Description:</label>
                <input name="description" class="easyui-textbox">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveCategory()"
           style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close')"
           style="width:90px">Cancel</a>
    </div>
    <div id="dlg_c" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
         closed="true" buttons="#dlg-buttons">
        <div class="ftitle">Product Information</div>
        <form id="fm_c" method="post" novalidate>
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
    <div id="dlg-buttons_c">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveProductForCat()"
           style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close')"
           style="width:90px">Cancel</a>
    </div>
    <%--htmls elements for additional popups--%>
</div>
</body>
<script>
    var toolbar = [{
        text: 'Add',
        iconCls: 'icon-add',
        handler: function () {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'New Category');
            $('#fm').form('clear');
            url = '/product-categories/';
        }
    }, {
        text: 'Edit',
        iconCls: 'icon-edit',
        handler: editCategory
    }, {
        text: 'Delete',
        iconCls: 'icon-remove',
        handler: deleteCategory
    }];
    var toolbarForP = [{
        text: 'Add',
        iconCls: 'icon-add',
        handler: function () {
            var catId = $('#tab-set').tabs('getSelected').data('c-id');
            $('#dlg_c').dialog('open').dialog('center').dialog('setTitle', 'New Product');
            $('#fm_c').form('clear');
            $('#category').combobox('reload');
            $('#fm_c').form('load', {categoryId: catId});
            url = '/products/';
        }
    }, {
        text: 'Edit',
        iconCls: 'icon-edit',
        handler: editProductForCat
    }, {
        text: 'Delete',
        iconCls: 'icon-remove',
        handler: deleteProductForCat
    }];
    var url;
    const C_TAB_PREF = 'tab_';
    const C_GRID_PREF = 'grid_';
    function linkMaker(value, row, index) {
        return '<a class="c-link" data-c-id="' + row.id + '" href="/products/categories/' + row.id + '">' + value + '</a>'
    }
    function customOnLoadSuccess(data) {
        $('.c-link').click(function (event) {
            event.preventDefault();
            var url = $(this).attr('href');
            var name = $(this).text();
            var catId = $(this).data('c-id');
            var tabId = C_TAB_PREF + catId;
            var gridId = C_GRID_PREF + catId;
            if($('#' + tabId).length){
                $('#tab-set').tabs('select', name);
            } else {
                var content = $('<table>').attr('id', gridId);
                $('#tab-set').tabs('add',{
                    id: tabId,
                    title: name,
                    content: content,
                    closable: true
                });

                $('#' + gridId).datagrid({
                    url: url,
                    method: 'get',
                    border: false,
                    pagination: true,
                    fit: true,
                    fitColumns: true,
                    singleSelect: true,
                    columns: [[
                        {field: 'name', title: "Name", width: 30},
                        {field: 'category', width: 20, title: "Category", formatter: function(value){return value.name}},
                        {field: 'description', width: 50, title: "Description"},
                    ]],
                    toolbar: toolbarForP
                }).datagrid('load', {
                    name: name,
                    address: '#' + tabId
                });
            }
        });
    }
    function saveCategory() {
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
                    $('#pc_datagrid').datagrid('reload');    // reload the user data
                }
            }
        });
    }
    function editCategory() {
        var row = $('#pc_datagrid').datagrid('getSelected');
        if (row) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Product Category');
            $('#fm').form('load', row);
            url = '/product-categories/' + row.id;
        }
    }
    function deleteCategory() {
        var row = $('#pc_datagrid').datagrid('getSelected');
        if (row) {
            $.messager
                    .confirm(
                            'Confirm',
                            'Are you sure you want to delete ' + row.name + '?',
                            function (result) {
                                if (result) {
                                    $.ajax({
                                        url: '/product-categories/' + row.id,
                                        method: 'delete',
                                        success: function (result) {
                                            if (result == 'success') {
                                                $('#pc_datagrid').datagrid('reload');
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
    function editProductForCat(){
        var row = $('#p_datagrid').datagrid('getSelected');
        $('#category').combobox('reload');
        if (row) {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Product');
            $('#fm').form('load', row);
            url = '/products/' + row.id;
            $('#category').combobox('select', row.category.id);
        }
    }
    function saveProductForCat() {
        $('#fm_c').form('submit', {
            url: url,
            onSubmit: function () {
                $('#dlg_c').dialog('close');        // close the dialog
                return $(this).form('validate');
            },
            success: function (response) {
                if (response !== 'success') {
                    $.messager.show({
                        title: 'Error',
                        msg: "Unexpected Error Occurred!!!"
                    });
                } else {
                    $('#pc_datagrid').datagrid('reload');    // reload the user data
                }
            }
        });
    }
    function deleteProductForCat(){
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
