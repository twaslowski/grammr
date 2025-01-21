import logging
import os

import pika
from pika.adapters.blocking_connection import BlockingChannel, BlockingConnection
from pika.connection import ConnectionParameters

from analysis.domain.analysis_request import AnalysisRequest
from analysis.domain.morphological_analysis import MorphologicalAnalysis
from analysis.service import analysis_service

exchange = os.environ["EXCHANGE"]
rabbitmq_host = os.environ["RABBITMQ_HOST"]


logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)


def main():
    connection = BlockingConnection(ConnectionParameters(host=rabbitmq_host))
    channel = connection.channel()
    logger.info("Successfully established connection to RabbitMQ")

    queue_name = setup_queue(channel)
    channel.basic_consume(queue=queue_name, on_message_callback=analyze, auto_ack=True)

    logger.info("Starting to consume messages ...")
    channel.start_consuming()


def setup_queue(channel: BlockingChannel) -> str:
    language_code = os.environ["LANGUAGE_CODE"]
    queue_name = f"{exchange}_{language_code}"
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(
        exchange=exchange,
        queue=queue_name,
        routing_key=f"morphological-analysis.requested.{language_code}",
    )
    logger.info(f"Successfully created and bound queue {queue_name}")
    return queue_name


def analyze(
    channel: BlockingChannel,
    method: pika.spec.Basic.Deliver,
    properties: pika.spec.BasicProperties,
    body: bytes,
):
    request = AnalysisRequest.model_validate_json(body)
    logger.info(f"Received message with requestId: {request.request_id}")
    analysis = analysis_service.perform_analysis(request)
    logger.info(
        f"Finished analysis for requestId: {analysis.request_id}; publishing ..."
    )
    publish(channel, analysis)


def publish(channel: BlockingChannel, analysis: MorphologicalAnalysis):
    channel.basic_publish(
        exchange,
        "morphological-analysis.complete",
        analysis.model_dump_json()
    )


if __name__ == "__main__":
    main()
