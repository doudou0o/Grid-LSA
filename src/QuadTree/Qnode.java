package QuadTree;

import java.util.ArrayList;
import java.util.List;

import struct.point;

public class Qnode {
	private Qnode childQnode_1 = null;
	private Qnode childQnode_2 = null;
	private Qnode childQnode_3 = null;
	private Qnode childQnode_4 = null;
	private boolean isIndex;
	private List<Qpoint> poList;
	private int valueSum;
	
	private double[] range;//left,bottom,right,top
	
	public Qnode(double[] LeftBottom,double[] RightTop) {
		poList = new ArrayList<Qpoint>(QuadTree.MaxNumeber);
		this.isIndex = false;
		valueSum = 0 ;
		range = new double[4];
		this.range[0]=LeftBottom[0];
		this.range[1]=LeftBottom[1];
		this.range[2]=RightTop[0];
		this.range[3]=RightTop[1];
	}
	public Qnode(double Left,double Bottom,double Right,double Top) {
		poList = new ArrayList<Qpoint>(QuadTree.MaxNumeber);
		this.isIndex = false;
		valueSum = 0 ;
		range = new double[4];
		this.range[0]=Left;
		this.range[1]=Bottom;
		this.range[2]=Right;
		this.range[3]=Top;
	}
	
	public boolean isIndex() {
		return isIndex;
	}
	
	public List<Qpoint> getall() {
		return poList;
	}
	public int getValue() {
		return valueSum;
	}
	public void insert(Qpoint p) {
		valueSum += p.value;
		if (isIndex) {
			distributeTOchilds(p);
		}
		else{
			poList.add(p);
			if (poList.size() > QuadTree.MaxNumeber) 
				split();
		}
	}
	public int rangeQuery(double[] queryLD, double[] queryRT,List<point> pointlist,boolean isOverlap){
		int ans = 0;
		if (isIndex) {
			if (isOverlap) {
				ans += childQnode_1.rangeQuery(queryLD, queryRT, pointlist, isOverlap);
				ans += childQnode_2.rangeQuery(queryLD, queryRT, pointlist, isOverlap);
				ans += childQnode_3.rangeQuery(queryLD, queryRT, pointlist, isOverlap);
				ans += childQnode_4.rangeQuery(queryLD, queryRT, pointlist, isOverlap);
			}
			else{
				ans += openChild(queryLD, queryRT, pointlist,childQnode_1);
				ans += openChild(queryLD, queryRT, pointlist,childQnode_2);
				ans += openChild(queryLD, queryRT, pointlist,childQnode_3);
				ans += openChild(queryLD, queryRT, pointlist,childQnode_4);
			}
		}
		else{
			if(isOverlap){
				ans = valueSum;
				pointlist.addAll(getall());
			}
			else
				ans = findpoints( queryLD , queryRT , pointlist);
		}
		return ans;
	}
	
	private int openChild(double[] queryLD, double[] queryRT, List<point> pointlist, Qnode child) {
		int ans=0;
		if (child.range[0] >= queryLD[0] && child.range[1] >= queryLD[1] &&
			child.range[2] <= queryRT[0] && child.range[3] <= queryRT[1]	)
		//overlap
			ans = child.rangeQuery(queryLD, queryRT, pointlist, true);
		else if (isIntersect(queryLD,queryRT,child)) {
		//intersect
			ans = child.rangeQuery(queryLD, queryRT, pointlist, false);
		}
		return ans;
	}
	private boolean isIntersect(double[] queryLD, double[] queryRT, Qnode child) {
		if( Abs((child.range[0]+child.range[2]) - (queryLD[0]+queryRT[0]))/2 <= (child.range[2]-child.range[0] + queryRT[0]-queryLD[0])/2 && 
			Abs((child.range[1]+child.range[3]) - (queryLD[1]+queryRT[1]))/2 <= (child.range[3]-child.range[1] + queryRT[1]-queryLD[1])/2 )
			return true;
		return false;
	}
	private double Abs(double a) {
		if (a<0) a = -a;
		return a;
	}
	private int findpoints(double[] queryLD, double[] queryRT, List<point> pointlist) {
		if (poList == null)
			return 0;
		int ans=0;
		for (int i = 0; i < poList.size(); i++) {
			Qpoint p = poList.get(i);
			if (p.xcoordinate >= queryLD[0] && p.xcoordinate <= queryRT[0] &&
				p.ycoordinate >= queryLD[1] && p.ycoordinate <= queryRT[1]	)
			{
					pointlist.add(p);
					ans += p.value;
			}
		}
		return ans;
	}
	private void split() {
		isIndex=true;
		createChilds();
		for (int i = 0; i < poList.size(); i++) {
			Qpoint p = poList.get(i);
			distributeTOchilds(p);
		}
		poList.clear();
		poList = null;
	}
	
	private void distributeTOchilds(Qpoint p) {
		double center_x = ( range[0] + range[2] )/2;
		double center_y = ( range[1] + range[3] )/2;
		if (p.xcoordinate >= center_x) {
			// point in two right
			if (p.ycoordinate >= center_y) {
				childQnode_1.insert(p);
			}
			else childQnode_4.insert(p);
		}
		else{
			// point in two left
			if (p.ycoordinate >= center_y) {
				childQnode_2.insert(p);
			}
			else childQnode_3.insert(p);
		}
	}
	private void createChilds() {
		double center_x = ( range[0] + range[2] )/2;
		double center_y = ( range[1] + range[3] )/2;
		childQnode_1 = new Qnode(center_x,center_y,range[2],range[3]);
		childQnode_2 = new Qnode(range[0],center_y,center_x,range[3]);
		childQnode_3 = new Qnode(range[0],range[1],center_x,center_y);
		childQnode_4 = new Qnode(center_x,range[1],range[2],center_y);
	}
}
