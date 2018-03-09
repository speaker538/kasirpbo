/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasir;

/**
 *
 * @author PICO
 */
public class usersesi {
    private static String u_username;
    private static String u_password;
     
    public static String getU_username() {
        return u_username;
    }
 
    public static void setU_username(String u_username) {
        usersesi.u_username = u_username;
    }
    
    public static String getU_password(){
        return u_password;
    }
    
    public static void setU_password(String u_password){
        usersesi.u_password = u_password;
    }
}
