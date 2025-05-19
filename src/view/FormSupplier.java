package view;

import Form.FormTambahSupplier;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.Koneksi;
import java.sql.*;

public class FormSupplier extends javax.swing.JPanel {

    public FormSupplier() {
        initComponents();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nama", "Telepon", "Alamat", "Pemilik"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Semua sel tidak dapat diedit langsung
            }
        };
        tbl_supplier.setModel(model);

        loadDataSupplier();

        JTableHeader header = tbl_supplier.getTableHeader();
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

        tbl_supplier.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tbl_supplier.getTableHeader().setOpaque(false);
        tbl_supplier.getTableHeader().setBackground(new Color(0, 102, 204));
        tbl_supplier.getTableHeader().setForeground(Color.WHITE);

        tbl_supplier.setRowHeight(30);
        tbl_supplier.setShowGrid(true);
        tbl_supplier.setGridColor(Color.LIGHT_GRAY);
        tbl_supplier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbl_supplier.setSelectionBackground(new Color(204, 229, 255));
        tbl_supplier.setSelectionForeground(Color.BLACK);
        tbl_supplier.setShowVerticalLines(true);

        styleButton(btn_tambah, "TAMBAH");
        styleButton(btn_edit, "EDIT");
        styleButton(btn_hapus, "HAPUS");
    }

    private void styleButton(javax.swing.JButton button, String text) {
        button.setText(text);
        button.setBackground(new java.awt.Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new java.awt.Font("Serif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new java.awt.Color(100, 149, 237));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new java.awt.Color(70, 130, 180));
            }
        });
    }

    private void loadDataSupplier() {
        DefaultTableModel model = (DefaultTableModel) tbl_supplier.getModel();
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getConnection();
            String sql = "SELECT * FROM supplier";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Id_Supplier"),
                    rs.getString("Nama"),
                    rs.getString("Nomor_Telepon"),
                    rs.getString("Alamat"),
                    rs.getString("Nama_Pemilik")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }

    private void updateSupplier() {
        int selectedRow = tbl_supplier.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
            return;
        }

        int idSupplier = (int) tbl_supplier.getValueAt(selectedRow, 0);
        String nama = (String) tbl_supplier.getValueAt(selectedRow, 1);
        String telepon = String.valueOf(tbl_supplier.getValueAt(selectedRow, 2));
        String alamat = (String) tbl_supplier.getValueAt(selectedRow, 3);
        String pemilik = (String) tbl_supplier.getValueAt(selectedRow, 4);

        String newNama = JOptionPane.showInputDialog(this, "Nama:", nama);
        String newTelepon = JOptionPane.showInputDialog(this, "Telepon:", telepon);
        String newAlamat = JOptionPane.showInputDialog(this, "Alamat:", alamat);
        String newPemilik = JOptionPane.showInputDialog(this, "Pemilik:", pemilik);

        try {
            Connection conn = Koneksi.getConnection();
            String sql = "UPDATE supplier SET Nama=?, Nomor_Telepon=?, Alamat=?, Nama_Pemilik=? WHERE Id_Supplier=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newNama);
            pst.setString(2, newTelepon);
            pst.setString(3, newAlamat);
            pst.setString(4, newPemilik);
            pst.setInt(5, idSupplier);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            loadDataSupplier();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update data: " + e.getMessage());
        }
    }

    private void deleteSupplier() {
        int selectedRow = tbl_supplier.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int idSupplier = (int) tbl_supplier.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = Koneksi.getConnection();
                String sql = "DELETE FROM supplier WHERE Id_Supplier=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, idSupplier);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                loadDataSupplier();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal hapus data: " + e.getMessage());
            }
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
        jLabel1 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_supplier = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data Supplier");

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

        tbl_supplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_supplier);

        jLabel3.setBackground(new java.awt.Color(0, 51, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("08 - 06 - 2025");

        jLabel5.setBackground(new java.awt.Color(0, 51, 255));
        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("User :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah)
                    .addComponent(btn_edit)
                    .addComponent(btn_hapus))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        updateSupplier();
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        deleteSupplier();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        FormTambahSupplier form = new FormTambahSupplier((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        loadDataSupplier(); // refresh data setelah form ditutup
    }//GEN-LAST:event_btn_tambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_supplier;
    // End of variables declaration//GEN-END:variables
}
