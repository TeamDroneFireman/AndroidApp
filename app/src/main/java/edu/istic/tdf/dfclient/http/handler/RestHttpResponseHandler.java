package edu.istic.tdf.dfclient.http.handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import edu.istic.tdf.dfclient.http.exception.HttpException;
import edu.istic.tdf.dfclient.rest.handler.IRestReturnHandler;
import edu.istic.tdf.dfclient.rest.serializer.RestSerializerBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Handles a OK HTTP client response, deserializes the result and calls the callback
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
        this.deserializer = RestSerializerBuilder.build();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        callback.onError(new HttpException(e, call, null));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // If HTTP code is successful (200..399)
        if(response.isSuccessful()) {
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
        } else { // If HTTP code is not successful
            callback.onError(
                    new HttpException(
                        new Exception("Http code is not successfull"),
                        call,
                        response
                    )
            );
        }
    }
}