# COMP 361 Project - Splendor

## Table of Contents

 * [Project Description](#project-description)
 * [Project Structure](#project-structure)
 * [Useful Links](#useful-links)
    * [Code Style and Tools](#code-style-and-tools)
    * [Requirements](#requirements)
 * [Authors](#authors)

## Project Description
Splendor is a board game for 2-4 players. More information about the game can be found [here]
(https://boardgamegeek.com/boardgame/148228/splendor). <br>
In addition, we added expansions to the game. The expansions are listed below:
* (Cities expansion)[https://www.ultraboardgames.com/splendor/cites-of-splendor.php]
* (Trader expansion)[https://boardgamegeek.com/thread/1889165/trade-routes-expansion-trigger-sell-goods]

## Project Structure

### Server

Contains the server side of the game. The server is a Spring Boot application that has multiple 
REST API endpoints that can be used to interact with the game.
See the [API documentation](docs/rest_interface_description.pdf).

### Client
The client is a pygame application that can be used to play the game. The client can be run in 
two ways:
* Running the main.py file in the client directory
* Running the docker container

More information can be found in the [setup section](##Setup).

### Lobby Service
This project is dependent on the [Lobby Service](https://github.com/m5c/BoardGamePlatform).
The Lobby Service manages the sessions and user accounts.


## Code Style and Tools

This project follows the best practices of the [Google's Checkstyle Configuration](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml).

## Setup

1. Clone the repository
2. Install [Docker](https://docs.docker.com/install/)
3. Run the following command in the root directory of the project:
    ```bash
    docker-compose up
    ```
4. TODO.


## Authors

Fill e.g. names + link to github profiles in list below.

 * [Youssef Samaan](https://github.com/YoussefSamaan2)
 * [Kevin Yu](https://github.com/iveykun)
 * [Wassim Wazzi](https://github.com/wassimwazzi)
 * [Rui Cong Su](https://github.com/a-lil-birb)
 * [Felicia Sun](https://github.com/Felicia-Sun)
 * [Jessie Xu](https://github.com/XiaoyuJessieXu1)
 * [Maximilian Schiedermeier](https://github.com/m5c)
 


