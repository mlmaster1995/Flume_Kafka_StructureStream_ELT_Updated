#Copyright 2021 C.Young
#Licensed under the Apache License, Version 2.0 (the "License");
#you may not use this file except in compliance with the License.
#You may obtain a copy of the License at
#http://www.apache.org/licenses/LICENSE-2.0
#Unless required by applicable law or agreed to in writing, software
#distributed under the License is distributed on an "AS IS" BASIS,
#WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#See the License for the specific language governing permissions and
#limitations under the License.

#*********************************************************************#
# - start extract system data stream with Flume                       #
# - vmstate_flume_kafka.conf is in the git repo                       #
# - ".conf" file should be in the same path as this bash file         #
#*********************************************************************#

#! /usr/bin/bash

clear

flume-ng agent -n agt -f ./vmstate_flume_kafka.conf
