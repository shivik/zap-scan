import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

class KafkaProducerUtil(bootstrapServers: String) {

  private val props = new Properties()
  props.put("bootstrap.servers", bootstrapServers)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  private val producer = new KafkaProducer[String, String](props)

  def send(topic: String, key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, value)
    producer.send(record, (_: RecordMetadata, exception: Exception) =>
      if (exception != null) {
        println(s"[Kafka] Failed to send message to topic $topic: ${exception.getMessage}")
      } else {
        println(s"[Kafka] Successfully sent message for key $key to topic $topic")
      }
    )
  }

  def close(): Unit = producer.close()
}
