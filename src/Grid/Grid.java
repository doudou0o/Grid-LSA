package Grid;

import java.util.HashMap;
import java.util.List;

import struct.point;
import calculate.calculation;

public class Grid {
	private static double x_Interval,y_Interval;
	private static double[] Max_Range;
	public GNode[][] nodes;


	public Grid(double[] most_topright,double[] most_downleft,
			double[] nodesize,double[] Max_Range){
		x_Interval=nodesize[0];
		y_Interval=nodesize[1];
		Grid.Max_Range = Max_Range;
		
		create_nodes(most_topright[0]-most_downleft[0], most_topright[1]-most_downleft[0], nodesize);
	}
	public void Insert_Gpoint(Gpoint p) {
		int Dx = (int) (p.xcoordinate / x_Interval);
		int Dy = (int) (p.ycoordinate / y_Interval);
		
		nodes[Dx][Dy].add_Gpoint(p);
		
		if ((x_Interval*(Dx+1) - p.xcoordinate) < Max_Range[0])
			if((Dx+1)<nodes.length){
				nodes[Dx+1][Dy].add_value(p.value);
				nodes[Dx+1][Dy].add_hline(p.ycoordinate);
			}
		if ((y_Interval*(Dy+1) - p.ycoordinate) < Max_Range[1])
			if((Dy+1)<nodes[0].length){
				nodes[Dx][Dy+1].add_value(p.value);
				nodes[Dx][Dy+1].add_vline(p.xcoordinate);
			}
		if ((x_Interval*(Dx+1) - p.xcoordinate) < Max_Range[0] && (y_Interval*(Dy+1) - p.ycoordinate) < Max_Range[1])
			if((Dx+1)<nodes.length && (Dy+1)<nodes[0].length)
				nodes[Dx+1][Dy+1].add_value(p.value);
	
	}
	public int rangQuery(double[] close,double[] far, List<point> pointlist) {
		int value=0;
		int lowest = (int) (close[1]/y_Interval);
		int highest = (int) (far[1]/y_Interval);
		if (highest>=nodes.length) highest = nodes.length-1;
		int left = (int) (close[0]/x_Interval);
		int right = (int) (far[0]/x_Interval);
		if (right>=nodes[0].length) right = nodes[0].length-1;
		for (int x = lowest; x < highest+1; x++) {
			for (int y = left; y < right+1; y++) {
				for(Gpoint gp : nodes[x][y].getPoints()){
					if (gp.xcoordinate>=leftdown[0]&&gp.xcoordinate<=righttop[0]
							&&gp.ycoordinate>=leftdown[1]&&gp.ycoordinate<=righttop[1]) {
						if(ansmap.containsKey(gp.ID)){
							//System.out.println("warn: there is a repeating point. please watch");
							continue;
						}
						ansmap.put(gp.ID, gp);
						value += gp.value;
					}
				}
			}
		}
 		return value;
	}
	public void delete() {
	}

    private void create_nodes(double gridLength,double gridWidth,double[] size) {
		if (gridLength <=0 || gridWidth<=0) {
			System.err.println("Grid init wrong");
			return ;
		}
    	int cell_number_x = (int) (gridLength/size[0])+1;
		int cell_number_y = (int) (gridWidth/size[1])+1;
		
		nodes = new GNode[cell_number_x][cell_number_y];
		
		for (int i = 0; i < cell_number_y; i++) {
			for (int j = 0; j < cell_number_x; j++) {
				nodes[i][j] = new GNode();
			}
		}	
	}


}
