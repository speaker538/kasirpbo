/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author PICO
 */
public class koneksi {
    Connection con;
    Statement stm;
    
    public Connection getConnection(){
        try {
              con = DriverManager.getConnection("jdbc:mysql://localhost/kasirpbo2","root","");
              stm = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Gagal \n"+e);
        }
        return con;
    }
}
