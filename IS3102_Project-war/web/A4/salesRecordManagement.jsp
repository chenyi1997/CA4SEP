<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>        
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Sales Record Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="customerServiceManagement.jsp"> Customer Service </a>                                 
                                </li>
                                <li class="active">
                                    <i class="icon icon-align-center"></i> Sales Record Management
                                </li>
                                                              
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <style>
                        select {
                            max-width: 300px;
                        }
                    </style>                                                           

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        String errMsg = request.getParameter("errMsg");
                                        if (errMsg == null || errMsg.equals("")) {
                                            errMsg = "Select regional office first, then select store";
                                        }
                                        out.println(errMsg);
                                    %>
                                </div>
                            
                                <div class="panel-body">

                                    <form action="../SalesRecordManagement_SalesRecordServlet">
                                        <div class="form-group">
                                            <label for="select_regionalOffice">Regional Office</label>
                                            <select id="select_regionalOffice" class="form-control" name="regionalOffice" onchange="getStore()">
                                                <option>--Select regional office--</option>
                                                <%
                                                    List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) session.getAttribute("regionalOffices");
                                                                                                                                                       
                                                    for (RegionalOfficeEntity r : regionalOfficeList) {
                                                %>

                                                <option value="<%= r.getId()%>"><%= r.getName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                                 
                                        </div>
                                        <div class="form-group">
                                            <label for="select_store">Store</label>
                                            <select id="select_store" class="form-control" name="storeName" required="true">
                                            </select>
                                        </div>
                                        <input type="submit" class="btn btn-primary" value="Access">

                                    </form>

                                </div>                               

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->                    
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

        <%
            if (session.getAttribute("alertMessage") != null) {
        %>
        <script>
            alert("<%= session.getAttribute("alertMessage")%>");
        </script>
        <%
            }
        %>

        <script>
            function getStore() {
                var regionalOfficeId = $("#select_regionalOffice").find('option:selected').val();
                $.get('../SalesRecordManagement_ajax_Servlet', {regionalOfficeId: regionalOfficeId}, function(responseText) {
                    var stores = responseText.trim().split(';');
                    var x = document.getElementById("select_store");
                    while (x.length > 0) {
                        x.remove(0);
                    }
                    for (var i = 0; i < stores.length - 1; i++) {
                        var option = document.createElement("option");
                        option.text = stores[i];
                        x.add(option);
                    }
                });
            }
        </script>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            }
            );
        </script>
    </body>
</html>
