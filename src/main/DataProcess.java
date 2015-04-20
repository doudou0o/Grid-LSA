package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import struct.point;

public class DataProcess {
	Set<point> data = new HashSet<point>();
	
	boolean processData(double A,double B){
		point po = new point();
		po.xcoordinate = A;
		po.ycoordinate = B;
		boolean ans = data.contains(po);
		if (!ans) {
			data.add(po);
		}	
		return ans;
	}
	void destory(){
		data.clear();
	}
	
	public static void main(String[] args) {
		readfile("data0/NE.txt");
	}
	private static void readfile(String filePath) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				DataProcess DP = new DataProcess();int sum=0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(lineTxt);
					int value = new Integer(st.nextToken()).intValue();
					int ID = new Integer(st.nextToken()).intValue();
					double xco = new Double(st.nextToken()).doubleValue();
					double yco = new Double(st.nextToken()).doubleValue();
					
					if( DP.processData(xco, yco) ){
						System.out.println("ID="+ID+" x="+xco+" y="+yco);
						sum++;
						continue;
					}
//					Gpoint p = new Gpoint(value, ID, xco, yco);
//					Qpoint q = new Qpoint(value, ID, xco, yco);
//					g_object.Insert_Gpoint(p);
//					Qtree.Insert(q);
				}
				DP.destory();System.out.println(sum);
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
