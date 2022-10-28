import os

from login import login
from game import *

os.chdir(os.path.dirname(os.path.abspath(__file__)))  # to make image imports start from current directory

if __name__ == '__main__':
    # login.login()
    splendor.play()