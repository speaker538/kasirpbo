/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasir.form_admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kasir.koneksi;

/**
 *
 * @author PICO
 */
public class kelola_user extends javax.swing.JDialog {
    Connection con;
    String sql;
    Statement stat;
    ResultSet rs;
    private DefaultTableModel tabeluser;
    
    /**
     * Creates new form kelola_user
     */
    public kelola_user(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        koneksi DB = new koneksi();
        con = DB.getConnection();    
        tampilkan();
        nonaktifkanpanel();
        panelUser.setVisible(true);
        setVisible(true);
    }
      
    private void nonaktifkanpanel(){
        panelTambahUser.setVisible(false);
        panelUser.setVisible(false);
        panelEditUser.setVisible(false);
    }
    
    private void tampilkan(){
        Object []baris = {"Username", "Password", "Nama", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir", "Telp", "Alamat", "Akses"};
        tabeluser = new DefaultTableModel(null, baris);
        tabelUser.setModel(tabeluser);
        
        try {
            sql = "SELECT * FROM user order by username ASC";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String username = rs.getString("username");
               String password = rs.getString("password");
               String nama = rs.getString("nama");
               String jk = rs.getString("jenis_kel");
               String tempat = rs.getString("tempat_lahir");
               String tanggal = rs.getString("tanggal_lahir");
               String telp = rs.getString("telepon");
               String alamat = rs.getString("alamat");
               String akses = rs.getString("akses");
               String[] data = {username, password, nama, jk, tempat, tanggal, telp, alamat, akses};
               tabeluser.addRow(data);
           } 
        } catch(Exception e){            
        }
    }
    
    private void clear(){
        txt_username.setText(""); //-- Komponen untuk tambah user
        txt_password.setText("");
        txt_nama.setText("");
        txt_tempat.setText("");
        txt_telpon.setText("");
        txt_alamat.setText("");
        cbAkses.setSelectedIndex(0);
        rbLaki.setSelected(true);
        date_tanggal.setDate(null);
        
        txt_eusername.setText(""); //-- Komponen untuk edit user
        txt_epassword.setText("");
        txt_enama.setText("");
        txt_etempat.setText("");
        txt_etelpon.setText("");
        txt_ealamat.setText("");
        cbeAkses.setSelectedIndex(0);
        rb_eLaki.setSelected(true);
        date_etanggal.setDate(null);
    }
    
    private void insertUser(){
        String  username, password, nama, jenis_kelamin, tempat_lahir, telpon, alamat, akses;
        java.util.Date tanggal_lahir;
        username = txt_username.getText();  
        password = txt_password.getText();
        nama = txt_nama.getText();        
        if (rbLaki.isSelected()){
            jenis_kelamin = "Laki-Laki";
        } else {
            jenis_kelamin = "Perempuan";
        }
        tempat_lahir = txt_tempat.getText();
        tanggal_lahir = (java.util.Date) this.date_tanggal.getDate();
        telpon = txt_telpon.getText();
        alamat = txt_alamat.getText();
        akses = (String)cbAkses.getSelectedItem();
        if (nama.equals("") || username.equals("") || password.equals("") || tempat_lahir.equals("") 
                 || telpon.equals("") || alamat.equals("")) {
            JOptionPane.showMessageDialog(null, "Mohon Lengkapi Semua Data");
        } else {

            if (JOptionPane.showConfirmDialog(null, "Tambah User ? \nNama : " + nama + "\nTTL : "
                + tempat_lahir + "\nAkses : " + akses +  "\nUsername : " + username , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {                    
                    String sqlkode = "insert into user values (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1,username);
                    p2.setString(2, password);
                    p2.setString(3, nama);
                    p2.setString(4, jenis_kelamin);
                    p2.setString(5, tempat_lahir);
                    p2.setDate(6, new java.sql.Date(tanggal_lahir.getTime()));
                    p2.setString(7, telpon);
                    p2.setString(8, alamat); 
                    p2.setString(9, akses);                          
                    p2.executeUpdate();
                    clear();
                    if (JOptionPane.showConfirmDialog(null, "Tambah Sukses, Tambah Lagi?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    } else {
                        tampilkan();
                        nonaktifkanpanel();
                        panelUser.setVisible(true);
                    }                       
                } catch (Exception e) {                      
                    JOptionPane.showMessageDialog(null, "Tambah User Gagal");
                    System.out.println(e);
                } 
            }
        }  
    }
    
    private void updateUser(){
        String  username, password, nama, jenis_kelamin, tempat_lahir, telpon, alamat, akses;
        java.util.Date tanggal_lahir;
        username = txt_eusername.getText();  
        password = txt_epassword.getText();
        nama = txt_enama.getText();        
        jenis_kelamin = null;
        if (rb_eLaki.isSelected()){
            jenis_kelamin = "Laki-Laki";
        } else {
            jenis_kelamin = "Perempuan";
        }
        tempat_lahir = txt_etempat.getText();
        tanggal_lahir = (java.util.Date) this.date_etanggal.getDate();
        telpon = txt_etelpon.getText();
        alamat = txt_ealamat.getText();
        akses = (String)cbeAkses.getSelectedItem();
        
        if (JOptionPane.showConfirmDialog(null, "Perbarui Data ? \nUsername: " + username , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                String sqlkode = "UPDATE user set password =?, nama=?, jenis_kel=?, tempat_lahir=?, tanggal_lahir=?, telepon=?, alamat=?, akses=? where username='" 
                        + username + "'";
                PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                p2.setString(1, password);
                p2.setString(2, nama);
                p2.setString(3, jenis_kelamin);
                p2.setString(4, tempat_lahir);
                p2.setDate(5, new java.sql.Date(tanggal_lahir.getTime()));               
                p2.setString(6, telpon);
                p2.setString(7, alamat);
                p2.setString(8, akses);
                p2.executeUpdate();     
                JOptionPane.showMessageDialog(null, "Edit User Sukses");
                tampilkan();
                nonaktifkanpanel();
                panelUser.setVisible(true);
            } catch(SQLException se){
            }
        } else {            
        }        
    }
    
    private void hapusUser(){
        int baris = tabelUser.getSelectedRow();
        String username = tabeluser.getValueAt(baris, 0).toString();      
        int ok = JOptionPane.showConfirmDialog(null,"Hapus Data ? \nUsername : " + username,"Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(ok==0){
            try{
                String sql="DELETE from user where username='" + username + "'";
                Statement st = con.createStatement();
                st.executeUpdate(sql);
                
                JOptionPane.showMessageDialog(null, "Hapus User Sukses");
                tampilkan();
                clear();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Hapus User Gagal");
            }
        }
    }
    
    private void cariUser(){
        String cari = txtcariUser.getText();

        Object []baris = {"Username", "Password", "Nama", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir", "Telp", "Alamat", "Akses"};
        tabeluser = new DefaultTableModel(null, baris);
        tabelUser.setModel(tabeluser);
        
        try {
            sql="SELECT * from user where " + "username like '%"+cari+"%' "               
                   + "OR nama like '%"+cari+"%' "
                   + "OR tempat_lahir like '%"+cari+"%' "
                   + "OR alamat like '%"+cari+"%' " 
                   + "order by nama asc";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String username = rs.getString("username");
               String password = rs.getString("password");
               String nama = rs.getString("nama");
               String jk = rs.getString("jenis_kel");
               String tempat = rs.getString("tempat_lahir");
               String tanggal = rs.getString("tanggal_lahir");
               String telp = rs.getString("telepon");
               String alamat = rs.getString("alamat");
               String akses = rs.getString("akses");
               String[] data = {username, password, nama, jk, tempat, tanggal, telp, alamat, akses};
               tabeluser.addRow(data);
           } 
        } catch(Exception e){            
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgjenis_kelamin = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelUser = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnTambahUser = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelUser = new javax.swing.JTable();
        txtcariUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnMenuUtama = new javax.swing.JButton();
        panelTambahUser = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        txt_password = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        rbLaki = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        txt_telpon = new javax.swing.JTextField();
        cbAkses = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        txt_tempat = new javax.swing.JTextField();
        date_tanggal = new com.toedter.calendar.JDateChooser();
        panelEditUser = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_eusername = new javax.swing.JTextField();
        txt_epassword = new javax.swing.JTextField();
        txt_enama = new javax.swing.JTextField();
        rb_eLaki = new javax.swing.JRadioButton();
        rbePerempuan = new javax.swing.JRadioButton();
        txt_etelpon = new javax.swing.JTextField();
        cbeAkses = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_ealamat = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btn_Batal = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txt_etempat = new javax.swing.JTextField();
        date_etanggal = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA USER");

        jPanel2.setBackground(new java.awt.Color(0, 51, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel2.setLayout(new java.awt.CardLayout());

        panelUser.setBackground(new java.awt.Color(0, 51, 153));
        panelUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel4.setBackground(new java.awt.Color(0, 51, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        btnTambahUser.setText("TAMBAH USER");
        btnTambahUser.setPreferredSize(new java.awt.Dimension(130, 40));
        btnTambahUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahUserActionPerformed(evt);
            }
        });
        jPanel4.add(btnTambahUser);

        btnEdit.setText("EDIT");
        btnEdit.setPreferredSize(new java.awt.Dimension(130, 40));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel4.add(btnEdit);

        btnHapus.setText("HAPUS");
        btnHapus.setPreferredSize(new java.awt.Dimension(130, 40));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        jPanel4.add(btnHapus);

        tabelUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelUserMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabelUser);

        txtcariUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariUserKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cari");

        btnMenuUtama.setText("MENU UTAMA");
        btnMenuUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuUtamaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUserLayout = new javax.swing.GroupLayout(panelUser);
        panelUser.setLayout(panelUserLayout);
        panelUserLayout.setHorizontalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUserLayout.createSequentialGroup()
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelUserLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                    .addGroup(panelUserLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMenuUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelUserLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtcariUser, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panelUserLayout.setVerticalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcariUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                .addComponent(btnMenuUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(panelUser, "card2");

        panelTambahUser.setBackground(new java.awt.Color(0, 51, 153));
        panelTambahUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("TAMBAH USER");

        jPanel10.setBackground(new java.awt.Color(0, 51, 153));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Username");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Password");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nama");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Jenis Kelamin");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Alamat");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Telepon");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Hak Akses");

        rbLaki.setBackground(new java.awt.Color(0, 51, 153));
        bgjenis_kelamin.add(rbLaki);
        rbLaki.setForeground(new java.awt.Color(255, 255, 255));
        rbLaki.setText("Laki-Laki");

        rbPerempuan.setBackground(new java.awt.Color(0, 51, 153));
        bgjenis_kelamin.add(rbPerempuan);
        rbPerempuan.setForeground(new java.awt.Color(255, 255, 255));
        rbPerempuan.setText("Perempuan");

        cbAkses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrator", "Kasir" }));

        txt_alamat.setColumns(20);
        txt_alamat.setRows(5);
        jScrollPane4.setViewportView(txt_alamat);

        btnTambah.setText("SIMPAN");
        btnTambah.setPreferredSize(new java.awt.Dimension(90, 40));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        jPanel11.add(btnTambah);

        btnBatal.setText("BATAL");
        btnBatal.setPreferredSize(new java.awt.Dimension(90, 40));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel11.add(btnBatal);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("TTL");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_username)
                                .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(rbLaki)
                                .addGap(18, 18, 18)
                                .addComponent(rbPerempuan))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(txt_tempat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(date_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbAkses, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addGap(25, 25, 25)
                                        .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(cbAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(rbLaki)
                            .addComponent(rbPerempuan))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel29)
                                .addComponent(txt_tempat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(date_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout panelTambahUserLayout = new javax.swing.GroupLayout(panelTambahUser);
        panelTambahUser.setLayout(panelTambahUserLayout);
        panelTambahUserLayout.setHorizontalGroup(
            panelTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahUserLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel21)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTambahUserLayout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTambahUserLayout.setVerticalGroup(
            panelTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(panelTambahUser, "card3");

        panelEditUser.setBackground(new java.awt.Color(0, 51, 153));
        panelEditUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelEditUser.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("EDIT USER");

        jPanel8.setBackground(new java.awt.Color(0, 51, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Username");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Password");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Nama");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Jenis Kelamin");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Alamat");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Telepon");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Hak Akses");

        txt_eusername.setEnabled(false);

        rb_eLaki.setBackground(new java.awt.Color(0, 51, 153));
        bgjenis_kelamin.add(rb_eLaki);
        rb_eLaki.setForeground(new java.awt.Color(255, 255, 255));
        rb_eLaki.setText("Laki-Laki");

        rbePerempuan.setBackground(new java.awt.Color(0, 51, 153));
        bgjenis_kelamin.add(rbePerempuan);
        rbePerempuan.setForeground(new java.awt.Color(255, 255, 255));
        rbePerempuan.setText("Perempuan");

        cbeAkses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrator", "Kasir" }));

        txt_ealamat.setColumns(20);
        txt_ealamat.setRows(5);
        jScrollPane3.setViewportView(txt_ealamat);

        btnSimpan.setText("SIMPAN");
        btnSimpan.setPreferredSize(new java.awt.Dimension(90, 40));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanel9.add(btnSimpan);

        btn_Batal.setText("BATAL");
        btn_Batal.setPreferredSize(new java.awt.Dimension(90, 40));
        btn_Batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BatalActionPerformed(evt);
            }
        });
        jPanel9.add(btn_Batal);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("TTL");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_eusername)
                                .addComponent(txt_epassword, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                            .addComponent(txt_enama, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(rb_eLaki)
                                .addGap(18, 18, 18)
                                .addComponent(rbePerempuan))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txt_etempat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(date_etanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbeAkses, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(25, 25, 25)
                                        .addComponent(txt_etelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_eusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txt_etelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_epassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(cbeAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txt_enama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(rb_eLaki)
                            .addComponent(rbePerempuan))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(txt_etempat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(date_etanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout panelEditUserLayout = new javax.swing.GroupLayout(panelEditUser);
        panelEditUser.setLayout(panelEditUserLayout);
        panelEditUserLayout.setHorizontalGroup(
            panelEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditUserLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditUserLayout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelEditUserLayout.setVerticalGroup(
            panelEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(panelEditUser, "card4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(288, 288, 288))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahUserActionPerformed
        nonaktifkanpanel();
        panelTambahUser.setVisible(true);        
    }//GEN-LAST:event_btnTambahUserActionPerformed

    private void btn_BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BatalActionPerformed
        tampilkan();
        clear();
        nonaktifkanpanel();
        panelUser.setVisible(true);
    }//GEN-LAST:event_btn_BatalActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (txt_eusername.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Terlebih Dulu");
        } else {
            nonaktifkanpanel();
            panelEditUser.setVisible(true);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        tampilkan();
        clear();
        nonaktifkanpanel();
        panelUser.setVisible(true);
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        insertUser();       
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tabelUserMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelUserMouseReleased
        if(evt.getClickCount()==1){
            int row = tabelUser.getSelectedRow();
            if (tabelUser.getSelectedRow()!=-1){
                txt_eusername.setText(tabelUser.getModel().getValueAt(row,0).toString());
                txt_epassword.setText(tabelUser.getModel().getValueAt(row,1).toString());
                txt_enama.setText(tabelUser.getModel().getValueAt(row,2).toString());
                String jk = (tabelUser.getModel().getValueAt(row,3).toString());
                if (jk.equals("Laki-Laki")){
                    rb_eLaki.setSelected(true);
                } else {
                    rbePerempuan.setSelected(true);
                }
                txt_etempat.setText(tabelUser.getModel().getValueAt(row,4).toString());
                date_etanggal.setDate(java.sql.Date.valueOf(tabelUser.getValueAt(row, 5).toString()));  
                txt_etelpon.setText(tabelUser.getModel().getValueAt(row,6).toString());
                txt_ealamat.setText(tabelUser.getModel().getValueAt(row,7).toString());
                cbeAkses.setSelectedItem(tabelUser.getModel().getValueAt(row,8).toString());
            } else {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tabelUserMouseReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        updateUser();        
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (txt_eusername.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Yang Ingin Dihapus");
        } else {
            hapusUser();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtcariUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariUserKeyReleased
        cariUser();
    }//GEN-LAST:event_txtcariUserKeyReleased

    private void btnMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuUtamaActionPerformed
        dispose();
    }//GEN-LAST:event_btnMenuUtamaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(kelola_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(kelola_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(kelola_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(kelola_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                kelola_user dialog = new kelola_user(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgjenis_kelamin;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnMenuUtama;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahUser;
    private javax.swing.JButton btn_Batal;
    private javax.swing.JComboBox cbAkses;
    private javax.swing.JComboBox cbeAkses;
    private com.toedter.calendar.JDateChooser date_etanggal;
    private com.toedter.calendar.JDateChooser date_tanggal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelEditUser;
    private javax.swing.JPanel panelTambahUser;
    private javax.swing.JPanel panelUser;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JRadioButton rb_eLaki;
    private javax.swing.JRadioButton rbePerempuan;
    private javax.swing.JTable tabelUser;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextArea txt_ealamat;
    private javax.swing.JTextField txt_enama;
    private javax.swing.JTextField txt_epassword;
    private javax.swing.JTextField txt_etelpon;
    private javax.swing.JTextField txt_etempat;
    private javax.swing.JTextField txt_eusername;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_password;
    private javax.swing.JTextField txt_telpon;
    private javax.swing.JTextField txt_tempat;
    private javax.swing.JTextField txt_username;
    private javax.swing.JTextField txtcariUser;
    // End of variables declaration//GEN-END:variables
}
