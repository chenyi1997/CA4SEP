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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 *
 * @author ChenYi
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        //PrintWriter out = response.getWriter();
        
        
        //retrieve all user payment info 
        String cardName="";
        long cardNo;
        double finalPrice =0.0;
        int securityCode;
        int month,year=0;
        long memberId;
        long countryId;
        ArrayList<ShoppingCartLineItem>shoppingCart = null;
        
        //check if session got memberID. send over
        if(session.getAttribute("memberID")!=null){
                memberId = (long)session.getAttribute("memberID");
        }
        else{
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp"
                    + "?errMsg=Your session has expired, please login again.");
            return;
        }
        try{
        //check if got session 
        if((ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart")!=null){
            //set the countryId
            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");
            countryId = shoppingCart.get(0).getCountryID();
            for(ShoppingCartLineItem i:shoppingCart){
                finalPrice += (i.getPrice() * i.getQuantity());
            }
            Response res = createPaymentRecord(memberId,finalPrice,countryId);
           
            if(res.getStatus()==200){
                long salesRecordId = Long.parseLong(res.readEntity(String.class));
               //link shopping cart item to the sales record
                
                for(ShoppingCartLineItem item: shoppingCart){
                    //add the corresponding line item data to database
                    Response itemResponse = updateQuantityItemRecord(salesRecordId,item);    
                }
                
                //reset the shooppingCart
                session.setAttribute("shoppingCart", new ArrayList<>());
                session.setAttribute("transactionId", salesRecordId);
                out.println(salesRecordId);
                //retriee shop info for collection
                
                 //get back to shoppingCart
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp"
                            + "?goodMsg="
                            + "Thank you for shopping at Island Furniture. You have checkout successfully!");              
            }
            else{
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp"
                            + "?errMsg="
                            + "Error checking out.");
            }
           
         
            }
            } catch(Exception ex){
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp"
                            + "?errMsg="
                            + "Error checking out.");
            }   
        }
    
    public Response updateQuantityItemRecord(long salesRecordID,ShoppingCartLineItem item){
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                .path("updateQuantityFromItem")
                .queryParam("salesRecordID", salesRecordID)
                .queryParam("itemEntityID", item.getId())
                .queryParam("quantity",item.getQuantity())
                .queryParam("countryID",item.getCountryID());

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        return invocationBuilder.put(Entity.entity(item, MediaType.APPLICATION_JSON));
        
    }
    
    public Response createPaymentRecord(long memberId,double finalPrice,long countryId){
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                .path("createECommerceTransactionRecord")
                .queryParam("finalPrice", finalPrice)
                .queryParam("countryId", countryId);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        return invocationBuilder.put(Entity.entity(String.valueOf(memberId),MediaType.APPLICATION_JSON));
//        Response res = invocationBuilder.put(Entity.entity(String.valueOf(memberId),MediaType.APPLICATION_JSON));
//        String result = res.readEntity(String.class);
//        return result;
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
