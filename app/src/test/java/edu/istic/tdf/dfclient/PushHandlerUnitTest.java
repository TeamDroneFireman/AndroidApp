package edu.istic.tdf.dfclient;

import android.os.Bundle;

import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.HashMap;

import edu.istic.tdf.dfclient.http.TdfHttpClient;
import edu.istic.tdf.dfclient.push.IPushCommand;
import edu.istic.tdf.dfclient.push.PushHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the push handling
 */
public class PushHandlerUnitTest {

    /**
     * Tests that the push handler handles topics correctly
     */
    @Test
    public void handlePush_knownCatcher() {

        PushHandler pushHandler = new PushHandler();
        IPushCommand command = mock(IPushCommand.class);
        pushHandler.addCatcher("/topic", command);

        // Mock push bundle content
        Bundle bundle = mock(Bundle.class);
        when(bundle.containsKey(PushHandler.TOPIC_BUNDLE_KEY)).thenReturn(true);
        when(bundle.getString(PushHandler.TOPIC_BUNDLE_KEY)).thenReturn("/topic");

        pushHandler.handlePush("sender", bundle);

        verify(command).execute(bundle);
    }
}