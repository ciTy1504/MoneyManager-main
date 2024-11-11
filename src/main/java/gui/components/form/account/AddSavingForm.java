package gui.components.form.account;


import gui.pages.SavingsPage;
import gui.pages.SettingsPage;
import gui.app.App;
import gui.components.util.ErrorModal;
import gui.components.util.Modal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import server.model.Account;
import server.service.AccountService;

public class AddSavingForm extends VBox {
    private final Modal modal;
    private TextField nameField;
    private TextField goalField;
    private final SettingsPage settingsPage;

    public AddSavingForm( Modal modal, SettingsPage settingsPage) {
        setupForm();
        this.modal = modal;
        this.settingsPage = settingsPage;
    }
    
    private Region createSpacer () {
    	Region region = new Region();
    	HBox.setHgrow(region, Priority.ALWAYS);
    	return region;
    }
    
    private void setupForm() {
        setSpacing(20);

        Label titleLabel = new Label("Add Saving");
        titleLabel.getStyleClass().add("header1");
        
        Label nameLabel = new Label("Saving name: ");
        nameField = new TextField();
        nameField.setPromptText("Saving Name");
        nameField.getStyleClass().add("input-field");
        HBox nameRow = new HBox(10, nameLabel, createSpacer(), nameField);
        
        
        Label goalLabel = new Label("Goal amount: ");
        goalField = new TextField("0");
        goalField.setPromptText("Goal Amount");
        goalField.getStyleClass().add("input-field");
        HBox goalRow = new HBox(10, goalLabel, createSpacer(), goalField);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> modal.close());
        cancelButton.getStyleClass().addAll("button", "fill-neutral", "border-neutral");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveNewSaving());
        saveButton.getStyleClass().addAll("button", "fill-blue", "border-blue");

        buttons.getChildren().addAll(cancelButton, saveButton);
        
        getChildren().addAll(titleLabel, nameRow, goalRow, buttons);
    }

    private void saveNewSaving() {
        try {
        	String name = nameField.getText();
            double goal = Double.parseDouble(goalField.getText());
            Account newSaving = new Account(App.getInstance().getAccountList().get(App.getInstance().getAccountList().size() - 1).getId() + 1, name, "Saving", 0, goal);

            new AccountService().addAccount(newSaving);
        	
        	modal.close();
            settingsPage.refresh();
        }
        catch (NumberFormatException e) {
        	new ErrorModal("Saving goal is invalid").show();
        }
        catch (Exception e) {
        	new ErrorModal(e.getMessage()).show();
        }
    }
}
