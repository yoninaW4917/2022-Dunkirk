package frc.robot.base.shooter.odometry;

import frc.robot.base.shooter.target.FilterFactory;
import frc.robot.utils.Equation;

public class ShooterTrackingModel {
    public final FilterFactory<?> kTargetFilter; 
    public final Equation kFlywheelModel;
    public final Equation kTurretModel;
    public final double kAcquistionTimeout;
    public final double kMinOutput;

    public ShooterTrackingModel(
        FilterFactory<?> kTargetFilter,
        double kAcquistionTimeout,
        double kMinOutput,
        Equation kFlywheelModel,
        Equation kTurretModel
    ) {
        this.kAcquistionTimeout = kAcquistionTimeout;
        this.kFlywheelModel = kFlywheelModel;
        this.kTargetFilter = kTargetFilter;
        this.kTurretModel = kTurretModel;
        this.kMinOutput = kMinOutput;
    }
}