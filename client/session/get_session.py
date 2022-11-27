import requests

def get_session_details():
    url = 'http://localhost:4242/api/sessions'    
    response = requests.get(url)
    print(response.json())
    return response

def get_session_details(session):
    url = f'http://localhost:4242/api/sessions/{session}'
    response = requests.get(url)
    print(response.json())
    return response

get_session_details()
get_session_details(4989443599829874511)

