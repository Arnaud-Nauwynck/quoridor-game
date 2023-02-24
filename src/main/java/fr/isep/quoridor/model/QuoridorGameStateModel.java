package fr.isep.quoridor.model;

import java.util.ArrayList;
import java.util.List;

import fr.isep.quoridor.dto.MoveDirection;
import fr.isep.quoridor.dto.QuoridorGameStateDTO;
import fr.isep.quoridor.dto.QuoridorGameStateDTO.PlayerStateDTO;
import fr.isep.quoridor.dto.QuoridorGameStateDTO.WallPosDTO;
import fr.isep.quoridor.dto.WallDirection;
import fr.isep.quoridor.model.QuoridorGameMove.PawnQuoridorGameMove;
import fr.isep.quoridor.model.QuoridorGameMove.PutWallQuoridorGameMove;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

/**
 * Quoridor game state model
 */
@Getter
public class QuoridorGameStateModel {

	public final int N;
	
	private int playerTurn;

	private final List<PlayerStateModel> players = new ArrayList<>();

	private final List<WallPosModel> walls = new ArrayList<>();

	/** redundant with <code>walls</code> 
	 * [i][j]==true if any horyzontal wall is set at [i-1][j] or at [i][j]   
	 */
	private final boolean horyzontalWallGrid[][];

	/** redundant with <code>walls</code> 
	 * [i][j]==true if any vertical wall is set at [i][j-1] or at [i][j]   
	 */
	private final boolean verticalWallGrid[][];
	
	@AllArgsConstructor
	@Getter
	public static class WallPosModel {
		/** beforeX = x-0.5, beforeY = y-0.5 */
		public final int beforeX;
		public final int beforeY;
		public final WallDirection dir;
	}

	@AllArgsConstructor
	@Getter
	public static class PlayerStateModel {
		public final String playerName;
		private int x, y;
		private int wallCount;

		Pos getPos() {
			return new Pos(x, y); 	
		}

		public Pos movePawn(MoveDirection dir) {
			val pos = new Pos(x, y).move(dir);
			this.x = pos.x;
			this.y = pos.y;
			return pos;
		}
	}

	// ------------------------------------------------------------------------

	public QuoridorGameStateModel(QuoridorGameStateDTO src) {
		this.N = src.N;
		final int Np1 = N+1;
		this.horyzontalWallGrid = new boolean[Np1][Np1];
		this.verticalWallGrid = new boolean[Np1][Np1];
		for(int i = 0; i <= N; i++) {
			horyzontalWallGrid[i] = new boolean[Np1];
			verticalWallGrid[i] = new boolean[Np1];
		}
		this.playerTurn = src.playerTurn;
		for(PlayerStateDTO p : src.players) {
			players.add(new PlayerStateModel(p.playerName, p.x, p.y, p.wallCount));
		}
		for(WallPosDTO wall: src.walls) {
			addWall(wall.beforeX, wall.beforeY, wall.dir);
		}
	}

	// ------------------------------------------------------------------------
	
	public boolean canPlayerMove(QuoridorGameMove move) {
		val player = players.get(playerTurn);
		switch(move.moveType()) {
		case movePawn: {
			PawnQuoridorGameMove move2 = (PawnQuoridorGameMove) move;
			return canPlayerMovePawn(move2.pawnDir);
		}
		case putWall: {
			PutWallQuoridorGameMove move2 = (PutWallQuoridorGameMove) move;
			if (player.wallCount <= 0) {
				return false;
			}
			return canPayerAddWall(move2.wallBeforeX, move2.wallBeforeY, move2.wallDirection);
		}
		default: return false;
		}
	}
	
	/** check pawn does not go outside board, and does not cross a wall */
	private boolean canPlayerMovePawn(MoveDirection pawnDir) {
		val player = players.get(playerTurn);
		val x = player.getX();
		val y = player.getY();
		if (x < 0 || x > N || y < 0 || y > N) {
			return false;
		}
		switch(pawnDir) {
		case Right:
			if (x >= N-1 || verticalWallGrid[x][y]) {
				return false;
			}
			break;
		case Up:
			if (y <= 0 || horyzontalWallGrid[x][y-1]) {
				return false;
			}
			break;
		case Left:
			if (x <= 0 || verticalWallGrid[x-1][y]) {
				return false;
			}
			break;
		case Down:
			if (y >= N-1 || horyzontalWallGrid[x][y+1]) {
				return false;
			}
			break;
		default: throw new IllegalStateException();
		}
		return true;
	}

	private boolean canPayerAddWall(int wallBeforeX, int wallBeforeY, WallDirection wallDirection) {
		// TODO Auto-generated method stub
		// TODO check no walls crossings, no already set wall fragments
		return false;
	}


	public void playerMove(QuoridorGameMove move) {
		if (! canPlayerMove(move)) {
			throw new IllegalArgumentException();
		}
		val player = players.get(playerTurn);
		switch(move.moveType()) {
		case movePawn: {
			PawnQuoridorGameMove move2 = (PawnQuoridorGameMove) move;
			player.movePawn(move2.pawnDir);
		} break;
		case putWall: {
			PutWallQuoridorGameMove move2 = (PutWallQuoridorGameMove) move;
			addWall(move2.wallBeforeX, move2.wallBeforeY, move2.wallDirection);
			player.wallCount--;
		} break;
		}
		this.playerTurn = (playerTurn + 1) % players.size();
	}

	private void addWall(int beforeX, int beforeY, WallDirection dir) {
		walls.add(new WallPosModel(beforeX, beforeY, dir));
		// update redundant wall grid
		if (dir == WallDirection.Horyzontal) {
			horyzontalWallGrid[beforeX][beforeY] = true;
			horyzontalWallGrid[beforeX-1][beforeY] = true;
		} else {
			horyzontalWallGrid[beforeX][beforeY-1] = true;
			horyzontalWallGrid[beforeX][beforeY] = true;
		}
	}

}
