The files in this folder belong to a programming project conducted within a course "Allgemeines
Programmierpraktikum" at the University of Göttingen in 2017. The goal of the project was to
implement an application for a board game with a complicated set of rules. There were multiple
requirements defined for the aplication itself, e.g.: the application had to be implemented in
Java following the principles of object-oriented programming (OOP); include GUI; allow scaling
of the board size; allow participation of human players as well as automatic ones with diverse
playing strategies etc. It was implemented in a team. 

These files belong to my part of the work and mainly represent the implementation of the game
board itself (see, in particular, the main class SimpleBoard). The files in the folder
**extension** implement an extended version of SimpleBoard - ResetBoard - which allows not only
to conduct allowed moves and keep track of the game status but also to store and undo moves
(e.g. to allow a human player to go back in a game and try different moves or to support an
automatic player in computing the possible consequences of its potential moves few steps ahead
and making a decision based on that).

Main classes here:

**SimpleBoard**: this class represents the board on which all players' moves have to be
		executed, it implements the main logic of the game.
		It contains information about all tokens which are currently
		on the board as well as about the current turn and game status. It verifies if
		any new move is a legal one, if it leads to the end of the game, executes
		legal moves and performes all necessary changes related to them.  
		When the game is finished, the SimpleBoard saves the information on the win type.
		It has 2 extensions: Board and ResetBoard.

**Entity**:	this class represents a token in the game. Every object of this class contains
		information about its current position on the board, height, possible moves, if
		it is blocked, and if it is a base (has height 0 and no possible moves).
		Additionally, such objects contain information about the board size, the maximal
		allowed tower height, and the maximal possible step range.
  
