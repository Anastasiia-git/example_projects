package towerwarspp.board;

import towerwarspp.preset.*;
import towerwarspp.io.*;
import static towerwarspp.preset.PlayerColor.*;
import static towerwarspp.preset.Status.*;
import static towerwarspp.board.ChangeType.*;
import java.util.HashSet;
import java.util.Vector;
import java.util.Stack;
import java.util.ListIterator;
import java.lang.Exception;
import java.lang.IllegalStateException;

/**
 * This class represents an extended board which, on the one hand, has the same functionality as {@link Board}
 * and on the other hand, allows to store and undo moves in a game. Any changes related to the players' moves are
 * stacked. Multiple methods of the superclass which are responsible for executing specific move types and performing
 * corresponding changes are overriden.
 *
 * @author Anastasiia Kysliak
 * @version 11-08-2017
 */
public class ResetBoard extends Board{
	
	private Stack<Change> stack = new Stack<Change>();

	private int moveCnt = 0;

	public ResetBoard(int n) {
		super(n);
	}
	public ResetBoard (int size, PlayerColor turn, Vector<Entity> lRed, Vector<Entity> lBlue, Entity[][] board, Position redB, Position blueB) {
		super(size, turn, lRed, lBlue, board, redB, blueB);
	}
	public ResetBoard (SimpleBoard boardO) {
		super(boardO.size, boardO.turn, boardO.listRed, boardO.listBlue, boardO.getBoard(), boardO.redBase, boardO.blueBase);
	}
	public Status makeMove(Move move) {
		moveCnt++;
		return super.makeMove(move);
	}

	public void undoLastMove() {
		while(!stack.empty() && stack.peek().getOrder() == moveCnt) {
			Change change = stack.pop();
			switch(change.getType()) {
				case MOVE_ADDED: undoMoveAdded(change); break;
				case MOVE_REMOVED: undoMoveRemoved(change); break;
				case ALL_MOVES_REMOVED: undoAllMovesRemoved(change); break;
				case RANGE_INC: undoRangeIncrease(change); break;
				case RANGE_DEC: undoRangeDecrease(change); break;
				case ENTITY_ADDED: undoEntityAdded(change); break;
				case ENTITY_REMOVED: undoEntityRemoved(change); break;
				case POSITION_CHANGED: undoPositionChanged(change); break;
				case ELEMENT_REPLACED: undoElementReplaced(change); break;
				case HEIGHT_INCREASED: undoHeightIncreased(change); break;
				case HEIGHT_DECREASED: undoHeightDecreased(change); break;
				case TOWER_BLOCKED: undoTowerBlocked(change); break;
				case TOWER_UNBLOCKED: undoTowerUnblocked(change);
			}
		}
		this.status = OK;
        	this.turn = (turn == RED ? BLUE : RED);
		winType = null;
		--moveCnt;
	}
	private void undoMoveAdded(Change change) {
		Entity ent = change.getEntity();
		try{
			ent.removeMove(change.getPosition(), change.getRange());
		}
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoMoveAdded");
		}
	}
	private void undoMoveRemoved(Change change) {
		Entity ent = change.getEntity();
		try{
			ent.addMove(change.getPosition(), change.getRange());
		} 
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoMoveRemoved");
		}
	}
	private void undoAllMovesRemoved(Change change) {
		Entity ent = change.getEntity();
		try{
			ent.setAllMoves(change.getAllMoves(), change.getRange(), change.getMoveCounter());
		} 
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoAllMovesRemoved" + e);
		}
	}
	private void undoRangeIncrease(Change change) {
		Entity ent = change.getEntity();
		ent.decRange();
	}
	private void undoRangeDecrease(Change change) {
		Entity ent = change.getEntity();
		try {
			ent.incRange(change.getRangeMoves());
		} 
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoRangeIncrease");
		}
	}
	private void undoEntityAdded(Change change) {
		Entity ent = change.getEntity();
		Vector<Entity> list= (ent.getColor() == RED? listRed: listBlue);
		list.remove(ent);
	}
	private void undoEntityRemoved(Change change) {
		Entity ent = change.getEntity();		
		Vector<Entity> list = (ent.getColor() == RED? listRed: listBlue);
		list.add(ent);
	}
	private void undoPositionChanged(Change change) {
		try {
			Position pos = change.getPosition();
			change.getEntity().setPosition(pos);
		} 
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoPositionChanged");
		}
	}
	private void undoElementReplaced(Change change) {
		try {
			super.setElement(change.getEntity(), change.getPosition());
		} 
		catch(Exception e) {
			System.out.println("Mistake in Evaluator undoElementReplaced");
		}
	}
	private void undoHeightIncreased(Change change) {
		Entity tower = change.getEntity();
		tower.decHeight();
	}
	private void undoHeightDecreased(Change change) {
		Entity tower = change.getEntity();
		tower.incHeight();
	}
	private void undoTowerBlocked(Change change) {
		Entity tower = change.getEntity();
		tower.setBlocked(false);
	}
	private void undoTowerUnblocked(Change change) {
		Entity tower = change.getEntity();
		tower.setBlocked(true);
	}
	/**
	* Puts the specified figure on the specified position on the board.
	* @param ent the figure in question.
	* @param pos position for the speciifiied figure to be placed on.
	*/
	@Override
	protected void setElement(Entity ent, Position pos) {
		stack.push(new Change(super.getElement(pos), pos, ELEMENT_REPLACED, moveCnt));
		super.setElement(ent, pos);
	}
	@Override
	protected void setBlocked(Entity tower, boolean block) {
		stack.push(new Change(tower, block? TOWER_BLOCKED: TOWER_UNBLOCKED, moveCnt));
		tower.setBlocked(block);
	}
	@Override
	protected void addMove(Entity ent, Position pos, int range) {
		if(!ent.hasMove(pos, range)) {
			stack.push(new Change(ent, pos, range, MOVE_ADDED, moveCnt));
			ent.addMove(pos, range);
		}
	}
	@Override
	protected void removeMove(Entity ent, Position pos, int range) {
		if(ent.hasMove(pos, range)) {
			stack.push(new Change(ent, pos, range, MOVE_REMOVED, moveCnt));
			ent.removeMove(pos, range);
		}
	}
	@Override
	protected void removeAllMoves(Entity ent) {
		stack.push(new Change(ent, ent.getMoves(), ent.getRange(), ent.getMoveCounter(), moveCnt));
		ent.removeAllMoves();
	}
	/**
	* Increases the step range of the specified figure (stone) by n and adds 
	* newly available positions in the new range to its list of possible moves.
	* @param stone the figure (stone) whose step width has to be increased.
	* @param n amount of steps that has to be added.
	*/
	@Override
	protected void addRanges(Entity stone, int n) {
		for(int i = 0; i < n; ++i) {
			incRange(stone);
			Vector<Position> opponents = findPositionsInRange(stone.getPosition(), stone.getRange());
			ListIterator<Position> it = opponents.listIterator();
			while(it.hasNext()) {
				Position opponentPos = it.next(); 
				if(checkMoveForStone(opponentPos, stone.getColor(), stone.getRange() )) {
					super.addMove(stone, opponentPos, stone.getRange());
				}
			}
		}
	}
	@Override
	protected void incRange(Entity ent) {
		stack.push(new Change(ent, RANGE_INC, moveCnt));		
		ent.incRange();
	}
	@Override
	protected void decRange(Entity ent) {
		int range = ent.getRange();
		stack.push(new Change(ent, ent.getRangeMoves(range), range, moveCnt));
		ent.decRange();
	}
	@Override
	protected void incHeight(Entity tower) {
		stack.push(new Change(tower, HEIGHT_INCREASED, moveCnt));
		tower.incHeight();
	}
	@Override
	protected void decHeight(Entity tower) {
		stack.push(new Change(tower, HEIGHT_DECREASED, moveCnt));
		tower.decHeight();
	}
	@Override
	protected void setPosition(Entity ent, Position pos) {
		stack.push(new Change(ent, ent.getPosition(), POSITION_CHANGED, moveCnt));
		ent.setPosition(pos);
	}
	/**
	* Adds the specified figure to the list of movable figures of the corresponding color.
	* @param ent the figure in question.
	*/
	@Override
	protected void addToList(Entity ent) {
		Vector<Entity> list = getEntityList(ent.getColor());
		if(!list.contains(ent)) {
			stack.push(new Change(ent, ENTITY_ADDED, moveCnt));
			list.add(ent);
		}
	}
	/**
	* Removes the specified figure from the the list of movable figures of the corresponding color.
	* @param ent the figure in question.
	*/
	@Override
	protected void removeFromList(Entity ent) {
		Vector<Entity> list = getEntityList(ent.getColor());
		if(list.contains(ent) ){
			stack.push(new Change(ent, ENTITY_REMOVED, moveCnt));
			list.remove(ent);
		}
	}
	@Override
	public Vector<Move> allPossibleMoves(PlayerColor col) {
		Vector<Move> allMoves = new Vector<Move>();
		Vector<Entity> list = (col == RED? listRed: listBlue);
		for(Entity ent: list) {
			Vector<HashSet<Move>> entMoves = ent.getMoves();
			for(HashSet<Move> rangeMoves: entMoves) {
				for(Move move: rangeMoves) {
					allMoves.add(move);
				}
			}
		}
		return allMoves;
	}
	private void printEntMoves(Entity ent) {
		Vector<HashSet<Move>> moves = ent.getMoves();
		for(HashSet<Move> rangeMoves: moves) {
			for(Move move: rangeMoves) {
				System.out.print(move + " ");
			}
		}
		System.out.println();
	}
	private boolean checkMoveForStone(Position pos, PlayerColor col, int dist) {
		Entity opponent = getElement(pos);
		if(opponent == null
			|| (opponent.getColor() != col && (opponent.isBase() || !opponent.isBlocked() || dist == 1) )
			|| (opponent.getColor() == col && !opponent.isBase() && (!opponent.isMaxHeight() || opponent.isBlocked() ) ) ) {
			return true;
		}
		return false;
	}
}

