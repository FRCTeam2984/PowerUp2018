package org.ljrobotics.frc2018.utils;

public class Motion{
private double y;
private double rotation;

  public Motion(double y, double rotation){
    this.y=y;
    this.rotation=rotation;
  }
  public double getY(){
    return this.y;
  }
  public double getRotation(){
    return this.rotation;
  }
}
