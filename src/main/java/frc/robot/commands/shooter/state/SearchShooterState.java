package frc.robot.commands.shooter.state;

import org.jetbrains.annotations.NotNull;

import frc.robot.Constants;
import frc.robot.base.TimedStateCommand;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.LedMode;
import frc.robot.subsystems.Limelight.TargetType;
import frc.robot.utils.Toggleable;

// TODO update doc

/**
 * <h3>Operates the turret in the "Searching" state.</h3>
 * 
 * <p> In this state, the turret waits for the limelight led's to
 * turn on and checks to see if there are any targets. There's always
 * a chance that the limelight may detect a target the first time it's
 * led's turn since the robot should be generally facing it's target
 * upon shooting, and this state checks for that possibility. If there
 * is no target by the time the aqusition delay time runs, it switches
 * over to the {@code kSweeping} state.</p>
 */
public class SearchShooterState extends TimedStateCommand {
    private final Limelight limelight;

    private boolean done;

    public SearchShooterState(Limelight limelight) {
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        super.initialize();
        
        if (!Toggleable.isEnabled(limelight))
            throw new RuntimeException("Cannot search shooter when requirements are not enabled.");

        limelight.enable();
        limelight.setLedMode(LedMode.kModeOn);

        done = false;
    }

    @Override
    public void execute() {
        if (limelight.hasTarget() && limelight.getTargetType() == TargetType.kHub) {
            next("frc.robot.shooter:align");
            done = true;
        } else if (getElapsedTime() > Constants.Vision.ACQUISITION_DELAY) {
            next("frc.robot.shooter:sweep");
            done = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public @NotNull String getStateName() {
        return "frc.robot.shooter:search";
    }
}
