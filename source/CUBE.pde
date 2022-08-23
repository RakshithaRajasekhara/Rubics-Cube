import peasy.*;

PeasyCam cam;
float speed=.25;

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
    
 
void setup(){
  size(400,400,P3D);
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
  int r=int(random(allMoves.length));
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
void turnX(int index,int dir){
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
void turnY(int index ,int dir){
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
void turnZ(int index,int dir ){
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

void keyPressed(){
 if(key==' '){
currentMove.start();
  //started=true;
 }
  //applyMove(move);
}
void applyMove(char move){
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



void draw()
{
  background(51);
   fill(255);
   textSize(32);
   text(counter,100,100);
   text(counter,100,100);
   
   
  
  
  rotateX(-.5);
  rotateY(0.4);
  rotateZ(0.1);
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
