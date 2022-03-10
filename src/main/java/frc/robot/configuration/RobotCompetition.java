package frc.robot.configuration;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Constants;
//Constants
import frc.robot.base.shooter.ShooterConfiguration;
import frc.robot.base.shooter.SweepDirection;
import frc.robot.base.shooter.ShooterMode;
import frc.robot.base.Joystick.ButtonType;
import frc.robot.base.RobotConfiguration;
import frc.robot.base.ValueProperty;
import frc.robot.base.Joystick;

// Misc
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;


import frc.robot.commands.autonomous.trajectoryAuto.TwoBallsAuto;
import frc.robot.commands.autonomous.trajectoryAuto.OneBallAuto;
import frc.robot.subsystems.shooter.*;
import frc.robot.commands.shooter.*;
import frc.robot.subsystems.*;
import frc.robot.commands.*;

public class RobotCompetition implements RobotConfiguration {
    private final ShooterFlywheel      Flywheel;
    private final ShooterTurret        turret;
    private final Pneumatics           Pneumatics;
    private final DriveTrain           DriveTrain;
    private final Limelight            limelight;
    private final Indexer              Indexer;
    private final Intake               Intake;
    private final Climber              Climber;

    private final Joystick             joystickPrimary; // = new XboxController(0);
    private final Joystick             joystickSecondary;

    private final ReverseIntakeIndexer reverse;
    private final IndexerIntakeActive  indexerIntakeActive;
    private final IndexerIntakeTest    test;
    private final DefaultDrive         defaultDrive;
    private final IntakeActive         intakeActive;

    private final ValueProperty<ShooterConfiguration> shooterConfiguration;
    private final ValueProperty<SweepDirection> shooterSweepDirection;
    private final ValueProperty<Integer> shooterOffset;
    public final ValueProperty<Boolean> climberActive;

    private final SendableChooser<Command> autoCommandSelector;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotCompetition(RobotContainer robot) {
        joystickSecondary = robot.joystickSecondary;
        joystickPrimary   = robot.joystickPrimary;
        Pneumatics        = robot.Pneumatics;
        DriveTrain        = robot.DriveTrain;
        limelight         = robot.limelight;
        Flywheel          = robot.Flywheel;
        Climber           = robot.Climber;
     // Pigeon            = robot.Pigeon
        Intake            = robot.Intake;
        Indexer           = robot.Indexer;
        turret            = robot.turret;

        // Init controller
        shooterSweepDirection = new ValueProperty<>(SweepDirection.kLeft);
        shooterConfiguration  = new ValueProperty<>(Constants.Shooter.CONFIGURATIONS.get(ShooterMode.kFar));
        shooterOffset         = new ValueProperty<>(0);
        climberActive         = robot.climberActive;
        
        // Initialize sub systems

        // Init commands
        indexerIntakeActive = new IndexerIntakeActive(Indexer, Intake);
        intakeActive        = new IntakeActive(Intake, Indexer);
        defaultDrive        = new DefaultDrive(DriveTrain, joystickPrimary.getController());
        reverse             = new ReverseIntakeIndexer(Intake, Indexer);
        test                = new IndexerIntakeTest(Indexer, Intake);

        autoCommandSelector = new SendableChooser<Command>();

        configureButtonBindings();
        configureCommands();
        configureDashboard();
    }

    private void configureDashboard() {
        autoCommandSelector.setDefaultOption("Default", new TwoBallsAuto(DriveTrain, Intake, Indexer, limelight, turret, Flywheel, shooterConfiguration, shooterSweepDirection, shooterOffset));
        autoCommandSelector.addOption("One", new OneBallAuto(DriveTrain, Indexer, limelight, turret, Flywheel, shooterConfiguration, shooterSweepDirection, shooterOffset));
        autoCommandSelector.addOption("Two", new TwoBallsAuto(DriveTrain, Intake, Indexer, limelight, turret, Flywheel, shooterConfiguration, shooterSweepDirection, shooterOffset));

        SmartDashboard.putData("Auto Chooser", autoCommandSelector);

        Shuffleboard.getTab("Shooter")
            .add("Hood up", new HoodUp(turret));

        Shuffleboard.getTab("Shooter")
            .add("Hood down", new HoodDown(turret));

        Shuffleboard.getTab("Shooter")
            .add("Shooter Offset - Increment", new ConfigureProperty<Integer>(shooterOffset, p -> p.set(p.get() + Constants.Shooter.OFFSET_INCREMENT)));

        Shuffleboard.getTab("Shooter")
            .add("Shooter Offset - Decrement", new ConfigureProperty<Integer>(shooterOffset, p -> p.set(p.get() - Constants.Shooter.OFFSET_INCREMENT)));
    }


    private void configureCommands() {
        DriveTrain.setDefaultCommand(defaultDrive);
        Climber.setDefaultCommand(new DefaultElevator(Climber, joystickSecondary.getController()));
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
        Trigger climberToggleTrigger = new Trigger(climberActive::get);
        Trigger shooterModeTrigger = new Trigger(() -> shooterConfiguration.get().getMode() == ShooterMode.kNear);

        // Bind start to go to the next drive mode
        joystickPrimary.getButton(ButtonType.kStart)
            .whenPressed(DriveTrain::cycleDriveMode);

        // Bind right bumper to
        joystickPrimary.getButton(ButtonType.kRightBumper)
            .whenPressed(new FastGear(DriveTrain));

        joystickPrimary.getButton(ButtonType.kRightBumper)
            .whenReleased(new SlowGear(DriveTrain));

        // but_main_A.whenPressed();
        joystickPrimary.getButton(ButtonType.kB)
            .whileHeld(new ReverseIntakeIndexer(Intake, Indexer));
        
        // TODO: temp
        joystickPrimary.getButton(ButtonType.kX)
            .whileHeld(new IndexerIntakeActive(Indexer, Intake));

        joystickPrimary.getButton(ButtonType.kX)
            .whenReleased(new RunIndexerBack(Intake, Indexer).withTimeout(0.2));

        joystickSecondary.getButton(ButtonType.kStart)
            .whenPressed((new ToggleShooterElevator(climberActive, turret, limelight, DriveTrain, Flywheel, Indexer, Climber))
            .beforeStarting(new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kNear)));

        joystickSecondary.getButton(ButtonType.kX)
            .and(climberToggleTrigger)
            .whenActive(new AutoAlign(Climber, DriveTrain, 180));

        joystickSecondary.getButton(ButtonType.kB)
            .and(climberToggleTrigger)
            .whenActive(DriveTrain::resetGyro);

        joystickSecondary.getButton(ButtonType.kY)
            .and(climberToggleTrigger)
            .whenActive(Climber::zeroEncoder);

        joystickSecondary.getButton(ButtonType.kLeftBumper)
            .and(climberToggleTrigger)
            .whenActive(new FindElevatorZero(Climber));

        joystickSecondary.getButton(ButtonType.kRightBumper)
            .and(climberToggleTrigger.negate())
            .and(shooterModeTrigger.negate())
            .whileActiveContinuous(new OperateShooter(limelight, turret, Flywheel, Indexer, shooterSweepDirection, shooterConfiguration, shooterOffset))
            .whenInactive(new RotateTurret(turret, 0));
        
        joystickSecondary.getButton(ButtonType.kRightBumper)
            .and(climberToggleTrigger.negate())
            .and(shooterModeTrigger)
            .whileActiveContinuous(new RunShooter(Flywheel, Indexer, Constants.Shooter.NEAR_FLYWHEEL_VELOCITY, 0.85))
            .whenInactive(new RotateTurret(turret, 0));

        joystickSecondary.getButton(ButtonType.kUpPov)
            .and(joystickSecondary.getButton(ButtonType.kA).negate())
            .and(climberToggleTrigger.negate())
            .whenActive(new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kFar));

        joystickSecondary.getButton(ButtonType.kDownPov)
            .and(joystickSecondary.getButton(ButtonType.kA).negate())
            .and(climberToggleTrigger.negate())
            .whenActive(new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kNear));

        joystickSecondary.getButton(ButtonType.kLeftPov)
            .and(joystickSecondary.getButton(ButtonType.kA).negate())
            .and(climberToggleTrigger.negate())
            .whenActive(new ConfigureProperty<>(shooterSweepDirection, SweepDirection.kLeft));

        joystickSecondary.getButton(ButtonType.kRightPov)
            .and(joystickSecondary.getButton(ButtonType.kA).negate())
            .and(climberToggleTrigger.negate())
            .whenActive(new ConfigureProperty<>(shooterSweepDirection, SweepDirection.kRight));

        joystickSecondary.getButton(ButtonType.kA)
            .and(climberToggleTrigger.negate())
            .whileActiveContinuous(
                new SequentialCommandGroup(  
                new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kLow),
                new RunShooter(Flywheel, Indexer, Constants.Shooter.LOW_FLYWHEEL_VELOCITY, 0.5)));

        joystickSecondary.getButton(ButtonType.kB)
            .and(climberToggleTrigger.negate())
            .whileActiveContinuous(
                new SequentialCommandGroup(  
                new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kGuard),
                new RunShooter(Flywheel, Indexer, Constants.Shooter.GUARD_FLYWHEEL_VELOCITY, 0.5)));
    }

    /**
     * Method for scheduling commands at the beginning of teleop.
     */
    @Override
    public Command getTeleopCommand() {
        return new ParallelCommandGroup(
            new ConfigureShooter(turret, limelight, shooterConfiguration, ShooterMode.kFar),
            new FindElevatorZero(Climber)
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    @Override
    public Command getAutonomousCommand() {
        return autoCommandSelector.getSelected(); 
    }
}