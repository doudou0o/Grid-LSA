package Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import calculate.calculation;

public class GNode {
	private double value;
	private double distance;
	public double upperboundscore;
	public List<Gpoint> pointlist;
	public List<Gpoint> shadowlist;
	public List<Gpoint> specialshadowlist;
	
	GNode(){
		pointlist=new ArrayList<Gpoint>();
		shadowlist=new ArrayList<Gpoint>();
		specialshadowlist=new ArrayList<Gpoint>();
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
	void add_value(int value){
		this.value += value;
	}
	void updatescore(calculation c){
		upperboundscore = c.calculate_score(distance,0,value);
	}
	void initdistance(double dis) {
		this.distance = dis;
	}
	public void addspecialshadow(Gpoint p) {
		specialshadowlist.add(p);
		add_value(p.value);
	}
	public double getvalue() {
		return this.value;
	}

	
}
