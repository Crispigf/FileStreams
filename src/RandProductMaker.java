import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RandProductMaker extends JFrame {

    private JTextField nameField, descriptionField, idField, costField, recordCountField;
    private RandomAccessFile randomAccessFile;
    private int recordCount;

    public RandProductMaker() {
        setTitle("Random Product Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        add(nameLabel);
        add(nameField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(50);
        add(descriptionLabel);
        add(descriptionField);

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(6);
        add(idLabel);
        add(idField);

        JLabel costLabel = new JLabel("Cost:");
        costField = new JTextField(10);
        add(costLabel);
        add(costField);

        JLabel recordCountLabel = new JLabel("Record Count:");
        recordCountField = new JTextField(10);
        recordCountField.setEditable(false);
        add(recordCountLabel);
        add(recordCountField);

        JButton addButton = new JButton("Add");
        add(addButton);



        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductRecord();
            }
        });

        // Open/create the random access file
        try {
            randomAccessFile = new RandomAccessFile("product_data.dat", "rw");
            recordCount = (int) (randomAccessFile.length() / Product.getRecordSize());
            updateRecordCountField();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addProductRecord() {
        try {
            String name = padField(nameField.getText(), 35);
            String description = padField(descriptionField.getText(), 75);
            String id = padField(idField.getText(), 6);
            double cost = Double.parseDouble(costField.getText());

            Product product = new Product(id, name, description, cost);

            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(product.toBytes());

            recordCount++;
            updateRecordCountField();

            // Clear input fields
            nameField.setText("");
            descriptionField.setText("");
            idField.setText("");
            costField.setText("");
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product record.");
        }
    }

    private void updateRecordCountField() {
        recordCountField.setText(String.valueOf(recordCount));
    }

    private String padField(String data, int length) {
        if (data.length() > length) {
            return data.substring(0, length);
        } else {
            StringBuilder paddedData = new StringBuilder(data);
            for (int i = data.length(); i < length; i++) {
                paddedData.append(" ");
            }
            return paddedData.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductMaker();
            }
        });
    }
}
