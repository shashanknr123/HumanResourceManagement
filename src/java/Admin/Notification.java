/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

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
@WebServlet(name = "Notification", urlPatterns = {"/Notification"})
public class Notification extends HttpServlet {

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

            String title = request.getParameter("title");
            String date = request.getParameter("date");
            String desc = request.getParameter("desc");

            String pkid = request.getParameter("pkid");
            String meth = request.getParameter("meth");

            DBsingletone db = new DBsingletone();
            java.sql.Connection c = db.getConnection();

            System.out.println("DB Conected");

            if (meth.equals("Add")) {
                PreparedStatement ps = c.prepareStatement("insert into notification(title,date,description) values('" + title + "','" + date + "','" + desc + "')");
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (meth.equals("Update")) {
                PreparedStatement ps = c.prepareStatement("update notification set title = '" + title + "',date = '" + date + "',description = '" + desc + "' where pkid=" + pkid + "");
//                System.out.println(name+""+mname);

                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (meth.equals("Delete")) {
                PreparedStatement ps = c.prepareStatement("delete from notification where pkid = '" + pkid + "'");
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (meth.equals("Get")) {
                PreparedStatement ps = c.prepareStatement("select * from notification");
                ResultSet rs = ps.executeQuery();
                JSONArray Json = new JSONArray();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("pkid", rs.getString("pkid"));

                    obj.put("title", rs.getString("title"));
                    obj.put("date", rs.getString("date"));
                    obj.put("desc", rs.getString("description"));

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
