import requests

# some_file.py
import sys

# caution: path[0] is reserved for script path (or '' in REPL)
sys.path.insert(1, 'client\config')
from config.config import LOBBY_SERVICE_URL


def create_session(username, access_token, savegameid):
    url = f"{LOBBY_SERVICE_URL}/api/sessions?access_token={access_token}"
    data = {
        "creator": username,
        "game": "splendor",
        "savegame": "",
    }
    header = {
        "Content-Type": "application/json"
    }
    response = requests.post(url, json=data, headers=header)
    print(response.content.decode('utf-8'))
    return response


def launch_session(access_token, session):
    url = f"{LOBBY_SERVICE_URL}/api/sessions/{session}?access_token={access_token}"
    response = requests.post(url)
    print(response)
    if response.status_code == 200:
        print("SUCCESSS!!!!!!!")
        return session
    print("FAILLL")
    return session

# test_access_token = "h9UONiYVQ43aYWAGFBEv2tsbqiY="
# create_session("maex", test_access_token, "")
# launch_session("nble56mnjuhvdtfjNQYijVCuYNk=", 636302944309790167)