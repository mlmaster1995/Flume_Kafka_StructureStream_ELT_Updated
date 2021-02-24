"""
Copyright 2021 C.Young
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""
# Airflow 2.0.0 DAG
import json
from airflow.models import DAG
from datetime import datetime, timedelta
from confluent_kafka import Producer

from airflow.providers.http.sensors.http import HttpSensor
from airflow.providers.http.operators.http import SimpleHttpOperator
from airflow.models import XCom

from airflow.operators.bash import BashOperator
from airflow.operators.dummy import DummyOperator
from airflow.operators.python import PythonOperator
from airflow.sensors.filesystem import FileSensor
from airflow.utils.task_group import TaskGroup

root_path = '/home/airflow/airflow/file_folder/'

default_args={
        'owner':'airflow',
        'start_date':datetime(2021, 2, 22),
        'depends_on_past': False,
        'email': 'kyang3@lakeheadu.ca',
        'email_on_failure': False,
        'email_on_retry': False,
        }


def _process_data(task_instance):
    raw_data = task_instance.xcom_pull(task_ids='extract_data')
    json_list = raw_data['summary']
    process_data = []
    # process json to string 
    for data in json_list:
        process_data.append(f'{data["active_cases"]}|{data["active_cases_change"]}|{data["avaccine"]}|{data["cases"]}|{data["cumulative_avaccine"]}|{data["cumulative_cases"]}|{data["cumulative_cvaccine"]}|{data["cumulative_deaths"]}|{data["cumulative_dvaccine"]}|{data["cumulative_recovered"]}|{data["cumulative_testing"]}|{data["cvaccine"]}|{data["date"]}|{data["deaths"]}|{data["dvaccine"]}|{data["province"]}|{data["recovered"]}|{data["testing"]}|{data["testing_info"]}')
    
    task_instance.xcom_push(key='process_data', value = process_data)

def _write_to_kafka(task_instance):
    topic='test'
    list_data = task_instance.xcom_pull(key='process_data', task_ids='process_data')
    p = Producer({'bootstrap.servers': 'localhost:9092'})
    for index, data in enumerate(list_data): 
        print(index, data)
        p.produce(topic, key=f'{topic}{index}', value=data)
        p.flush(10)

with DAG(
        dag_id="covid19_data_pipeline",
        default_args = default_args, 
        description="Extract Canada Covid19 Stats Data",
        schedule_interval=None, 
        )as dag:
    
    is_api_available = HttpSensor(
            task_id='is_api_available',
            http_conn_id='chris_http',
            method='GET', 
            endpoint='summary',
            response_check=lambda response: "summary" in response.text,
            poke_interval=5,
            timeout=10, 
            )
    
    extract_data = SimpleHttpOperator(
            task_id='extract_data',
            http_conn_id='chris_http',
            endpoint='summary',
            method='GET',
            data=None, 
            response_filter = lambda response: json.loads(response.text),
            log_response=True
            )
    
    process_data = PythonOperator(
            task_id='process_data', 
            python_callable = _process_data
            )
   
    write_to_kafka = PythonOperator(
            task_id='write_to_kafka', 
            python_callable=_write_to_kafka
            )

    is_api_available >> extract_data >> process_data >> write_to_kafka












