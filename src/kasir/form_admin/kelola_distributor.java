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
public class kelola_distributor extends javax.swing.JDialog {
    Connection con;
    String sql;
    Statement stat;
    ResultSet rs;
    private DefaultTableModel tabeldistributor;
    /**
     * Creates new form kelola_distributor
     */
    public kelola_distributor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        koneksi DB = new koneksi();
        con = DB.getConnection(); 
        tampilkan();
        nonaktifkanpanel();
        setVisible(true);
    }

    private void nonaktifkanpanel(){
        panelDistributor.setVisible(true);
        panelTambahDistributor.setVisible(false);
        panelEditDistributor.setVisible(false);     
        panelAturButton.setVisible(false);
    }
    
    private void siapkaninsertupdate(boolean st){
        txtCariDistributor.setEnabled(st);
        tabelDistributor.setEnabled(st);
        btnTambahDistributor.setEnabled(st);
        btnEditDistributor.setEnabled(st);
        btnHapusDistributor.setEnabled(st);
    }
    
    private void clear(){
        txt_kddistributor.setText(""); //-- Komponen untuk tambah disb
        txt_namadistributor.setText("");
        txt_alamat.setText("");
        txt_telepon.setText("");
        
        txt_ekddistributor.setText(""); //-- Komponen untuk edit disb
        txt_enamadistributor.setText("");
        txt_ealamat.setText("");
        txt_etelepon.setText("");
        
    }
    
    private void tampilkan(){
        Object []baris = {"Kode Distributor", "Nama Distributor", "Telpon", "Alamat"};
        tabeldistributor = new DefaultTableModel(null, baris);
        tabelDistributor.setModel(tabeldistributor);
        
        try {
            sql = "SELECT * FROM distributor order by kode_distributor ASC";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
               String kode = rs.getString("kode_distributor");
               String nama = rs.getString("nama");
               String telepon = rs.getString("telepon");
               String alamat = rs.getString("alamat");    
               String[] data = {kode, nama, telepon, alamat};
               tabeldistributor.addRow(data);
           } 
        } catch(Exception e){            
        }
    }
    
    private void kodedistributorbaru(){
        try {
            String sql = "SELECT MAX(right(kode_distributor,5)) from distributor";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txt_kddistributor.setText("DSB-000001");
                } else {
                    rs.last();
                    int auto_id = rs.getInt(1)+1;
                    String no = String.valueOf(auto_id);
                    int noLong = no.length();
                    
                    for(int a=0;a<6-noLong;a++){
                        no = "0" + no;
                    }
                     txt_kddistributor.setText("DSB-" +no);
                }
            }
            rs.close();
            stmt.close();          
        } catch (Exception e){
            System.out.println("Kesalahan" + e.toString());
        }
    } 
    
    private void insertDistributor(){
        String  kd_dsb, nama_dsb, telepon, alamat;
        kd_dsb = txt_kddistributor.getText();  
        nama_dsb = txt_namadistributor.getText();
        telepon = txt_telepon.getText();               
        alamat = txt_alamat.getText();
       
        if (nama_dsb.equals("") || alamat.equals("") || telepon.equals("")) {
            JOptionPane.showMessageDialog(null, "Mohon Lengkapi Semua Data");
        } else {

            if (JOptionPane.showConfirmDialog(null, "Tambah Distributor ? \nKode Distributor : " + kd_dsb + "\nNama Distributor : "
                + nama_dsb , "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {                    
                    String sqlkode = "insert into distributor values (?,?,?,?)";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1, kd_dsb);
                    p2.setString(2, nama_dsb);
                    p2.setString(3, telepon);
                    p2.setString(4, alamat);                      
                    p2.executeUpdate();
                    clear();
                    if (JOptionPane.showConfirmDialog(null, "Tambah Sukses, Tambah Lagi?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        kodedistributorbaru();
                        tampilkan();
                    } else {
                        tampilkan();
                        nonaktifkanpanel();
                        siapkaninsertupdate(true);
                    }                       
                } catch (Exception e) {                      
                    JOptionPane.showMessageDialog(null, "Tambah Distributor Gagal");
                    System.out.println(e);
                } 
            }
        }  
    }
    
    private void updateDistributor(){ 
        String  kd_dsb, nama_dsb, telepon, alamat;
        kd_dsb = txt_ekddistributor.getText();  
        nama_dsb = txt_enamadistributor.getText();
        telepon = txt_etelepon.getText();               
        alamat = txt_ealamat.getText();
        if (nama_dsb.equals("") || alamat.equals("") || telepon.equals("")) {
            JOptionPane.showMessageDialog(null, "Mohon Lengkapi Semua Data");
        } else {
            if (JOptionPane.showConfirmDialog(null, "Perbarui Data ? \nKode Distributor : " + kd_dsb, "Konfirmasi", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                           
                try {                    
                    String sqlkode = "UPDATE distributor set nama =?, telepon=?, alamat=? where kode_distributor='" 
                        + kd_dsb + "'";
                    PreparedStatement p2=(PreparedStatement) con.prepareStatement(sqlkode);
                    p2.setString(1, nama_dsb);
                    p2.setString(2, telepon);
                    p2.setString(3, alamat);                        
                    p2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Edit Distributor Sukses");
                    tampilkan();
                    nonaktifkanpanel();
                    siapkaninsertupdate(true);
                } catch(SQLException se){
                    JOptionPane.showMessageDialog(null, "Edit Distributor Gagal");
                }
            } else {            
            }        
        }
    }
    
    private void hapusDistributor(){
        int baris = tabelDistributor.getSelectedRow();
        String kd_dsb = tabeldistributor.getValueAt(baris, 0).toString();    
        String nm_dsb = tabeldistributor.getValueAt(baris, 1).toString();      
        int ok = JOptionPane.showConfirmDialog(null,"Hapus Data ? \nNama Distributor : " + nm_dsb,"Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(ok==0){
            try{
                String sql="DELETE from distributor where kode_distributor='" + kd_dsb + "'";
                Statement st = con.createStatement();
                st.executeUpdate(sql);
                
                JOptionPane.showMessageDialog(null, "Hapus Distributor Sukses");
                tampilkan();
                clear();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Hapus Distributor Gagal");
            }
        }
    }
    
    private void cariDistributor(){
         String cari = txtCariDistributor.getText();

        Object []baris = {"Kode Distributor", "Nama Distributor", "Telpon", "Alamat"};
        tabeldistributor = new DefaultTableModel(null, baris);
        tabelDistributor.setModel(tabeldistributor);
        
        try {
            sql="SELECT * from distributor where " + "kode_distributor like '%"+cari+"%' "               
                   + "OR nama like '%"+cari+"%' " 
                   + "OR alamat like '%"+cari+"%' "
                   + "OR telepon like '%"+cari+"%' " 
                   + "order by kode_distributor asc";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
             while(rs.next()){
               String kode = rs.getString("kode_distributor");
               String nama = rs.getString("nama");
               String telepon = rs.getString("telepon");
               String alamat = rs.getString("alamat");    
               String[] data = {kode, nama, telepon, alamat};
               tabeldistributor.addRow(data);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelDistributor = new javax.swing.JTable();
        txtCariDistributor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        panelDistributor = new javax.swing.JPanel();
        panelTambahDistributor = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_kddistributor = new javax.swing.JTextField();
        txt_namadistributor = new javax.swing.JTextField();
        txt_telepon = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        panelEditDistributor = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_ekddistributor = new javax.swing.JTextField();
        txt_enamadistributor = new javax.swing.JTextField();
        txt_etelepon = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_ealamat = new javax.swing.JTextArea();
        panelButtonAksi = new javax.swing.JPanel();
        btnTambahDistributor = new javax.swing.JButton();
        btnEditDistributor = new javax.swing.JButton();
        btnHapusDistributor = new javax.swing.JButton();
        panelAturButton = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA DISTRIBUTOR");

        tabelDistributor.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabelDistributorMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabelDistributor);

        txtCariDistributor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariDistributorKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cari");

        panel.setBackground(new java.awt.Color(0, 51, 153));
        panel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        panel.setLayout(new java.awt.CardLayout());

        panelDistributor.setBackground(new java.awt.Color(0, 51, 153));

        javax.swing.GroupLayout panelDistributorLayout = new javax.swing.GroupLayout(panelDistributor);
        panelDistributor.setLayout(panelDistributorLayout);
        panelDistributorLayout.setHorizontalGroup(
            panelDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        panelDistributorLayout.setVerticalGroup(
            panelDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        panel.add(panelDistributor, "card2");

        panelTambahDistributor.setBackground(new java.awt.Color(0, 51, 153));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TAMBAH DISTRIBUTOR");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Kode Distributor");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Telepon");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Alamat");

        txt_kddistributor.setEnabled(false);

        txt_alamat.setColumns(20);
        txt_alamat.setRows(5);
        txt_alamat.setPreferredSize(new java.awt.Dimension(130, 60));
        jScrollPane2.setViewportView(txt_alamat);

        javax.swing.GroupLayout panelTambahDistributorLayout = new javax.swing.GroupLayout(panelTambahDistributor);
        panelTambahDistributor.setLayout(panelTambahDistributorLayout);
        panelTambahDistributorLayout.setHorizontalGroup(
            panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                        .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telepon, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                                .addComponent(txt_kddistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102)
                                .addComponent(jLabel7))
                            .addComponent(txt_namadistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTambahDistributorLayout.setVerticalGroup(
            panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTambahDistributorLayout.createSequentialGroup()
                        .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(txt_kddistributor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_namadistributor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTambahDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_telepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panel.add(panelTambahDistributor, "card2");

        panelEditDistributor.setBackground(new java.awt.Color(0, 51, 153));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("EDIT DISTRIBUTOR");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Kode Distributor");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nama");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Telepon");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Alamat");

        txt_ekddistributor.setEnabled(false);

        txt_ealamat.setColumns(20);
        txt_ealamat.setRows(5);
        txt_ealamat.setPreferredSize(new java.awt.Dimension(130, 60));
        jScrollPane3.setViewportView(txt_ealamat);

        javax.swing.GroupLayout panelEditDistributorLayout = new javax.swing.GroupLayout(panelEditDistributor);
        panelEditDistributor.setLayout(panelEditDistributorLayout);
        panelEditDistributorLayout.setHorizontalGroup(
            panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditDistributorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditDistributorLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelEditDistributorLayout.createSequentialGroup()
                        .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_etelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelEditDistributorLayout.createSequentialGroup()
                                .addComponent(txt_ekddistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102)
                                .addComponent(jLabel12))
                            .addComponent(txt_enamadistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelEditDistributorLayout.setVerticalGroup(
            panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditDistributorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditDistributorLayout.createSequentialGroup()
                        .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12)
                            .addComponent(txt_ekddistributor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txt_enamadistributor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelEditDistributorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_etelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panel.add(panelEditDistributor, "card2");

        panelButtonAksi.setBackground(new java.awt.Color(0, 51, 153));
        panelButtonAksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btnTambahDistributor.setText("TAMBAH");
        btnTambahDistributor.setPreferredSize(new java.awt.Dimension(100, 35));
        btnTambahDistributor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDistributorActionPerformed(evt);
            }
        });
        panelButtonAksi.add(btnTambahDistributor);

        btnEditDistributor.setText("EDIT");
        btnEditDistributor.setPreferredSize(new java.awt.Dimension(100, 35));
        btnEditDistributor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDistributorActionPerformed(evt);
            }
        });
        panelButtonAksi.add(btnEditDistributor);

        btnHapusDistributor.setText("HAPUS");
        btnHapusDistributor.setPreferredSize(new java.awt.Dimension(100, 35));
        btnHapusDistributor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusDistributorActionPerformed(evt);
            }
        });
        panelButtonAksi.add(btnHapusDistributor);

        panelAturButton.setBackground(new java.awt.Color(0, 51, 153));
        panelAturButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btnTambah.setText("SIMPAN");
        btnTambah.setPreferredSize(new java.awt.Dimension(80, 35));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        panelAturButton.add(btnTambah);

        btnBatal.setText("BATAL");
        btnBatal.setPreferredSize(new java.awt.Dimension(80, 35));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        panelAturButton.add(btnBatal);

        jButton1.setText("MENU UTAMA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(188, 188, 188)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelButtonAksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelAturButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCariDistributor, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCariDistributor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelButtonAksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelAturButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
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

    private void btnTambahDistributorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDistributorActionPerformed
        panelTambahDistributor.setVisible(true);
        panelDistributor.setVisible(false);
        panelAturButton.setVisible(true);
        btnTambah.setText("TAMBAH");
        kodedistributorbaru();
        siapkaninsertupdate(false);
    }//GEN-LAST:event_btnTambahDistributorActionPerformed

    private void btnEditDistributorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDistributorActionPerformed
        if (txt_ekddistributor.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Terlebih Dulu");
        } else {
            panelEditDistributor.setVisible(true);
            panelDistributor.setVisible(false);
            panelAturButton.setVisible(true);
            btnTambah.setText("SIMPAN");
            siapkaninsertupdate(false);    
        }       
    }//GEN-LAST:event_btnEditDistributorActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        nonaktifkanpanel();
        clear();
        siapkaninsertupdate(true);
        tampilkan();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (btnTambah.getText().equals("TAMBAH")){
            insertDistributor();
        } else if (btnTambah.getText().equals("SIMPAN")){
            updateDistributor();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariDistributorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariDistributorKeyReleased
        cariDistributor();
    }//GEN-LAST:event_txtCariDistributorKeyReleased

    private void btnHapusDistributorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDistributorActionPerformed
        if (txt_ekddistributor.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Pilih Data Yang Ingin Dihapus");
        } else {
            hapusDistributor();
        }
    }//GEN-LAST:event_btnHapusDistributorActionPerformed

    private void tabelDistributorMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDistributorMouseReleased
        if(evt.getClickCount()==1){
            int row = tabelDistributor.getSelectedRow();
            if (tabelDistributor.getSelectedRow()!=-1){
                txt_ekddistributor.setText(tabelDistributor.getModel().getValueAt(row,0).toString());
                txt_enamadistributor.setText(tabelDistributor.getModel().getValueAt(row,1).toString());
                txt_etelepon.setText(tabelDistributor.getModel().getValueAt(row,2).toString());
                txt_ealamat.setText(tabelDistributor.getModel().getValueAt(row,3).toString());
            } else {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tabelDistributorMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(kelola_distributor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(kelola_distributor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(kelola_distributor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(kelola_distributor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                kelola_distributor dialog = new kelola_distributor(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEditDistributor;
    private javax.swing.JButton btnHapusDistributor;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahDistributor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panelAturButton;
    private javax.swing.JPanel panelButtonAksi;
    private javax.swing.JPanel panelDistributor;
    private javax.swing.JPanel panelEditDistributor;
    private javax.swing.JPanel panelTambahDistributor;
    private javax.swing.JTable tabelDistributor;
    private javax.swing.JTextField txtCariDistributor;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextArea txt_ealamat;
    private javax.swing.JTextField txt_ekddistributor;
    private javax.swing.JTextField txt_enamadistributor;
    private javax.swing.JTextField txt_etelepon;
    private javax.swing.JTextField txt_kddistributor;
    private javax.swing.JTextField txt_namadistributor;
    private javax.swing.JTextField txt_telepon;
    // End of variables declaration//GEN-END:variables
}
