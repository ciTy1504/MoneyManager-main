package gui.app;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

import gui.pages.OverviewPage;

public class AppSettings {
    private static AppSettings instance;

    private boolean darkMode;
    private String currency;
    private int numberOfDecimalPlaces;
    private boolean currencyBeforeAmount;
    private static final String SETTINGS_FILE = "app-settings.properties";

    private AppSettings() {
        load();
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            synchronized (AppSettings.class) {
                if (instance == null) {
                    instance = new AppSettings();
                }
            }
        }
        return instance;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        save();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        save(); // Save settings after modification
    }

    public int getNumberOfDecimalPlaces() {
        return numberOfDecimalPlaces;
    }

    public void setNumberOfDecimalPlaces(int numberOfDecimalPlaces) {
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
        save(); // Save settings after modification
    }

    public boolean isCurrencyBeforeAmount() {
        return currencyBeforeAmount;
    }

    public void setCurrencyBeforeAmount(boolean currencyBeforeAmount) {
        this.currencyBeforeAmount = currencyBeforeAmount;
        save();
    }

    private void load() {
        Properties properties = new Properties();
        try {
            Path path = Paths.get(SETTINGS_FILE);
            if (Files.exists(path)) {
                FileInputStream fileInputStream = new FileInputStream(path.toFile());
                properties.load(fileInputStream);
                fileInputStream.close();

                darkMode = Boolean.parseBoolean(properties.getProperty("dark_mode", "false")); // Default to "light" if not set
                currency = properties.getProperty("currency", "$"); // Default to "USD" if not set
                numberOfDecimalPlaces = Integer.parseInt(properties.getProperty("decimal_places", "2")); // Default to 2
                currencyBeforeAmount = Boolean.parseBoolean(properties.getProperty("currency_before_amount", "true")); // Default to true
            } else {
                darkMode = false;
                currency = "USD";
                numberOfDecimalPlaces = 2;
                currencyBeforeAmount = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        Properties properties = new Properties();
        properties.setProperty("dark_mode", String.valueOf(darkMode));
        properties.setProperty("currency", currency);
        properties.setProperty("decimal_places", String.valueOf(numberOfDecimalPlaces));
        properties.setProperty("currency_before_amount", String.valueOf(currencyBeforeAmount));

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SETTINGS_FILE);
            properties.store(fileOutputStream, "App Settings");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       OverviewPage.getInstance().requestReloading();
    }
}
