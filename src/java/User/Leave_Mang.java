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
import jakarta.servlet.http.HttpSession;
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
@WebServlet(name = "Leave_Mang", urlPatterns = {"/Leave_Mang"})
public class Leave_Mang extends HttpServlet {

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

            String fdate = request.getParameter("fdate");
            String tdate = request.getParameter("tdate");
            String type = request.getParameter("type");
            String ndays = request.getParameter("ndays");
            String reason = request.getParameter("reason");

            String pkid = request.getParameter("pkid");
            String meth = request.getParameter("meth");

            System.out.println("User.Leave_Mang.processRequest()");

            DBsingletone db = new DBsingletone();
            java.sql.Connection c = db.getConnection();

            HttpSession session = request.getSession();

            String uid = session.getAttribute("uid").toString();
            System.out.println(fdate + tdate + type + ndays + reason + uid);

            if (meth.equals("Add")) {
                PreparedStatement ps = c.prepareStatement("insert into leavemang(applied_date,uid,fromdate,todate,leavetype,no_of_days,reason,isactive) values(now(),'" + uid + "','" + fdate + "','" + tdate + "','" + type + "','" + ndays + "','" + reason + "','1')");
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (meth.equals("Delete")) {
                PreparedStatement ps = c.prepareStatement("delete from leavemang where lid = '" + pkid + "'");
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    out.print(true);
                } else {
                    out.print(false);
                }
            } else if (meth.equals("Getall")) {
                PreparedStatement ps = c.prepareStatement("SELECT d.File,r.rid, u.`ufname`,u.`ulname`,d.`doc_title`,d.`doc_des`,d.`Author`,s.`stream`,s.`Year`,r.`isactive` FROM `requesttable` r JOIN `addusers` u ON u.`pkid` = r.`uid` JOIN `document` d ON d.`did` = r.`did` JOIN `stream` s ON s.`Sid` = d.`Stream` where r.isactive=0");
                ResultSet rs = ps.executeQuery();
                JSONArray js = new JSONArray();
                while (rs.next()) {
                    JSONObject json = new JSONObject();
                    json.put("lid", rs.getString("pkid"));
                    json.put("applied_date", rs.getString("applied_date"));
                    json.put("no_of_days", rs.getString("ulname"));
                    json.put("fromdate", rs.getString("fdate"));
                    json.put("todate", rs.getString("tdate"));
                    json.put("reason", rs.getString("reason"));

                    js.put(json);
                    System.out.println(json + "" + json);
                }
                out.print(js);
            } else if (meth.equals("Get")) {
                PreparedStatement ps = c.prepareStatement("select * from leavemang");
                ResultSet rs = ps.executeQuery();
                JSONArray Json = new JSONArray();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("pkid", rs.getString("lid"));
                    obj.put("appliedDate", rs.getString("applied_date"));
                    obj.put("fdate", rs.getString("ndays"));
                    obj.put("tdate", rs.getString("todate"));
                    obj.put("type", rs.getString("leavetype"));
                    obj.put("ndays", rs.getString("no_of_days"));
                    obj.put("reason", rs.getString("reason"));

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
