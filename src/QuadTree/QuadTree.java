package QuadTree;

import java.util.List;

import struct.point;

public class QuadTree {
	protected static int MaxNumeber;
	private Qnode root;
	public QuadTree(int max,double[] LeftBottom, double[] RightTop) {
		MaxNumeber = max;
		root = new Qnode(LeftBottom, RightTop);
	}
	public void Insert(Qpoint p){
		root.insert(p);
	}
	public int rangQuery(double[] leftdown,double[] righttop, List<point> pointlist){
		return root.rangeQuery(leftdown,righttop,pointlist,false);
	}
	
}
