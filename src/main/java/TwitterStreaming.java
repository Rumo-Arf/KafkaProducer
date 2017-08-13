import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.List;

public class TwitterStreaming implements StatusListener {
    private AccessToken accesstoken;
    private Twitter twitter;

    public TwitterStreaming(String token, String sceret, String consumer) {
        this.accesstoken = new AccessToken(token, sceret);
        this.twitter = TwitterFactory.getSingleton();
    }

    private void Timeline() {
        try {
            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer("Consumer Key (API Key)", "Consumer Secret (API Secret)");
            twitter.setOAuthAccessToken(accesstoken);
            User user = twitter.verifyCredentials();

            List<Status> list = twitter.getUserTimeline(new Paging(1, 50));
            System.out.println("타임라인 계정:" + user.getScreenName());
            for (Status status : list) {
                System.out.println("작성자:" + status.getUser().getScreenName());
                System.out.println("타임라인내용:" + status.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Stream(String topics[], String languages[]) {

        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(this);

        // Set the filter //
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(topics);
        tweetFilterQuery.track(languages);

        // Start monitoring tweetes //
        twitterStream.filter(tweetFilterQuery);
    }

    public void onStatus(Status status) {
        System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText() + status.getCreatedAt());
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    public void onTrackLimitationNotice(int i) {

    }

    public void onScrubGeo(long l, long l1) {

    }

    public void onStallWarning(StallWarning stallWarning) {

    }

    public void onException(Exception e) {

    }
}
