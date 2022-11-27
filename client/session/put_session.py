import requests

def add_player(access_token, session, username):
    url = f'http://localhost:4242/api/sessions/{session}/players/{username}?access_token={access_token}'
    response = requests.put(url)
    print(response.json())
    return response

add_player("h9UONiYVQ43aYWAGFBEv2tsbqiY=", 2388798427305018889, "Linus")
