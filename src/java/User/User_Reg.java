/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import com.dbutil.DBsingletone;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Spoorthi
 */
@WebServlet(name = "User_Reg", urlPatterns = {"/User_Reg"})
public class User_Reg extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String number = request.getParameter("number");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            
            String pkid = request.getParameter("pkid");
            String meth = request.getParameter("meth");

            DBsingletone db = new DBsingletone();
            java.sql.Connection c = db.getConnection();

            System.out.println("DB Conected");

            if (meth.equals("Add")) {
                PreparedStatement ps = c.prepareStatement("insert into users(user_name,user_email,user_number,user_password,user_address) values('" + name + "','" + email + "','" + number + "','" + password + "','" + address + "')");
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            }else if (meth.equals("Get")) {
                PreparedStatement ps = c.prepareStatement("select * from users");
                ResultSet rs = ps.executeQuery();
                JSONArray Json = new JSONArray();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("pkid", rs.getString("aid"));
                    
                    obj.put("name", rs.getString("user_name"));
                    obj.put("email", rs.getString("user_email"));
                    obj.put("number", rs.getString("user_number"));
                    obj.put("password", rs.getString("user_password"));
                    obj.put("address", rs.getString("user_address"));
                    
                    Json.put(obj);
                }
                out.print(Json);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            Logger.getLogger(User_Reg.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(User_Reg.class.getName()).log(Level.SEVERE, null, ex);
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
