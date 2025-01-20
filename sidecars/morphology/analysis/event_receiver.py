import os
import pika
import sys


EXCHANGE = "morphological_analysis"


def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host="localhost"))
    channel = connection.channel()

    setup_queue(channel)

    def callback(ch, method, properties, body):
        print(f" [x] Received {body}")

    channel.basic_consume(queue="hello", on_message_callback=callback, auto_ack=True)

    print(" [*] Waiting for messages. To exit press CTRL+C")
    channel.start_consuming()


def setup_queue(channel):
    language_code = os.environ["LANGUAGE_CODE"]
    queue_name = f"{EXCHANGE}_{language_code}"
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(
        exchange=EXCHANGE,
        queue=queue_name,
        routing_key=f"morphological_analyis.{language_code}.*",
    )


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("Interrupted")
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
