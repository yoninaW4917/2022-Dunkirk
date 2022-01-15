// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  private TalonFX mot_armDriver;
  
  private boolean locked;


  /**
   * Constructor for the climber.
   */
  public Climber() {

    mot_armDriver = new TalonFX(Constants.Climber.mot_port);
    locked = false;

    //Gives absolute motor positions of 0 - 360 degrees, all positive values. 
    mot_armDriver.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void periodic() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void simulationPeriodic() {
  }

  /**
   * Method that moves arm based on velocity for the motor.
   * @param rate Rate at which to move the arm. - is backwards, + is forwards.
   */
  public void moveArm(double rate){

    // TODO currently takes in a fixed rate.
    if(!locked){
      //currently moves the motor at a rate of 180 degrees per 100ms.
      mot_armDriver.set(TalonFXControlMode.Velocity, 1.8);
    }
  }

  /**
   * Method for locking the arm.
   */
  public void lockArm(){
    locked = true;
  }

  /**
   * Method for unlocking the arm.
   */
  public void unlockArm(){
    locked = false;
  }

  /**
   * Method for getting the length of the arm extended. This is a calculated value. 
   * @return The length at which the arm is currently extended.
   */
  public double getLength(){
    // TODO add conversion for length.
    SmartDashboard.putNumber("Arm length", mot_armDriver.getSelectedSensorPosition());
    return mot_armDriver.getSelectedSensorPosition();
  }


}
