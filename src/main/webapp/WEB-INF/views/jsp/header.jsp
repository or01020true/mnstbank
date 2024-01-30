<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="org.example.hacking02_sk.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
    <link rel="stylesheet" href="/css/font.css">
</head>
<body>
    <%
        HttpSession ss = request.getSession(false);
        User user = (User) ss.getAttribute("user");
        String name = null;
        if (user != null) {
            name = user.getMyname();
        }
    %>
    <c:set var="name" value="<%= name %>" />
    <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: rgb(0, 118, 190)">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="/" style="font-size: 1.5em;">MNST</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/jsp/___NoticeBoard_List">문의게시판</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/banking/inout">이체</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/banking/acchistory">계좌조회</a>
                    </li>
                </ul>
                <ul class="navbar-nav navbar-right">
                    <c:choose>
                        <c:when test="${not empty name}">
                            <li class="nav-item">
                                <a class="nav-link">${name}</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="/member/login">로그인</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${not empty name}">
                            <li class="nav-item">
                                <a class="nav-link" href="/member/logout">로그아웃</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="/member/joinInfo">회원가입</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
</body>
</html>
