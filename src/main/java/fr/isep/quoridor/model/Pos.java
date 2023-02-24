package fr.isep.quoridor.model;

import fr.isep.quoridor.dto.MoveDirection;

public class Pos {
	public final int x;
	public final int y;
	
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Pos move(MoveDirection dir) {
		switch(dir) {
		case Right: return new Pos(x+1, y); 
		case Up: return new Pos(x, y-1); 
		case Left: return new Pos(x-1, y); 
		case Down: return new Pos(x, y+1);
		default: throw new IllegalArgumentException();
		}
	}

}