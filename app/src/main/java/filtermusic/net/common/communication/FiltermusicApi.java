package filtermusic.net.common.communication;

import android.support.annotation.NonNull;

import filtermusic.net.BuildConfig;
import retrofit.RestAdapter;

/**
 * Handles the retrieval of data from backend
 */
public class FiltermusicApi {

    private static final RestAdapter.LogLevel DEBUG_DEFAULT_LOG_LEVEL = RestAdapter.LogLevel.FULL;

    public FiltermusicApi() {
        //nothing to do
    }

    public FiltermusicService createFromRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://filtermusic.net/")
                .setConverter(new FeedXmlParser())
                .build();

        setRestAdapterLogLevel(restAdapter);

        FiltermusicService service = restAdapter.create(FiltermusicService.class);
        return service;
    }

    private void setRestAdapterLogLevel(final @NonNull RestAdapter restAdapter) {
        if (BuildConfig.DEBUG) {
            // use a special log level only when in debug mode
            restAdapter.setLogLevel(DEBUG_DEFAULT_LOG_LEVEL);
        }
    }
}
