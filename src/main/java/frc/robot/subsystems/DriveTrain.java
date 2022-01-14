package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.kDriveTrain;

public class DriveTrain extends SubsystemBase{
    
    private final WPI_TalonFX mot_leftFrontDrive;
    private final WPI_TalonFX mot_leftRearDrive;
    private final WPI_TalonFX mot_rightFrontDrive;
    private final WPI_TalonFX mot_rightRearDrive;

    private int driveMode;

    private final DifferentialDrive m_drive;

    private final Solenoid dsl_gear;


    public DriveTrain(){
        mot_leftFrontDrive = new WPI_TalonFX(kDriveTrain.CANLeftDriveFront);
        mot_leftFrontDrive.setInverted(true);

        mot_leftRearDrive = new WPI_TalonFX(kDriveTrain.CANLeftDriveFront);
        mot_leftRearDrive.follow(mot_leftFrontDrive);

        mot_rightFrontDrive = new WPI_TalonFX(kDriveTrain.CANLeftDriveFront);
        //mot_leftFrontDrive.setInverted(true);

        mot_rightRearDrive = new WPI_TalonFX(kDriveTrain.CANLeftDriveFront);
        mot_rightRearDrive.follow(mot_rightFrontDrive);

        m_drive = new DifferentialDrive(mot_leftFrontDrive, mot_rightFrontDrive);

        dsl_gear = new Solenoid(null, 0);

        driveMode = kDriveTrain.InitialDriveMode;
    }

    /**
     * This method is called once per scheduler run and is used to update smart dashboard data.
     */
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {

    }

    // ------------------------- Drive Modes ------------------------- //

    /**
     * @param acceleration the robot's forward speed 
     * 
     * @param deceleration the robot's backward speed
     * 
     * @param turn         the robot's angular speed about the z axis
     * 
     */
    public void aadilDrive(final double acceleration, final double deceleration, final double turn){
        double accelerate = acceleration - deceleration;

        m_drive.arcadeDrive(accelerate, turn, true);
    }
    
    /**
     * @param leftSpeed the robot's left side speed along the X axis [-1.0, 1.0]. Forward is positive.
     * 
     * @param rightSpeed the robot's right side speed along the X axis [-1.0, 1.0]. Forward is positive.
     * 
     */
    public void tanksDrive(double leftSpeed, double rightSpeed){
        m_drive.tankDrive(leftSpeed, rightSpeed);
    }

    /**
     * @return the current drive mode as an int
     * 
     */
    public int getDriveMode(){
        return driveMode;
    }

    /**
     * @return the current drive mode as a String
     * 
     */
    private String getDriveModeString(){
        switch(driveMode){
            case kDriveTrain.AADIL_DRIVE:
                return "ADDIL DRIVE";

            case kDriveTrain.TANK_DRIVE:
                return "TANK DRIVE";

            case kDriveTrain.ARCADE_DRIVE:
                return "TANK DRIVE";
            default:
                return "NONE";
        } 
    }

    /**
     * Puts the current drive mode into SmartDashboard 
     * 
     */
    public void displayDriveMode(){
        SmartDashboard.putString("Drive Mode", getDriveModeString());
        mot_leftFrontDrive.getSelectedSensorVelocity();
    }

    /**
     * directly set the drive mode to the specified int
     * 
     */
    public void setDriveMode(int newDriveMode){
        driveMode = newDriveMode;
    }

    /**
     * cycles to the next drive mode
     * 
     * Order:
     *      AADIL_DRIVE
     *      TANK_DRIVE
     *      ARCADE_DRIVE
     *      REPEAT
     */
    public void cycleDriveMode(){
        switch(driveMode){
            case kDriveTrain.AADIL_DRIVE:
                driveMode = kDriveTrain.TANK_DRIVE;
                break; 

            case kDriveTrain.TANK_DRIVE:
                driveMode = kDriveTrain.ARCADE_DRIVE;
                break; 

            case kDriveTrain.ARCADE_DRIVE:
                driveMode = kDriveTrain.AADIL_DRIVE;
                break; 
 
        } 
    }

    // ---------------------------- Encoders ---------------------------- //

    /**
     * Puts the positions and velocities of the left and right encoders into SmartDashboard 
     * 
     */
    public void displayEncoder(){
        SmartDashboard.putNumber("Left Position", getEncoderPositionLeft());
        SmartDashboard.putNumber("Left Velocity", getEncoderVelocityLeft());

        SmartDashboard.putNumber("Right Position", getEncoderPositionRight());
        SmartDashboard.putNumber("Right Velocity", getEncoderVelocityRight());
    }

    /**
     * @return the average position of the left encoders 
     * 
     */
    public double getEncoderPositionLeft(){
        return mot_leftFrontDrive.getSelectedSensorPosition() + mot_leftRearDrive.getSelectedSensorPosition() / 2;
    }

    /**
     * @return the average position of the right encoders 
     * 
     */
    public double getEncoderPositionRight(){
        return mot_rightFrontDrive.getSelectedSensorPosition() + mot_rightRearDrive.getSelectedSensorPosition() / 2;
    }

    /**
     * @return the average velocity of the left encoders 
     * 
     */
    public double getEncoderVelocityLeft(){
        return mot_leftFrontDrive.getSelectedSensorPosition() + mot_leftRearDrive.getSelectedSensorPosition() / 2;
    }

    /**
     * @return the average velocity of the right encoders 
     * 
     */ 
    public double getEncoderVelocityRight(){
        return mot_rightFrontDrive.getSelectedSensorPosition() + mot_rightRearDrive.getSelectedSensorPosition() / 2;
    }

    // ---------------------------- Solenoids ---------------------------- //

    /**
     * shifts the gear shift to fast 
     */ 
    public void fastShift(){
        dsl_gear.set(true);
    }

    /**
     * shifts the gear shift to slow 
     */ 
    public void slowShift(){
        dsl_gear.set(false);
    }
}
