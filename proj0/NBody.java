public class NBody{
//given file name, return double corresponding to the radius of the universe
  public static double readRadius(String fileName){
    In in = new In(fileName);
    in.readInt();//skip first #
    return in.readDouble();//return second number
  }

//given file name, return array of bodies
  public static Body[] readBodies(String fileName){
    In in = new In(fileName);
    int numOfBodies = in.readInt();
    in.readDouble();//skip size

    Body[] bodyArr = new Body[numOfBodies];

    for(int i = 0; i<bodyArr.length; i++){
      double xPos = in.readDouble();
      double yPos = in.readDouble();
      double xVel = in.readDouble();
      double yVel = in.readDouble();
      double mass = in.readDouble();
      String image = in.readString();
      bodyArr[i] = new Body(xPos, yPos, xVel, yVel, mass, image);
    }
    return bodyArr;
  }

  public static void main(String[] args){
    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];

    double radius = NBody.readRadius(filename);
    Body[] bodies = NBody.readBodies(filename);

    StdDraw.setScale(-radius, radius);
    StdDraw.picture(0, 0, "images/starfield.jpg");//setup and draw plane
    for(Body body : bodies){
      body.draw();//draw bodies
    }

    StdDraw.enableDoubleBuffering();
    double time = 0;//curr time
    while(time < T){
      double[] xForces = new double[bodies.length];
      double[] yForces = new double[bodies.length];

      int i = 0;//iterate through forces array to add forces to array
      for(Body body : bodies){
        xForces[i] = body.calcNetForceExertedByX(bodies);
        yForces[i] = body.calcNetForceExertedByY(bodies);
        i++;
      }

      for(int j = 0; j<bodies.length; j++){
        bodies[j].update(dt, xForces[j], yForces[j]);//update values
      }

      StdDraw.picture(0, 0, "images/starfield.jpg");//draw plane
      for(Body body : bodies){
        body.draw();//draw bodies
      }
      StdDraw.show();
      StdDraw.pause(10);
      time+=dt;
    }
    StdOut.printf("%d\n", bodies.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < bodies.length; i += 1) {
        StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                      bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                      bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
    }
  }
}
