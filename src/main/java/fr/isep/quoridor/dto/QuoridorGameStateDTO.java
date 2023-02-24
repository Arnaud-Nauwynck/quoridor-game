package fr.isep.quoridor.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;

public class QuoridorGameStateDTO {

	public static final int DEFAULT_N = 9;
	public static final int WALLS_FOR_2_PLAYERS = 10;

	public int N = DEFAULT_N;

	public int playerTurn;
	
	public List<PlayerStateDTO> players = new ArrayList<>();
	
	public List<WallPosDTO> walls = new ArrayList<>();
	
	@NoArgsConstructor @AllArgsConstructor
	public static class WallPosDTO {
		/** beforeX = x-0.5, beforeY=y-0.5 */
		public int beforeX;
		public int beforeY;
		public WallDirection dir;
	}

	@NoArgsConstructor @AllArgsConstructor
	public static class PlayerStateDTO {
		public String playerName;
		public int x;
		public int y;
		public int wallCount;
	}

	public static QuoridorGameStateDTO createDefault2Players() {
		val res = new QuoridorGameStateDTO();
		int N = res.N = DEFAULT_N;
		int wallCount = WALLS_FOR_2_PLAYERS;
		res.players.add(new PlayerStateDTO("player1", (N-1)/2, 0, wallCount));
		res.players.add(new PlayerStateDTO("player2", (N-1)/2, N-1, wallCount));
		return res;
	}

}
