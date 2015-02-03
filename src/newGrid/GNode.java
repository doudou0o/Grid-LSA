package newGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import QuadTree.Qpoint;
import QuadTree.QuadTree;
import calculate.calculation;

public class GNode {
	private double value;
	private double distance;
	public double upperboundscore;
	public List<Gpoint> pointlist;
	public List<Gpoint> shadowlist;
	public List<Gpoint> specialshadowlist;
	private List<Gpoint> queryPoints;
	private QuadTree QTree;
	private double[] LeftBottom;
	private double[] RightTop;
	
	GNode(){
		pointlist=new ArrayList<Gpoint>();
		shadowlist=new ArrayList<Gpoint>();
		specialshadowlist=new ArrayList<Gpoint>();
		queryPoints = new ArrayList<Gpoint>();
		this.QTree=null;
		this.value=0;
		this.distance=0;
		this.upperboundscore=0;
	}
	void add_Gpoint(Gpoint p){
		pointlist.add(p);
		add_value(p.value);
	}
	void add_shadow(Gpoint p){
		shadowlist.add(p);
		add_value(p.value);
	}
	void add_Querypoint(Gpoint p) {
		queryPoints.add(p);
	}
	void add_value(int value){
		this.value += value;
	}
	void updatescore(calculation c){
		upperboundscore = c.calculate_score(distance,0,value);
	}
	void initdistance(double dis) {
		this.distance = dis;
	}
	void initRange(double[] LeftBottom,double[] RightTop) {
		this.LeftBottom = LeftBottom;
		this.RightTop = RightTop;
	}
	void createQTree(int maxnum){
		this.QTree = new QuadTree(maxnum, this.LeftBottom, this.RightTop);
		queryPoints.addAll(pointlist);

		for(Gpoint p : queryPoints){
			this.QTree.Insert(new Qpoint(p.value, p.ID, p.xcoordinate, p.ycoordinate));
		}
	}
	int RangeQuery(double[] leftdown,double[] righttop,List ansList){
		return QTree.rangQuery(leftdown, righttop, ansList);
	}
	public void addspecialshadow(Gpoint p) {
		specialshadowlist.add(p);
		add_value(p.value);
	}
	public double getvalue() {
		return this.value;
	}
	public boolean hasQtree() {
		return (this.QTree!=null);
		
	}
}
