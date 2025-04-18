import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class currX extends JFrame {

    static class CurrencyItem {
        String code;
        String iconPath;

        CurrencyItem(String code, String iconPath) {
            this.code = code;
            this.iconPath = iconPath;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public currX() {
        setTitle("currX");
        setIconImage(new ImageIcon("logo.png").getImage());
        setSize(650, 550); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load and darken background image
        ImageIcon bgIcon = new ImageIcon("bg.jpg");
        Image bgImage = bgIcon.getImage();
        ImagePanel backgroundPanel = new ImagePanel(bgImage);

        // Layout for transparent panel on top
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);  // spacing between rows & fields
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 110;   
        gbc.ipady = 18;   

        JLabel heading = new JLabel("currX", SwingConstants.CENTER);
        heading.setForeground(new Color(0, 255, 127));
        heading.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(-20, 12, 20, 12);
        mainPanel.add(heading, gbc);
        gbc.insets = new Insets(12, 12, 12, 12);

        CurrencyItem[] currencies = getCurrencyList();

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        fromLabel.setForeground(Color.WHITE);
        mainPanel.add(fromLabel, gbc);

        JComboBox<CurrencyItem> fromCurrency = new JComboBox<>(currencies);
        fromCurrency.setRenderer(new CurrencyRenderer());
        fromCurrency.setBackground(new Color(0, 0, 0, 180));
        fromCurrency.setForeground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(fromCurrency, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel amtLabel = new JLabel("Amount:");
        amtLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        amtLabel.setForeground(Color.WHITE);
        mainPanel.add(amtLabel, gbc);

        JTextField amountField = new JTextField();
        amountField.setBackground(new Color(0, 0, 0, 180));
        amountField.setForeground(Color.WHITE);
        amountField.setCaretColor(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(amountField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        toLabel.setForeground(Color.WHITE);
        mainPanel.add(toLabel, gbc);

        JComboBox<CurrencyItem> toCurrency = new JComboBox<>(currencies);
        toCurrency.setRenderer(new CurrencyRenderer());
        toCurrency.setBackground(new Color(0, 0, 0, 180));
        toCurrency.setForeground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(toCurrency, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel convertedLabel = new JLabel("Converted:");
        convertedLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        convertedLabel.setForeground(Color.WHITE);
        mainPanel.add(convertedLabel, gbc);

        JTextField convertedField = new JTextField();
        convertedField.setEditable(false);
        convertedField.setBackground(new Color(0, 0, 0, 180));
        convertedField.setForeground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(convertedField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel rateLabel = new JLabel("Buy: ₹83.10 | Sell: ₹83.35", SwingConstants.CENTER);
        rateLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(rateLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipadx = 0;                       
        gbc.ipady = 0;
        gbc.anchor = GridBagConstraints.CENTER;  
        JButton convertButton=new JButton("Convert");
        convertButton.setPreferredSize(new Dimension(100, 32));
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        convertButton.setForeground(Color.WHITE);

        Color normalGreen = new Color(0, 153, 76); 
        Color darkGreen = new Color(7, 94, 65);    
        convertButton.setBackground(normalGreen);
        convertButton.setFocusPainted(false);
        convertButton.setBorderPainted(false);
        convertButton.setContentAreaFilled(false); 
        convertButton.setOpaque(false);


        convertButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fill = model.isPressed() ? darkGreen : normalGreen;
                g2.setColor(fill);
                g2.fillRect(0, 0, b.getWidth(), b.getHeight());

                g2.dispose();

                super.paint(g, c);
            }
        });

        convertButton.addActionListener(e -> {
            try {
                CurrencyItem from = (CurrencyItem) fromCurrency.getSelectedItem();
                CurrencyItem to = (CurrencyItem) toCurrency.getSelectedItem();
                String amountText = amountField.getText().trim();

                if (from == null || to == null || amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields properly.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountText);

                // Dummy rates (in reference to INR)
                double fromRate = getRate(from.code);
                double toRate = getRate(to.code);

                // Convert to INR, then to target
                double converted = (amount / fromRate) * toRate;

                convertedField.setText(String.format("%.2f", converted));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(convertButton, gbc);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        add(backgroundPanel);
        setVisible(true);
    }

    static class ImagePanel extends JPanel {
        Image image;

        public ImagePanel(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

    static class CurrencyRenderer extends JLabel implements ListCellRenderer<CurrencyItem> {
        public CurrencyRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends CurrencyItem> list, CurrencyItem value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                setText("  " + value.code);
                ImageIcon icon = new ImageIcon(value.iconPath);
                if (icon.getIconWidth() <= 0) {
                    System.out.println("⚠️ Missing image: " + value.iconPath);
                }
                Image img = icon.getImage().getScaledInstance(24, 16, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            }
            setBackground(isSelected ? new Color(60, 60, 60, 200) : new Color(0, 0, 0, 180));
            setForeground(Color.WHITE);
            return this;
        }
    }

    private CurrencyItem[] getCurrencyList() {
        List<CurrencyItem> list = new ArrayList<>();
        list.add(new CurrencyItem("USD", "us.png"));
        list.add(new CurrencyItem("INR", "in.png"));
        list.add(new CurrencyItem("EUR", "eu.png"));
        list.add(new CurrencyItem("JPY", "jp.png"));
        list.add(new CurrencyItem("GBP", "gb.png"));
        list.add(new CurrencyItem("AUD", "au.png"));
        list.add(new CurrencyItem("CAD", "ca.png"));
        list.add(new CurrencyItem("CHF", "ch.png"));
        list.add(new CurrencyItem("CNY", "cn.png"));
        list.add(new CurrencyItem("HKD", "hk.png"));
        list.add(new CurrencyItem("SGD", "sg.png"));
        list.add(new CurrencyItem("NZD", "nz.png"));
        list.add(new CurrencyItem("KRW", "kr.png"));
        list.add(new CurrencyItem("SEK", "se.png"));
        list.add(new CurrencyItem("NOK", "no.png"));
        list.add(new CurrencyItem("DKK", "dk.png"));
        list.add(new CurrencyItem("RUB", "ru.png"));
        list.add(new CurrencyItem("ZAR", "za.png"));
        list.add(new CurrencyItem("BRL", "br.png"));
        list.add(new CurrencyItem("MXN", "mx.png"));
        list.add(new CurrencyItem("IDR", "id.png"));
        list.add(new CurrencyItem("TRY", "tr.png"));
        list.add(new CurrencyItem("PLN", "pl.png"));
        list.add(new CurrencyItem("THB", "th.png"));
        list.add(new CurrencyItem("MYR", "my.png"));
        list.add(new CurrencyItem("PHP", "ph.png"));
        list.add(new CurrencyItem("HUF", "hu.png"));
        list.add(new CurrencyItem("CZK", "cz.png"));
        list.add(new CurrencyItem("AED", "ae.png"));
        list.add(new CurrencyItem("SAR", "sa.png"));
        list.add(new CurrencyItem("ILS", "il.png"));
        list.add(new CurrencyItem("ARS", "ar.png"));
        list.add(new CurrencyItem("CLP", "cl.png"));
        list.add(new CurrencyItem("COP", "co.png"));
        list.add(new CurrencyItem("EGP", "eg.png"));
        list.add(new CurrencyItem("PKR", "pk.png"));
        list.add(new CurrencyItem("BDT", "bd.png"));
        list.add(new CurrencyItem("VND", "vn.png"));
        list.add(new CurrencyItem("NGN", "ng.png"));
        list.add(new CurrencyItem("KES", "ke.png"));
        list.add(new CurrencyItem("GHS", "gh.png"));
        list.add(new CurrencyItem("UAH", "ua.png"));
        list.add(new CurrencyItem("IQD", "iq.png"));
        list.add(new CurrencyItem("IRR", "ir.png"));
        list.add(new CurrencyItem("LKR", "lk.png"));
        list.add(new CurrencyItem("MMK", "mm.png"));
        list.add(new CurrencyItem("DZD", "dz.png"));
        list.add(new CurrencyItem("MAD", "ma.png"));
        return list.toArray(new CurrencyItem[0]);
    }

    private double getRate(String currencyCode) {
        switch (currencyCode) {
            case "USD": return 83.10;
            case "EUR": return 90.50;
            case "INR": return 1.0;
            case "JPY": return 0.56;
            case "GBP": return 105.45;
            case "AUD": return 55.32;
            case "CAD": return 60.25;
            case "CHF": return 92.75;
            case "CNY": return 11.45;
            case "HKD": return 10.64;
            case "SGD": return 62.33;
            case "NZD": return 50.21;
            case "KRW": return 0.063;
            case "SEK": return 7.80;
            case "NOK": return 7.55;
            case "DKK": return 12.15;
            case "RUB": return 0.90;
            case "ZAR": return 4.55;
            case "BRL": return 16.35;
            case "MXN": return 4.75;
            case "IDR": return 0.0053;
            case "TRY": return 4.15;
            case "PLN": return 21.45;
            case "THB": return 2.40;
            case "MYR": return 17.55;
            case "PHP": return 1.49;
            case "HUF": return 0.24;
            case "CZK": return 3.55;
            case "AED": return 22.63;
            case "SAR": return 22.15;
            case "ILS": return 23.44;
            case "ARS": return 0.45;
            case "CLP": return 0.094;
            case "COP": return 0.021;
            case "EGP": return 2.70;
            case "PKR": return 0.30;
            case "BDT": return 0.76;
            case "VND": return 0.0035;
            case "NGN": return 0.52;
            case "KES": return 0.65;
            case "GHS": return 7.10;
            case "UAH": return 2.22;
            case "IQD": return 0.063;
            case "IRR": return 0.0019;
            case "LKR": return 0.27;
            case "MMK": return 0.039;
            case "DZD": return 0.61;
            case "MAD": return 8.25;
            default: return 1.0;
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(currX::new);
    }
}