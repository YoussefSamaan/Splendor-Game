import requests

def delete_session(access_token, session):
    url = f'http://localhost:4242/api/sessions/{session}?access_token={access_token}' # config.lobby_service_url + '/api/sessions'
    
    response = requests.delete(url)
    print(response.json())
    return response


def remove_player(access_token, session, username):
    url = f'http://localhost:4242/api/sessions/{session}/players/{username}?access_token={access_token}'
    response = requests.put(url)
    print(response.json())
    return response


# session number can be either 3165409446827231019 or "3165409446827231019"
# delete_session("Zln4F4hiJH3eKxLy7zKJpuILLIs=", 3165409446827231019)
remove_player("h9UONiYVQ43aYWAGFBEv2tsbqiY=", 2388798427305018889, "Linus")