package Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GNode {
	private double value;
	private List<Gpoint> pointlist = new ArrayList<Gpoint>();
	private Set<Double> vline = new HashSet<Double>();//vertical line
	private Set<Double> hline = new HashSet<Double>();//horizontal line
	
	GNode(){
		this.value=0;
	}
	void add_Gpoint(Gpoint p){
		pointlist.add(p);
		add_value(p.value);
		vline.add(p.xcoordinate);
		hline.add(p.ycoordinate);
	}

	void add_value(int value){
		this.value += value;
	}

	void clear(){
		this.value = 0;
		pointlist = new ArrayList<Gpoint>();
		vline = new HashSet<Double>();
		hline = new HashSet<Double>();
	}

	void add_vline(double e){
		vline.add(e);
	}
	void add_hline(double e){
		hline.add(e);
	}
	public double getvalue() {
		return this.value;
	}
	public Set<Double> getVline(){
		return this.vline;
	}
	public Set<Double> getHline() {
		return this.hline;
	}
	public List<Gpoint> getPoints(){
		return pointlist;
	}

	
}
