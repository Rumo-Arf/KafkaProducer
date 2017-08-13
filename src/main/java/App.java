import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class App {

    public static void main(String argc[]) {

        final String token = "218845119-52lyUXbj5S2h2LuU00An1FVXxEUCsrUelvQ4QOae";
        final String secret = "hufTSknQPSYpUkgjgoDPy456rVe7yaLAamSTPR9TG2SC2";
        final String consumerKey = "59cTnHULLFPFR9ZqmSQVKeLxr";
        final String consumerSecret = "6ftmPGju1v5YB2XMd891Xr66L5AaqG6CoVjq5eoKMuaUE6WzNQ";

        AccessToken accesstoken = new AccessToken(token, secret);

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        twitterStream.setOAuthAccessToken(accesstoken);

        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        KafkaCollector<TwitterStream> kafkaCollector = new KafkaCollector<TwitterStream>("korea", "en", twitterStream);

        threadPool.execute(kafkaCollector);

        threadPool.shutdown();
    }
}

interface IStream {
    void getStream();
}