import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_KEY = "44162e2c5ad2ba2b2c4ac1fb";  // Replace with your API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("CurrencyMate");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create labels and components
        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel toLabel = new JLabel("To Currency:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Dropdowns and text field
        String[] currencies = {"USD", "EUR", "INR", "JPY", "GBP", "CAD"};
        JComboBox<String> fromCurrency = new JComboBox<>(currencies);
        JComboBox<String> toCurrency = new JComboBox<>(currencies);
        JTextField amountField = new JTextField(10);

        // Button with style
        JButton convertButton = new JButton("Convert");
        convertButton.setBackground(Color.GREEN);
        convertButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Add ActionListener for button click
        convertButton.addActionListener((ActionEvent e) -> {
            try {
                String from = (String) fromCurrency.getSelectedItem();
                String to = (String) toCurrency.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                double converted = convertCurrency(from, to, amount);
                resultLabel.setText("Converted Amount: " + converted);
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
            }
        });

        // Create JPanel for organizing components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // Padding between components
        panel.add(fromLabel);
        panel.add(fromCurrency);
        panel.add(toLabel);
        panel.add(toCurrency);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(new JLabel("")); // Empty space
        panel.add(convertButton);
        panel.add(resultLabel);

        // Add panel to frame and make it visible
        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to convert currency using API
    public static double convertCurrency(String from, String to, double amount) {
        try {
            // Build the API URL
            URL url = new URL(API_URL + from);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the JSON response from the API
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
            JSONObject conversionRates = jsonObject.getJSONObject("conversion_rates");
            double rate = conversionRates.getDouble(to);

            // Return the converted amount
            return Math.round((amount * rate) * 100.0) / 100.0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }
}
