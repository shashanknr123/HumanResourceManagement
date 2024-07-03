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
@WebServlet(name = "Attendance", urlPatterns = {"/Attendance"})
public class Attendance extends HttpServlet {

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

            String arr = request.getParameter("arr");
            String date = request.getParameter("date");
            String meth = request.getParameter("meth");
            String studentid = request.getParameter("studentid");

            if (meth.equals("Add")) {
                PreparedStatement stmt = c.prepareStatement("select * from attendance where `attendance_date` = '" + date + "'");
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    PreparedStatement psdel = c.prepareStatement("delete from attendance where `attendance_date` = '" + date + "'");
                    int delst = psdel.executeUpdate();
                    if (delst > 0) {
                        System.out.println("inside if");
                        int cnt = 0;
                        JSONArray jsonArray = new JSONArray(arr);
                        for (int b = 0; b < jsonArray.length(); b++) {
                            JSONObject object = jsonArray.getJSONObject(b);
                            String str = "insert into attendance(attendance_date,user_id,present_absent) values ('" + date + "','" + object.getString("student") + "','" + object.getString("attendance") + "')";
                            PreparedStatement ps1 = c.prepareStatement(str);
                            int i1 = ps1.executeUpdate();
                            if (i1 == 1) {
                                cnt = cnt + 1;
                            } else {
                                cnt = 0;
                            }
                        }
                        if (cnt > 0) {
                            out.print(true);
                        } else {
                            out.print(false);
                        }
                    } else {
                        out.print(false);
                    }
                } else {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(arr);
                    for (int b = 0; b < jsonArray.length(); b++) {
                        JSONObject object = jsonArray.getJSONObject(b);
                        String str = "insert into attendance(attendance_date,user_id,present_absent) values ('" + date + "','" + object.getString("student") + "','" + object.getString("attendance") + "')";
                        PreparedStatement ps = c.prepareStatement(str);
                        int i = ps.executeUpdate();
                        if (i == 1) {
                            cnt = cnt + 1;
                        } else {
                            cnt = 0;
                        }
                    }
                    if (cnt > 0) {
                        out.print(true);
                    } else {
                        out.print(false);
                    }
                }
            } else if (meth.equals("GetOldReport")) {
                JSONArray Json = new JSONArray();
                PreparedStatement stmt = c.prepareStatement("SELECT s.`aid`,s.`user_name`,IFNULL(a.`present_absent`, 0) AS present_absent FROM `users` s LEFT JOIN `attendance` a ON a.`user_id` = s.`aid` AND `attendance_date` = '" + date + "'");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getString("aid"));
                    obj.put("uname", rs.getString("user_name"));
                    obj.put("present_absent", rs.getString("present_absent"));
                    Json.put(obj);
                }
                out.print(Json);
            } else if (meth.equals("GetOldReportByStudent")) {
                JSONArray Json = new JSONArray();
                PreparedStatement stmt = c.prepareStatement("SELECT s.`aid`,s.`user_name`,IFNULL(a.`present_absent`, 0) AS present_absent FROM `users` s LEFT JOIN `attendance` a ON a.`user_id` = s.`aid` WHERE s.`id` = '" + studentid + "'");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("uname", rs.getString("user_name"));
                    obj.put("attendance_date", rs.getString("attendance_date"));
                    obj.put("present_absent", rs.getString("present_absent"));
                    Json.put(obj);
                }
                out.print(Json);
            }

        } catch (Exception e) {
          
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
