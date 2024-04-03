package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;
import java.util.Map;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class DrugReviewPanel extends javax.swing.JFrame {
    private Connection con = null;
    private PreparedStatement pre = null;
    private ResultSet res = null;
    private String selectedDrug = "";
    private DBconnect connection = null;
    private SentimentAnalyzer sentimentAnalyzer = null;

    public DrugReviewPanel() {
        initComponents();
        con = Connect.connect();
        connection = new DBconnect();
        sentimentAnalyzer = new SentimentAnalyzer();
        populateDrugComboBox();
        enableDrugListEvent();
        drug_list();
    }

    private void drug_list() {
        String sql = "select NAME from drugs";
        try {
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();
            drug_review1.setModel(DbUtils.resultSetToTableModel(res));
            enableDrugListEvent();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 2);
        }
    }
    
    private void populateDrugComboBox() {
        String sql = "select NAME from drugs";
        try {
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Select Drug");
            while (res.next()) {
                model.addElement(res.getString("NAME"));
            }
            drugComboBox.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 2);
        }
    }

    // Function to insert review into MongoDB
    public void insertReview(String drugName, String review) {
        // Analyze sentiment of the review
        String reviewType = sentimentAnalyzer.analyzeSentiment(review);

        // Insert review and sentiment into MongoDB
        connection.insertReview(drugName, review, reviewType);
    }

    // Function to fetch reviews for a given drug name from MongoDB
    public void fetchAndDisplayReviews(String drugName) {
        List<String> reviews = connection.fetchReviews(drugName);

        // Display reviews in the JTable
        if (reviews != null && !reviews.isEmpty()) {
            String[][] data = new String[reviews.size()][1];
            for (int i = 0; i < reviews.size(); i++) {
                String reviewType = sentimentAnalyzer.analyzeSentiment(reviews.get(i));
                data[i][0] = reviewType + " feedback ->  " + reviews.get(i);
            }
            drug_review1.setModel(new javax.swing.table.DefaultTableModel(
                    data,
                    new String[]{"Review"}
            ));
            disableDrugListEvent();
        } else {
            // If there are no reviews, clear the table
            drug_review1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Review"}
            ));
            JOptionPane.showMessageDialog(null, "No reviews found for " + drugName, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void enableDrugListEvent() {
        drug_review1.setEnabled(true);
        drug_review1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drug_reviewMouseClicked(evt);
            }
        });
    }

    private void disableDrugListEvent() {
        drug_review1.setEnabled(false);
        drug_review1.removeMouseListener(drug_review1.getMouseListeners()[0]);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        drug_review1 = new javax.swing.JTable();
        reviewField = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        drugComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Drug Review");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Drug Review");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(274, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(273, 273, 273))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));

        drug_review1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        drug_review1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Drug", "Review", "Sentiment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(drug_review1);

        reviewField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        submitButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        backButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        drugComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(drugComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reviewField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(backButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(drugComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reviewField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(backButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
    }// </editor-fold>                        

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        selectedDrug = (String) drugComboBox.getSelectedItem();
        if (selectedDrug.equals("Select Drug")) {
            JOptionPane.showMessageDialog(null, "Please select a drug.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String review = reviewField.getText().trim();
        if (review.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a review.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        insertReview(selectedDrug, review);
        reviewField.setText("");
        fetchAndDisplayReviews(selectedDrug);
        if (!selectedDrug.equals("")) {
            this.dispose();
        }
    }                                             

    private void drug_reviewMouseClicked(java.awt.event.MouseEvent evt) {                                         
        int row = drug_review1.getSelectedRow();
        selectedDrug = drug_review1.getModel().getValueAt(row, 0).toString();
        fetchAndDisplayReviews(selectedDrug);
        drugComboBox.setSelectedItem(selectedDrug);
    }                                        

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Reset the drug list view
        drug_list();
        enableDrugListEvent();
    }                                           

    // Variables declaration - do not modify                     
    private javax.swing.JTable drug_review1;
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox<String> drugComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField reviewField;
    private javax.swing.JButton submitButton;
    // End of variables declaration                   
}
