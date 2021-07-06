<!DOCTYPE html>
<html lang="en">
<head>
<% 
String title = (String) request.getAttribute("title");
%>
<title><%= title %></title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="../component/style.jsp"/>

</head>

<body>

<jsp:include page="../component/script.jsp"/>

<jsp:include page="../component/header.jsp"/>