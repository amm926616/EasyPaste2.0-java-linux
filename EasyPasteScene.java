import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EasyPasteScene extends Scene implements NativeKeyListener {
    private static int i = 0;
    private boolean ctrlPressed = false;

    private static List<String> lines;

    private static TextArea display;

    public EasyPasteScene() {
        super(createEasyPasteScene());

        /* Initialize JNativeHook */
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
        }

        // Add the key listener
        GlobalScreen.addNativeKeyListener(this);
    }

    private static VBox createEasyPasteScene() {
        VBox root = new VBox();

        display = new TextArea(); // Using textArea as display
        // setting the dimension of the display
        display.setPrefWidth(400);
        display.setPrefHeight(80);

        // wrap the long lines
        display.setWrapText(true);

        // make the display uneditable
        display.setEditable(false);

        // chooseFileButton
        Button chooseFileButton = new Button("Choose Text File");
        chooseFileButton.setStyle("-fx-background-color: skyblue; -fx-text-fill: black");
        chooseFileButton.setPrefWidth(400);

        // chooseFileButton action
        chooseFileButton.setOnAction(actionEvent -> {
            File newFile = getFile(new Stage());

            if (!(newFile == null)) {
                System.out.println("file selection canceled. Exiting...");
                if (!(lines == null)) {
                    lines.clear();
                }

                i = 0;
                lines = getLines(newFile);

                checkAndUpdateDisplay();
                copyLine(lines.get(i));
            }
        });

        // put everything inside root node
        root.getChildren().addAll(display, chooseFileButton);
        return root;
    }

    private static File getFile(Stage dialogWindow) {
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));

        file = fileChooser.showOpenDialog(dialogWindow);

        return file;
    }

    public static List<String> getLines(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    private static void copyLine(String line) {
        Platform.runLater(() -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(line);
            clipboard.setContent(clipboardContent);
        });
    }

    private static void checkAndUpdateDisplay() {
        if (i < 0) {
            i = lines.size() - 1;
        } else if (i > lines.size() - 1) {
            i = 0;
        }
        display.setText("(" + (i + 1) + "): " + lines.get(i));
    }

    private void upLine() {
        i -= 1;
        if (!(lines == null)) {
            checkAndUpdateDisplay();
            copyLine(lines.get(i));
            System.out.println(i);
        }
    }

    private void downLine() {
        i += 1;
        if (!(lines == null)) {
            checkAndUpdateDisplay();
            copyLine(lines.get(i));
            System.out.println(i);
        }
    }

    private void nextLine() {
        i += 2;
        if (!(lines == null)) {
            checkAndUpdateDisplay();
            copyLine(lines.get(i));
            System.out.println(i);
        }
    }

    private void backLine() {
        i -= 2;
        if (!(lines == null)) {
            checkAndUpdateDisplay();
            copyLine(lines.get(i));
            System.out.println(i);
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = true;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = false;
        }

        if (ctrlPressed) {
            int keyCode = e.getKeyCode();

            if (keyCode == NativeKeyEvent.VC_UP) {
                upLine();
            } else if (keyCode == NativeKeyEvent.VC_DOWN || keyCode == NativeKeyEvent.VC_V) {
                downLine();
            } else if (keyCode == NativeKeyEvent.VC_LEFT) {
                backLine();
            } else if (keyCode == NativeKeyEvent.VC_RIGHT) {
                nextLine();
            }
        }
    }

    public void stop() {
        GlobalScreen.removeNativeKeyListener(this);
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook.");
            System.err.println(ex.getMessage());
        }
    }
}
