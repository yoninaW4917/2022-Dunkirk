// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.Climber;
// Subsystems
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pigeon;
import frc.robot.subsystems.Pneumatics;


// Commands
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.commands.AutoAlign;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.DefaultElevator;
import frc.robot.commands.ElevateTo;
import frc.robot.commands.FastGear;
import frc.robot.commands.IndexerActive;
import frc.robot.commands.FindElevatorZero;
import frc.robot.commands.IntakeActive;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.SlowGear;
import frc.robot.Constants.kAuto;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
// Misc
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;


import frc.robot.commands.IntakeSimulationTesting;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.ReverseIntakeIndexer;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pneumatics;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...

  // Define main joystick
  private final XboxController joystick_main; // = new XboxController(0);
  private final XboxController joystick_secondary;
  private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_LBumper, but_main_RBumper,
      but_main_LAnalog, but_main_RAnalog, but_main_Back, but_main_Start;
  private JoystickButton but_sec_A, but_sec_B, but_sec_X, but_sec_Y, but_sec_LBumper, but_sec_RBumper,
      but_sec_LAnalog, but_sec_RAnalog, but_sec_Back, but_sec_Start, but_sec_Left, but_sec_Up, but_sec_Right,
      but_sec_Down;

  // Subsystems defined
  private final DriveTrain DriveTrain;
  private final Pigeon Pigeon;

  private final Indexer Indexer;
  private final Intake Intake;
 
  private final Pneumatics Pneumatics;
  private final Climber Climber;

  // Commands defined
  // private final ExampleCommand m_autoCommand;
  private final DefaultDrive defaultDrive;

  private final ReverseIntakeIndexer reverse;
  private final IndexerActive indexerActive;  
  //private final IntakeIndexGo m_intakeIndexGo;
  //private final ReverseIntakeIndexer m_reverseIntakeIndex;
  //private final IntakeSimulationTesting m_intakeSimulationTesting;
  // private final TestIndexBelt m_testIndexBelt;
  // private final TestIndexShoot m_testIndexShoot;
  // private final TestIndexProto m_testIndexProto;


  private final IntakeActive intakeActive; 
  //private final ReverseIntake reverseIntake; 

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Init controller
    joystick_main = new XboxController(0);
    joystick_secondary = new XboxController(1);

    // Init button binds
    but_main_A = new JoystickButton(joystick_main, XboxController.Button.kA.value);
    but_main_B = new JoystickButton(joystick_main, XboxController.Button.kB.value);
    but_main_X = new JoystickButton(joystick_main, XboxController.Button.kX.value);
    but_main_Y = new JoystickButton(joystick_main, XboxController.Button.kY.value);
    but_main_LBumper = new JoystickButton(joystick_main, XboxController.Button.kLeftBumper.value);
    but_main_RBumper = new JoystickButton(joystick_main, XboxController.Button.kRightBumper.value);
    but_main_LAnalog = new JoystickButton(joystick_main, XboxController.Button.kLeftStick.value);
    but_main_RAnalog = new JoystickButton(joystick_main, XboxController.Button.kRightStick.value);
    but_main_Back = new JoystickButton(joystick_main, XboxController.Button.kBack.value);
    but_main_Start = new JoystickButton(joystick_main, XboxController.Button.kStart.value);

     // Initialize sub systems
     DriveTrain = new DriveTrain();
     Pneumatics = new Pneumatics();
     Pigeon = new Pigeon();
     Intake = new Intake();
     Indexer = new Indexer();
     Climber = new Climber();

     // Init commands
     defaultDrive = new DefaultDrive((DriveTrain), joystick_main);
     indexerActive = new IndexerActive(Indexer);
     reverse = new ReverseIntakeIndexer(Intake, Indexer);
     intakeActive = new IntakeActive(Intake);
 
    //  m_intakeIndexGo = new IntakeIndexGo(Indexer, Intake);
    //  m_reverseIntakeIndex = new ReverseIntakeIndexer(Intake);
    //  m_intakeSimulationTesting = new IntakeSimulationTesting(Intake);
    //  m_testIndexBelt = new TestIndexBelt(Indexer);
    //  m_testIndexProto = new TestIndexProto(Indexer);
    //  m_testIndexShoot = new TestIndexShoot(Indexer);

 
    but_sec_A = new JoystickButton(joystick_secondary, XboxController.Button.kA.value);
    but_sec_B = new JoystickButton(joystick_secondary, XboxController.Button.kB.value);
    but_sec_X = new JoystickButton(joystick_secondary, XboxController.Button.kX.value);
    but_sec_Y = new JoystickButton(joystick_secondary, XboxController.Button.kY.value);
    but_sec_LBumper = new JoystickButton(joystick_secondary, XboxController.Button.kLeftBumper.value);
    but_sec_RBumper = new JoystickButton(joystick_secondary, XboxController.Button.kRightBumper.value);
    but_sec_LAnalog = new JoystickButton(joystick_secondary, XboxController.Button.kLeftStick.value);
    but_sec_RAnalog = new JoystickButton(joystick_secondary, XboxController.Button.kRightStick.value);
    but_sec_Back = new JoystickButton(joystick_secondary, XboxController.Button.kBack.value);
    but_sec_Start = new JoystickButton(joystick_secondary, XboxController.Button.kStart.value);
   
    // Configure the button bindings
    configureButtonBindings();

    // Sets default command to be DefaultDrive
    DriveTrain.setDefaultCommand(defaultDrive);
    // Indexer.setDefaultCommand(indexerActive);
    Climber.setDefaultCommand(new DefaultElevator(Climber, joystick_secondary));
    CommandScheduler.getInstance().schedule(new FindElevatorZero(Climber));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Bind start to go to the next drive mode
    but_main_Start.whenPressed(() -> DriveTrain.cycleDriveMode());

    // Bind right bumper to
    but_main_RBumper.whenPressed(new FastGear(DriveTrain));
    but_main_RBumper.whenReleased(new SlowGear(DriveTrain));


    // but_main_A.whenPressed();
    but_main_X.whileHeld(new IntakeActive(Intake));
    but_main_B.whileHeld(new ReverseIntakeIndexer(Intake, Indexer));

    but_main_Y.whenPressed(new FindElevatorZero(Climber));

    // but_main_A.whenActive( new MoveToDistance(DriveTrain));
    // but_main_B.toggleWhenPressed( new MoveToAngle(DriveTrain));

    // but_sec_Left.whenPressed(new ElevateTo(Climber, true, 0));
    // but_sec_Left.whenPressed(() -> {
    // System.out.println(true);
    // });

    but_sec_X.whenPressed(new AutoAlign(Climber, DriveTrain, Pigeon, 180));
    but_sec_B.whenPressed(() -> {
      Pigeon.reset();
    });
    but_sec_Y.whenPressed(() -> {
      Climber.zeroEncoder();
    });

    but_sec_RBumper.whenPressed(new FindElevatorZero(Climber));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    // creates configuration for trajectory
    var feedForward = new SimpleMotorFeedforward(kAuto.ksVolts, kAuto.kvVoltSecondsPerMeter,
        kAuto.kaVoltSecondsSquaredPerMeter);
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(feedForward, kAuto.kDriveKinematics,
        10);

    TrajectoryConfig config = new TrajectoryConfig(kAuto.kMaxSpeed, kAuto.kMaxAcceleration);
    config.setKinematics(kAuto.kDriveKinematics).addConstraint(autoVoltageConstraint);
    // Generates a trajectory that tells the robot to move from its original
    // location
    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(new Pose2d(0, 0, new Rotation2d(0)),
        List.of(
            new Translation2d(1, 0)),
        new Pose2d(0, 1, new Rotation2d(0)),
        config); // new Translation2d(1, 1), new Translation2d(2, -1)

    RamseteCommand autoCommand = new RamseteCommand(trajectory, Pigeon::getPose,
        new RamseteController(kAuto.kRamseteB, kAuto.kRamseteZeta),
        new SimpleMotorFeedforward(kAuto.ksVolts, kAuto.kvVoltSecondsPerMeter,
            kAuto.kMaxAcceleration),
        kAuto.kDriveKinematics, DriveTrain::getWheelSpeeds,
        new PIDController(kAuto.kPDriveVel, 0, 0), new PIDController(kAuto.kPDriveVel, 0, 0),
        DriveTrain::tankDriveVolts, DriveTrain);

    // Reset odometry to the starting pose of the trajectory.
    DriveTrain.zeroEncoders();
    Pigeon.resetOdometry(trajectory.getInitialPose());

    // returns the autonomous command
    // makes sure that after the auto command is finished running the robot stops.
    return autoCommand.andThen(() -> DriveTrain.tankDriveVolts(0, 0));
  }
}
