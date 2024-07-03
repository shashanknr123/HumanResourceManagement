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
import org.json.JSONArray;

import org.json.JSONObject;

/**
 *
 * @author Office
 */
@WebServlet(name = "UserAttendance", urlPatterns = {"/UserAttendance"})
public class UserAttendance extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {

            DBsingletone db = new DBsingletone();
            java.sql.Connection c = db.getConnection();
            System.out.println("User.UserAttendance.processRequest()");
            String meth = request.getParameter("meth");
            String date = request.getParameter("date");

            System.out.println("meth" + meth);
            System.out.println("date" + date);

            if (meth.equals("GetOldReport")) {
                JSONArray Json = new JSONArray();
                PreparedStatement stmt = c.prepareStatement("SELECT us.aid,us.`user_name`,us.`user_email`,IFNULL(a.`present_absent`, 0) AS present_absent FROM `users` us LEFT JOIN `attendance` a ON a.`user_id`=us.aid AND `attendance_date` = '" + date + "'");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getString("aid"));
                    obj.put("user_name", rs.getString("user_name"));
                    obj.put("user_email", rs.getString("user_email"));
                    obj.put("present_absent", rs.getString("present_absent"));
                    Json.put(obj);
                }
                out.print(Json);
                System.out.println("Json" + Json);
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
