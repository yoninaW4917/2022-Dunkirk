// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Pneumatics {
        public static final int MODULE = 0;
        public static final double MIN_PSI = 0;
        public static final double MAX_PSI = 60;

    }

    public final class kDriveTrain{

        // CAN IDs  (not initialized)
        public static final int CANLeftDriveFront = 4;
        public static final int CANRightDriveFront = 6;
        public static final int CANLeftDriveBack = 14;
        public static final int CANRightDriveBack = 15;
        
        // Current Limits
        public static final double CurrentLimit = 65;
        public static final double TriggerThresholdCurrent = 65;
        public static final double triggerThresholdTime = 0;

        // Double Solenoid
        public static final int ForwardChannel = 0;
        public static final int ReverseChannel = 1;

        // Drive Modes
        public static final int InitialDriveMode = 0;

        public static final int AADIL_DRIVE = 0;
        public static final int TANK_DRIVE = 1;

    }

    public final class kGyroSystem{

        public static final int CANPigeon = 0;

    }
}