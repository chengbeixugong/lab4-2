//package com.surpriseshelf.snake;
/**
* 游戏Model类负责所有游戏相关数据及运行
* @author WangYu
* @version 1.0
* Description:
* </pre>
* Create on :Date :2005-6-13  Time:15:58:33
* LastModified:
* History:
*/
//SnakeModel.java
import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
class Model extends Observable implements Runnable {
   boolean[][] matrix;                         // matrix of food
   LinkedList<Node> nodeArray = new LinkedList<Node>();    // body
   Node food;
   int xMax;
   int yMax;
   int direction = 4;
   boolean runState = false;                    // state of runState

   int timeInterval = 20;
   double speedChangeRate = 0.1;
   boolean paused = false;

   int score = 0;                              // 得分
   int countMove = 0;                          // 吃食物前移动次数
   // define direction flags;
   public static final int LEFT = 1;
   public static final int UP = 2;
   public static final int RIGHT = 3;
   public static final int DOWN = 4;

   public Model( int xMax, int yMax) {
       this.xMax = xMax;
       this.yMax = yMax;
       reset();
       int m=10,n=0;
       n=m%3;
   }

   public void reset(){
       direction = Model.UP;
       timeInterval = 200;
       paused = false;
       score = 0;
       countMove = 0;

       // initial matrix, 全部清0
       matrix = new boolean[xMax][];
       for (int i = 0; i < xMax; ++i) {
           matrix[i] = new boolean[yMax];
           Arrays.fill(matrix[i], false);
       }

       // initial the snake
       int initArrayLength = xMax > 20 ? 10 : xMax / 2;
       nodeArray.clear();
       for (int i = 0; i < initArrayLength; ++i) {
           int x = xMax / 2 + i;
           int y = yMax / 2;
           nodeArray.addLast(new Node(x, y));
           matrix[x][y] = true;
       }

       food = createFood();
       matrix[food.x][food.y] = true;
   }

   public void changeDirection(int newDirection) {
       if (direction % 2 != newDirection % 2) {
           direction = newDirection;
       }
   }

   public boolean moveOn() {
       Node n = (Node) nodeArray.getFirst();
       int x = n.x;
       int y = n.y;

       // 根据方向增减坐标值
       switch (direction) {
           case UP:
               y--;
               break;
           case DOWN:
               y++;
               break;
           case LEFT:
               x--;
               break;
           case RIGHT:
               x++;
               break;
       }
       //to avoid crash into walls
       if(x<0){
//    	   y=Math.abs(Math.abs(y)-yMax);
    	   x = x + xMax;
    	   changeDirection(3);
       }
       if(x>=xMax){
    	   x = x - xMax;
       }
       if(y<0||y>=yMax){
    	   y=Math.abs(Math.abs(y)-yMax);
       }
       // 新坐标落有效范围内则进行处理
       if ((0 <= x && x < xMax) && (0 <= y && y < yMax)) {

           if (matrix[x][y]) {        // if food
               if (x == food.x && y == food.y) {
                   nodeArray.addFirst(food);
                   // score according to speed
                   int scoreGet = (10000 - 200 * countMove) / timeInterval;
                   score += scoreGet > 0 ? scoreGet : 10;
                   countMove = 0;
                   food = createFood();
                   matrix[food.x][food.y] = true;
                   return true;
               } else                  // eat yourself
                   return false;

           } else {                // no food
               nodeArray.addFirst(new Node(x, y));
               matrix[x][y] = true;
               n = (Node) nodeArray.removeLast();
               matrix[n.x][n.y] = false;
               countMove++;
               return true;
           }
       }
       return false;
   }

   public void run() {
       runState = true;
       while (runState) {
           try {
               Thread.sleep(timeInterval);
           } catch (Exception e) {
               break;
           }

           if (!paused) {
               if (moveOn()) {
                   setChanged();           // Model通知View数据已经更新
                   notifyObservers();
               } else {
                   JOptionPane.showMessageDialog(null,
                           "惨死",
                           "比赛结束",
                           JOptionPane.INFORMATION_MESSAGE);
                   break;
               }
           }
       }
       runState = false;
   }

   private Node createFood() {
       int x = 0;
       int y = 0;
       // 随机获取有效区域内与蛇体和食物重叠位置
       do {
           Random r = new Random();
           x = r.nextInt(xMax);
           y = r.nextInt(yMax);
       } while (matrix[x][y]);

       return new Node(x, y);
   }
   public void speedUp() {
       timeInterval *= speedChangeRate;
   }

   public void speedDown() {
       timeInterval /= speedChangeRate;
   }

   public void changePauseState() {
       paused = !paused;
   }

   public String toString() {
       String result = "";
       for (int i = 0; i < nodeArray.size(); ++i) {
           Node n = (Node) nodeArray.get(i);
           result += "[" + n.x + "," + n.y + "]";
       }
       return result;
   }
}

class Node {
   int x;
   int y;
   Node(int x, int y) {
       this.x = x;
       this.y = y;
   }
}
