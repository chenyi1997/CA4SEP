/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 *
 * @author ChenYi
 */

@WebServlet(name = "ECommerce_GetMember", urlPatterns = {"/ECommerce_GetMember"})
public class ECommerce_GetMember extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            Member member = getMember((String) session.getAttribute("memberEmail"));
            session.setAttribute("member", member); // rertrive the object,member
            session.setAttribute("memberID", member.getId());
            session.setAttribute("memberName", member.getName());
            //chaneg here
            //null pointer exception, probably caused by goodmsg
            String msg = request.getParameter("goodMsg");
            if (msg != null)
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?goodMsg=" + msg);
            else
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?");

        } catch (NoResultException ex) {
            out.println("Failed to find any member.");
        } catch (Exception ex) {
            out.println("\nServer failed to getMemberByEmail:\n" + ex);
            ex.printStackTrace();
        }
    }
    
    public Member getMember(String email){
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity/getMemberByEmail")
                    .queryParam("email", email);
          
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

            Response res = invocationBuilder.get();
            if (res.getStatus() == 200) {
                Member member = res.readEntity(Member.class);
                System.out.println("status: " + res.getStatus());
                return member;
            }
            else{
                return null;
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
