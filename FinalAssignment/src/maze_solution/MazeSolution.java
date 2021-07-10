package maze_solution;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeSolution {

	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the size and name of your maze");
		int n = in.nextInt();
		String filename = in.next();
		File file=new File(filename);
		
		Scanner input = new Scanner(file);
		int[][] map = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = input.nextInt();
			}
		}
		
		long startTime = System.currentTimeMillis();
//		int[][] map = {
//				{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
//				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//				{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1 },
//				{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
//				{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
//				{ 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
//				{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1 },
//				{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1 },
//				{ 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1 },
//				{ 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1 },
//				{ 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1 },
//				{ 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
//				{ 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
//				{ 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }};

		MapStructure QuestionMap = new MapStructure(map, map.length, new Point(0, 0),
				new Point(map.length-1, map.length-1));
		
		new Algorithm().Go(QuestionMap);
//		QuestionMap.setMapStructure(new Point(map.length-1, map.length-1), new Point(0, 0));
//		new Algorithm().Back(QuestionMap);	//从终点运行回去标记出所有的路径
		
		printAnswer(map);
		
		long endTime=System.currentTimeMillis();
		System.out.println("This program takes " + (endTime - startTime) + "ms.");
	}
	
	
	public static void printAnswer(int[][] maps)
	{
		for (int i = 0; i < maps.length; i++)
		{
			for (int j = 0; j < maps[i].length; j++)
			{
				System.out.print(maps[i][j] + " ");
			}
			System.out.println();
		}
	}

}

class Coord{
	int x;
	int y;
	
	Coord(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	boolean isEquals(Coord goal){
		if(goal.x == x && goal.y == y)
			return true;
		return false;
	}
}

class Point{
	Coord coord;
	Point last;	
	int Gx;		//移动距离
	int Hx;	//离终点的距离
	int air;
	
	//节点坐标
	Point(int x, int y){
		this.coord = new Coord(x,y);
	}
	
	Point(Coord coord, Point parent, int Gx, int Hx){
		this.coord = coord;
		this.last = parent;
		this.Gx = Gx;
		this.Hx = Hx;
	}
	Point(int air){
		this.air = air;
	}
}

class MapStructure{
	int[][] map;
	int n;	//地图的宽和高
	Point start;
	Point end;
	
	MapStructure(int [][]map, int n, Point start,Point end){
		this.map = map;
		this.n = n;
		this.start = start;
		this.end = end;
	}
	
	void setMapStructure(Point start,Point end){
		this.start = start;
		this.end = end;
	}
}

class Algorithm{
	final static int bar = 1;
	final static int sign = 2;
	final static int value = 1; //只能上下左右移动
	
	ArrayList<Point> openList = new ArrayList<Point>();		//放入可移动的路径
	ArrayList<Point> closeList = new ArrayList<Point>();	//放入走过的路径
	
	boolean isEndCoord(Coord coord, Coord end){
		if(coord != null && end.isEquals(coord))
			return true;
		return false;
	}
	
	boolean canAddopenList(MapStructure map, int x, int y){
		if(x<0 || x>=map.n || y<0 || y>=map.n)
			return false;
		if(map.map[y][x] == bar)
			return false;
		if(isIncloseList(x, y))
			return false;
		if(isInopenList(x, y))
			return false;
		return true;
	}
	boolean isInopenList(int x, int y){
		for(Point point : openList){
			if(x == point.coord.x && y == point.coord.y)
				return true;
		}
		return false;
	}
	
	boolean isIncloseList(int x, int y){
		if(closeList.isEmpty())
			return false;
		for(Point point : closeList){
			if(x == point.coord.x && y == point.coord.y)
				return true;
		}
		return false;
	}
	
	int calcHx(Coord goal, Coord end){
		return Math.abs(goal.x - end.x) + Math.abs(goal.y - end.y);
	}
	
	void addPointInopenList(MapStructure map, Point current, int x, int y){
		if(canAddopenList(map, x, y)){
			Point end = map.end;
			Point goal = new Point(x, y);
			int Gx = current.Gx + value;
			int Hx = calcHx(current.coord, end.coord);
//			System.out.println("addPointInopenList➡canAddopenList");
			
			if(isEndCoord(goal.coord, end.coord)){
				goal = end;
				goal.last = current;
				goal.Gx = Gx;
				goal.Hx = calcHx(goal.coord, end.coord);
//				System.out.println("addPointInopenList➡canAddopenList➡isEndCoord.");
			}
			else
				goal = new Point(goal.coord, current, Gx, Hx);
			openList.add(goal);
//			System.out.println("addPointInopenList➡canAddopenList➡isNotEndCoord.");
		}
//		System.out.println("canNotAddopenList");
	}
	
	void addPointInopenList(MapStructure map, Point current){
		int x = current.coord.x;
		int y = current.coord.y;
		
		addPointInopenList(map, current, x-1, y);	//左
		addPointInopenList(map, current, x+1, y);	//右
		addPointInopenList(map, current, x, y-1);	//上
		addPointInopenList(map, current, x, y+1);	//下
	}

	void drawPath(int[][] map, Point end){
		while(end != null){
			map[end.coord.y][end.coord.x] = sign;
			end = end.last;
		}
	}
	
	void getPathInformation(int[][] map, Point end){
		int path = 1;
		System.out.println("The shortest path needs " + end.Gx + " steps.");
		while(end != null){
			for(Point point : openList){
				if(point.Hx == end.Hx && point.Gx == end.Gx){
					path++;
				}
			}
			map[end.coord.y][end.coord.x] = sign;
			end = end.last;
		}
		System.out.println("The shortest path number is " + path);
//		if(isIncloseList(9,8))
//			System.out.println("yes");
//		else
//			System.out.println("no");
	}
	
	//从地图左上角为所有的0赋予气
	void addBreathbyLeft(MapStructure map){
		int i = 0;
		int j = 0;
		Point zero = new Point(j, i);
		for (i = 1; i < map.n-1; i++) {
			for (j = 1; j < map.n-1; j++) {
				zero.air = 0;
				if(map.map[i][j] == 0){
					if(map.map[i][j+1] == 0)
						zero.air++;
					if(j >= 1 && map.map[i][j-1] == 0)
						zero.air++;
					if(map.map[i+1][j] == 0)
						zero.air++;
					if(i >= 1 && map.map[i-1][j] == 0)
						zero.air++;
				}
//				System.out.println("zero.air: " + zero.air);
				if(zero.air <= 1){		//判断迷宫中只有一格气的点，说明它是死胡同，将它改为1
					map.map[i][j] = bar;
//					System.out.println("0➡1");
				}
			}
		}
	}
	
	//从地图右下角为所有的0赋予气
		void addBreathbyRight(MapStructure map){
			int i = 0;
			int j = 0;
			Point zero = new Point(j, i);
			for (i = map.n-2; i > 0; i--) {
				for (j = map.n-2; j > 0; j--) {
					zero.air = 0;
					if(map.map[i][j] == 0){
						if(map.map[i][j+1] == 0)
							zero.air++;
						if(j >= 1 && map.map[i][j-1] == 0)
							zero.air++;
						if(map.map[i+1][j] == 0)
							zero.air++;
						if(i >= 1 && map.map[i-1][j] == 0)
							zero.air++;
					}
//					System.out.println("zero.air: " + zero.air);
					if(zero.air <= 1){		//判断迷宫中只有一格气的点，说明它是死胡同，将它改为1
						map.map[i][j] = bar;
//						System.out.println("0➡1");
					}
				}
			}
		}
	
	//从左上和右下两次遍历，将所有死胡同堵死
	void changeBreath(MapStructure map){
		for (int i = 0; i < map.map.length-1; i++) {
			addBreathbyLeft(map);
			addBreathbyRight(map);
		}
		System.out.println("Get rid of all dead paths (they don't have extra breath)");
	}
	
	void Go(MapStructure map){
		if(map == null){
			System.out.println("The map structure is wrong!");
			return;
		}
		openList.clear();
		closeList.clear();
		
		changeBreath(map);
		
		openList.add(map.start);
		movePoint(map);
		getPathInformation(map.map, map.end);
		System.out.println("This is all maze paths (The sign of the pathway is '2'):");
	}
	
	void Back(MapStructure map){
		openList.clear();
		closeList.clear();
		
		openList.add(map.start);
		movePoint(map);
	}
	
	Point findMinInopenList(){
		Point goal = openList.get(0);
		for(Point point : openList){
			if(point.Gx + point.Hx < goal.Gx + goal.Hx)
				goal = point;
		}
		return goal;
	}
	
	void movePoint(MapStructure map){
		while(!openList.isEmpty()){
			Point goal = findMinInopenList();
			openList.remove(goal);
			closeList.add(goal);
			addPointInopenList(map, goal);
			if(isIncloseList(map.end.coord.x, map.end.coord.y)){
				drawPath(map.map, map.end);
				break;
			}
		}
	}
}
















