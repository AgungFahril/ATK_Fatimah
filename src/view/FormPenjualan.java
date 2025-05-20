package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import main.Koneksi;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import main.Session;

public class FormPenjualan extends javax.swing.JPanel {

    public FormPenjualan() {
        initComponents();
        inisialisasiForm();
        
    }

    private void inisialisasiForm() {
        text_kasir.setText(Session.getNama());
        label_user.setText("Login sebagai: " + Session.getRole());

        resetForm();
        styleButtons();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "Barcode", "Nama", "Harga", "Stok", "Satuan", "Jumlah", "Total"
        });
        table.setModel(model);
    }

    private void styleButtons() {
        styleButton(btn_simpan, "SIMPAN");
        styleButton(btn_batal, "BATAL");
        styleButton(btn_tambah, "TAMBAH");
        styleButton(btn_edit, "EDIT");
        styleButton(btn_hapus, "HAPUS");
    }

    private void styleButton(JButton button, String text) {
        button.setText(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    
    
    
    private void cetakStruk(String idPenjualan, String tanggal, String kasir, int total, int diskon, int bayar, int kembalian) {
    System.out.println("======= STRUK PENJUALAN =======");
    System.out.println("ID Penjualan: " + idPenjualan);
    System.out.println("Tanggal     : " + tanggal);
    System.out.println("Kasir       : " + kasir);
    System.out.println("Total       : " + total);
    System.out.println("Diskon      : " + diskon);
    System.out.println("Bayar       : " + bayar);
    System.out.println("Kembalian   : " + kembalian);
    System.out.println("Detail barang:");
    
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        String nama = model.getValueAt(i, 1).toString();
        String jumlah = model.getValueAt(i, 5).toString();
        String totalBarang = model.getValueAt(i, 6).toString();
        System.out.println("- " + nama + " x" + jumlah + " = " + totalBarang);
    }
    System.out.println("================================");
}

    
    
    
    
    private void kurangiStok(Connection con, String barcode, int jumlah) throws SQLException {
    if (jumlah <= 0) {
        throw new IllegalArgumentException("Jumlah pengurangan stok harus lebih dari 0");
    }

    String sql = "UPDATE barang SET stok = stok - ? WHERE barcode = ? AND stok >= ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, jumlah);
        ps.setString(2, barcode);
        ps.setInt(3, jumlah);
        int updated = ps.executeUpdate();

        if (updated == 0) {
            throw new SQLException("Stok tidak mencukupi atau barcode tidak ditemukan: " + barcode);
        } else {
            System.out.println("Stok dikurangi untuk barcode: " + barcode + ", sebanyak: " + jumlah);
        }
    }
}



    
    

    private void kosongkanInputBarang() {
        text_barcode.setText("");
        text_nama.setText("");
        text_satuan.setText("");
        text_harga.setText("");
        text_jumlah.setText("");
        text_stok.setText("");
        text_barcode.requestFocus();
    }

     private void tambahAtauUpdateTabel() {
        try {
            String barcode = text_barcode.getText().trim();
            String nama = text_nama.getText();
            String satuan = text_satuan.getText();
            double harga = Double.parseDouble(text_harga.getText().trim());
            int jumlah = Integer.parseInt(text_jumlah.getText().trim());
            int stok = Integer.parseInt(text_stok.getText().trim());

            if (jumlah > stok) {
                JOptionPane.showMessageDialog(this, "Jumlah melebihi stok tersedia!");
                return;
            }

            double total = harga * jumlah;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            boolean barangAda = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (barcode.equals(model.getValueAt(i, 0))) {
                    int jumlahLama = Integer.parseInt(model.getValueAt(i, 5).toString());
                    int jumlahBaru = jumlahLama + jumlah;

                    if (jumlahBaru > stok) {
                        JOptionPane.showMessageDialog(this, "Jumlah total melebihi stok!");
                        return;
                    }

                    double totalBaru = harga * jumlahBaru;
                    model.setValueAt(jumlahBaru, i, 5);
                    model.setValueAt(totalBaru, i, 6);
                    barangAda = true;
                    break;
                }
            }

            if (!barangAda) {
                model.addRow(new Object[]{barcode, nama, harga, stok, satuan, jumlah, total});
            }

            kosongkanInputBarang();
            hitungTotalHarga();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input harga atau jumlah tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     private void hitungTotalHarga() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        double subtotal = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                subtotal += Double.parseDouble(model.getValueAt(i, 6).toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        text_subtotal.setText(String.valueOf((int) subtotal));
        int diskon = 0;
        try {
            diskon = Integer.parseInt(text_diskon.getText().trim());
        } catch (NumberFormatException e) {
            diskon = 0;
        }
        int totalBersih = (int) subtotal - diskon;
        text_total.setText(String.valueOf(totalBersih));
    }


     private void cariBarang() {
        String barcode = text_barcode.getText().trim();
        if (barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Barcode tidak boleh kosong!");
            return;
        }

        try (Connection con = Koneksi.getConnection()) {
            String sql = "SELECT * FROM barang WHERE barcode = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, barcode);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        text_nama.setText(rs.getString("Nama_barang"));
                        text_satuan.setText(rs.getString("Satuan"));
                        text_harga.setText(rs.getString("Harga"));
                        text_stok.setText(rs.getString("Stok"));
                        text_tanggal.setDate(new java.util.Date());
                        text_jumlah.setText("1");
                        text_jumlah.selectAll();
                        text_jumlah.requestFocus();
                    } else {
                        JOptionPane.showMessageDialog(this, "Barcode tidak ditemukan");
                        kosongkanInputBarang();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari barang", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        text_subtotal.setText("0");
        text_total.setText("0");
        text_diskon.setText("0");
        text_bayar.setText("0");
        text_kembalian.setText("0");
        kosongkanInputBarang();
    }


    
    private int ambilIdBarangDariBarcode(Connection con, String barcode) throws SQLException {
    String sql = "SELECT id_barang FROM barang WHERE barcode = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, barcode);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_barang");
            } else {
                System.err.println("ID barang tidak ditemukan untuk barcode: " + barcode);
            }
        }
    }
    return -1; // Tidak ditemukan
}


    
   private void simpanTransaksi() {
    if (table.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Belum ada barang yang dibeli!");
        return;
    }

    try {
        // Ambil nilai dari komponen
        String tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd").format(text_tanggal.getDate());
        String kasir = text_kasir.getText();
        int total = Integer.parseInt(text_total.getText().trim());
        int diskon = Integer.parseInt(text_diskon.getText().trim());
        int bayar = Integer.parseInt(text_bayar.getText().trim());
        int kembalian = bayar - (total - diskon);

        if (bayar < (total - diskon)) {
            JOptionPane.showMessageDialog(this, "Jumlah bayar kurang dari total setelah diskon!");
            return;
        }

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false);

            // Ambil ID user dari nama kasir
            int idUser = 0;
            String sqlUser = "SELECT id_user FROM users WHERE nama = ?";
            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setString(1, kasir);
                ResultSet rs = psUser.executeQuery();
                if (rs.next()) {
                    idUser = rs.getInt("id_user");
                } else {
                    JOptionPane.showMessageDialog(this, "User tidak ditemukan!");
                    return;
                }
            }

            // Simpan ke tabel penjualan
            String sqlPenjualan = "INSERT INTO penjualan (id_user, tanggal, total, diskon, bayar, kembalian) VALUES (?, ?, ?, ?, ?, ?)";
            int idPenjualan = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlPenjualan, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idUser);
                ps.setDate(2, java.sql.Date.valueOf(tanggal));
                ps.setInt(3, total);
                ps.setInt(4, diskon);
                ps.setInt(5, bayar);
                ps.setInt(6, kembalian);
                ps.executeUpdate();

                ResultSet rsKey = ps.getGeneratedKeys();
                if (rsKey.next()) {
                    idPenjualan = rsKey.getInt(1);
                }
            }

            // Simpan ke penjualanrinci dan kurangi stok
            String sqlRinci = "INSERT INTO penjualanrinci (Jumlah_Jual, Satuan, Harga_Satuan, Total, Id_Penjualan, Id_Barang, barcode) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psRinci = conn.prepareStatement(sqlRinci)) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    String barcode = table.getValueAt(i, 0).toString();
                    String nama = table.getValueAt(i, 1).toString();
                    double harga = Double.parseDouble(table.getValueAt(i, 2).toString());
                    int stok = Integer.parseInt(table.getValueAt(i, 3).toString());
                    String satuan = table.getValueAt(i, 4).toString();
                    int jumlah = Integer.parseInt(table.getValueAt(i, 5).toString());
                    double totalItem = Double.parseDouble(table.getValueAt(i, 6).toString());

                    int idBarang = ambilIdBarangDariBarcode(conn, barcode);
                    if (idBarang == -1) continue; // skip jika tidak ditemukan

                    psRinci.setInt(1, jumlah);
                    psRinci.setString(2, satuan);
                    psRinci.setDouble(3, harga);
                    psRinci.setDouble(4, totalItem);
                    psRinci.setInt(5, idPenjualan);
                    psRinci.setInt(6, idBarang);
                    psRinci.setString(7, barcode);
                    psRinci.addBatch();

                    kurangiStok(conn, barcode, jumlah);
                }
                psRinci.executeBatch();
            }

            conn.commit();

            cetakStruk(String.valueOf(idPenjualan), tanggal, kasir, total, diskon, bayar, kembalian);
            resetForm();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
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

        jPanel1 = new main.gradasiwarna();
        jLabel3 = new javax.swing.JLabel();
        label_user = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        text_barcode = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        text_kasir = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        text_satuan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        text_nama = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        text_harga = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        text_stok = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        text_jumlah = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        text_diskon = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        text_total = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        text_subtotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        text_kembalian = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        text_bayar = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        text_tanggal = new com.toedter.calendar.JDateChooser();
        btn_cari = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));

        jLabel3.setBackground(new java.awt.Color(0, 51, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("08 - 06 - 2025");

        label_user.setBackground(new java.awt.Color(0, 51, 255));
        label_user.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        label_user.setForeground(new java.awt.Color(255, 255, 255));
        label_user.setText("Login Sebagai: Kasir");

        jLabel6.setBackground(new java.awt.Color(0, 51, 255));
        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Data Penjualan");

        btn_simpan.setText("simpan");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_batal.setText("batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(0, 51, 255));
        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Barcode");

        text_barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_barcodeKeyPressed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(0, 51, 255));
        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Kasir");

        text_kasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_kasirActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(0, 51, 255));
        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Satuan");

        jLabel10.setBackground(new java.awt.Color(0, 51, 255));
        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nama");

        jLabel11.setBackground(new java.awt.Color(0, 51, 255));
        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Harga");

        jLabel12.setBackground(new java.awt.Color(0, 51, 255));
        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Stok");

        jLabel13.setBackground(new java.awt.Color(0, 51, 255));
        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Jumlah");

        text_jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_jumlahKeyPressed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(table);

        text_diskon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                text_diskonKeyReleased(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(0, 51, 255));
        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Diskon");

        jLabel15.setBackground(new java.awt.Color(0, 51, 255));
        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Total   :");

        jLabel16.setBackground(new java.awt.Color(0, 51, 255));
        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Sub Total");

        text_kembalian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                text_kembalianKeyReleased(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(0, 51, 255));
        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Kembalian");

        text_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                text_bayarKeyReleased(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(0, 51, 255));
        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Bayar");

        btn_tambah.setText("tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_edit.setText("edit");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setText("hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        jLabel19.setBackground(new java.awt.Color(0, 51, 255));
        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Tanggal");

        btn_cari.setText("cari");
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(text_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(text_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(text_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(text_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label_user)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(text_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(187, 396, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8)
                                                    .addComponent(text_kasir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(33, 33, 33)
                                .addComponent(text_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(text_total, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addGap(18, 18, 18)
                                    .addComponent(text_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(label_user)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_simpan)
                                    .addComponent(btn_batal))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(text_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_kasir, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah)
                    .addComponent(btn_edit)
                    .addComponent(btn_hapus)
                    .addComponent(btn_cari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(text_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(text_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        simpanTransaksi();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_batalActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
            tambahAtauUpdateTabel();

    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void text_barcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_barcodeKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String barcode = text_barcode.getText().trim();
        if (barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan barcode terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cariBarang(); // Sudah menangani input & hasilnya
    }
    }//GEN-LAST:event_text_barcodeKeyPressed

    private void text_kembalianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_kembalianKeyReleased
        try {
            int bayar = Integer.parseInt(text_bayar.getText());
            int total = Integer.parseInt(text_total.getText());
            int kembalian = bayar - total;
            text_kembalian.setText(String.valueOf(kembalian));
        } catch (NumberFormatException e) {
            text_kembalian.setText("0");
        }
    }//GEN-LAST:event_text_kembalianKeyReleased

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cariBarang();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void text_diskonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_diskonKeyReleased
        try {
            int totalKotor = Integer.parseInt(text_subtotal.getText().trim());
            int diskon = Integer.parseInt(text_diskon.getText().trim());
            if (diskon > totalKotor) {
                JOptionPane.showMessageDialog(this, "Diskon tidak boleh lebih dari total harga!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                text_diskon.setText("0");
                return;
            }
            int totalBersih = totalKotor - diskon;
            text_total.setText(String.valueOf(totalBersih));
        } catch (NumberFormatException e) {
            text_total.setText("0");
        }
    }//GEN-LAST:event_text_diskonKeyReleased

    private void text_bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_bayarKeyReleased
        try {
            int bayar = Integer.parseInt(text_bayar.getText().trim());
            int totalBersih = Integer.parseInt(text_total.getText().trim());
            int kembalian = bayar - totalBersih;
            text_kembalian.setText(String.valueOf(kembalian));
        } catch (NumberFormatException e) {
            text_kembalian.setText("0");
        }
    }//GEN-LAST:event_text_bayarKeyReleased

    private void text_jumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_jumlahKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        try {
            int jumlah = Integer.parseInt(text_jumlah.getText());
            int stok = Integer.parseInt(text_stok.getText());
            if (jumlah > stok) {
                JOptionPane.showMessageDialog(this, "Jumlah melebihi stok tersedia!");
                return;
            }
            tambahAtauUpdateTabel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka yang valid");
        }
    }
    }//GEN-LAST:event_text_jumlahKeyPressed

    private void text_kasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_kasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_kasirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_user;
    private javax.swing.JTable table;
    private javax.swing.JTextField text_barcode;
    private javax.swing.JTextField text_bayar;
    private javax.swing.JTextField text_diskon;
    private javax.swing.JTextField text_harga;
    private javax.swing.JTextField text_jumlah;
    private javax.swing.JTextField text_kasir;
    private javax.swing.JTextField text_kembalian;
    private javax.swing.JTextField text_nama;
    private javax.swing.JTextField text_satuan;
    private javax.swing.JTextField text_stok;
    private javax.swing.JTextField text_subtotal;
    private com.toedter.calendar.JDateChooser text_tanggal;
    private javax.swing.JTextField text_total;
    // End of variables declaration//GEN-END:variables
}
