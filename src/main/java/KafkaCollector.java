import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;

import java.util.Properties;

public class KafkaCollector<T> implements Runnable {
    private static KafkaProducer producer;
    private Properties props;

    private String topic;
    private String lang;
    private String bootstrapservers;

    private TwitterStream stream;

    KafkaCollector(final String topic, final String lang, TwitterStream stream) {
        this.topic = topic;
        this.stream = stream;
        this.lang = lang;

        props = new Properties();
        props.put("bootstrap.servers", "arf.iptime.org:9092,mcse.iptime.org:9092,155.230.14.93:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<String, String>(props);

        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                producer.send(new ProducerRecord<String, String>("NEW", topic, status.getText() + " " + status.getCreatedAt()));
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            public void onScrubGeo(long l, long l1) {

            }

            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        stream.addListener(listener);
    }

    private void StopKafakaProducer() {
        producer.close();
    }

    public void run() {
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(topic);
        tweetFilterQuery.language(lang);
        stream.filter(tweetFilterQuery);
    }

}

