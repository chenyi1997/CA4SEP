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

/**
 *
 * @author ChenYi
 */
@WebServlet(name = "ECommerce_RemoveItemFromListServlet", urlPatterns = {"/ECommerce_RemoveItemFromListServlet"})
public class ECommerce_RemoveItemFromListServlet extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try{
        
        /*
        http://stackoverflow.com/questions/10136062/get-selected-rows-from-a-html-table-using-servlets-and-jsp
        get selected rows from a html table using servlets and jsp
            
        <input type="checkbox" name="delete" value="<%=item.getSKU()%>" />
        etParameterValues from name 
         */

        String[] selectedItem = request.getParameterValues("delete");
        
        //retrive the cart
        ArrayList<ShoppingCartLineItem> shoppingCart;
        
        //if cart exists
        if (session.getAttribute("shoppingCart") != null) {
            shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");

            //check if the cartItem contains the string inside the selectedItem
            for (String selectedItemString : selectedItem) {
                for(int i=0;i<shoppingCart.size();i++) {
                    if (shoppingCart.get(i).getSKU().equals(selectedItemString)) {
                        shoppingCart.remove(i);
                    }
                }
            }

            /*
            java.util.ConcurrentModificationException
            https://stackoverflow.com/questions/8104692/how-to-avoid-java-util-concurrentmodificationexception-when-iterating-through-an
            -> Solution: Use an iterator
             if (i.getSKU().contains(selectedItemString)) {
                        shoppingCart.remove(i);
             }
            */
            
            //return the item to the cart
            session.setAttribute("shoppingCart", shoppingCart);
            
            String goodMsg = "Successfully removed: "+selectedItem.length+ " record(s).";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + goodMsg);
        }
        else{
            String errMsg = "There is nothing in the cart";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + errMsg); 
        }

    }catch(Exception ex){
        ex.printStackTrace();
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
