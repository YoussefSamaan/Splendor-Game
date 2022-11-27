import requests
# from config import config

def create_session(username, access_token, savegameid):
    url = f'http://localhost:4242/api/sessions?access_token={access_token}' # config.lobby_service_url + '/api/sessions'
    data = {
        "creator": username,
        "game": "xox",
        "savegame": savegameid
    }
    parameters = {
        "access_token": access_token
    }
    header = {
        "Content-Type" : "application/json"
    }
    response = requests.post(url, json=data, headers=header)
    print(response.json())
    return response

def launch_session(access_token, session):
    url = f'http://localhost:4242/api/sessions/{session}?access_token={access_token}' # config.lobby_service_url + '/api/sessions'
    response = requests.post(url)
    print(response.json())
    return response

test_access_token = "h9UONiYVQ43aYWAGFBEv2tsbqiY="
create_session("maex", test_access_token, "")
#launch_session(test_access_token, 551890973492251833)