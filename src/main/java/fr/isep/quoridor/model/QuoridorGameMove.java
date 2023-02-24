package fr.isep.quoridor.model;

import fr.isep.quoridor.dto.MoveDirection;
import fr.isep.quoridor.dto.WallDirection;
import lombok.AllArgsConstructor;

/**
 * immutable base-class for Move of Quoridor game state
 */
public abstract class QuoridorGameMove {

	enum MoveType { movePawn, putWall };
	
	public abstract MoveType moveType();
	
	@AllArgsConstructor
	public static class PawnQuoridorGameMove extends QuoridorGameMove {
		
		public final MoveDirection pawnDir;

		@Override
		public MoveType moveType() {
			return MoveType.movePawn;
		}
	}
	
	@AllArgsConstructor
	public static class PutWallQuoridorGameMove extends QuoridorGameMove {
		
		public final int wallBeforeX;
		public final int wallBeforeY;
		public final WallDirection wallDirection;

		@Override
		public MoveType moveType() {
			return MoveType.movePawn;
		}
	}

}
