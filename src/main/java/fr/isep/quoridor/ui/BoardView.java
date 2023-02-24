package fr.isep.quoridor.ui;

import fr.isep.quoridor.model.QuoridorGameStateModel;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.val;

public class BoardView {

	private static final int N = 9;
	
	private static final Color BOARD_COLOR = //Color.MAROON 
				Color.color(0.5019608f, 0.0f, 0.0f);
	private static final Color LINE_COLOR =
				BOARD_COLOR.brighter();
	private static final Color WALL_COLOR =
			Color.GOLDENROD;
	
	private static final Color[] PAWN_COLORS = new Color[] {
			Color.WHITE, Color.BLACK, Color.BLUE, Color.GREEN
	};
	
	private final Canvas canvas;

	private QuoridorGameStateModel model;
	
	// ------------------------------------------------------------------------
	
	public BoardView(int width, QuoridorGameStateModel model) {
		this.canvas = new Canvas(width, width);
		this.model = model;
	}
	
	// ------------------------------------------------------------------------
	
	public Node getComponent() {
		return canvas;
	}

	/**
	 * <PRE>
	 *   x=0
	 *   \/ x=offset
	 *      \/
	 *       cellW
	 *      <-->                  N-1
	 *      +---+---+---+--    --+---+
	 *      | 0 | 1 | 2 |    ... | 8 | 
	 *      +---+---+---+--    --+---+
	 *      |   |   |   |    ... |   | 
	 *      +---+---+---+--    --+---+
	 *      ..
	 *      +---+---+---+--    --+---+
	 *  N-1 | 8 |                    |
	 *      +---+---+---+--    --+---+
	 * 
	 * </PRE>
	 */
	void paint() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		final double w = canvas.getWidth();
		final double h = canvas.getHeight();
		final double cellW = w / (N+1);
		final double cellH = h / (N+1);
		final double offsetX = cellW/2, offsetY = cellH/2;  
		final double lineW = cellW*10/100.0, lineH = cellH*10/100.0;
		final double lineWBy2 = lineW/2, lineHBy2 = lineH/2;

		gc.setFill(LINE_COLOR);
		gc.fillRect(0, 0, w, h);

		gc.setFill(BOARD_COLOR);
		gc.fillRect(offsetX, offsetY, N*cellW, N*cellH);
		
		gc.setFill(LINE_COLOR);
		// horyzontal lines
		for(int i = 0; i <= N; i++) {
			double y = offsetY + i * cellH - lineHBy2;
			gc.fillRect(0, y, w, lineH);
		}
		// vertical lines
		for(int i = 0; i <= N; i++) {
			double x = offsetX + i * cellW - lineWBy2;
			gc.fillRect(x, 0, lineW, h);
		}
		
		// walls
		gc.setFill(WALL_COLOR);
		for(val wall: model.getWalls()) {
			val indX = wall.beforeX;
			val indY = wall.beforeY;
			switch(wall.dir) {
			case Horyzontal: {
				val x = offsetX + indX * cellW + lineWBy2;
				val y = offsetY + indY * cellH - lineHBy2;
				gc.fillRect(x, y, 2*cellW - lineW, lineH);
			} break;
			case Vertical: {
				val x = offsetX + indX * cellW - lineWBy2;
				val y = offsetY + indY * cellH + lineHBy2;
				gc.fillRect(x, y, lineW, 2*cellH - lineH);
			} break;
			}
		}

		// pawns
		int playerIndex = 0; // for colors
		val pawnRadius = cellW/2*8/10.0;
		for(val player: model.getPlayers()) {
			val indX = player.getX();
			val indY = player.getY();
			val color = PAWN_COLORS[playerIndex++];
			gc.setFill(color);
			val centerX = offsetX + indX * cellW + cellW/2;
			val centerY = offsetY + indY * cellH + cellH/2;
			gc.fillOval(centerX-pawnRadius, centerY-pawnRadius, 2*pawnRadius, 2*pawnRadius);
		}
		
	}
}
