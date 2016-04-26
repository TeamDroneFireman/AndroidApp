package edu.istic.tdf.dfclient.push;

import android.os.Bundle;

/**
 * Is a command to execute on push received
 */
public interface IPushCommand {
    void execute(Bundle bundle);
}
