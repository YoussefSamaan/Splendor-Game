import requests

# some_file.py
import sys

# caution: path[0] is reserved for script path (or '' in REPL)
sys.path.insert(1, 'client\config')
from config import *
LOBBY_SERVICE_URL = config.get_url()
def get_all_sessions():
    url = f"{LOBBY_SERVICE_URL}/api/sessions"    
    response = requests.get(url)
<<<<<<< HEAD
    print(response.json())
=======
    # print(response.json())
>>>>>>> a4325d368be37a69e954cdec1b00ceba2d9ecf03
    return response

def get_all_sessions_long_polling(hash_code):
    url = f"{LOBBY_SERVICE_URL}/api/sessions?hash={hash_code}"  
    response = requests.get(url)
    # print(response.json())
    return response


def get_session(session):
    url = f'{LOBBY_SERVICE_URL}/api/sessions/{session}'
    response = requests.get(url)
    # print(response.json())
    return response

# get_all_sessions()
# get_session(4989443599829874511)

