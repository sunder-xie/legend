<style>
    /**
     * 左侧导航样式 start
     */
    .aside-nav {
        width: 140px;
        margin-right: 15px;
        margin-bottom: 10px;
        padding: 5px 0 10px 0;
        background: #fff;
        border: 1px solid #ddd;
    }

    .aside-nav li.aside-nav-root {
        padding-left: 15px;
        font: bold 14px/40px "Microsoft YaHei","Hiragino Sans GB",Helvetica,Arial,sans-serif;
        color: #000;
        cursor: pointer;
    }

    .aside-nav li a {
        position: relative;
        display: block;
        height: 100%;
        color: #333;
    }

    .aside-nav li.aside-nav-root a {
        font-weight: bold;
    }

    .aside-nav li.aside-nav-list a {
        padding-left: 15px;
    }

    .aside-nav li.aside-nav-list a:hover {
        background: #e2e7d0;
    }

    .aside-nav li.aside-nav-list a.current:before,
    .aside-nav li.aside-nav-list a:hover:before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 3px;
        height: 100%;
        background: #607b0a;
    }

    .aside-nav li.aside-nav-list a.current {
        background: #e2e7d0;
    }

    .aside-nav li.aside-nav-list {
        padding-bottom: 5px;
        border-bottom: 1px solid #ddd;
    }

    .aside-nav li.aside-nav-list:last-child {
        border-bottom: none;
    }

    .aside-nav li.aside-nav-list dd {
        margin-top: 0;
        line-height: 27px;
    }

    .aside-nav h1.aside-nav-title {
        padding: 8px 5px;
        font-size: 20px;
    }

    /**
     * 左侧导航样式 end
     */
    .aside {
        float: left;
        margin-right: -7px;
        margin-top: 12px;;
    }


    .aside-main {
        float: left;
        width: 823px;
    }
</style>
<#include "yqx/page/marketing/left-nav.ftl">
