package Chess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

abstract class Pieces {
	boolean White;
	boolean state = true;
	int verMove;
	int horiMove;
	int yPos;
	int xPos;
	Board board;
	List<PTuple> canAttack = new ArrayList<PTuple>();

	public Pieces(int x, int y, boolean white, Board b) {
		xPos = x;
		yPos = y;
		White = white;
		board = b;

	};

	abstract boolean Move(int x, int y);

	boolean doubleMoved() {
		return false;
	};

	boolean didNotMove() {
		return false;
	};

	List<PTuple> returnLOS() {
		return canAttack;
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	};
	
	PTuple returnPosT() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable;
	}

	Pieces returnThis() {
		return this;
	};

	abstract void Die();

	abstract String returnType();

	abstract void updateLOS();

	boolean isWhite() {
		return White;
	};

	boolean isEqualTo(Pieces piece) {
		if (piece == null) {
			return false;
		}
		;

		if (this == piece) {
			return true;
		} else {
			return false;
		}
	}
}

//Stores the Pieces and their positions
class Board {
	boolean game = true;
	HashMap<Integer, HashMap<Integer, Pieces>> mapMap = new HashMap<Integer, HashMap<Integer, Pieces>>();
	HashMap<Integer, Pieces> PieceMap1 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap2 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap3 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap4 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap5 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap6 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap7 = new HashMap<Integer, Pieces>();
	HashMap<Integer, Pieces> PieceMap8 = new HashMap<Integer, Pieces>();
	List<PTuple> WLOS = new ArrayList<PTuple>();
	List<PTuple> BLOS = new ArrayList<PTuple>();
	Pieces[] W_pawn = new Pieces[8];
	Pieces[] B_pawn = new Pieces[8];
	Pieces[] Placeholders = new Pieces[8];
	Pieces[] Kings = new Pieces[2];

	boolean game(){
		return game;
	}

	boolean LOSContains(PTuple p, boolean white) {
		if (white) {
			for (PTuple p2 : WLOS) {
				if (p.isEqualTo(p2)) {
					return true;
				}
			}
			return false;
		} else {
			for (PTuple p2 : BLOS) {
				if (p.isEqualTo(p2)) {
					return true;
				}
			}
			return false;
		}
	}

	boolean LOSContains(int x, int y, boolean white) {
		PTuple p = new PTuple(x, y);
		if (white) {
			for (PTuple p2 : WLOS) {
				if (p.isEqualTo(p2)) {
					return true;
				}
			}
			return false;
		} else {
			for (PTuple p2 : BLOS) {
				if (p.isEqualTo(p2)) {
					return true;
				}
			}
			return false;
		}
	}

	Board() {

	};

	List<PTuple> returnLOS(boolean white) {
		if (white) {
			return WLOS;
		} else {
			return BLOS;
		}
	}

	Pieces evalCheckmate() {
		for (Pieces k : Kings) {
			k.updateLOS();
			if(k.returnLOS().size() == 0 && this.LOSContains(k.returnPosT(), !k.isWhite())) {
				return k;
			}
		}
		return null;
	}

	void updateLOS() {
		BLOS.clear();
		WLOS.clear();
		for (int i = 1; i < 9; i++) {
			for (int x = 1; x < 9; x++) {
				if (this.returnPiece(i, x) != null){
					this.returnPiece(i, x).updateLOS();
					if (this.returnPiece(i, x).returnLOS() != null) {
						if (this.returnPiece(i, x).isWhite()) {
							WLOS.addAll(this.returnPiece(i, x).returnLOS());

						} else {
							BLOS.addAll(this.returnPiece(i, x).returnLOS());
						}
					}
				}
			}
		}
	}

	Pieces returnPiece(PTuple P) {
		Pieces Piece = (Pieces) mapMap.get(P.getValues(false)).get(P.getValues(true));
		return Piece;
	};

	Pieces returnPiece(int x, int y) {
		Pieces Piece = (Pieces) mapMap.get(y).get(x);
		return Piece;
	};

	HashMap<Integer, HashMap<Integer, Pieces>> shareBoard() {
		return mapMap;
	};

	void setBoard(Board board) {
		mapMap.put(1, PieceMap1);
		mapMap.put(2, PieceMap2);
		mapMap.put(3, PieceMap3);
		mapMap.put(4, PieceMap4);
		mapMap.put(5, PieceMap5);
		mapMap.put(6, PieceMap6);
		mapMap.put(7, PieceMap7);
		mapMap.put(8, PieceMap8);

		for (int i = 0; i < 8; i++) {

			Pawn W_Pawn = new Pawn(i + 1, 2, true, this);
			W_pawn[i] = W_Pawn;
			this.recordPos(W_pawn[i]);
		}

		for (int i = 0; i < 8; i++) {
			Pawn B_Pawn = new Pawn(i + 1, 7, false, this);
			B_pawn[i] = B_Pawn;
			this.recordPos(B_pawn[i]);
		}

		Rook W_Rook1 = new Rook(1, 1, true, this);
		recordPos(W_Rook1);
		Rook W_Rook2 = new Rook(8, 1, true, this);
		recordPos(W_Rook2);
		Rook B_Rook1 = new Rook(1, 8, false, this);
		recordPos(B_Rook1);
		Rook B_Rook2 = new Rook(8, 8, false, this);
		recordPos(B_Rook2);
		Horse W_Horse1 = new Horse(2, 1, true, this);
		recordPos(W_Horse1);
		Horse W_Horse2 = new Horse(7, 1, true, this);
		recordPos(W_Horse2);
		Horse B_Horse1 = new Horse(2, 8, false, this);
		recordPos(B_Horse1);
		Horse B_Horse2 = new Horse(7, 8, false, this);
		recordPos(B_Horse2);
		Bishop W_Bishop1 = new Bishop(3, 1, true, this);
		recordPos(W_Bishop1);
		Bishop W_Bishop2 = new Bishop(6, 1, true, this);
		recordPos(W_Bishop2);
		Bishop B_Bishop1 = new Bishop(3, 8, false, this);
		recordPos(B_Bishop1);
		Bishop B_Bishop2 = new Bishop(6, 8, false, this);
		recordPos(B_Bishop2);
		Queen W_Queen = new Queen(4, 1, true, this);
		recordPos(W_Queen);
		Queen B_Queen = new Queen(4, 8, false, this);
		recordPos(B_Queen);
		King W_King = new King(5, 1, true, this);
		recordPos(W_King);
		Kings[0] = W_King;
		King B_King = new King(5, 8, false, this);
		recordPos(B_King);
		Kings[1] = B_King;

	}

	void kill(Pieces Piece) {
		Piece.Die();
		mapMap.get(Piece.yPos).remove(Piece.xPos);

	}

	void remove(Pieces Piece) {
		mapMap.get(Piece.yPos).remove(Piece.xPos);
	}

	void recordPos(Pieces Piece) {
		mapMap.get(Piece.yPos).put(Piece.xPos, Piece);

	}
	
	void setGame(boolean b) {
		game = b;
	}

	boolean notBlocked(Pieces Piece, int x, int y) {
		int xPos = Piece.returnPos() / 10;
		int yPos = Piece.returnPos() % 10;
		ArrayList<Boolean> chk = new ArrayList<Boolean>();
		if(x > 8|| y > 8 || x < 0 || y < 0) {
			return false;
		}
		if ((x == xPos) && (y != yPos) || (x != xPos && y == yPos)) {
			if (x == xPos) {
				if(yPos < y) {
					for (int yP = yPos; yP < y+1; yP++) {
						Pieces result = this.returnPiece(xPos, yP);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);			
						} else {
							chk.add(true);
						}
					}
				} else {
					for (int yP = yPos; yP > y-1; yP--) {
						Pieces result = this.returnPiece(xPos, yP);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);			
						} else {
							chk.add(true);
						}
					}
				}

			} else {
				if(xPos < x) {
					for (int xP = xPos; xP < x+1; xP++) {
						Pieces result = this.returnPiece(xP, yPos);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}
				} else {
					for (int xP = xPos; xP > x-1; xP--) {
						Pieces result = this.returnPiece(xP, yPos);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}
				}

			}
		} else if (Math.abs(x - xPos) == Math.abs(y - yPos)) {
			if (xPos > x) {
				if (yPos > y) {
					for (int c = 1; c < Math.abs(x - xPos) + 1; c++) {
						Pieces result = this.returnPiece(xPos - c, yPos - c);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}
				} else {
					for (int c = 1; c < Math.abs(x - xPos) + 1; c++) {
						Pieces result = this.returnPiece(xPos - c, yPos + c);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}
				}
			} else {
				if (yPos > y) {
					for (int c = 1; c < Math.abs(x - xPos) + 1; c++) {
						Pieces result = this.returnPiece(xPos + c, yPos - c);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}

				} else {
					for (int c = 1; c < Math.abs(x - xPos) + 1; c++) {
						Pieces result = this.returnPiece(xPos + c, yPos + c);
						if (result != null && result.isWhite() == Piece.isWhite() && result != Piece) {
							chk.add(false);	
						} else {
							chk.add(true);
						}
					}
				}
			}
		} else {
			return false;
		}
		if(!chk.contains(false)) {
			return true;
		} else {
			return false;
		}



	}

}

public class Chess {
	public static void main(String arg[]) {
		Scanner scan = new Scanner(System.in);

		if ((arg.length > 0) && (arg[0] == "-graphics")) {

			// 1. Create the frame.
			JFrame frame = new JFrame("Chess Demo", null);

			// 2. Optional: What happens when the frame closes?
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// 3. Create components and put them in the frame.
			ImageIcon Board = new ImageIcon("Chessboard.png");
			ImageIcon Rectangle = new ImageIcon("GrayRectangle.png");
			JLabel Label1 = new JLabel(Board, JLabel.CENTER);
			JLabel Label2 = new JLabel(Rectangle, JLabel.CENTER);

			// 4. Size the frame.
			frame.pack();

			// 5. Show it.
			frame.setVisible(true);
		} else {
			boolean result;
			Board board = new Board();
			board.setBoard(board);
			boolean whiteIsPlaying = false;
			int turn = 0;
			int tempY = 1;
			while (board.game()) {
				result = false;

				turn++;
				board.updateLOS();
				if (whiteIsPlaying == true) {
					whiteIsPlaying = false;
				} else {
					whiteIsPlaying = true;
				}

				System.out.println("  12345678");
				for (int y = 1; y != 9; y++) {
					StringBuilder sb = new StringBuilder();
					for (int x = 1; x != 9; x++) {
						if (board.returnPiece(x, 9 - y) == null) {
							sb.append(" ");
							tempY = 9 - y;
						} else {
							String Type = new String();
							Type = board.returnPiece(x, 9 - y).returnType();
							sb.append(Type.substring(0, 1));
							tempY = 9 - y;
						}

					}

					System.out.println(tempY + " " + sb.toString());
				}
				if (board.evalCheckmate() != null) {
					System.out.println("GAME! THE WINNER IS:");
					if (board.evalCheckmate().isWhite()) {
						System.out.println("BLACK!");
						board.setGame(false);
					} else {
						System.out.println("WHITE!");
						board.setGame(false);
					}
				}
				while (result == false) {
					if (whiteIsPlaying == true) {
						System.out.println("White's turn.");
					} else {
						System.out.println("Black's turn.");
					}
					System.out.println("Choose a piece by its position. Format:number,number");
					String I_Piece = scan.next();
					Pieces selection = board.returnPiece(Integer.parseInt(I_Piece.substring(0, 1)),
							Integer.parseInt(I_Piece.substring(2, 3)));
					if (selection == null) {
						System.out.println("There are no pieces.");
					} else {
						System.out
						.println("Now choose a position to move it. Selected Piece-" + selection.returnType());
						String I_Pos = scan.next();
						int casted_x = Integer.parseInt(I_Pos.substring(0, 1));
						int casted_y = Integer.parseInt(I_Pos.substring(2, 3));
						if (whiteIsPlaying && selection.isWhite()) {
							board.remove(selection);
							if (board.returnPiece(casted_x, casted_y) != null) {
								board.kill(board.returnPiece(casted_x, casted_y));
							}
							result = selection.Move(casted_x, casted_y);
							board.recordPos(selection);
						} else {
							if (!whiteIsPlaying && !selection.isWhite()) {
								board.remove(selection);
								if (board.returnPiece(casted_x, casted_y) != null) {
									board.kill(board.returnPiece(casted_x, casted_y));
								}
								result = selection.Move(casted_x, casted_y);
								board.recordPos(selection);
							} else {
								System.out.println("That move is invalid.");

							}

						}

					}

				}

			}
			System.out.println("Thanks for playing! type anything to exit.");
			String end = scan.next();
			scan.close();

		}

	}
}

class Pawn extends Pieces {
	String type = "Pawn";
	boolean Moved = false;
	boolean doubleMoved = false;

	public Pawn(int x, int y, boolean white, Board b) {
		super(x, y, white, b);
	}

	boolean doubleMoved() {
		return doubleMoved;
	}

	boolean Move(int x, int y) {
		if (!state) {
			return false;
		}
		if (((White && y - yPos == 2) || (!White && yPos - y == 2)) && !Moved) {
			if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
			yPos = y;
			xPos = x;
			Moved = true;
			doubleMoved = true;
			this.updateLOS();
			return true;
			} else {
				System.out.println("There is a friendly piece in that square.");
				return false;
			}
		} else if ((White && y - yPos == 1) || (!White && yPos - y == 1)) {
			if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
			yPos = y;
			xPos = x;
			Moved = true;
			doubleMoved = false;
			this.updateLOS();
			return true;
			} else {
				System.out.println("There is a friendly piece in that square.");
				return false;
			}

		} else if ((board.returnPiece(x, (y + 1)).returnType() == "Pawn") && board.returnPiece(x, (y + 1)).doubleMoved()) {
			if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
			yPos = y;
			xPos = x;
			this.updateLOS();
			return true;
			} else {
				System.out.println("There is a friendly piece in that square.");
				return false;
			}
		} else {
			System.out.println("That move is invalid.");
			return false;
		}
	}

	void updateLOS() {
		this.canAttack.clear();
		PTuple temp = new PTuple(xPos + 1, yPos + 1);
		PTuple temp2 = new PTuple(xPos - 1, yPos + 1);
		if(temp.getValues(true) > 0 && temp.getValues(true) < 9 && temp.getValues(false) > 0 && temp.getValues(false) < 9) {
		this.canAttack.add(temp);
		}
		if(temp2.getValues(true) > 0 && temp2.getValues(true) < 9 && temp2.getValues(false) > 0 && temp2.getValues(false) < 9) {
		this.canAttack.add(temp2);
		}
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	String returnType() {
		String temp = this.type;
		return temp;
	}

	boolean isWhite() {
		return White;
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white pawn was captured!");
		} else {
			System.out.println("A black pawn was captured!");
		}

	}
}

class Bishop extends Pieces {
	String type = "Bishop";

	public Bishop(int x, int y, boolean white, Board board) {

		super(x, y, white, board);

	}

	boolean Move(int x, int y) {
		if (White && (xPos - x == yPos - y) || (!White && (x - xPos == y - yPos))) {
			if (board.notBlocked(this, x, y)) {
				xPos = x;
				yPos = y;
				this.updateLOS();
				return true;
			} else {
				System.out.println("There is a piece in the way.");
			}

		} else {
			System.out.println("That move is invalid.");
		}

		return false;
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white Bishop was captured!");
		} else {
			System.out.println("A black Bishop was captured!");
		}

	}

	String returnType() {
		String temp = this.type;
		return temp;
	}

	boolean isWhite() {
		return White;
	}

	void updateLOS() {
		this.canAttack.clear();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				if (White && (xPos - x == yPos - y) || (!White && (x - xPos == y - yPos))) {
					if (xPos < x) {
						if (board.notBlocked(this, x, y)) {
							this.canAttack.add(new PTuple(x,y));
						} 
					}
				}
			}
		}
	}
}


class Rook extends Pieces {
	boolean neverMoved = true;
	String type = "Rook";

	public Rook(int x, int y, boolean white, Board board) {
		super(x, y, white, board);
	}

	boolean Move(int x, int y) {
		if (x != xPos || y == yPos) {
			boolean notBlocked = board.notBlocked(this, x - 1, y);
			if (notBlocked) {
				xPos = x;
				this.updateLOS();
				return true;
			}
		} else if (x == xPos || y != yPos) {
			boolean	notBlocked = board.notBlocked(this, x, y - 1);
			if (notBlocked) {
				yPos = y;
				this.updateLOS();
				return true;
			}

		} else {
			System.out.println("That move is invalid.");
			return false;
		}
		return false;
	}

	void updateLOS() {
		this.canAttack.clear();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				if(x == xPos && y == yPos) {
					continue;
				}
				if (x != xPos && y == yPos) {
					boolean notBlocked = board.notBlocked(this, x, y);
					if (notBlocked) {
						this.canAttack.add(new PTuple(x, y));
					}
				} else if (x == xPos && y != yPos) {
					boolean notBlocked = board.notBlocked(this, x, y);			
					if (notBlocked) {
						this.canAttack.add(new PTuple(x, y));
					}

				}
			}
		}
	}

	boolean didNotMove() {
		return neverMoved;
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white Rook was captured!");
		} else {
			System.out.println("A black Rook was captured!");
		}
	}

	String returnType() {
		String temp = this.type;
		return temp;
	}

	boolean isWhite() {
		return White;
	}
}


class King extends Pieces {
	boolean neverMoved = true;
	String type = "King";
	public King(int x, int y, boolean white, Board board) {

		super(x, y, white, board);
	}

	void updateLOS() {
		this.canAttack.clear();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				if(x == xPos && y == yPos) {
					continue;
				}
				if ((Math.abs(xPos - x) <= 1) && (Math.abs(yPos - y) <= 1)
						&& board.LOSContains(x, y, !this.isWhite()) == false) {
					if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
					this.canAttack.add(new PTuple(x, y));
					}
				}
			}
		}

	}

	boolean Move(int x, int y) {
		if ((Math.abs(xPos - x) <= 1) && (Math.abs(yPos - y) <= 1)
				&& board.LOSContains(x, y, !this.isWhite()) != true) {
			if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
			xPos = x;
			yPos = y;
			this.updateLOS();
			return true;
			} else {
				System.out.println("There is a friendly piece in that square.");
				return false;
			}
			
		} else {
			boolean noPieces = true;
			for (int pos = xPos; pos < x; pos++) {
				Pieces result = board.returnPiece(yPos, pos);
				if (result == null) {
					noPieces = true;
				} else {
					System.out.println("There is a piece in the way.");
					return false;
				}

			}

			if (noPieces
					&& (board.returnPiece(yPos, x + 1).returnType() == "Rook"
					&& board.LOSContains(x, y, !this.isWhite()) != true)
					&& board.returnPiece(yPos, x + 1).didNotMove()) {
				board.returnPiece(yPos, x + 1).Move(y, x - 1);
				xPos = x;
				yPos = y;
				this.updateLOS();
				board.returnPiece(yPos, x + 1).updateLOS();
				return true;
			}

		}

		return false;
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white King was captured!");
		} else {
			System.out.println("A black King was captured!");
		}

	}

	String returnType() {
		String temp = this.type;
		return temp;
	};

	boolean isWhite() {
		return White;
	}
}

class Queen extends Pieces {
	String type = "Queen";

	public Queen(int x, int y, boolean white, Board board) {

		super(x, y, white, board);
	}

	boolean Move(int x, int y) {
		if ((x == xPos) && (y != yPos) || (x != xPos && y == yPos)) {
			if (board.notBlocked(this, x, y)) {
				xPos = x;
				yPos = y;
				this.updateLOS();
				return true;
			} 
		} else if (Math.abs(x - xPos) == Math.abs(y - yPos)) {
			if(board.notBlocked(this, x, y)) {
				xPos = x;
				yPos = y;
				this.updateLOS();
				return true;
			}	
		}  else if (White && (xPos - x == yPos - y) || (!White && (x - xPos == y - yPos))) {
			if (board.notBlocked(this, x, y)) {
				xPos = x;
				yPos = y;
				this.updateLOS();
				return true;
			} else {
				System.out.println("There is a piece in the way.");
			}
		}
		System.out.println("That move is invalid.");
		return false;
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white Queen was captured!");
		} else {
			System.out.println("A black Queen was captured!");
		}

	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	String returnType() {
		String temp = this.type;
		return temp;
	}

	boolean isWhite() {
		return White;
	}

	void updateLOS() {
		this.canAttack.clear();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				if(x == xPos && y == yPos) {
					continue;
				}
				if ((x == xPos) && (y != yPos) || (x != xPos && y == yPos)) {
					if (board.notBlocked(this, x, y)) {
						this.canAttack.add(new PTuple(x,y));
					} 
				} else {
					if (Math.abs(x - xPos) == Math.abs(y - yPos)) {
						if(board.notBlocked(this, x,y)) {
							this.canAttack.add(new PTuple(x,y));
						}	
					}
				}
			}
		}
	}
}


class Horse extends Pieces {
	String type = "Horse";

	public Horse(int x, int y, boolean white, Board board) {

		super(x, y, white, board);
	};

	boolean Move(int x, int y) {
		if ((Math.abs(xPos - x) == 1 && Math.abs(yPos - y) == 2)
				|| (Math.abs(xPos - x) == 2 && Math.abs(yPos - y) == 1)) {
			if(((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null)) {
			xPos = x;
			yPos = y;
			this.updateLOS();
			return true;
			} else {
				System.out.println("There is a friendly piece in that square.");
				return false;
			}
		} else {
			System.out.println("That move is invalid");
		}

		return false;
	}

	void updateLOS() {
		canAttack.clear();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				if(x == xPos && y == yPos) {
					continue;
				}
				if ((Math.abs(xPos - x) == 1 && Math.abs(yPos - y) == 2)
						|| (Math.abs(xPos - x) == 2 && Math.abs(yPos - y) == 1)) {
					if((board.returnPiece(x,y) != null && board.returnPiece(x,y).isWhite() != this.isWhite()) || board.returnPiece(x,y) == null) {
					canAttack.add(new PTuple(x, y));
					}
				}
			}
		}
	}

	void Die() {
		state = false;
		if (this.isWhite()) {
			System.out.println("A white Horse was captured!");
		} else {
			System.out.println("A black Horse was captured!");
		}
	}

	int returnPos() {
		PTuple Returnable = new PTuple(xPos, yPos);
		return Returnable.toInt();
	}

	String returnType() {
		String temp = this.type;
		return temp;
	}

	boolean isWhite() {
		return White;
	}

}

