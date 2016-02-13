<%--
  Created by IntelliJ IDEA.
  User: vohidjon-linux
  Date: 1/10/16
  Time: 9:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar-section">
    <ul>
        <li>
            <a href="/product-categories/entry">
                <div>Product Categories</div>
            </a>
        </li>
        <li>
            <a href="/products/entry">
                <div>Products</div>
            </a>
        </li>
        <li>
            <a href="/staff/entry">
                <div>Staff Members</div>
            </a>
        </li>
        <li>
            <a href="/suppliers/entry">
                <div>Suppliers</div>
            </a>
        </li>
        <li>
            <a href="/supply/entry">
                <div>Supply</div>
            </a>
        </li>
    </ul>
</div>
<script>
    $(document).ready(function () {
        var pathname = window.location.pathname;
        $('.sidebar-section a[href="' + pathname + '"] div').addClass('active-sidebar-link');
    });
</script>