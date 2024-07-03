/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author VSS-AND
 */
public class DBsingletone {
       
    private static final DBsingletone only_one = new DBsingletone();
    private static Connection con;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HumanReManagement", "root", "shashank");
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBsingletone getDbSingletone() {
        return only_one;
    }

    public Connection getConnection() {
      
        return con;
    }

}
    

