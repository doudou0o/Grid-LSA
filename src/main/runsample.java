package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import calculate.calculation;
import struct.line;
import struct.point;
import Grid.GNode;
import Grid.Gpoint;
import Grid.Grid;
import QuadTree.Qpoint;
import QuadTree.QuadTree;

public class runsample {
	private String datafile = "data0/Gaussian_10w.txt";
//	private String datafile = "data0/uniform_50w.txt";
	private String queryfile = "queries";

	private Grid g_object;;
	private QuadTree Qtree;

	private double ratio; // y/x
	private double aerfa;
	private double beita;
	private double[] original_Range;
	private double[] original_position;
	private double[] Max_Range;
	private double[] Node_Size;

	private double[] cross_point;// far-corner
	private double[] bestpos;
	private double[] bestrange;
	private double I;
	
	private double[] needR1 = new double[2];
	private static Logger logger = LogManager.getLogger(runsample.class
			.getName());

	public static void main(String[] args) throws IOException {
		new runsample();
	}

	private runsample() throws IOException {
		LineNumberReader lr = readQueryFile(queryfile);
		String line = null;
		line = lr.readLine();

		while (line != null) {
			logger.info("   ");
			logger.info( datafile ) ;
			logger.info("*********new query now*********");
			logger.info(line);
			Init();
			readoneLine(line);

			long start = System.currentTimeMillis();
			double[] most_topright = { 1000000, 1000000 };
			double[] most_downleft = { 0, 0 };

			g_object = new Grid(most_topright, most_downleft, Node_Size,
					Max_Range, original_position);
			Qtree = new QuadTree(20, most_downleft, most_topright);
			long middle = System.currentTimeMillis();

			readfile(datafile);
			g_object.updatescore(aerfa, beita);

			Grid_LSA();
			long end = System.currentTimeMillis();

			logger.error("**********");
			logger.error("I=" + I);
			logger.error("range= " + "length:" + bestrange[0] + "width:"
					+ bestrange[1]);
			logger.error("position= " + bestpos[0] + "," + bestpos[1]);
			logger.error("**********");
			logger.error("middle-Second: " + ((middle - start) / 1000.0f));
			logger.error(("Minutes: " + ((end - start) / 1000.0f) / 60.0f));
			logger.error("Second: " + ((end - start) / 1000.0f));
			logger.error("cross-point " + needR1[0]+" "+needR1[1]);

			line = lr.readLine();
		}

	}

	private void Grid_LSA() {
		my_query(g_object.grid_top_right ,1);
		my_query(g_object.grid_top_left  ,2);
		my_query(g_object.grid_down_left ,3);
		my_query(g_object.grid_down_right,4);
	}

	private void my_query(GNode[][] subgrid, int q) {
		if (subgrid == null)
			return;
		line linex[] = new line[1];
		line liney[] = new line[1];

		calculation calcu = new calculation(aerfa, beita);

		int maxnum = subgrid.length * subgrid[0].length;
		for (int index = 0; index < maxnum; index++) {
			GNode node = getNode(subgrid, index);
			if (node.upperboundscore <= I) {
				// prune nodes
				continue;
			}
			// create lines for node
			Strategy_line Sline = new Strategy_line(node);
			linex[0] = null;
			liney[0] = null;
			Iterator<Entry<Double, line>> itx = Sline.Hash_x.entrySet().iterator();
			Iterator<Entry<Double, line>> ity = Sline.Hash_y.entrySet().iterator();
			while (itx.hasNext() || ity.hasNext()) {
				// get one horizontal line and one vertical line
				if (!ity.hasNext()) {
					ity = Sline.Hash_y.entrySet().iterator();
					linex[0] = null;
				}
				Sline.choose(linex, liney, itx, ity);

				// prune lines
				double A = linex[0].is_valid(liney[0].coordinate, Max_Range[1]);
				double B = liney[0].is_valid(linex[0].coordinate, Max_Range[0]);
				if (A == -1 || B == -1)
					continue;

				// RangeQuery
				double[] r1 = new double[2];// right-top
				double[] r2 = new double[2];// left-down
				
				cross_point = new double[2];
				cross_point[0] = linex[0].coordinate;
				cross_point[1] = liney[0].coordinate;
		
				initRange(q,r1,r2);
				
				int valuesum = 0;
				List<point> pointlist = new ArrayList<point>();

					 valuesum = Qtree.rangQuery(r2, r1, pointlist);

				int getvalue = valuesum;

				// prune line
				double Max_score_theoretical = calcu.calculate_score(r1, r2,
						original_position, original_Range, original_Range,
						getvalue);
				if (Max_score_theoretical <= I)
					continue;
			
				ComparatorList compare = new ComparatorList();
				System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
				Collections.sort(pointlist, compare);
				
//				Sort(pointlist);
//				new QuickSort().quickSort(pointlist, Max_Range, r1);

				// Shrink
				double[] cur_Range = new double[2];
				cur_Range[0] = Max_Range[0];
				cur_Range[1] = Max_Range[1];
				int cur_value = getvalue;
				int iterator = 0;
				int listlenth = pointlist.size();
				int pre_point_value = 0;

				while (iterator < listlenth&& !(cur_Range[0] < A || cur_Range[1] < B || Max_score_theoretical < I)) 
				{
					if (cur_Range[0] < original_Range[0]) {//always calculate smallest one
						changeback_r(r1,r2,q);
						double cur_score = calcu.calculate_score(r1, r2,original_position, original_Range,original_Range, cur_value);
						if (cur_score > I) recordbest(cur_score, cur_Range, r1, r2);
						break;
					}
					double cur_score = calcu.calculate_score(r1, r2,original_position, original_Range,cur_Range, cur_value);
					if (cur_score > I) recordbest(cur_score, cur_Range, r1, r2);
					point cur_point = pointlist.get(iterator++);
					
					change_curRange_r2_value(r1, r2, cur_Range, cur_point,q);
					cur_value = cur_value - pre_point_value;
					Max_score_theoretical=Max_score_theoretical - pre_point_value;
					pre_point_value = cur_point.value;
				}
			}
		}
	}
	private void changeback_r(double[] r1, double[] r2, int q) {
		switch (q) {
		case 1:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1];
			break;
		case 2:
			r1[0] = cross_point[0] + original_Range[0];
			r1[1] = cross_point[1];
			break;
		case 3:
			r1[0] = cross_point[0] + original_Range[0];
			r1[1] = cross_point[1] + original_Range[1];
			break;
		case 4:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1] + original_Range[1];
			break;

		default:
			System.err.println("backr is error");
			break;
		}
		r2[0] = r1[0] - original_Range[0];
		r2[1] = r1[1] - original_Range[1];
	}

	private void initRange(int q, double[] r1, double[] r2) {
		switch (q) {
		case 1:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1];
			break;
		case 2:
			r1[0] = cross_point[0] + Max_Range[0];
			r1[1] = cross_point[1];
			break;
		case 3:
			r1[0] = cross_point[0] + Max_Range[0];
			r1[1] = cross_point[1] + Max_Range[1];
			break;
		case 4:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1] + Max_Range[1];
			break;

		default:
			System.err.println("initRange is error");
			break;
		}
		r2[0] = r1[0] - Max_Range[0];
		r2[1] = r1[1] - Max_Range[1];
	}


	private GNode getNode(GNode[][] subgrid, int index) {
		int posx = 0, posy = 0;
		posx = (int) (index / subgrid[0].length);
		posy = index - posx * subgrid[0].length;
		return subgrid[posx][posy];
	}

	private void readoneLine(String line) {
		StringTokenizer st = new StringTokenizer(line);
		int id = new Integer(st.nextToken()).intValue();
		if (id == 0);
		original_Range[0] = new Double(st.nextToken()).doubleValue();// orignal size
		original_Range[1] = new Double(st.nextToken()).doubleValue();// orignal size
		original_position[0] = new Double(st.nextToken()).doubleValue();// orignal position
		original_position[1] = new Double(st.nextToken()).doubleValue();// orignal position
		Max_Range[0] = new Double(st.nextToken()).doubleValue();// maxrange size
		Max_Range[1] = new Double(st.nextToken()).doubleValue();// maxrange size
		aerfa = new Double(st.nextToken()).doubleValue();// aerfa
		beita = new Double(st.nextToken()).doubleValue();// beita
		Node_Size[0] = new Double(st.nextToken()).doubleValue();// node size
		Node_Size[1] = new Double(st.nextToken()).doubleValue();// node size
		ratio = Max_Range[1] / Max_Range[0];
		if (ratio - (original_Range[1] / original_Range[0]) > 0.000001) {
			System.err.print("input wrong");
			System.err.print("ratio is not same");
			System.exit(-2);
		}
		I=0;
	}

	private LineNumberReader readQueryFile(String filename) {
		LineNumberReader lr = null;
		try {
			lr = new LineNumberReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open query file " + filename + ".");
			System.exit(-1);
		}
		return lr;
	}

	private void readfile(String filePath) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(lineTxt);
					int value = new Integer(st.nextToken()).intValue();
					int ID = new Integer(st.nextToken()).intValue();
					double xco = new Double(st.nextToken()).doubleValue();
					double yco = new Double(st.nextToken()).doubleValue();
					Gpoint p = new Gpoint(value, ID, xco, yco);
					Qpoint q = new Qpoint(value, ID, xco, yco);
					g_object.Insert_Gpoint(p);
					Qtree.Insert(q);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
	}

	private void recordbest(double cur_score, double[] cur_Range, double[] r1,
			double[] r2) {
		I = cur_score;
		bestrange[0] = cur_Range[0];
		bestrange[1] = cur_Range[1];
		bestpos[0] = (r1[0] + r2[0]) / 2;
		bestpos[1] = (r1[1] + r2[1]) / 2;
		
		needR1[0] = cross_point[0];
		needR1[1] = cross_point[1];
	}

	private void change_curRange_r2_value(double[] r1, double[] r2,
			double[] cur_Range, point cur_point,int q) {

		double temp_length = Math.abs( cross_point[0] - cur_point.xcoordinate );
		double temp_width  = Math.abs( cross_point[1] - cur_point.ycoordinate );
	
		if ((temp_length * ratio) <= temp_width) {
			cur_Range[0] = temp_width / ratio;
			cur_Range[1] = temp_width;
		} 
		else {
			cur_Range[0] = temp_length;
			cur_Range[1] = temp_length * ratio;
		}
		switch (q) {
		case 1:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1];
			break;
		case 2:
			r1[0] = cross_point[0] + cur_Range[0];
			r1[1] = cross_point[1];
			break;
		case 3:
			r1[0] = cross_point[0] + cur_Range[0];
			r1[1] = cross_point[1] + cur_Range[1];
			break;
		case 4:
			r1[0] = cross_point[0];
			r1[1] = cross_point[1] + cur_Range[1];
			break;
		default:
			break;
		}
		r2[0] = r1[0] - cur_Range[0];
		r2[1] = r1[1] - cur_Range[1];
	}

	private void Init() {
		original_Range = new double[2];
		original_position = new double[2];
		Max_Range = new double[2];
		Node_Size = new double[2];
		bestpos = new double[2];
		bestrange = new double[2];
		I = 0;
	}

	private class Strategy_line {
		private HashMap<Double, line> Hash_x;
		private HashMap<Double, line> Hash_y;

		// 构造函数
		public Strategy_line(GNode node) {
			Hash_x = new HashMap<Double, line>();
			Hash_y = new HashMap<Double, line>();
			for (int i = 0; i < node.pointlist.size(); i++) {
				Gpoint gp = node.pointlist.get(i);
				addtoHashset(gp.ID, gp.xcoordinate, gp.ycoordinate, gp.value);
			}
			for (int i = 0; i < node.shadowlist.size(); i++) {
				Gpoint gp = node.shadowlist.get(i);
				addtoHashset(gp.ID, gp.xcoordinate, gp.ycoordinate, gp.value);
			}
		}

		private void addtoHashset(int id, double x, double y, int value) {
			line temp;

			temp = Hash_x.get(x);
			if (temp != null) {
				temp.add_pointinline(id, y);
			} else
				Hash_x.put(x, new line(x, id, y));

			temp = Hash_y.get(y);
			if (temp != null) {
				temp.add_pointinline(id, x);
			} else
				Hash_y.put(y, new line(y, id, x));
		}

		private void choose(line[] linex, line[] liney, Iterator itx,
				Iterator ity) {
			Map.Entry<Double, line> entry;
			if (linex[0] == null) {
				entry = (Entry) itx.next();
				linex[0] = entry.getValue();
			}
			entry = (Entry) ity.next();
			liney[0] = entry.getValue();
		}
	}

	private class ComparatorList implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			point p1 = (point) o1;
			point p2 = (point) o2;
			double[] data1pos = new double[2];
			double[] data2pos = new double[2];
			data1pos[0] = p1.xcoordinate;
			data1pos[1] = p1.ycoordinate;
			data2pos[0] = p2.xcoordinate;
			data2pos[1] = p2.ycoordinate;
			changetoRelativepos(data1pos);
			changetoRelativepos(data2pos);
			double pos1, pos2;
			pos1 = min(data1pos[0], data1pos[1] * ratio);
			pos2 = min(data2pos[0], data2pos[1] * ratio);

			if (pos1 < pos2)
				return -1;
			if ((pos1 == pos2) && (p1.ID < p2.ID))
				return -1;
			return 1;
//			return p1.ID - p2.ID;
		}

		private void changetoRelativepos(double[] datapos) {
			double a = Max_Range[0] - Math.abs(cross_point[0] - datapos[0]);
			double b = Max_Range[1] - Math.abs(cross_point[1] - datapos[1]);
			datapos[0] = a;
			datapos[1] = b;
		}

		private double min(double a, double b) {
			return (a < b) ? a : b;
		}

	}
}
