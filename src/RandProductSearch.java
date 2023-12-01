import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RandProductSearch extends JFrame {

    private JTextField searchField;
    private JTextArea displayArea;
    private RandomAccessFile randomAccessFile;

    public RandProductSearch() {
        setTitle("Product Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("Enter partial product name:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });

        try {
            randomAccessFile = new RandomAccessFile("product_data.dat", "r");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening product data file.");
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void searchProducts() {
        String partialName = searchField.getText().trim();
        displayArea.setText("");

        if (partialName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a partial product name.");
            return;
        }

        try {
            randomAccessFile.seek(0);

            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                String name = readFixedLengthString(randomAccessFile, 35);
                String description = readFixedLengthString(randomAccessFile, 75);
                String id = readFixedLengthString(randomAccessFile, 6);
                double cost = randomAccessFile.readDouble();

                if (name.trim().toLowerCase().contains(partialName.toLowerCase())) {
                    displayArea.append("Name: " + name.trim() + "\n");
                    displayArea.append("Description: " + description.trim() + "\n");
                    displayArea.append("ID: " + id.trim() + "\n");
                    displayArea.append("Cost: " + cost + "\n\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching products.");
        }
    }

    private String readFixedLengthString(RandomAccessFile file, int length) throws IOException {
        byte[] buffer = new byte[length];
        file.readFully(buffer);
        return new String(buffer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductSearch();
            }
        });
    }
}
