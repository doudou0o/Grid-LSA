package Grid;

import java.util.HashMap;
import java.util.List;

import struct.point;
import calculate.calculation;

public class Grid {
	private static double x_Interval,y_Interval;
	private static double[] CENTRE;
	private static double[] Max_Range;
	public GNode[][] grid_top_right;
	public GNode[][] grid_top_left;
	public GNode[][] grid_down_left;
	public GNode[][] grid_down_right;

	public Grid(double[] most_topright,double[] most_downleft,
			double[] range,double[] Max_Range,double[] centre){
		x_Interval=range[0];
		y_Interval=range[1];
		Grid.CENTRE = centre;
		Grid.Max_Range = Max_Range;
		
		create_subgrid(most_topright[0]-centre[0], most_topright[1]-centre[1], range, 1);
		create_subgrid(centre[0]-most_downleft[0], most_topright[1]-centre[1], range, 2);
		create_subgrid(centre[0]-most_downleft[0], centre[1]-most_downleft[1], range, 3);
		create_subgrid(most_topright[0]-centre[0], centre[1]-most_downleft[1], range, 4);	
	}
	public void Insert_Gpoint(Gpoint p) {
		double Dx = p.xcoordinate - CENTRE[0];
		double Dy = p.ycoordinate - CENTRE[1];
		
		if (Dx >= 0 && Dy >= 0) {
			if (grid_top_right != null) 
			InsertGpoint2subgrid(1,Dx,Dy,p); 
		}
		if (Dx <= 0 && Dy >= 0) {
			if (grid_top_left != null)
			InsertGpoint2subgrid(2,-Dx,Dy,p); 
		}
		if (Dx <= 0 && Dy <= 0) {
			if (grid_down_left != null)
			InsertGpoint2subgrid(3,-Dx,-Dy,p); 
		}
		if (Dx >= 0 && Dy <= 0) {
			if (grid_down_right != null)
			InsertGpoint2subgrid(4,Dx,-Dy,p); 
		}
		
	
	}
	public int rangQuery(double[] leftdown,double[] righttop, List<point> pointlist) {
		int value=0;
		double close[]=new double[2];double far[]=new double[2];
		HashMap<Integer, Gpoint> Map=new HashMap<Integer, Gpoint>();
		
		if (grid_top_right!= null&&(righttop[0]-CENTRE[0])>0 && (righttop[1]-CENTRE[1])>0) {
			far[0]=righttop[0]-CENTRE[0];	far[1]=righttop[1]-CENTRE[1];
			close[0]=leftdown[0]-CENTRE[0];	close[1]=leftdown[1]-CENTRE[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(grid_top_right,close,far,Map,leftdown,righttop);
		}
		if (grid_top_left!=null&&(CENTRE[0]-leftdown[0])>0 && (righttop[1]-CENTRE[1])>0) {
			far[0]=CENTRE[0]-leftdown[0];	far[1]=righttop[1]-CENTRE[1];
			close[0]=CENTRE[0]-righttop[0];	close[1]=leftdown[1]-CENTRE[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(grid_top_left,close,far,Map,leftdown,righttop);
		}
		if (grid_down_left!=null&&(CENTRE[0]-leftdown[0])>0 && (CENTRE[1]-leftdown[1])>0) {
			far[0]=CENTRE[0]-leftdown[0];	far[1]=CENTRE[1]-leftdown[1];
			close[0]=CENTRE[0]-righttop[0];	close[1]=CENTRE[1]-righttop[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(grid_down_left,close,far,Map,leftdown,righttop);
		}
		if (grid_down_right!=null&&(righttop[0]-CENTRE[0])>0 && (CENTRE[1]-leftdown[1])>0) {
			far[0]=righttop[0]-CENTRE[0];	far[1]=CENTRE[1]-leftdown[1];
			close[0]=leftdown[0]-CENTRE[0];	close[1]=CENTRE[1]-righttop[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(grid_down_right,close,far,Map,leftdown,righttop);
		}
		for (Gpoint gp : Map.values()) {
			pointlist.add(gp);
		}
		return value;
	}
	public void delete() {		
	}
	public void updatescore(double aerfa,double beita) {
		calculation cal = new calculation(aerfa, beita);
		if (grid_top_right != null){
		for (int i = 0; i < grid_top_right.length; i++) {
			GNode [] grid_row = grid_top_right[i];
			for (int j = 0; j < grid_row.length; j++) {
				grid_top_right[i][j].updatescore(cal);
			}
		}
		}
		
		if (grid_top_left != null){
		for (int i = 0; i < grid_top_left.length; i++) {
			GNode [] grid_row = grid_top_left[i];
			for (int j = 0; j < grid_row.length; j++) {
				grid_top_left[i][j].updatescore(cal);
			}
		}
		}
		
		if (grid_down_left != null){
		for (int i = 0; i < grid_down_left.length; i++) {
			GNode [] grid_row = grid_down_left[i];
			for (int j = 0; j < grid_row.length; j++) {
				grid_down_left[i][j].updatescore(cal);
			}
		}
		}
		
		if (grid_down_right != null){
		for (int i = 0; i < grid_down_right.length; i++) {
			GNode [] grid_row = grid_down_right[i];
			for (int j = 0; j < grid_row.length; j++) {
				grid_down_right[i][j].updatescore(cal);
			}
		}
		}
	}	
/*	public List<GNode> changetoArraylist(int subgrid) {
	List<GNode> ans=new ArrayList<>();
	
	for (int i = 0; i < MAX_x; i++) {
		for (int j = 0; j < MAX_y; j++) {
			ans.add(grid[i][j]);
		}
	}
	ComparatorIData compare = new ComparatorIData();
	System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	Collections.sort(ans,compare);
	return ans;
	}
*/	
    
    private void create_subgrid(double gridLength,double gridWidth,double[] range,int subgrid) {
		if (gridLength <=0 || gridWidth<=0) {
			return ;
		}
    	int cell_number_x = (int) (gridLength/range[0])+1;
		int cell_number_y = (int) (gridWidth/range[1])+1;
		switch (subgrid) {
		case 1: grid_top_right = new GNode[cell_number_y][cell_number_x];
			break;
		case 2: grid_top_left = new GNode[cell_number_y][cell_number_x];
			break;
		case 3: grid_down_left = new GNode[cell_number_y][cell_number_x];
			break;
		case 4: grid_down_right = new GNode[cell_number_y][cell_number_x];
			break;
		}
		
		init_subgrid(subgrid,cell_number_x,cell_number_y);
	}
    private void init_subgrid(int subgrid, int cell_number_x, int cell_number_y) {
		GNode[][] subgrid_G = null;
		switch (subgrid) {
		case 1: subgrid_G = grid_top_right;
			break;
		case 2: subgrid_G = grid_top_left;
			break;
		case 3: subgrid_G = grid_down_left;
			break;
		case 4: subgrid_G = grid_down_right;
			break;
		}
		 
		for (int i = 0; i < cell_number_y; i++) {
			for (int j = 0; j < cell_number_x; j++) {
				double dis=0;
				double y_dis=0,x_dis=0;
				subgrid_G[i][j] = new GNode();
				if(0 != i )y_dis=y_Interval* i - Max_Range[1]*0.5;
				if(0 != j )x_dis=x_Interval* j - Max_Range[0]*0.5;
				
				dis = new calculation().distance_cal(x_dis,y_dis);
				subgrid_G[i][j].initdistance(dis);
			}
		}	
	}
	private void InsertGpoint2subgrid(int subgrid,double Dx,double Dy, Gpoint p) {
		int node_column = (int) (Dx/x_Interval);
		int node_row    = (int) (Dy/y_Interval);
		
		switch (subgrid) {
		case 1: grid_top_right[node_row][node_column].add_Gpoint(p);
				shadow(grid_top_right,node_row,node_column,p);
			break;
		case 2: grid_top_left[node_row][node_column].add_Gpoint(p);
				shadow(grid_top_left,node_row,node_column,p);
			break;
		case 3: grid_down_left[node_row][node_column].add_Gpoint(p);
				shadow(grid_down_left,node_row,node_column,p);
			break;
		case 4: grid_down_right[node_row][node_column].add_Gpoint(p);
				shadow(grid_down_right,node_row,node_column,p);
			break;
		}
	}
	private void shadow(GNode[][] subgrid_G, int node_row,
			int node_column, Gpoint p) {
		int max_row    = subgrid_G.length - 1 ;
		int max_column = subgrid_G[0].length - 1 ;
		Gpoint temp_p;
		//shadow up
		temp_p = new Gpoint(p.value, p.ID, p.xcoordinate, p.ycoordinate);
		if (node_row < max_row) {
			double shadowdistance = (node_row+1) * y_Interval - p.ycoordinate;
			if (shadowdistance <= Max_Range[1]){ 
				temp_p.ycoordinate = (node_row+1) * y_Interval ;
//				temp_p.ycoordinate = - temp_p.ycoordinate ;
				subgrid_G[node_row+1][node_column].add_shadow(temp_p);
			}
		}
		//shadow right
		temp_p = new Gpoint(p.value, p.ID, p.xcoordinate, p.ycoordinate);
		if (node_column < max_column) {
			double shadowdistance = (node_column+1) * x_Interval - p.xcoordinate;
			if (shadowdistance <= Max_Range[0]){ 
				temp_p.xcoordinate = (node_column+1) * x_Interval ;
//				temp_p.xcoordinate = - temp_p.xcoordinate;
				subgrid_G[node_row][node_column+1].add_shadow(temp_p);
			}
		}
		//shadow right-up
		temp_p = new Gpoint(p.value, p.ID, p.xcoordinate, p.ycoordinate);
		if (node_row < max_row && node_column < max_column) {
			double shadowdistance = (node_row+1) * y_Interval - p.ycoordinate;
			double shadowdistance2 = (node_column+1) * x_Interval - p.xcoordinate;
			if (shadowdistance <= Max_Range[1] && shadowdistance2 <= Max_Range[0]){ 
				temp_p.xcoordinate = (node_column+1) * x_Interval ;
				temp_p.ycoordinate = (node_row+1) * y_Interval ;
//				temp_p.xcoordinate = - temp_p.xcoordinate;
//				temp_p.ycoordinate = - temp_p.ycoordinate;
				subgrid_G[node_row+1][node_column+1].addspecialshadow(temp_p);
			}
		}
		
	}
	private int rangeQueryinsubgrid(GNode[][] subgrid, double[] close, double[] far, HashMap<Integer, Gpoint> ansmap, double[] leftdown, double[] righttop ){
		int value=0;
		int lowest = (int) (close[1]/y_Interval);
		int highest = (int) (far[1]/y_Interval);
		if (highest>=subgrid.length) highest = subgrid.length-1;
		int left = (int) (close[0]/x_Interval);
		int right = (int) (far[0]/x_Interval);
		if (right>=subgrid[0].length) right = subgrid[0].length-1;
		for (int x = lowest; x < highest+1; x++) {
			for (int y = left; y < right+1; y++) {
				for(Gpoint gp : subgrid[x][y].pointlist){
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
}
