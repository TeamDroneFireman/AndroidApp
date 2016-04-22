package edu.istic.tdf.dfclient.rest.handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import edu.istic.tdf.dfclient.rest.IRestReturnHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Handles a HTTP client response, deserializes the result and calls the callback
 * @param <Result> The expected result type in callback
 */
public class RestHttpResponseHandler<Result> implements Callback{

    /**
     * Rest callback to call on response
     */
    IRestReturnHandler<Result> callback;

    /**
     * The deserialization type
     */
    Type resultType;

    /**
     * The deserializor
     */
    Gson deserializer;

    /**
     * Constructs a RestHttpResponseHandler
     * @param callback Rest callback to call on response
     * @param resultType Result type to deserialize to
     */
    public RestHttpResponseHandler(IRestReturnHandler<Result> callback, Type resultType) {
        this.callback = callback;
        this.resultType = resultType;
        this.deserializer = new Gson();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        callback.onError(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // TODO : TREAT FUCKING HTTP CODE !

        // Get response body
        String responseBody = response.body().string();

        // Initialize result
        Result result = null;
        try {
            result = deserializer.fromJson(responseBody, resultType);
        } catch (Exception e) {
            callback.onError(e);
        }

        callback.onSuccess(result);
    }
}