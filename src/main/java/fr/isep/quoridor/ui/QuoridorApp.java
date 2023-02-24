package fr.isep.quoridor.ui;


import fr.isep.quoridor.dto.QuoridorGameStateDTO;
import fr.isep.quoridor.dto.QuoridorGameStateDTO.WallPosDTO;
import fr.isep.quoridor.dto.WallDirection;
import fr.isep.quoridor.model.QuoridorGameStateModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.val;

public class QuoridorApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		val gameStateDTO = QuoridorGameStateDTO.createDefault2Players(); 
		boolean debug = true;
		if (debug) {
			val wall1 = new WallPosDTO(1, 1, WallDirection.Horyzontal);
			gameStateDTO.walls.add(wall1);

			val wall2 = new WallPosDTO(2, 2, WallDirection.Vertical);
			gameStateDTO.walls.add(wall2);
		}
		val model = new QuoridorGameStateModel(gameStateDTO);
		
		val root = new BorderPane();
		
		BoardView board = new BoardView(700, model);
 		root.setCenter(board.getComponent());
		
		root.setBottom(new Label("Footer"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		board.paint();
	}

}
