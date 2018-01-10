/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import EntityManager.MemberEntity;
import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author samue
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String secondpassword = request.getParameter("repassword");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String country = request.getParameter("country");
        String address = request.getParameter("address");
        String secQuestion = request.getParameter("securityQuestion");
        String secAnswer = request.getParameter("securityAnswer");
        String age = request.getParameter("age");
        String income = request.getParameter("income");
        
        if (password != null && secondpassword != null && password.length() > 0 && secondpassword.length() > 0){
            if (!password.equals(secondpassword)) {
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?errMsg=Passwords do not match. Please key again.");
                return;
            }else if (password.length() < 8){
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?errMsg=Passwords too short. At least 8 characters.");
                return;
            }
        }
        if (name.length() <= 0 ||
            email.length() <= 0 ||
            phone.length() <= 0 ||
            address.length() <= 0 ||
            secQuestion.length() <= 0 ||
            secAnswer.length() <= 0 ||
            age.length() <= 0 ||
            income.length() <= 0)
        {
            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?errMsg=Some fields are empty!");
            return;
        }

        
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity").path("edit")
                .queryParam("id", member.getId())
                .queryParam("name", name)
                .queryParam("email", email)
                .queryParam("phone", phone)
                .queryParam("city", country)
                .queryParam("address", address)
                .queryParam("securityQuestion", secQuestion)
                .queryParam("securityAnswer", secAnswer)
                .queryParam("age", age)
                .queryParam("income", income)
                .queryParam("password", password);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response res = invocationBuilder.get();

       if (res.getStatus() != 200) {
            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?errMsg=error");
        }
       else {
           response.sendRedirect("ECommerce_GetMember?goodMsg=Account Updated Successfully");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ECommerce_MemberEditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ECommerce_MemberEditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
