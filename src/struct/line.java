package struct;

import java.util.ArrayList;
import java.util.List;

public class line {

	public Double coordinate;
	public List<pointInline> point_list;
//	public AVL<pointInline> pointAvl;

	public line(double dis, int id, double otherdis) {
		this.coordinate = dis;
		point_list = new ArrayList<pointInline>();
//		pointAvl = new AVL<pointInline>();
		add_pointinline(id,otherdis);
	}

	public void add_pointinline(int id, double otherdis) {
		point_list.add(new pointInline(id, otherdis));
//		pointAvl.insert(new pointInline(id, otherdis));
	}

	@Override
	public boolean equals(Object obj) {
		line o = (line) obj;
		if (o.coordinate == this.coordinate)
			return true;
		return false;
	}
	@Override
	public int hashCode() {
		return coordinate.hashCode();
	}

	public double is_valid(double Intersection, double max) {
		
		double min=9999999;
		for (int i = 0; i < point_list.size(); i++) {
			pointInline p = point_list.get(i);
			if (p.other_coordinate <= Intersection) {
				if ((Intersection - p.other_coordinate) < min)
					min = Intersection - p.other_coordinate;
			}
		}
		if (min > max || (9999999-min)<0.0001)
			min = -1;

		return min;
	}
//	public double FindnearestPos(double Intersection, double max) {
//		pointInline V = new pointInline(-1, Intersection);
//		pointInline z = new pointInline(-2, -1);
//		pointInline ans = pointAvl.findMaxUnderV(V, z);
//		
//		if(ans.other_coordinate == -1)return -1;
//		double D = Intersection - ans.other_coordinate;
//		if( D>max )return -1;
//		else return D;
//		
//	}
	
	class pointInline implements Comparable<pointInline> {
		int ID;
		double other_coordinate;

		public pointInline(int id, double oc) {
			this.ID = id;
			this.other_coordinate = oc;
		}
		public double getCoo() {
			return other_coordinate;
		}

		@Override
		public int compareTo(pointInline arg0) {
			if (this.other_coordinate < arg0.other_coordinate) 
				return -1;
			else if (this.other_coordinate > arg0.other_coordinate) 
				return 1;
			return 0;
		}
	}
}


