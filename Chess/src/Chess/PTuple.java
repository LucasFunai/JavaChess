package Chess;

public class PTuple {
	int left;
	int right;
	String leftString;
	String RightString;

	PTuple(int x, int y) {
		left = x;
		right = y;
	};

	public PTuple(String string, int Int) {
		leftString = string;
		right = Int;
	}

	public int getValues(boolean Right) {
		if (Right) {
			return right;
		} else {
			return left;
		}
	}

	boolean isEqualTo(PTuple T) {
		if (T == null) {
			return false;
		}
		;
		if ((this.getValues(true) == T.getValues(true)) && (this.getValues(false) == T.getValues(false))) {
			return true;
		} else {
			return false;
		}
	};

	int toInt() {
		return ((this.getValues(false) * 10) + this.getValues(true));
	};
}