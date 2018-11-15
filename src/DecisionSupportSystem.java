import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Decision Support System is a simple program which solve some of the most
 * important question "take off or not", because of one of the most dangerous
 * parts of the flight is a take off. This application is based on an arcraft
 * flight manual An-12. So, answer is correct only for An-12.
 * Initial data
 * - the size of the runway, stopped behind after a start in the moment of decision-making
 *  (at least 500 m);
 * - the number of working engines (at least three)
 * - flap deviation angle (for runways covered with artificial surface - at least 15 °,
 *   for unpaved or snowy runways - at least 25 °)
 * - condition of the runway.
 *
 *  @author Oleg Malyshkin
 *  @version 1.0
 *
 */

public class DecisionSupportSystem extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Система допомоги прийняття рішення Ан-12");
        Parent root = FXMLLoader.load(
                getClass().getResource("GUI/view/mainView.fxml")
        );
        Scene scene = new Scene(root);
        primaryStage.setOnCloseRequest((windowEvent) -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}