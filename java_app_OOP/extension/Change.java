package towerwarspp.board;

import towerwarspp.preset.*;
import towerwarspp.io.*;
import static towerwarspp.preset.PlayerColor.*;
import static towerwarspp.preset.Status.*;
import static towerwarspp.board.ChangeType.*;
import java.util.HashSet;
import java.util.Vector;
import java.util.ListIterator;
import java.lang.Exception;

/**
 * This class represents information on a change which has occurred as a result of a move.
 * It contains all necessary information required to undo the change.
 * For diverse change types, diverse constructors are provided.
 *
 * @author Anastasiia Kysliak
 * @version 15-07-17
 */
public class Change {
	private ChangeType type;
	private Position position;
	private Entity ent = null;
	private HashSet<Move> rangeMoves = null;
	private Vector<HashSet<Move>> allMoves = null;
	private int range = -1;
	private int moveCounter = -1;
	private int order;
//ENTITY_ADDED, ENTITY_REMOVED, HEIGHT_INCREASED, HEIGHT_DECREASED, TOWER_BLOCKED, TOWER_UNBLOCKED, RANGE_INC
	public Change(Entity ent, ChangeType type, int order) {
		this.ent = ent;
		this.type = type;
		this.order = order;
	}
//MOVE_ADDED, MOVE_REMOVED
	public Change(Entity ent, Position moveEndPos, int range, ChangeType type, int order) {
		this(ent, type,  order);
		this.position = moveEndPos;
		this.range = range;
	}
// RANGE_DEC	
	public Change(Entity ent, HashSet<Move> rangeMoves, int range, int order) {
		this(ent, RANGE_DEC, order);
		this.rangeMoves = rangeMoves;
		this.range = range;
	}
// ALL_MOVES_REMOVED
	public Change(Entity ent, Vector<HashSet<Move>> allMoves, int range, int moveCnt, int order) {
		this(ent, ALL_MOVES_REMOVED, order);
		//System.out.println("moveCNT =" + moveCnt);
		this.allMoves = allMoves;
		this.range = range;
		this.moveCounter = moveCnt;
	}
// ELEMENT_REPLACED, POSITION_CHANGED
	public Change(Entity ent, Position pos, ChangeType type, int order) {
		this(ent, type, order);
		this.position = pos;
	}
	public ChangeType getType() {
		return type;
	}
	public int getOrder() {
		return order;
	}
	public Entity getEntity() {
		return ent;
	}
	/**
	*
	*
	* @throws Exception if this change's type is not RANGE_DEC
	*/
	public HashSet<Move> getRangeMoves() throws Exception {
		if (rangeMoves == null)
			throw new Exception("Illegal operation in Change: rangeMoves == null");
		return rangeMoves;
	}
	/**
	*
	*
	* @throws Exception if this change's type is not ALL_MOVES_REMOVED
	*/
	public Vector<HashSet<Move>> getAllMoves() throws Exception{
		if (allMoves == null)
			throw new Exception("Illegal operation in Change: allMoves == null");
		return allMoves;
	}
	/**
	*
	*
	* @throws Exception if this change's type is not MOVE_ADDED, MOVE_REMOVED, RANGE_DEC or ALL_MOVES_REMOVED
	*/
	public int getRange() throws Exception {
		if (range < 0)
			throw new Exception("Illegal operation in Change: range < 0");
		return range;
	}
	/**
	*
	*
	* @throws Exception if this change's type is not MOVE_ADDED, MOVE_REMOVED, ELEMENT_REPLACED or POSITION_CHANGED.
	*/
	public Position getPosition() throws Exception {
		if (position == null)
			throw new Exception("Illegal operation in Change: position == null");
		return position;
	}
	/**
	*
	*
	* @throws Exception if this change's type is not ALL_MOVES_REMOVED.
	*/
	public int getMoveCounter() throws Exception{
		if (moveCounter == -1)
			throw new Exception("Illegal operation in Change: moveCounter == -1");
		return moveCounter;
	}
}
