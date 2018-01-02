/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import EntityManager.MemberEntity;
import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ChenYi
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {
    
  
    private String result = "";
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(); 
        
        try{
            /*
            To retrieve user email through session
            */      
            String email = (String)request.getParameter("email");
            
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String city = request.getParameter("country");
            int securityQuestion = Integer.parseInt(request.getParameter("securityQuestion"));
            String securityAnswer = request.getParameter("securityAnswer");
            int age =Integer.parseInt(request.getParameter("age"));
            int income =Integer.parseInt(request.getParameter("income"));                    
            String password = request.getParameter("password");      
            
            if (password != null && password != "" ) {
                String repassword = request.getParameter("repassword");
                
                /**
                 * The server side validation
                 * 
                 */
                //i was talking about this redirect
                if (repassword.equals(password)) { // If the repeated password is correct
                    result = updateMemberDetail(name,email,phone,city,address,securityQuestion,securityAnswer,age,income,password);
                    response.sendRedirect("ECommerce_GetMember?goodMsg=Account updated successfully");
                } 
                else { 
                    // password properly
                    
                    result = "errMsg=Your password does not match the repeated input.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?");  
                }
            } else { // Only update details
                result = updateMemberDetail(name,email,phone,city,address,securityQuestion,securityAnswer,age,income, null);
                response.sendRedirect("ECommerce_GetMember?goodMsg=Account updated successfully");  
            }           
            

           
        }catch(Exception ex){
            out.println(ex);
            ex.printStackTrace();
        }
      
    }
    
    public String updateMemberDetail(String name,String email,String phone,String city,String address, int securityQuestion,String securityAnswer,int age,int income,String password){
        try{
           Client client = ClientBuilder.newClient();

            WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity/updateMemberByEmail")                
                    .queryParam("name", name)
                    .queryParam("email", email)
                    .queryParam("phone", phone)
                    .queryParam("city", city)
                    .queryParam("address", address)
                    .queryParam("securityQuestion",securityQuestion)
                    .queryParam("securityAnswer",securityAnswer)
                    .queryParam("age",age)
                    .queryParam("income",income)
                    .queryParam("passwordHash", password);
               
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.get();
            
            if (res.getStatus() == 200) {
                return "goodMsg=Your profile has been updated.";
            }
            
            
            else{
                return "errorMsg"+res.getStatus();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
   /**/

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
        processRequest(request, response);
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
        processRequest(request, response);
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
