package view;

import Form.FormTambahBarang;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;

import Form.FormTambahBarang;
import java.awt.Component;

import main.Koneksi;
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class FormInventori extends javax.swing.JPanel {

    private Connection conn;

    public FormInventori() {
        initComponents();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nama", "Kategori", "Satuan", "Harga", "Stok", "Barcode", "ID Supplier"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Semua sel tidak dapat diedit langsung
            }
        };
        table_barang.setModel(model);
        
        
        
        JTableHeader header = table_barang.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
                label.setOpaque(true);
                label.setBackground(new Color(0, 123, 255));
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 0, 10, 0)));
                return label;
            }
        });
        
        table_barang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table_barang.getTableHeader().setOpaque(false);
        table_barang.getTableHeader().setBackground(new Color(0, 102, 204));
        table_barang.getTableHeader().setForeground(Color.WHITE);

        table_barang.setRowHeight(30);
        table_barang.setShowGrid(true);
        table_barang.setGridColor(Color.LIGHT_GRAY);
        table_barang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table_barang.setSelectionBackground(new Color(204, 229, 255));
        table_barang.setSelectionForeground(Color.BLACK);
        table_barang.setShowVerticalLines(true);
        
        
        
        
        

        tampilkanBarang();

        // Style tombol
        btn_tambah.setText("TAMBAH");
        btn_tambah.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_tambah.setForeground(Color.WHITE);
        btn_tambah.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_tambah.setFocusPainted(false);
        btn_tambah.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_tambah.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_tambah.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_tambah.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_tambah.setBackground(new java.awt.Color(70, 130, 180));
            }
        });

        // Style tombol
        btn_edit.setText("EDIT");
        btn_edit.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_edit.setForeground(Color.WHITE);
        btn_edit.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_edit.setFocusPainted(false);
        btn_edit.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_edit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_edit.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_edit.setBackground(new java.awt.Color(70, 130, 180));
            }
        });

        // Style tombol
        btn_hapus.setText("HAPUS");
        btn_hapus.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_hapus.setForeground(Color.WHITE);
        btn_hapus.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_hapus.setFocusPainted(false);
        btn_hapus.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_hapus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_hapus.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_hapus.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_hapus.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
        
        // Style tombol
        btn_cetakbarcode.setText("CETAK BARCODE");
        btn_cetakbarcode.setBackground(new java.awt.Color(70, 130, 180)); // warna biru steel blue
        btn_cetakbarcode.setForeground(Color.WHITE);
        btn_cetakbarcode.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        btn_cetakbarcode.setFocusPainted(false);
        btn_cetakbarcode.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn_cetakbarcode.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn_cetakbarcode.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_cetakbarcode.setBackground(new java.awt.Color(100, 149, 237)); // Cornflower Blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_cetakbarcode.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
    }

    public void tampilkanBarang() {
    DefaultTableModel model = (DefaultTableModel) table_barang.getModel();
    model.setRowCount(0); // reset isi tabel

    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/atk", "root", "");
        String sql = "SELECT b.Id_barang, b.Nama_barang, b.Kategori, b.Satuan, b.Harga, b.Stok, b.barcode, s.nama AS nama_supplier "
                   + "FROM barang b "
                   + "LEFT JOIN supplier s ON b.Id_Supplier = s.id_supplier";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("Id_barang"),      // Gantikan kode_barang dengan Id_barang
                rs.getString("Nama_barang"),
                rs.getString("Kategori"),
                rs.getString("Satuan"),
                rs.getString("Harga"),
                rs.getString("Stok"),
                rs.getString("barcode"),
                rs.getString("nama_supplier")
            });
        }

        rs.close();
        st.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data barang: " + e.getMessage());
    }
}




    
    private void updatebarang() {
    int selectedRow = table_barang.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
        return;
    }

    // Ambil data dari tabel sebagai String, lalu parse sesuai tipe data
    String idStr = table_barang.getValueAt(selectedRow, 0).toString();
    String namabarang = table_barang.getValueAt(selectedRow, 1).toString();
    String kategori = table_barang.getValueAt(selectedRow, 2).toString();
    String satuan = table_barang.getValueAt(selectedRow, 3).toString();
    String hargaStr = table_barang.getValueAt(selectedRow, 4).toString();
    String stokStr = table_barang.getValueAt(selectedRow, 5).toString();

    // Konversi string ke tipe yang dibutuhkan
    int idbarang = Integer.parseInt(idStr);
    double harga = Double.parseDouble(hargaStr);
    int stok = Integer.parseInt(stokStr);

    // ComboBox options
    String[] kategoriOptions = {"Alat Tulis", "Kertas & Buku", "Perekat", "Alat Ukur"};
    String[] satuanOptions = {"Pcs", "Pak", "Lusin", "Box", "Buah"};

    // Form panel
    javax.swing.JTextField txtNama = new javax.swing.JTextField(namabarang);
    javax.swing.JComboBox<String> cbKategori = new javax.swing.JComboBox<>(kategoriOptions);
    cbKategori.setSelectedItem(kategori);

    javax.swing.JComboBox<String> cbSatuan = new javax.swing.JComboBox<>(satuanOptions);
    cbSatuan.setSelectedItem(satuan);

    javax.swing.JTextField txtHarga = new javax.swing.JTextField(String.valueOf(harga));
    javax.swing.JTextField txtStok = new javax.swing.JTextField(String.valueOf(stok));

    javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
    panel.add(new javax.swing.JLabel("Nama Barang:"));
    panel.add(txtNama);
    panel.add(new javax.swing.JLabel("Kategori:"));
    panel.add(cbKategori);
    panel.add(new javax.swing.JLabel("Satuan:"));
    panel.add(cbSatuan);
    panel.add(new javax.swing.JLabel("Harga:"));
    panel.add(txtHarga);
    panel.add(new javax.swing.JLabel("Stok:"));
    panel.add(txtStok);

    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Data Barang",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        try {
            String newNama = txtNama.getText();
            String newKategori = cbKategori.getSelectedItem().toString();
            String newSatuan = cbSatuan.getSelectedItem().toString();
            double newHarga = Double.parseDouble(txtHarga.getText());
            int newStok = Integer.parseInt(txtStok.getText());

            Connection conn = Koneksi.getConnection();
            String sql = "UPDATE barang SET Nama_barang=?, Kategori=?, Satuan=?, Harga=?, Stok=? WHERE Id_barang=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newNama);
            pst.setString(2, newKategori);
            pst.setString(3, newSatuan);
            pst.setDouble(4, newHarga);
            pst.setInt(5, newStok);
            pst.setInt(6, idbarang);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            tampilkanBarang();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update data: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka.");
        }
    }
}



    
    private void cetakBarcode() {
    int selectedRow = table_barang.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dicetak barcode-nya.");
        return;
    }

    try {
        String kode = table_barang.getValueAt(selectedRow, 6).toString(); // Ambil dari kolom 'barcode'
        String nama = table_barang.getValueAt(selectedRow, 1).toString(); // Ambil nama barang

        int barcodeWidth = 300;
        int barcodeHeight = 100;
        int labelHeight = 25;
        int textHeight = 20;
        int padding = 20;

        int imageHeight = labelHeight + barcodeHeight + textHeight + padding;

        BufferedImage resultImage = new BufferedImage(barcodeWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultImage.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, barcodeWidth, imageHeight);

        // Tambahkan nama barang di atas barcode
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fmLabel = g.getFontMetrics();
        int labelWidth = fmLabel.stringWidth(nama);
        int labelX = (barcodeWidth - labelWidth) / 2;
        int labelY = fmLabel.getAscent() + 10;
        g.drawString(nama, labelX, labelY);

        // Generate barcode
        BitMatrix matrix = new MultiFormatWriter().encode(kode, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight);
        BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(matrix);

        int barcodeY = labelY + 10;
        g.drawImage(barcodeImage, 0, barcodeY, null);

        // Tambahkan kode di bawah barcode
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fmCode = g.getFontMetrics();
        int codeWidth = fmCode.stringWidth(kode);
        int codeX = (barcodeWidth - codeWidth) / 2;
        int codeY = barcodeY + barcodeHeight + fmCode.getAscent() + 5;
        g.drawString(kode, codeX, codeY);

        g.dispose();

        String folder = "C:/barcode";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File outputFile = new File(dir, "barcode_" + kode + ".png");
        ImageIO.write(resultImage, "PNG", outputFile);

        JOptionPane.showMessageDialog(this, "Barcode berhasil disimpan di:\n" + outputFile.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mencetak barcode: " + e.getMessage());
    }
}


    
    
    
    
    
//    private void deleteSupplier() {
//        int selectedRow = tbl_supplier.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
//            return;
//        }
//
//        int idSupplier = (int) tbl_supplier.getValueAt(selectedRow, 0);
//        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (confirm == JOptionPane.YES_OPTION) {
//            try {
//                Connection conn = Koneksi.getConnection();
//                String sql = "DELETE FROM supplier WHERE Id_Supplier=?";
//                PreparedStatement pst = conn.prepareStatement(sql);
//                pst.setInt(1, idSupplier);
//                pst.executeUpdate();
//                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
//                loadDataSupplier();
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(this, "Gagal hapus data: " + e.getMessage());
//            }
//        }
//    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new main.gradasiwarna();
        jLabel1 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_barang = new javax.swing.JTable();
        btn_cetakbarcode = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(755, 509));
        setMinimumSize(new java.awt.Dimension(755, 509));
        setPreferredSize(new java.awt.Dimension(755, 509));
        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(755, 509));
        jPanel1.setMinimumSize(new java.awt.Dimension(755, 509));
        jPanel1.setPreferredSize(new java.awt.Dimension(755, 509));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data Barang");

        btn_tambah.setText("TAMBAH");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_edit.setText("EDIT");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setText("HAPUS");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(0, 51, 255));
        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("User :");

        jLabel3.setBackground(new java.awt.Color(0, 51, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("08 - 06 - 2025");

        table_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table_barang);

        btn_cetakbarcode.setText("CETAK BARCODE");
        btn_cetakbarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakbarcodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cetakbarcode))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_tambah)
                            .addComponent(btn_edit)
                            .addComponent(btn_hapus))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cetakbarcode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        FormTambahBarang form = new FormTambahBarang((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        
        
        tampilkanBarang();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        updatebarang();
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
         int selectedRow = table_barang.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
        return;
    }

    String idStr = table_barang.getValueAt(selectedRow, 0).toString();
    String stokStr = table_barang.getValueAt(selectedRow, 5).toString();

    try {
        int stok = Integer.parseInt(stokStr);
        if (stok > 0) {
            JOptionPane.showMessageDialog(this, "Data tidak dapat dihapus karena stok masih tersedia.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Stok tidak valid.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            int idbarang = Integer.parseInt(idStr);

            Connection conn = Koneksi.getConnection();
            String sql = "DELETE FROM barang WHERE Id_barang = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idbarang);
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                tampilkanBarang();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal dihapus.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID barang tidak valid.");
        }
    }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_cetakbarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakbarcodeActionPerformed
      cetakBarcode();
    }//GEN-LAST:event_btn_cetakbarcodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cetakbarcode;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_barang;
    // End of variables declaration//GEN-END:variables
}
