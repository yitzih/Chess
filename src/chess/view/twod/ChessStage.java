package chess.view.twod;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 * The main stage for each Application
 */
public class ChessStage extends Stage {

    public ChessStage(ChessGameUI gameUI) throws Exception {

        Scene scene = new Scene(gameUI);

        this.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("Board.css").toExternalForm());

        this.setTitle("Chess");
        this.setResizable(false);
        this.sizeToScene();

    }

}
