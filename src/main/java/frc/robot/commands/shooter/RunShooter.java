package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.shooter.ShooterFlywheel;

/**
 * This command runs the turret flywheel at a speed
 * porportional to the distance of the turret from the
 * shooting target and operates the indexer once the rpm
 * reaches it's setpoint.
 * 
 * @author Keith Davies
 */
public final class RunShooter extends CommandBase {
    private final ShooterFlywheel flywheel;
    private final Indexer indexer;
    private final double target;

    public RunShooter(
        ShooterFlywheel flywheel,
        Indexer indexer,
        double target
    ) {
        this.flywheel = flywheel;
        this.indexer = indexer;
        this.target = target;
    }

    @Override
    public void initialize() {
        flywheel.enable();
        indexer.enable();

        flywheel.setVelocity(target);
    }


    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        flywheel.disable();
        indexer.disable();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}