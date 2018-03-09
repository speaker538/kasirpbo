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
public class kelola_barang extends javax.swing.JDialog {
    Connection con;
    String sql;
    Statement stat;
    ResultSet rs;
    private DefaultTableModel tabelbarang;
    /**
     * Creates new form kelola_barang
     */
    public kelola_barang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        koneksi DB = new koneksi();
        con = DB.getConnection();    
        tampilkan();
        nonaktifkanpanel();
        panelBarang.setVisible(true);
        setVisible(true);
    }
    
    private void nonaktifkanpanel(){
        panelTambahBarang.setVisible(false);
        panelBarang.setVisible(false);
        panelEditBarang.setVisible(false);
    }
    
    private void clear(){
        txt_kdbarang.setText(""); //-- Komponen untuk tambah barang
        txt_nmbarang.setText("");
        txt_stokbarang.setText("");
        txt_hargajual.setText("");
        txt_hargapokok.setText("");
        txt_diskon.setText("");
        
        txt_ekdbarang.setText(""); //-- Komponen untuk edit barang
        txt_enmbarang.setText("");
        txt_estok.setText("");
        txt_ehargajual.setText("");
        txt_ehargapokok.setText("");
        txt_ediskon.setText("");
    }
    
    private void tampilkan(){
        Object []baris = {"Kode Barang", "Nama Barang", "Stok", "Harga Pokok", "Harga Jual", "Diskon"};
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
               String h_pokok = rs.getString("harga_pokok");
               String h_jual = rs.getString("harga_jual");
               String diskon = rs.getString("diskon");
               String[] data = {kode, nama, stok, h_pokok, h_jual, diskon};
               tabelbarang.addRow(data);
           } 
        } catch(Exception e){            
        }
    }
    
    private void kodebarangbaru(){
        try {
            String sql = "SELECT MAX(right(kode_barang,5)) from barang";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txt_kdbarang.setText("BRG-000001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1)+1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    
                    for(int a=0;a<6-noLong;a++){
                        no = "0" + no;
                    }
                    txt_kdbarang.setText("BRG-" +no);
                }
            }
            rs.close();
            stmt.close();          
        } catch (Exception e){
            System.out.println("Kesalahan" + e.toString());
        }
    } 
    
     private void insertBarang(){
        String  kd_barang, barang, stok, h_pokok, h_jual, diskon;
        kd_barang = txt_kdbarang.getText();  
        barang = txt_nmbarang.getText();
        stok = txt_stokbarang.getText();               
        h_pokok = txt_hargapokok.getText();
        h_jual = txt_hargajual.getText();
        diskon = txt_diskon.getText();
        if (barang.equals("") || stok.equals("") || diskon.equals("") 
                 || h_jual.equals("") || h_pokok.equals("")) {
            JOptionPane.showMessageDialog(null, "Mohon Lengkapi Semua Data");
        } else {

            if (JOptionPane.showConfirmDialog(null, "Tambah Barang ? \nKode Barang : " + kd_barang + "\nNama Barang : "
                + barang , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {                    
                    String sqlkode = "insert into barang values (?,?,?,?,?,?)";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1, kd_barang);
                    p2.setString(2, barang);
                    p2.setString(3, stok);
                    p2.setString(4, h_pokok);
                    p2.setString(5, h_jual);
                    p2.setString(6, diskon);                        
                    p2.executeUpdate();
                    clear();
                    if (JOptionPane.showConfirmDialog(null, "Tambah Sukses, Tambah Lagi?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        kodebarangbaru();
                    } else {
                        tampilkan();
                        nonaktifkanpanel();
                        panelBarang.setVisible(true);
                    }                       
                } catch (Exception e) {                      
                    JOptionPane.showMessageDialog(null, "Tambah Barang Gagal");
                    System.out.println(e);
                } 
            }
        }  
    }
    
    private void updateBarang(){ 
        String  kd_barang, barang, stok, h_pokok, h_jual, diskon;
        kd_barang = txt_ekdbarang.getText();  
        barang = txt_enmbarang.getText();
        stok = txt_estok.getText();               
        h_pokok = txt_ehargapokok.getText();
        h_jual = txt_ehargajual.getText();
        diskon = txt_ediskon.getText();
        if (barang.equals("") || stok.equals("") || diskon.equals("") 
                 || h_jual.equals("") || h_pokok.equals("")) {
            JOptionPane.showMessageDialog(null, "Mohon Lengkapi Semua Data");
        } else {
            if (JOptionPane.showConfirmDialog(null, "Perbarui Data ? \nKode Barang : " + kd_barang, "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                           
                try {                    
                    String sqlkode = "UPDATE barang set nama_barang =?, stok=?, harga_pokok=?, harga_jual=?, diskon=? where kode_barang='" 
                        + kd_barang + "'";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1, barang);
                    p2.setString(2, stok);
                    p2.setString(3, h_pokok);
                    p2.setString(4, h_jual);
                    p2.setString(5, diskon);                        
                    p2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Edit Barang Sukses");
                    tampilkan();
                    nonaktifkanpanel();
                    panelBarang.setVisible(true);
                } catch(SQLException se){
                     JOptionPane.showMessageDialog(null, "Edit Barang Gagal");
                }
            } else {            
            }        
        }
    }
    
    private void hapusBarang(){
        int baris = tabelBarang.getSelectedRow();
        String kd_barang = tabelbarang.getValueAt(baris, 0).toString();    
        String nm_barang = tabelbarang.getValueAt(baris, 1).toString();      
        int ok = JOptionPane.showConfirmDialog(null,"Hapus Data ? \nNama Barang : " + nm_barang,"Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(ok==0){
            try{
                String sql="DELETE from barang where kode_barang='" + kd_barang + "'";
                Statement st = con.createStatement();
                st.executeUpdate(sql);
                
                JOptionPane.showMessageDialog(null, "Hapus Barang Sukses");
                tampilkan();
                clear();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Hapus Barang Gagal");
            }
        }
    } 
    
    private void cariBarang(){
        String cari = txtcariBarang.getText();

        Object []baris = {"Kode Barang", "Nama Barang", "Stok", "Harga Pokok", "Harga Jual", "Diskon"};
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
               String h_pokok = rs.getString("harga_pokok");
               String h_jual = rs.getString("harga_jual");
               String diskon = rs.getString("diskon");
               String[] data = {kode, nama, stok, h_pokok, h_jual, diskon};
               tabelbarang.addRow(data);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelBarang = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnTambahBarang = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        txtcariBarang = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnMenuUtama = new javax.swing.JButton();
        panelTambahBarang = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txt_kdbarang = new javax.swing.JTextField();
        txt_nmbarang = new javax.swing.JTextField();
        txt_hargapokok = new javax.swing.JTextField();
        txt_diskon = new javax.swing.JTextField();
        txt_hargajual = new javax.swing.JTextField();
        txt_stokbarang = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        panelEditBarang = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txt_ekdbarang = new javax.swing.JTextField();
        txt_enmbarang = new javax.swing.JTextField();
        txt_ehargapokok = new javax.swing.JTextField();
        txt_ediskon = new javax.swing.JTextField();
        txt_ehargajual = new javax.swing.JTextField();
        txt_estok = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA BARANG");

        jPanel2.setBackground(new java.awt.Color(0, 51, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel2.setLayout(new java.awt.CardLayout());

        panelBarang.setBackground(new java.awt.Color(0, 51, 153));
        panelBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel4.setBackground(new java.awt.Color(0, 51, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        btnTambahBarang.setText("TAMBAH BARANG");
        btnTambahBarang.setPreferredSize(new java.awt.Dimension(130, 40));
        btnTambahBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBarangActionPerformed(evt);
            }
        });
        jPanel4.add(btnTambahBarang);

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
        tabelBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelBarangMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabelBarang);

        txtcariBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariBarangKeyReleased(evt);
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

        javax.swing.GroupLayout panelBarangLayout = new javax.swing.GroupLayout(panelBarang);
        panelBarang.setLayout(panelBarangLayout);
        panelBarangLayout.setHorizontalGroup(
            panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBarangLayout.createSequentialGroup()
                .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBarangLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE))
                    .addGroup(panelBarangLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMenuUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBarangLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtcariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panelBarangLayout.setVerticalGroup(
            panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBarangLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                .addComponent(btnMenuUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(panelBarang, "card2");

        panelTambahBarang.setBackground(new java.awt.Color(0, 51, 153));
        panelTambahBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("TAMBAH BARANG");

        jPanel10.setBackground(new java.awt.Color(0, 51, 153));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Kode Barang");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Nama Barang");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Harga Pokok");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Diskon");

        txt_kdbarang.setEnabled(false);

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
        jLabel29.setText("Harga Jual");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Rp.");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Rp.");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Rp.");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Stok");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_diskon, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(txt_hargapokok)
                            .addComponent(txt_hargajual)))
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_kdbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nmbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_stokbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_kdbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txt_nmbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txt_stokbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel30)
                    .addComponent(txt_hargapokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelTambahBarangLayout = new javax.swing.GroupLayout(panelTambahBarang);
        panelTambahBarang.setLayout(panelTambahBarangLayout);
        panelTambahBarangLayout.setHorizontalGroup(
            panelTambahBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahBarangLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel21)
                .addContainerGap(461, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTambahBarangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );
        panelTambahBarangLayout.setVerticalGroup(
            panelTambahBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(panelTambahBarang, "card3");

        panelEditBarang.setBackground(new java.awt.Color(0, 51, 153));
        panelEditBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelEditBarang.setForeground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("EDIT BARANG");

        jPanel12.setBackground(new java.awt.Color(0, 51, 153));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Kode Barang");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Nama Barang");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Harga Pokok");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Diskon");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Harga Jual");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Rp.");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Rp.");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Rp.");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Stok");

        txt_ekdbarang.setEnabled(false);

        btnSimpan.setText("SIMPAN");
        btnSimpan.setPreferredSize(new java.awt.Dimension(90, 40));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanel13.add(btnSimpan);

        btnBatal1.setText("BATAL");
        btnBatal1.setPreferredSize(new java.awt.Dimension(90, 40));
        btnBatal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatal1ActionPerformed(evt);
            }
        });
        jPanel13.add(btnBatal1);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ediskon, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(txt_ehargapokok)
                            .addComponent(txt_ehargajual)))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ekdbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_enmbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_estok, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txt_ekdbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txt_enmbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txt_estok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel38)
                    .addComponent(txt_ehargapokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_ehargajual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txt_ediskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelEditBarangLayout = new javax.swing.GroupLayout(panelEditBarang);
        panelEditBarang.setLayout(panelEditBarangLayout);
        panelEditBarangLayout.setHorizontalGroup(
            panelEditBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditBarangLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel25)
                .addContainerGap(491, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditBarangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
        );
        panelEditBarangLayout.setVerticalGroup(
            panelEditBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(panelEditBarang, "card4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(jLabel1)
                .addContainerGap(265, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(460, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(29, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBarangActionPerformed
        nonaktifkanpanel();
        panelTambahBarang.setVisible(true);
        kodebarangbaru();
    }//GEN-LAST:event_btnTambahBarangActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (txt_ekdbarang.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Terlebih Dulu");
        } else {
            nonaktifkanpanel();
            panelEditBarang.setVisible(true);           
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (txt_ekdbarang.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Yang Ingin Dihapus");
        } else {
            hapusBarang();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tabelBarangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMouseReleased
        if(evt.getClickCount()==1){
            int row = tabelBarang.getSelectedRow();
            if (tabelBarang.getSelectedRow()!=-1){
                txt_ekdbarang.setText(tabelBarang.getModel().getValueAt(row,0).toString());
                txt_enmbarang.setText(tabelBarang.getModel().getValueAt(row,1).toString());
                txt_estok.setText(tabelBarang.getModel().getValueAt(row,2).toString());                
                txt_ehargapokok.setText(tabelBarang.getModel().getValueAt(row,3).toString());
                txt_ehargajual.setText(tabelBarang.getModel().getValueAt(row,4).toString());
                txt_ediskon.setText(tabelBarang.getModel().getValueAt(row,5).toString());
            } else {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tabelBarangMouseReleased

    private void txtcariBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariBarangKeyReleased
        cariBarang();
    }//GEN-LAST:event_txtcariBarangKeyReleased

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        insertBarang();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        tampilkan();
        clear();
        nonaktifkanpanel();
        panelBarang.setVisible(true);
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        updateBarang();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatal1ActionPerformed
        tampilkan();
        clear();
        nonaktifkanpanel();
        panelBarang.setVisible(true);
    }//GEN-LAST:event_btnBatal1ActionPerformed

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
            java.util.logging.Logger.getLogger(kelola_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(kelola_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(kelola_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(kelola_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                kelola_barang dialog = new kelola_barang(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBatal1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnMenuUtama;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahBarang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBarang;
    private javax.swing.JPanel panelEditBarang;
    private javax.swing.JPanel panelTambahBarang;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTextField txt_diskon;
    private javax.swing.JTextField txt_ediskon;
    private javax.swing.JTextField txt_ehargajual;
    private javax.swing.JTextField txt_ehargapokok;
    private javax.swing.JTextField txt_ekdbarang;
    private javax.swing.JTextField txt_enmbarang;
    private javax.swing.JTextField txt_estok;
    private javax.swing.JTextField txt_hargajual;
    private javax.swing.JTextField txt_hargapokok;
    private javax.swing.JTextField txt_kdbarang;
    private javax.swing.JTextField txt_nmbarang;
    private javax.swing.JTextField txt_stokbarang;
    private javax.swing.JTextField txtcariBarang;
    // End of variables declaration//GEN-END:variables
}
