/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
 * @author warren
 */
@WebServlet(name = "ECommerce_MinusFurnitureToListServlet", urlPatterns = {"/ECommerce_MinusFurnitureToListServlet"})
public class ECommerce_MinusFurnitureToListServlet extends HttpServlet {
   
    
    ArrayList<ShoppingCartLineItem> shoppingCart;
    ShoppingCartLineItem shoppingCartLineItem;
    int quantity =0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        /*
        //Debugging Purpose
        response.getOutputStream().println(request.getParameter("SKU"));
         */
        String SKU = request.getParameter("SKU");
        // response.getOutputStream().println(request.getParameter("quantity"));
        shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
        shoppingCartLineItem = new ShoppingCartLineItem();
        try {
            if(removeItem(SKU)){ 
                if (quantity != 1) {
                    String goodMsg = "Item quantity reduced successfully!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + goodMsg);  
                } 
                else  {
                    String errMsg = "Error. Quantity cannot be less than 1";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + errMsg);
                }
            }
        } catch (Exception ex) {

        }

    }
    
    public boolean removeItem(String SKU) {
        if (shoppingCart != null) {
            for (ShoppingCartLineItem i:shoppingCart) {
                if (i.getSKU().equals(SKU)) {
                    if (i.getQuantity() == 1) {
                        shoppingCartLineItem.setName(i.getName());
                        break;
                    }
                    quantity = i.getQuantity() - 1;
                    i.setQuantity(quantity);
                    shoppingCartLineItem = i;
                    break;
                }
            }
            return true;
        }
        return false;
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
