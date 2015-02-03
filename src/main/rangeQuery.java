package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import struct.point;
import Grid.Gpoint;
import QuadTree.Qpoint;
import QuadTree.QuadTree;

public class rangeQuery {
	private String datafile = "data0/Gaussian_10w.txt";
	QuadTree Qtree;
	public static void main(String[] args) {	
		new rangeQuery();
	}
	public rangeQuery() {
		double[] most_topright = { 1000000, 1000000 };
		double[] most_downleft = { 0, 0 };
		Qtree = new QuadTree(20, most_downleft, most_topright);
		readfile(datafile);
		
		double[] leftdown = new double[]{507463.0307-3982, 487244.0942-3982};
		double[] righttop = new double[]{507463.0307,487244.0942};
		
		List<point> pointlist = new ArrayList<point>();
		double value = Qtree.rangQuery(leftdown, righttop, pointlist);
		
		System.out.println(value);
		
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

}
