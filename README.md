## OWASP ZAP API Scanner with Kafka Integration
This project provides a Scala-based utility to scan specific API endpoints using OWASP ZAP and stream the scan results or alerts to an Apache Kafka topic for further processing or monitoring.

Prerequisites
Java 11+ or higher installed

Scala 2.13+ installed

SBT (Scala Build Tool) installed

OWASP ZAP running as a daemon with API enabled

Apache Kafka broker running and accessible

Setup Instructions
Start OWASP ZAP

Run OWASP ZAP in daemon mode, allowing remote API access. Example:

bash
zap.sh -daemon -port 8080 -config api.key=<your-zap-api-key>
Start Kafka

Make sure your Kafka broker is running on the configured bootstrap server (default: localhost:9092).

Configure the project

Update the EnterpriseZapKafkaScannerApp.scala file with your ZAP API key and Kafka bootstrap server if different.

Verify the target base URL and API endpoints for scanning.

Build and Run
Clone or download the project source code.

Build the project using SBT:

bash
sbt compile
Run the scanner application:

bash
sbt run
The app will:

Connect to OWASP ZAP API.

Scan the specified API endpoints by running spider and active scans.

Send JSON scan results asynchronously to the configured Kafka topic.

Log scan progress and Kafka messaging status in the console.

Customize
Add or remove API endpoints to scan by modifying the endpoints list in EnterpriseZapKafkaScannerApp.scala.

Customize Kafka topic and ZAP configurations in the same file.

Extend scanning retries, concurrency, or detailed logging as needed.
