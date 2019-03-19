#!/usr/bin/python

import requests
import sys
import json
import os

IP=sys.argv[1]
url = "http://"+IP+"/api/list"

payload = ""
headers = {}
response = requests.request("GET", url, data=payload, headers=headers)
#print(response.text)
data = json.loads(response.text)
if data['httpStatus'] > 299:
    print "Student API :: List FAILURE"
    os.system('exit 1')
else:
    print "Student API :: List SUCCESS"



url = "http://"+IP+"/api"
payload = "{\r\n\t  \"studentName\": \"Meghan Mahadev\",\r\n      \"studentAddr\": \"Hyderabad\",\r\n      \"studentAge\": \"2\",\r\n      \"studentQulaification\": \"Nursary\",\r\n      \"studentPercent\": \"99%\",\r\n      \"studentYearPassword\": \"2017\"\r\n    }"
headers = {
    'Content-Type': "application/json",
    }
response = requests.request("POST", url, data=payload, headers=headers)
data = json.loads(response.text)
ID = str(data['data']['object']['student_id'])
if data['httpStatus'] > 299:
    print "Student API :: CREATE FAILURE"
    os.system('exit 1')
else:
    print "Student API :: CREATE SUCCESS"

url = "http://"+IP+"/api/"+ID
payload = ""
headers = {}
response = requests.request("DELETE", url, data=payload, headers=headers)
data = json.loads(response.text)
if data['httpStatus'] > 299:
    print "Student API :: DELETE FAILURE"
    os.system('exit 1')
else:
    print "Student API :: DELETE SUCCESS"