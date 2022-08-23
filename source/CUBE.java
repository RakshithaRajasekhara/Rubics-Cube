import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CUBE extends PApplet {



PeasyCam cam;
float speed=.25f;

int dim=3;
Cubie[] cube=new Cubie[dim*dim*dim];
Move[] allMoves=new Move[]{
new Move(0,1,0,1),
new Move(0,1,0,-1),
new Move(0,-1,0,1),
new Move(0,-1,0,-1),
new Move(1,0,0,-1),
new Move(1,0,0,-1),
new Move(-1,0,0,1),
new Move(-1,0,0,-1),
new Move(0,0,1,1),
new Move(0,0,1,-1),
new Move(0,0,-1,1),
new Move(0,0,-1,-1)
};

ArrayList<Move> sequence= new ArrayList<Move>();
int counter=0;
boolean started=false;
Move currentMove;
    
 
public void setup(){
  
  cam=new PeasyCam(this,400);
   int index=0;

  for(int x=-1;x<=1;x++)
  {
    for(int y=-1;y<=1;y++)
    {
      for(int z=-1;z<=1;z++)
      {
        
     
        PMatrix3D matrix=new PMatrix3D();
        matrix.translate(x,y,z);
        cube[index]=new Cubie(matrix,x,y,z);
        index++;
        
      }
      println(sequence);
     }
  }
  //cube[0].c=color(255,0,0);
  //cube[2].c=color(0,0,255);
  for(int i=0;i<100;i++)
  {
  int r=PApplet.parseInt(random(allMoves.length));
  Move m=allMoves[r];
  sequence.add(m);
  }
  currentMove=sequence.get(counter);
  for(int i=sequence.size()-1;i>=0;i--)
  {
   Move nextMove=sequence.get(i).copy(); 
      nextMove.reverse();
      sequence.add(nextMove);
   }
  // move.start();
  
   
}

//String flipCase(char c)
//{
//String s=""+c;
//if(s.equals(s.toLowerCase())){
//return s.toUpperCase();
//}else{
//return s.toLowerCase();
//}

//}





int index=0;
public void turnX(int index,int dir){
  for(int i=0;i<cube.length;i++)
  { 
    Cubie qb=cube[i];
    if(qb.x ==index){
      PMatrix2D matrix=new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.y,qb.z);//rotate before translate
      qb.update(qb.x,round(matrix.m02),round(matrix.m12));
     qb.turnFacesX(dir);
    }
  }
}
public void turnY(int index ,int dir){
  for(int i=0;i<cube.length;i++)
  { 
    Cubie qb=cube[i];
    if(qb.y ==index){
      PMatrix2D matrix=new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.x,qb.z);//rotate before translate
      qb.update(round(matrix.m02),qb.y,round(matrix.m12));
      qb.turnFacesY(dir);
    }
  }
}
public void turnZ(int index,int dir ){
  for(int i=0;i<cube.length;i++)
  { 
    Cubie qb=cube[i];
    if(qb.z ==index){
      PMatrix2D matrix=new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.x,qb.y);//rotate before translate
     qb.update(round(matrix.m02),round(matrix.m12),round(qb.z));
      qb.turnFacesZ(dir);
     
    }
  }
}

public void keyPressed(){
 if(key==' '){
currentMove.start();
  //started=true;
 }
  //applyMove(move);
}
public void applyMove(char move){
  switch(move)
  {
    case 'f':
    turnZ(1,1);
    break;
    case 'F':
    turnZ(1,-1);
    break;
    case 'b':
    turnZ(-1,1);
    break;
    case 'B':
    turnZ(-1,-1);
    break;
    case 'u':
    turnY(1,1);
    break;
    case 'U':
    turnY(1,-1);
    break;
    case 'd':
    turnY(-1,1);
    break;
    case 'D':
    turnY(-1,-1);
    break;
    case 'l':
    turnX(-1,1);
    break;
    case 'L':
    turnX(-1,-1);
    break;
    case 'r':
    turnX(1,1);
    break;
    case 'R':
    turnX(1,-1);
    break;
  }    
    
  }



public void draw()
{
  background(51);
   fill(255);
   textSize(32);
   text(counter,100,100);
   text(counter,100,100);
   
   
  
  
  rotateX(-.5f);
  rotateY(0.4f);
  rotateZ(0.1f);
  currentMove.update();
 currentMove.update();
     if(currentMove.finished()){
        if(counter<sequence.size()-1){
          counter++;
          currentMove=sequence.get(counter);
          currentMove.start();
        }
      }
    
  //if(started)
  //{
  //  if(frameCount %1 ==0)
  //  {  
  //    if(counter<sequence.size())
  //    {
  //    char move=sequence.charAt(counter);
  //    applyMove(move);
  //    counter++;
  //    }
  //   }
  //}
  scale(50);
  for(int i=0;i<cube.length;i++)
  { 
    push();
    if(abs(cube[i].z)>0&& cube[i].z==currentMove.z){
    rotateZ(currentMove.angle);
  }
  else if(abs(cube[i].x)>0&& cube[i].x==currentMove.x)
  {
      rotateX(currentMove.angle);
  }
  else if(abs(cube[i].y)>0&& cube[i].y==currentMove.y)
  {
      rotateY(-currentMove.angle);
  }
      cube[i].show();
      pop();
   }
}
class Cubie
{ //constructor
  PMatrix3D matrix;//vectore object for the position
 int x=0;
 int y=0;
 int z=0;
 int c;
 Face[] faces=new Face[6];
 
  boolean highlight=false;
  Cubie( PMatrix3D m,int x,int y,int z){
    this.matrix=m;
    this.x=x;
    this.y=y;
    this.z=z;
    c=color(255);
    faces[0]=new Face(new PVector(0,0,-1),color(0,0,255));
    faces[1]=new Face(new PVector(0,0,1),color(0,255,0));
    faces[2]=new Face(new PVector(0,1,0),color(255,255,255));
    faces[3]=new Face(new PVector(0,-1,0),color(255,255,0));
    faces[4]=new Face(new PVector(1,0,0),color(255,150,0));
    faces[5]=new Face(new PVector(-1,0,0),color(255,0,0));
    
    
  }
  public void turnFacesZ(int dir)
  {
    for(Face f:faces){
    f.turnZ(dir*HALF_PI);
    }
  
  }
   public void turnFacesY(int dir)
  {
    for(Face f:faces){
    f.turnY(dir*HALF_PI);
    }
  
  }
   public void turnFacesX(int dir)
  {
    for(Face f:faces){
    f.turnX(dir*HALF_PI);
    }
  
  }
  public void update(int x,int y,int z){
    matrix.reset();
    matrix.translate(x,y,z);
    this.x=x;
    this.y=y;
    this.z=z;
   }
  public void show(){
   //fill(c);   
   noFill();
    stroke(0);
    strokeWeight(0.1f);
    pushMatrix();
    applyMatrix(matrix);
    box(1);
    for(Face f:faces)
    {
      f.show();
    }
  popMatrix();
  
  
  }
}
class Face{
       PVector normal;
       int c;
       Face(PVector normal,int c)
       {
       this.normal=normal;
       this.c=c;
       
       }
        public void turnZ(float angle){
          PVector v2=new PVector();
             v2.x=round(normal.x* cos(angle)-normal.y*sin(angle));
             v2.y=round(normal.x*sin(angle)+normal.y*cos(angle));
             v2.z=round(normal.z);
             normal=v2;
           }
        public void turnY(float angle)
        {
        PVector v2=new PVector();
             v2.x=round(normal.x*cos(angle)-normal.z*sin(angle));
             v2.z=round(normal.x*sin(angle)+normal.z*cos(angle));
             v2.y=round(normal.y);
             normal=v2;
        }
        public void turnX(float angle)
        {
          PVector v2=new PVector();
               v2.y=round(normal.y*cos(angle)-normal.z*sin(angle));
               v2.z=round(normal.y*sin(angle)+normal.z*cos(angle));
               v2.x=round(normal.x);
               normal=v2;
        }
    
  

 
 public void show(){
   pushMatrix();
  fill(c);
  noStroke();
  rectMode(CENTER);
 // rotate(HALF_PI,normal.x,normal.y,normal.z);
  translate(.5f*normal.x,.5f*normal.y,.5f*normal.z);
 if(abs(normal.x)>0){
  rotateY(HALF_PI);
  }
  else if(abs(normal.y)>0){
  rotateX(HALF_PI);
  }
  
  square(0,0,1);
  popMatrix();
 }

}
class Move{
  float angle;
  int x=0;
  int y=0;
  int z=0;
  int dir;
  boolean animating=false;
  boolean finished=false;
  Move(int x,int y,int z,int dir)
  {
  this.x=x;
  this.y=y;
  this.z=z;
  this.dir=dir;
  }
  public Move copy(){
    return new Move(x,y,z,dir);
  }
  public void reverse()
  {
    dir*=-1;
  }
    
  
  public void start()
  {
      animating=true;
      finished=false;
      this.angle=0;
  }
  public boolean finished(){
  return finished;
  }
  public void update()
  {
    if(animating)
    {
      angle+=dir*speed;
      if(abs(angle)>HALF_PI)
      {
        angle=0;
        animating=false;
        finished=true;
        if(abs(z)>0){
        turnZ(z,dir);
        }else if(abs(x)>0){
        turnX(x,dir);
        }
         else if(abs(y)>0){
        turnY(y,dir);
        }
      }
    }
  }
}
  public void settings() {  size(400,400,P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CUBE" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
