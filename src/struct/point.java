package struct;

public class point {
	public int value;
	public int ID;
	public double xcoordinate;
	public double ycoordinate;

	public int getID() {
		return ID;
	}
	
	public int getValue() {
		return value;
	}

	public double getXcoordinate() {
		return xcoordinate;
	}

	public double getYcoordinate() {
		return ycoordinate;
	}
	@Override
	public boolean equals(Object obj) {
		point po = (point) obj;
		if (po.xcoordinate == this.xcoordinate
				&& po.ycoordinate == this.ycoordinate) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		Double x = new Double(xcoordinate*1000000+ycoordinate);
		return x.hashCode();
	}
}
