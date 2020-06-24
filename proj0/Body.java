public class Body {
  double xxPos;
  double yyPos;
  double xxVel;
  double yyVel;
  double mass;
  String imgFileName;

  final static double gcons = 6.67e-11; //Gravitational constant

//basic constructor
  public Body(double xP, double yP, double xV, double yV, double m, String img){
    xxPos = xP;
    yyPos = yP;
    xxVel = xV;
    yyVel = yV;
    mass = m;
    imgFileName = img;
  }

//constructor creating copy
  public Body(Body p){
    this.xxPos = p.xxPos;
    this.yyPos = p.yyPos;
    this.xxVel = p.xxVel;
    this.yyVel = p.yyVel;
    this.mass = p.mass;
    this.imgFileName = p.imgFileName;
  }

//calculate and return r: distance between
  public double calcDistance(Body other){
    double dx = other.xxPos - this.xxPos;
    double dy = other.yyPos - this.yyPos;
    return Math.sqrt(dx*dx+dy*dy);
  }

//calculate and return force exerted
  public double calcForceExertedBy(Body other){
    return gcons*this.mass*other.mass/(this.calcDistance(other)*this.calcDistance(other));
  }

//calculate and return force exerted in X direction
  public double calcForceExertedByX(Body other){
    return this.calcForceExertedBy(other)*(other.xxPos - this.xxPos)/this.calcDistance(other);
}

//calculate and return force exerted in Y direction
  public double calcForceExertedByY(Body other){
    return this.calcForceExertedBy(other)*(other.yyPos- this.yyPos)/this.calcDistance(other);
  }

//calculate Net Force of other objects on this in X
  public double calcNetForceExertedByX(Body[] others){
    double netForceX = 0.0;
    for(Body otherBody : others){
      if(!otherBody.equals(this)){//add every body except this
        netForceX+=this.calcForceExertedByX(otherBody);
      }
    }
    return netForceX;
  }

//calculate Net Force of other objects on this in Y
  public double calcNetForceExertedByY(Body[] others){
    double netForceY = 0.0;
    for(Body otherBody : others){//loop through each body
      if(!otherBody.equals(this)){//add every body except this
        netForceY+=this.calcForceExertedByY(otherBody);
      }
    }
    return netForceY;
  }

//update POS and VEL per dt
  public void update(double dt, double fX, double fY){
    double aX = fX/mass;//solve for acceleration in X dir
    double aY = fY/mass;//solve for acceleration in Y dir
    xxVel+=dt*aX;//update vel
    yyVel+=dt*aY;
    xxPos+=dt*xxVel;//update pos
    yyPos+=dt*yyVel;
  }

//draw body method
  public void draw(){
    StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
  }
}
