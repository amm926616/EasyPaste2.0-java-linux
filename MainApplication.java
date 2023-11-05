import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, ParseException {
        Button activationButton = new Button("activate");
        EasyPasteScene easyPasteScene = new EasyPasteScene();

        ActivationScene activationScene = new ActivationScene(new MainApplication(), activationButton, stage, easyPasteScene);

        SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
        subscriptionStatus.initialSetup();
        subscriptionStatus.checkExpiration();
        String state = subscriptionStatus.checkSubscription();

        if (state.equals("HAVENTPAIDFORTHEPROGRAMYET")) {
            stage.setScene(activationScene);
        } else if (state.equals("ALREADYPAIDFORTHEPROGRAM")) {
            stage.setScene(easyPasteScene);
            stage.setTitle("EasyPaste2.0 by AdamD178");
        } else {
            System.out.println("You Damn Son Of A Bitch!");
            System.exit(0);
        }

        stage.setOnCloseRequest(event -> {
            System.exit(0);
            easyPasteScene.stop();
        });

        Image iconImage = new Image("resources/logo.png");
        stage.getIcons().add(iconImage);

        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}