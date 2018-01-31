/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
      
        
        try {
            //get Values
            String SKU = request.getParameter("SKU");
            String category = (String) request.getParameter("cat");
            long id = Long.parseLong(request.getParameter("id"));
            double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");
            int itemQty = (Integer.parseInt(getQuantity(SKU)));
        
            //got stock 
            if (itemQty > 0) {
                ArrayList<ShoppingCartLineItem> shoppingCart;
                ShoppingCartLineItem item = new ShoppingCartLineItem();
                item.setImageURL(imageURL);
                item.setName(name);
                item.setSKU(SKU);
                item.setPrice(price);

                //got stock & cart
                if (session.getAttribute("shoppingCart") != null) {
                    shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
                    for (ShoppingCartLineItem i : shoppingCart) {
                        if (i.equals(item)) {
                            i.setQuantity(i.getQuantity() + 1);
                            i.setName(name);
                            i.setImageURL(imageURL);
                            i.setPrice(price);
                            i.setSKU(SKU);
                        }

                    }
                } else {
                    //Create the shoppingCart
                    shoppingCart = new ArrayList();
                    shoppingCart.add(item);
                    System.out.println(item);

                }
                session.setAttribute("shoppingCart", shoppingCart);
                String goodMsg = "Item successfully added into the cart!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + goodMsg);
            }
            else {
                //no stock
                String errMsg="Item not added to cart, not enough quantity available.";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="
                        + errMsg);
            }  
        } catch (Exception ex) {
            response.sendRedirect("/IS3102_Project_war/B/SG/shoppingCart.jsp?" );
            ex.printStackTrace();
        }
    }

    public String getQuantity(String SKU) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity/getQuantity")
                .queryParam("storeID", 59)
                .queryParam("SKU", SKU);
        
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        
        Response res = invocationBuilder.get();
        
        if (res.getStatus() == 200) {
            //int quantity = (Integer)res.getEntity();
            String quantity = res.readEntity(String.class);
            
            return quantity;
        } else {
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
