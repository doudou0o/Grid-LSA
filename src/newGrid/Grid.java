package newGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import struct.point;
import calculate.calculation;

public class Grid {
	private static double x_Interval,y_Interval;
	private static double[] CENTRE;
	private static double[] Max_Range;
	private int Qtreemaxnum;
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
//		HashMap<Integer, Gpoint> Map=new HashMap<Integer, Gpoint>();
		
		if (grid_top_right!= null&&(righttop[0]-CENTRE[0])>0 && (righttop[1]-CENTRE[1])>0) {
			far[0]=righttop[0]-CENTRE[0];	far[1]=righttop[1]-CENTRE[1];
			close[0]=leftdown[0]-CENTRE[0];	close[1]=leftdown[1]-CENTRE[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(1,close,far,pointlist,leftdown,righttop);
		}
		if (grid_top_left!=null&&(CENTRE[0]-leftdown[0])>0 && (righttop[1]-CENTRE[1])>0) {
			far[0]=CENTRE[0]-leftdown[0];	far[1]=righttop[1]-CENTRE[1];
			close[0]=CENTRE[0]-righttop[0];	close[1]=leftdown[1]-CENTRE[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(2,close,far,pointlist,leftdown,righttop);
		}
		if (grid_down_left!=null&&(CENTRE[0]-leftdown[0])>0 && (CENTRE[1]-leftdown[1])>0) {
			far[0]=CENTRE[0]-leftdown[0];	far[1]=CENTRE[1]-leftdown[1];
			close[0]=CENTRE[0]-righttop[0];	close[1]=CENTRE[1]-righttop[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(3,close,far,pointlist,leftdown,righttop);
		}
		if (grid_down_right!=null&&(righttop[0]-CENTRE[0])>0 && (CENTRE[1]-leftdown[1])>0) {
			far[0]=righttop[0]-CENTRE[0];	far[1]=CENTRE[1]-leftdown[1];
			close[0]=leftdown[0]-CENTRE[0];	close[1]=CENTRE[1]-righttop[1];
			if (close[0]<0) close[0]=0; 	if (close[1]<0) close[1]=0;
			value += rangeQueryinsubgrid(4,close,far,pointlist,leftdown,righttop);
		}
//		for (Gpoint gp : Map.values()) {//TODO
//			pointlist.add(gp);
//		}
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
	public void setQtreemaxnum(int maxnum) {
		Qtreemaxnum = maxnum;
	}
    
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
		double isright=0,istop=0;
		switch (subgrid) {
		case 1: subgrid_G = grid_top_right;isright=1;istop=1;
			break;
		case 2: subgrid_G = grid_top_left;isright=-1;istop=1;
			break;
		case 3: subgrid_G = grid_down_left;isright=-1;istop=-1;
			break;
		case 4: subgrid_G = grid_down_right;isright=1;istop=-1;
			break;
		}
		 
		for (int i = 0; i < cell_number_y; i++) {
			for (int j = 0; j < cell_number_x; j++) {
				double dis=0;
				double y_dis=0,x_dis=0;
				double[] LeftBottom = new double[2];
				double[] RightTop = new double[2];
				
				subgrid_G[i][j] = new GNode();
				if(0 != i )y_dis=y_Interval* i - Max_Range[1]*0.5;
				if(0 != j )x_dis=x_Interval* j - Max_Range[0]*0.5;
				dis = new calculation().distance_cal(x_dis,y_dis);
				subgrid_G[i][j].initdistance(dis);
				
				double far1 = x_Interval * (j+1) * isright + CENTRE[0];
				double far2 = y_Interval * (i+1) * istop   + CENTRE[1];
				double close1 = (x_Interval * j - Max_Range[0])*isright +CENTRE[0];
				double close2 = (y_Interval * i - Max_Range[1])*istop   +CENTRE[1];
				switch (subgrid) {
					case 1: RightTop[0]=far1;RightTop[1]=far2;LeftBottom[0]=close1;LeftBottom[1]=close2;
						break;
					case 2: RightTop[0]=close1;RightTop[1]=far2;LeftBottom[0]=far1;LeftBottom[1]=close2;
						break;
					case 3: RightTop[0]=close1;RightTop[1]=close2;LeftBottom[0]=far1;LeftBottom[1]=far2;
						break;
					case 4: RightTop[0]=far1;RightTop[1]=close2;LeftBottom[0]=close1;LeftBottom[1]=far2;
						break;
				}
				subgrid_G[i][j].initRange(LeftBottom,RightTop);
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
				subgrid_G[node_row+1][node_column].add_Querypoint(p);
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
				subgrid_G[node_row][node_column+1].add_Querypoint(p);
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
				subgrid_G[node_row+1][node_column+1].add_Querypoint(p);
			}
		}
		
	}
/*	private int rangeQueryinsubgrid(GNode[][] subgrid, double[] close, double[] far, HashMap<Integer, Gpoint> ansmap, double[] leftdown, double[] righttop ){
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
						if(ansmap.containsKey(gp.ID))continue;
						ansmap.put(gp.ID, gp);
						value += gp.value;
					}
				}
			}
		}
 		return value;
	}*/
	private int rangeQueryinsubgrid(int subgrid_id, double[] close, double[] far, List<point> pointlist, double[] leftdown, double[] righttop ){
		int value=0;
		GNode[][] subgrid = null;
		switch (subgrid_id) {
		case 1: subgrid = grid_top_right;
			break;
		case 2: subgrid = grid_top_left;
			break;
		case 3:	subgrid = grid_down_left;
			break;
		case 4: subgrid = grid_down_right;
			break;
		}
		int lowest = (int) (close[1]/y_Interval);
		int highest = (int) (far[1]/y_Interval);
		if (highest>=subgrid.length) highest = subgrid.length-1;
		int left = (int) (close[0]/x_Interval);
		int right = (int) (far[0]/x_Interval);
		if (right>=subgrid[0].length) right = subgrid[0].length-1;
		for (int x = highest; x < highest+1; x++) {
			for (int y = right; y < right+1; y++) {
				if (!subgrid[x][y].hasQtree()) {
					subgrid[x][y].createQTree(Qtreemaxnum);
				}
				
//				List<point> ansList = new ArrayList<point>();
				value = subgrid[x][y].RangeQuery(leftdown, righttop, pointlist);
//				for (point p : ansList) {
//					if(pointlist.containsKey(p.ID)){
//						System.out.println(subgrid_id+"repeating");
//						continue;
//					}
//					pointlist.put(p.ID, new Gpoint(p.value, p.ID, p.xcoordinate, p.ycoordinate));
//					value+=p.value;
//				}
			}
		}
 		return value;
	}
	
}
