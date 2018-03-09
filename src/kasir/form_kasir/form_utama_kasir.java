/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasir.form_kasir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kasir.koneksi;
import kasir.usersesi;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import kasir.form_utama;

/**
 *
 * @author PICO
 */
public class form_utama_kasir extends javax.swing.JFrame {
    Connection con;
    Statement stat;
    ResultSet rs;
    String sql;
    String username = usersesi.getU_username();
    private DefaultTableModel tabelbarang, tabeltransaksi;
    /**
     * Creates new form form_utama_kasir
     */
    public form_utama_kasir() {
        initComponents();        
        koneksi DB = new koneksi();             
        con = DB.getConnection();
        aturtext();
        tampilkan();
        nofakturbaru();
        loadData();
        panelEditDataDiri.setVisible(false);
        txtHapusKodeTransaksi.setVisible(false);
        txtHapusQtyTransaksi.setVisible(false);
        txtHapusStokTersedia.setVisible(false);
    }
    
    private void aturtext(){       
        try{
           sql = "SELECT * from user where username ='" + username + "'";
           Statement st=con.createStatement();
           ResultSet rs=st.executeQuery(sql);
           while(rs.next()){
               labelNama.setText(rs.getString("nama"));
               txt_eusername.setText(rs.getString("username"));
               txt_epassword.setText(rs.getString("password"));
               txt_enama.setText(rs.getString("nama"));
               txt_etempat.setText(rs.getString("tempat_lahir"));
               date_etanggal.setDate(rs.getDate("tanggal_lahir"));
               txt_etelpon.setText(rs.getString("telepon"));
               txt_ealamat.setText(rs.getString("alamat"));
               String jk = rs.getString("jenis_kel");
               if(jk.equals("Laki-Laki")){
                   rb_eLaki.setSelected(true);
               } else {
                   rbePerempuan.setSelected(true);
               }
           } 
        }
       catch(Exception ex){          
       }
    }
    
    private void tampilkan(){
        Object []baris = {"Kode Barang", "Nama Barang", "Stok", "Harga Jual"};
        tabelbarang = new DefaultTableModel(null, baris);
        tabelBarang.setModel(tabelbarang);
        
        try {
            sql = "SELECT * FROM barang order by kode_barang ASC";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String kode = rs.getString("kode_barang");
               String nama = rs.getString("nama_barang");
               String stok = rs.getString("stok");
               String h_jual = rs.getString("harga_jual");
               String[] data = {kode, nama, stok, h_jual};
               tabelbarang.addRow(data);
           } 
        } catch(Exception e){   
            System.out.println(e);
        }
    }    
    
    private void aturstok(){
        String cari = txt_cariBarang.getText();
        
    }
    private void nofakturbaru(){
        try {
            String sql = "SELECT MAX(right(no_faktur,5)) from penjualan";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txt_no_faktur.setText("J000001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1)+1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    
                    for(int a=0;a<6-noLong;a++){
                        no = "0" + no;
                    }
                    txt_no_faktur.setText("J" +no);
                }
            }
            rs.close();
            stmt.close();          
        } catch (Exception e){
            System.out.println("Kesalahan" + e.toString());
        }
    } 
    
    private void updateUser(){
        String  username, password, nama, jenis_kelamin, tempat_lahir, telpon, alamat;
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
        
        if (JOptionPane.showConfirmDialog(null, "Perbarui Data ? \nUsername: " + username , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                String sqlkode = "UPDATE user set password =?, nama=?, jenis_kel=?, tempat_lahir=?, tanggal_lahir=?, telepon=?, alamat=? where username='" 
                        + username + "'";
                PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                p2.setString(1, password);
                p2.setString(2, nama);
                p2.setString(3, jenis_kelamin);
                p2.setString(4, tempat_lahir);
                p2.setDate(5, new java.sql.Date(tanggal_lahir.getTime()));               
                p2.setString(6, telpon);
                p2.setString(7, alamat);
                p2.executeUpdate();     
                JOptionPane.showMessageDialog(null, "Edit User Sukses");
                panelEditDataDiri.setVisible(false);
                panelKasir.setVisible(true);
            } catch(SQLException se){
            }
        } else {            
        }        
    }
    
    private void cariBarang(){
        String cari = txt_namaBarang.getText();
        Object []baris = {"Kode Barang", "Nama Barang", "Stok", "Harga Jual"};
        tabelbarang = new DefaultTableModel(null, baris);
        tabelBarang.setModel(tabelbarang);
        
        try {
             sql="SELECT * from barang where " + "kode_barang like '%"+cari+"%' "               
                   + "OR nama_barang like '%"+cari+"%' " 
                   + "order by nama_barang asc";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String kode = rs.getString("kode_barang");
               String nama = rs.getString("nama_barang");
               String stok = rs.getString("stok");
               String h_jual = rs.getString("harga_jual");
               String[] data = {kode, nama, stok, h_jual};
               tabelbarang.addRow(data);
           } 
        } catch(Exception e){   
            System.out.println(e);
        }
    }
    
    private void Siapkan(){     
        String no_faktur = txt_no_faktur.getText();  
        String kode_barang = txt_kodeBarang.getText();  
        String hargajual_ = txt_hargaJual.getText();  
        String qty = txt_Qty.getText();
        String subtotal = txt_subTotal.getText();
        int stok, hitungqty, kuranginstok;
        stok = Integer.parseInt(txt_stokTersedia.getText());
        hitungqty = Integer.parseInt(qty);
        kuranginstok = stok-hitungqty;
        try {   
            String sql="Insert into pra_penjualan values (?,?,?,?,?)";  
            PreparedStatement p=(PreparedStatement)con.prepareStatement(sql);  
            p.setString(1, no_faktur);
            p.setString(2, kode_barang);
            p.setString(3, hargajual_);
            p.setString(4, qty);
            p.setString(5, subtotal);
            p.executeUpdate();
            p.close();
         
            String sqle ="UPDATE barang set stok=? WHERE kode_barang='" + kode_barang + "'";  
            PreparedStatement pa=(PreparedStatement)con.prepareStatement(sqle);  
            pa.setInt(1, kuranginstok);
            pa.executeUpdate();
            pa.close();
        } catch(SQLException e){ 
            System.out.println(e);  
        }
    }
    
    private void loadData(){
        Object []baris = {"Kode Barang", "Qty", "Sub Total"};
        tabeltransaksi = new DefaultTableModel(null, baris);
        tabelTransaksiBarang.setModel(tabeltransaksi);
        
        try {
            sql = "SELECT * FROM pra_penjualan order by kode_barang ASC";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String kode = rs.getString("kode_barang");
               String qty = rs.getString("qty");
               String subtotal = rs.getString("subtotal");
               String[] data = {kode, qty, subtotal};
               tabeltransaksi.addRow(data);
           } 
        } catch(Exception e){   
            System.out.println(e);
        }
        //menjumlahkan isi colom ke 4 dalam tabel
        int total = 0;
        for (int i =0; i< tabelTransaksiBarang.getRowCount(); i++){
            int amount = Integer.parseInt((String)tabelTransaksiBarang.getValueAt(i, 2));
            total += amount;
        }
        labelTotal.setText(""+total);
    }
        
      
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        labelNama = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelKasir = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_no_faktur = new javax.swing.JTextField();
        date_transaksi = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        txt_cariBarang = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_Qty = new javax.swing.JTextField();
        txt_kodeBarang = new javax.swing.JTextField();
        txt_namaBarang = new javax.swing.JTextField();
        txt_hargaJual = new javax.swing.JTextField();
        txt_stokTersedia = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelTransaksiBarang = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btn_tambahBarang = new javax.swing.JButton();
        btn_hapusBarang = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_subTotal = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCash = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtKembali = new javax.swing.JTextField();
        btnBeli = new javax.swing.JButton();
        btnHitung = new javax.swing.JButton();
        txtHapusKodeTransaksi = new javax.swing.JTextField();
        txtHapusQtyTransaksi = new javax.swing.JTextField();
        txtHapusStokTersedia = new javax.swing.JTextField();
        panelEditDataDiri = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_eusername = new javax.swing.JTextField();
        txt_epassword = new javax.swing.JTextField();
        txt_enama = new javax.swing.JTextField();
        rb_eLaki = new javax.swing.JRadioButton();
        rbePerempuan = new javax.swing.JRadioButton();
        txt_etelpon = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_ealamat = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btn_Batal = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txt_etempat = new javax.swing.JTextField();
        date_etanggal = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        jLabel2.setFont(new java.awt.Font("Broadway", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("KASIR KOPMA");

        jLabel4.setFont(new java.awt.Font("Broadway", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 0));
        jLabel4.setText("STITEK BONTANG");

        jLabel3.setFont(new java.awt.Font("Broadway", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("KS");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Status Online");

        jButton1.setText("EDIT DATA DIRI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        labelNama.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNama.setForeground(new java.awt.Color(0, 255, 51));
        labelNama.setText("Nama");

        jPanel2.setBackground(new java.awt.Color(0, 51, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        jPanel2.setLayout(new java.awt.CardLayout());

        panelKasir.setBackground(new java.awt.Color(0, 51, 153));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setText("No. Faktur");

        jLabel6.setText("Tanggal");

        jLabel7.setText("Barang");

        txt_no_faktur.setEnabled(false);

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelBarang.setPreferredSize(new java.awt.Dimension(300, 300));
        tabelBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelBarangMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabelBarang);

        txt_cariBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_cariBarangKeyReleased(evt);
            }
        });

        jLabel8.setText("Kode Barang");

        jLabel9.setText("Nama Barang");

        jLabel10.setText("Harga Jual (Rp.)");

        jLabel11.setText("Qty");

        jLabel12.setText("Stok Tersedia");

        txt_Qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_QtyActionPerformed(evt);
            }
        });
        txt_Qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_QtyKeyPressed(evt);
            }
        });

        txt_kodeBarang.setEnabled(false);

        txt_namaBarang.setEnabled(false);

        txt_hargaJual.setEnabled(false);

        txt_stokTersedia.setEnabled(false);

        tabelTransaksiBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelTransaksiBarang.setPreferredSize(new java.awt.Dimension(300, 100));
        tabelTransaksiBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelTransaksiBarangMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tabelTransaksiBarang);

        btn_tambahBarang.setText("ADD");
        btn_tambahBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahBarangActionPerformed(evt);
            }
        });
        jPanel4.add(btn_tambahBarang);

        btn_hapusBarang.setText("DEL");
        btn_hapusBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusBarangActionPerformed(evt);
            }
        });
        jPanel4.add(btn_hapusBarang);

        jLabel19.setText("Sub-Total (Rp.)");

        txt_subTotal.setEnabled(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setForeground(new java.awt.Color(255, 51, 51));

        jLabel21.setText("TOTAL (RP.)");

        labelTotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        labelTotal.setText("0");

        jLabel23.setText("CASH  Rp.");

        jLabel24.setText("KEMBALI Rp.");

        btnBeli.setText("BELI");
        btnBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeliActionPerformed(evt);
            }
        });

        btnHitung.setText("HITUNG");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtKembali, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(txtCash, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHitung))
                    .addComponent(btnBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(btnHitung))
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_no_faktur, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(date_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txt_namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_kodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_hargaJual)
                                    .addComponent(txt_stokTersedia, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_cariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_Qty))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtHapusKodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtHapusQtyTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(txtHapusStokTersedia)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(txt_subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_no_faktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(date_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txt_cariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(txt_Qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(txt_subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txt_kodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txt_namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txt_hargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txt_stokTersedia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHapusKodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHapusQtyTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHapusStokTersedia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelKasirLayout = new javax.swing.GroupLayout(panelKasir);
        panelKasir.setLayout(panelKasirLayout);
        panelKasirLayout.setHorizontalGroup(
            panelKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelKasirLayout.setVerticalGroup(
            panelKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(panelKasir, "card2");

        panelEditDataDiri.setBackground(new java.awt.Color(0, 51, 153));

        jPanel8.setBackground(new java.awt.Color(0, 51, 153));

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

        txt_eusername.setEnabled(false);

        rb_eLaki.setBackground(new java.awt.Color(0, 51, 153));
        buttonGroup1.add(rb_eLaki);
        rb_eLaki.setForeground(new java.awt.Color(255, 255, 255));
        rb_eLaki.setText("Laki-Laki");

        rbePerempuan.setBackground(new java.awt.Color(0, 51, 153));
        buttonGroup1.add(rbePerempuan);
        rbePerempuan.setForeground(new java.awt.Color(255, 255, 255));
        rbePerempuan.setText("Perempuan");

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
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(jLabel18)
                                .addGap(25, 25, 25)
                                .addComponent(txt_etelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(txt_epassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(txt_enama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout panelEditDataDiriLayout = new javax.swing.GroupLayout(panelEditDataDiri);
        panelEditDataDiri.setLayout(panelEditDataDiriLayout);
        panelEditDataDiriLayout.setHorizontalGroup(
            panelEditDataDiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 717, Short.MAX_VALUE)
            .addGroup(panelEditDataDiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelEditDataDiriLayout.createSequentialGroup()
                    .addGap(51, 51, 51)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(75, Short.MAX_VALUE)))
        );
        panelEditDataDiriLayout.setVerticalGroup(
            panelEditDataDiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
            .addGroup(panelEditDataDiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelEditDataDiriLayout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(115, Short.MAX_VALUE)))
        );

        jPanel2.add(panelEditDataDiri, "card3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(labelNama)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNama))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        updateUser();
        aturtext();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btn_BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BatalActionPerformed
        panelEditDataDiri.setVisible(false);
        panelKasir.setVisible(true);        
    }//GEN-LAST:event_btn_BatalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        panelEditDataDiri.setVisible(true);
        panelKasir.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabelBarangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMouseReleased
        if(evt.getClickCount()==1){
            int row = tabelBarang.getSelectedRow();
            if (tabelBarang.getSelectedRow()!=-1){
                txt_kodeBarang.setText(tabelBarang.getModel().getValueAt(row,0).toString());
                txt_namaBarang.setText(tabelBarang.getModel().getValueAt(row,1).toString());
                txt_hargaJual.setText(tabelBarang.getModel().getValueAt(row,3).toString());                
                txt_stokTersedia.setText(tabelBarang.getModel().getValueAt(row,2).toString());
                txt_cariBarang.setText(tabelBarang.getModel().getValueAt(row,1).toString());
            } else {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tabelBarangMouseReleased

    private void txt_cariBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariBarangKeyReleased
        cariBarang();
        loadData();
    }//GEN-LAST:event_txt_cariBarangKeyReleased

    private void txt_QtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QtyKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {            
            int qty, harga_jual, subtotal, stok;
            qty = Integer.parseInt(txt_Qty.getText());
            harga_jual = Integer.parseInt(txt_hargaJual.getText());
            stok = Integer.parseInt(txt_stokTersedia.getText());
            subtotal = qty * harga_jual;
            if (qty>stok){
                JOptionPane.showMessageDialog(this, "Maaf Stok Tidak Mencukupi. \nStok Tersedia : " + stok);
            } else if (txt_Qty.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Isikan Stok");
            } else {
                txt_subTotal.setText(""+subtotal);
            }
        }
    }//GEN-LAST:event_txt_QtyKeyPressed

    private void btn_tambahBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahBarangActionPerformed
        if (txt_kodeBarang.getText().equals("") || txt_Qty.getText().equals("") || txt_subTotal.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Lengkapi Form Dulu!");
        } else {
            Siapkan();
            loadData();
            tampilkan();           
            txt_Qty.setText("");
            txt_subTotal.setText("");           
        }
    }//GEN-LAST:event_btn_tambahBarangActionPerformed

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        int cash, harga, kembalian;
        cash = Integer.parseInt(txtCash.getText());
        harga = Integer.parseInt(labelTotal.getText());
        
        kembalian = cash - harga;
        txtKembali.setText(""+kembalian);
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btn_hapusBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusBarangActionPerformed
        String kd_barang = txtHapusKodeTransaksi.getText();
        String qty = txtHapusQtyTransaksi.getText();
        String stok = txt_stokTersedia.getText();
        String cekstok = txtHapusStokTersedia.getText();
        int kuantiti, liatstok, kembalikanstok;
        kuantiti = Integer.parseInt(qty);
        liatstok = Integer.parseInt(cekstok);
        kembalikanstok = kuantiti + liatstok;
        try {
                String sql="DELETE from pra_penjualan where kode_barang='" + kd_barang + "'AND qty='" + qty +"'";
                Statement st = con.createStatement();
                st.executeUpdate(sql);       
                
                String sqle="UPDATE barang set stok=? WHERE kode_barang='" + kd_barang + "'";                
                PreparedStatement p=(PreparedStatement)con.prepareStatement(sqle);          
                p.setInt(1, kembalikanstok);
                p.executeUpdate();  
                p.close();  
                JOptionPane.showMessageDialog(null, "Hapus Sukses");
                tampilkan();
                loadData();
                txtHapusKodeTransaksi.setText("");
                txtHapusQtyTransaksi.setText("");
                txtHapusStokTersedia.setText("");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Hapus  Gagal");
                System.out.println(e);
            }
    }//GEN-LAST:event_btn_hapusBarangActionPerformed

    private void txt_QtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_QtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_QtyActionPerformed

    private void tabelTransaksiBarangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiBarangMouseReleased
        if(evt.getClickCount()==1){
            int row = tabelTransaksiBarang.getSelectedRow();
            if (tabelTransaksiBarang.getSelectedRow()!=-1){
                txtHapusKodeTransaksi.setText(tabelTransaksiBarang.getModel().getValueAt(row,0).toString());
                txtHapusQtyTransaksi.setText(tabelTransaksiBarang.getModel().getValueAt(row,1).toString());
                
                String cari = txtHapusKodeTransaksi.getText();
                try {
                    String sqle = "SELECT * from barang where kode_barang ='" + cari + "'";
                    Statement st=con.createStatement();
                    ResultSet rs=st.executeQuery(sqle);
                    while(rs.next()){
                        txtHapusStokTersedia.setText(rs.getString("stok"));              
                    }  
                } catch(Exception ex){          
                }
            } else {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tabelTransaksiBarangMouseReleased

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
         if (JOptionPane.showConfirmDialog(null, "Yakin Ingin Keluar ?\nPeringatan :" , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {       
                    new form_utama().show();
                    dispose();
                   /*try {
                        String sql="DELETE from pra_penjualan";
                        Statement st = con.createStatement();
                        st.executeUpdate(sql);                                      
                    } catch(Exception e){               
                        System.out.println(e);
                    }*/
         } else {            
         }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeliActionPerformed
        String  no_faktur, cash, kembalian, total;java.util.Date tanggal_transaksi;
        no_faktur = txt_no_faktur.getText();  
        kembalian = txtKembali.getText();
        cash = txtCash.getText();                     
        total = labelTotal.getText();
        tanggal_transaksi = (java.util.Date) this.date_transaksi.getDate();
                try {                    
                    String sqlkode = "insert into penjualan values (?,?,?,?,?)";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1, no_faktur);
                    p2.setDate(2, new java.sql.Date(tanggal_transaksi.getTime()));
                    p2.setString(3, total);
                    p2.setString(4, cash);
                    p2.setString(5, kembalian);                       
                    p2.executeUpdate();
                    nofakturbaru();   
                    JOptionPane.showMessageDialog(null, "Beli Barang Sukses");
                    String sql="DELETE from pra_penjualan";
                    Statement st = con.createStatement();
                    st.executeUpdate(sql);
                    txtCash.setText("");
                    txtKembali.setText("");
                    loadData();
                } catch (Exception e) {                      
                    JOptionPane.showMessageDialog(null, "Beli Gagal");
                    System.out.println(e);
                } 
    }//GEN-LAST:event_btnBeliActionPerformed

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
            java.util.logging.Logger.getLogger(form_utama_kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(form_utama_kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(form_utama_kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(form_utama_kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new form_utama_kasir().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeli;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btn_Batal;
    private javax.swing.JButton btn_hapusBarang;
    private javax.swing.JButton btn_tambahBarang;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser date_etanggal;
    private com.toedter.calendar.JDateChooser date_transaksi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JPanel panelEditDataDiri;
    private javax.swing.JPanel panelKasir;
    private javax.swing.JRadioButton rb_eLaki;
    private javax.swing.JRadioButton rbePerempuan;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTable tabelTransaksiBarang;
    private javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtHapusKodeTransaksi;
    private javax.swing.JTextField txtHapusQtyTransaksi;
    private javax.swing.JTextField txtHapusStokTersedia;
    private javax.swing.JTextField txtKembali;
    private javax.swing.JTextField txt_Qty;
    private javax.swing.JTextField txt_cariBarang;
    private javax.swing.JTextArea txt_ealamat;
    private javax.swing.JTextField txt_enama;
    private javax.swing.JTextField txt_epassword;
    private javax.swing.JTextField txt_etelpon;
    private javax.swing.JTextField txt_etempat;
    private javax.swing.JTextField txt_eusername;
    private javax.swing.JTextField txt_hargaJual;
    private javax.swing.JTextField txt_kodeBarang;
    private javax.swing.JTextField txt_namaBarang;
    private javax.swing.JTextField txt_no_faktur;
    private javax.swing.JTextField txt_stokTersedia;
    private javax.swing.JTextField txt_subTotal;
    // End of variables declaration//GEN-END:variables
}
