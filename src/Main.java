import filehandling.FileManager;
import gui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Step 1: create data/ folder and .txt files if they don't exist
        FileManager.initialize();

        // Step 2: launch the GUI on the Event Dispatch Thread (Swing rule)
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
