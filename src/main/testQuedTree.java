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

public class testQuedTree {
	static QuadTree QTree;
	public static void main(String[] args) {
		double[] leftbottom = new double[2];
		double[] righttop = new double[2];
		leftbottom[0]=0;leftbottom[1]=0;
		righttop[0]=1000000;	righttop[1]=1000000;
		
		QTree = new QuadTree(20,leftbottom,righttop);
		readfile("data0/Gaussian_50w.txt");
		
		List<point> pointlist = new ArrayList<point>();
		double[] leftdown = {451000,451000};
		double[] rightt = {501000,501000};
		
		int ans = QTree.rangQuery(leftdown, rightt, pointlist);
		
		point a = pointlist.get(20);
		
		System.out.println(a.getID());
		System.out.println(a.ID);
		
		System.out.print("Test" + ans);
	}
	
	private static void readfile(String filePath){
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
					double xco=new Double(st.nextToken()).doubleValue();
					double yco=new Double(st.nextToken()).doubleValue();
					Qpoint p=new Qpoint(value, ID, xco, yco);
					QTree.Insert(p);
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
