import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class ActivationScene extends Scene {
    static String keyCode = PasswordGenerator.key;
    static String passCode = PasswordGenerator.getPassCode();
    public ActivationScene(MainApplication mainApplication, Button activateButton, Stage stage, Scene easyPasteScene) {
        super(createActivationScene(mainApplication, activateButton, stage, easyPasteScene));
    }

    private static VBox createActivationScene(MainApplication application, Button activateButton, Stage stage, Scene easyPasteScene) {
        VBox root = new VBox();

        Background background = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        root.setPadding(new Insets(20));

        HBox keyHBox = new HBox();
        Label keyLabel = new Label("key (click to copy): ");
        Hyperlink key = new Hyperlink(keyCode);

        key.setOnMouseClicked(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(keyCode);
            clipboard.setContent(clipboardContent);
        });

        keyHBox.getChildren().addAll(keyLabel, key);


        Label instruction = new Label("Use this key to ask for passcode from the developer.\n ");

        HBox linkHBox = new HBox();
        Label telegramLabel = new Label("Telegram Account: ");
        Hyperlink link = new Hyperlink("https://t.me/Adamd178");
        link.setOnAction(e -> {
            String url = "https://t.me/Adamd178";
            application.getHostServices().showDocument(url);
        });

        linkHBox.getChildren().addAll((telegramLabel), link);

        HBox passcodeHbox = new HBox();
        TextField textField = new TextField();

        activateButton.setOnAction(e -> {
            if (textField.getText().equals(passCode)) {
                SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
                try {
                    subscriptionStatus.putData("state", "ALREADYPAIDFORTHEPROGRAM", "expiredD", "0");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stage.setTitle("EasyPaste2.0 by AdamD178");
                stage.setScene(easyPasteScene);
            } else if (textField.getText().equals("adamdenaultisthebest")) {
                System.out.println("Welcome, master!");
                stage.setTitle("You are using cheat code!");
                stage.setScene(easyPasteScene);
            } else {
                System.out.println("fuck off!");
            }
        });

        passcodeHbox.getChildren().addAll(textField, activateButton);
        HBox.setHgrow(textField, Priority.ALWAYS);

        stage.setTitle("Activate EasyPaste!");

        root.getChildren().addAll(keyHBox, instruction, linkHBox, passcodeHbox);
        return root;
    }
}