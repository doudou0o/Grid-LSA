package calculate;

import java.util.logging.Logger;

public class calculation {
	private double aerfa = 0.00001;
	private double beita = 0.000001;
	private static calculation cal = null;
	
	private calculation() {
	}
	public static calculation getInstance(){
		if (cal == null) {
			cal = new calculation();
		}
		return cal;
	}
	
	public double calculate_score(double size, double value){
		return value - size * beita;
	}
	
	public double calculate_score(double[] r1, double[] r2,double[] original_position,
			double[] original_Range, double[] cur_Range,  int value) {
		return value - calculate_planty(r1, r2,original_position, original_Range, cur_Range);
	}
	
	public double calculate_score(double distance, double size, double value) {		
		return value - distance*aerfa - size*beita;
	}

	public double getDistance(double[] pointx, double[] pointy) {
		double x_dis = Math.abs(pointx[0] - pointy[0]);
		double y_dis = Math.abs(pointx[1] - pointy[1]);
		return getEuclideanDistance(x_dis, y_dis);
	}
	
	public double getEuclideanDistance(double x_dis, double y_dis) {
		return Math.sqrt(Math.pow(x_dis, 2)+Math.pow(y_dis, 2));
	}

	private double calculate_planty(double[] r1, double[] r2, double[] original_position,
			double[] original_Range, double[] cur_Range) {
		double x_dis = (r1[0]+r2[0])/2 - original_position[0];
		double y_dis = (r1[1]+r2[1])/2 - original_position[1];
		if (x_dis <0) x_dis = -x_dis;
		if (y_dis <0) y_dis = -y_dis;
		double distance_cost = getEuclideanDistance(x_dis, y_dis) ;
		double size_cost = ((cur_Range[0]*cur_Range[1]) - (original_Range[0]*original_Range[1]));

		if (distance_cost<0 || size_cost<0) {
			System.err.println("cost is wrong" + r2[1]+" "+r1[1]);
		}
		return ((distance_cost * aerfa) + (size_cost * beita));
	}


}
