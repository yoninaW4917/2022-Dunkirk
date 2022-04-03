package frc.robot.commands.training.shooter;

import frc.robot.Constants;
import frc.robot.base.training.Setpoint;
import frc.robot.base.training.TrainerContext;
import frc.robot.base.training.TrainerDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class UpdateTargetSetpoint extends CommandBase {
    private final TrainerDashboard dashboard;
    private final TrainerContext context;

    public UpdateTargetSetpoint(TrainerDashboard dashboard, TrainerContext context) {
        this.dashboard = dashboard;
        this.context = context;
    }

    @Override
    public void initialize() {
        context.setSetpoint(
            new Setpoint(
                context.getExecutionModel().calculate(context.getDistance()),
                Constants.Shooter.SPEED_RANGE
            )
        );
        
        dashboard.update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
