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
        PrintWriter out = response.getWriter();
        
        try {
            //get Values
            String SKU = request.getParameter("SKU");
            String category = (String) request.getParameter("cat");
            String name = request.getParameter("name");
            int quantity = getQuantity(SKU);
            
            //check if got stock
            if (quantity > 0) {

                ArrayList<ShoppingCartLineItem> shoppingCart;

                //create item
                ShoppingCartLineItem item = new ShoppingCartLineItem();
                item.setId(request.getParameter("id"));
                item.setImageURL(request.getParameter("imageURL"));
                item.setName(name);
                item.setSKU(SKU);
                item.setPrice(Double.parseDouble(request.getParameter("price")));

                //got item & cart
                if (session.getAttribute("shoppingCart") != null) {
                    //got cart
                    shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");

                    if (shoppingCart.contains(item)) {
                        //get the currentItem
                        for (ShoppingCartLineItem currentItem : shoppingCart) {
                            //if contains the item
                            if (currentItem.equals(item)) {
                                int currQuantity = currentItem.getQuantity();
                                // if quantity over stock 
                                if (currQuantity + 1 <= quantity) {
                                    currentItem.setQuantity(currQuantity + 1);
                                } else {
                                    response.sendRedirect("/IS3102_Project_war/B/SG/shoppingCart.jsp?="
                                            + URLEncoder.encode(category)
                                            + "&errorMsg=Item not added to cart, not enough quantity available.");
                                }
                                break;
                            }
                        }
                    } else {
                        item.setQuantity(1);
                        shoppingCart.add(item);
                    }
                } else {
                    //Create the shoppingCart
                    shoppingCart = new ArrayList();
                    item.setQuantity(1);
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
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+errMsg);
            }  
        } catch (Exception ex) {
            String errMsg="Item not added to cart, not enough quantity available.";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+errMsg);
            ex.printStackTrace();
        }
    }

    public int getQuantity(String SKU) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity/getQuantity")
                .queryParam("storeID", 59)
                .queryParam("SKU", SKU);
        
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        
        Response res = invocationBuilder.get();
        int quantity = Integer.parseInt(res.readEntity(String.class));
            
        return quantity;

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
