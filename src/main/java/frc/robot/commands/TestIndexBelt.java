package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeIndexer;
public class TestIndexBelt extends CommandBase{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private IntakeIndexer sys_intakeIndexer;

    public TestIndexBelt(IntakeIndexer subsystem){
        sys_intakeIndexer = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize(){}

    @Override
    public void execute(){
        sys_intakeIndexer.indexBeltOn(1);
    }

    @Override
    public void end(boolean interuppted){}

    @Override
    public boolean isFinished(){return false;}
    
}