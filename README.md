# CO2 Metrics Collector

### Features
* collect co2 sensor metrics
* get sensor current status
* get all historical sensor alerts
* get average of co2 level for a sensor for last 30 days
* get max of co2 level for a sensor for last 30 days

### Run app
./gradlew bootRun

### Design overview
Sensor metric is created via ```/api/v1/sensors/{uuid}/mesurements```

Sensor metric is stored in ```metrics``` table as is.
Metric status is calculated based on co2 value, if its greater than 2000, status:WARN, else status:OK.

Sensor metric update listener updates sensor status:
* to ALERT if there are 3 consecutive WARN statuses.
* to OK if there are 3 consecutive OK statuses.
* to WARN if current status is OK.

Sensor status is stored in ```sensor_current_status``` table.

Historical alerts are calculated in runtime, if 3 consecutive metrics have status WARN they are grouped to AlertAggregate with start/end date and co2 value from each metric.
