package towerwarspp.board;

/**
 * This enumeration represents possible types of changes which can occur as a result of a move.
 *
 * @author Anastasiia Kysliak
 * @version 14-07-17
 */
public enum ChangeType {
	MOVE_ADDED,
	MOVE_REMOVED,
	ALL_MOVES_REMOVED,
	RANGE_INC,
	RANGE_DEC,
	ENTITY_ADDED,
	ENTITY_REMOVED,
	POSITION_CHANGED,
	ELEMENT_REPLACED,
	HEIGHT_INCREASED,
	HEIGHT_DECREASED,
	TOWER_BLOCKED,
	TOWER_UNBLOCKED
}
