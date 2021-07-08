package maze_solution;

import java.util.ArrayList;

public class MazeSolution {

	public static void main(String[] args){
		int[][] map = {
				{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1 },
				{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
				{ 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }};
		
		MapStructure QuestionMap = new MapStructure(map, map.length, new Point(0, 0), new Point(4, 5));
		new Algorithm().Go(QuestionMap);
		printAnswer(map);
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
	Point parent;	
	int Gx;		//代价函数
	int Hx;	//估计函数
	
	//节点坐标
	Point(int x, int y){
		this.coord = new Coord(x,y);
	}
	
	Point(Coord coord, Point parent, int Gx, int Hx){
		this.coord = coord;
		this.parent = parent;
		this.Gx = Gx;
		this.Hx = Hx;
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
}

class Algorithm{
	final static int bar = 1;
	final static int sign = 2;
	final static int value = 1; //只能上下左右移动
	
	ArrayList<Point> openList = new ArrayList<Point>();	//放入可移动节点
	ArrayList<Point> closeList = new ArrayList<Point>();	//放入父节点和障碍节点
	
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
		return true;
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
				goal.parent = current;
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
	
	void Path(int[][] map, Point end){
		int path = 1;
		System.out.println("The shortest path needs " + end.Gx + " steps.");
		while(end != null){
			for(Point point : openList){
				if(point.Hx == end.Hx && point.Gx == end.Gx){
					path++;
//					System.out.println("point.xy: " + point.coord.x + ", " + point.coord.y +  " end.xy: " 
//							+ end.coord.x + ", " + end.coord.y);
				}	
			}
			map[end.coord.y][end.coord.x] = sign;
			end = end.parent;
		}
		System.out.println("The shortest path number is " + path);
		if(isIncloseList(9,8))
			System.out.println("yes");
		else
			System.out.println("no");
	}
	
	void Go(MapStructure map){
		if(map == null){
			System.out.println("The map structure is wrong!");
			return;
		}
		openList.clear();
		closeList.clear();
		
		openList.add(map.start);
		movePoint(map);
	}
	
	Point findMinInopenList(){
		Point goal = openList.get(0);
		for(Point point : openList){
			if(point.Gx + point.Hx < goal.Gx + point.Hx)
				goal = point;
		}
		return goal;
	}
	
	void movePoint(MapStructure map){
//		int count = 0;
		while(!openList.isEmpty()){
			Point goal = findMinInopenList();
			openList.remove(goal);
			closeList.add(goal);
			addPointInopenList(map, goal);
//			count++;
			if(isIncloseList(map.end.coord.x, map.end.coord.y)){
				Path(map.map, map.end);
				System.out.println("This is the shortest path.");
				break;
			}
//			System.out.println("movePoint runs:" + count);
		}
	}
}
















