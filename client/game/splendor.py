import os
import sys
import threading

from pygame.locals import *
from win32api import GetSystemMetrics
from typing import List, Callable, Tuple
from action import Action
from token_action import TokenAction
from game import server_manager
from game.action_manager import ActionManager
from deck import *
from sidebar import *
from splendorToken import Token
from color import Color

os.chdir(os.path.dirname(
    os.path.abspath(__file__)))  # to make image imports start from current directory
WIDTH, HEIGHT = GetSystemMetrics(0), GetSystemMetrics(1)
FPS = 60
FPSCLOCK = pygame.time.Clock()
pygame.init()
DISPLAYSURF = pygame.display.set_mode((WIDTH, HEIGHT), pygame.FULLSCREEN)
pygame.display.set_caption('Splendor')
fullScreen = True
DECKS = [BlueDeck, RedDeck3, YellowDeck, RedDeck2, GreenDeck, RedDeck1]
FLASH_MESSAGE = None
FLASH_TIMER = 0
FLASH_START = 0
FLASH_COLOR = GREEN
NUM_PLAYERS = 4  # For now
CURR_PLAYER = 0
action_manager = None
has_initialized = False

class IndividualTokenSelection:
    def __init__(self, token: Token, x_pos: int, y_pos: int) -> None:
        self.x_pos = x_pos
        self.y_pos = y_pos
        self.token = token

        self.amount = 0 # how many tokens are selected
        # TODO: make these buttons automatically based on position of button

        def incrementEvent():
            if self.amount < 3:
                self.amount += 1

        def decrementEvent():
            if self.amount > 0:
                self.amount -= 1
        X_SHIFT = 60
        Y_SHIFT = 0
        BUTTON_WIDTH = 90
        BUTTON_HEIGHT = 55
        green_rect = pygame.Rect(x_pos-X_SHIFT,y_pos,BUTTON_WIDTH,BUTTON_HEIGHT)
        red_rect = pygame.Rect(x_pos+X_SHIFT,y_pos,BUTTON_WIDTH,BUTTON_HEIGHT)
        self.incrementButton = Button(green_rect,incrementEvent,GREEN)
        self.decrementButton = Button(red_rect,decrementEvent,RED)
    
    def display(self):
        self.token.draw(DISPLAYSURF,self.x_pos,self.y_pos,amount=self.amount)
        self.incrementButton.display(DISPLAYSURF)
        self.decrementButton.display(DISPLAYSURF)


def initialize_game(board_json):
    pygame.display.set_mode((WIDTH, HEIGHT), pygame.FULLSCREEN)
    initialize_board()
    initialize_cards()
    initialize_tokens()
    initialize_nobles(board_json)
    initialize_players(board_json)
    initialize_sidebar()


def initialize_players(board_json):
    global NUM_PLAYERS
    players = board_json['players']
    NUM_PLAYERS = len(players)
    for i in range(0, NUM_PLAYERS):
        player = Player.instance(id=i, name=players[i]['name'])


def initialize_board():
    Board.instance(WIDTH, HEIGHT)


def initialize_sidebar():
    Sidebar.instance(WIDTH, HEIGHT)


def initialize_cards():
    BlueDeck.instance()
    RedDeck3.instance()
    YellowDeck.instance()
    RedDeck2.instance()
    GreenDeck.instance()
    RedDeck1.instance()


def initialize_tokens():
    Token.initialize()


def initialize_nobles(board_json):
    Noble.initialize(board_json['nobles'])


def show_flash_message():
    global FLASH_TIMER, FLASH_MESSAGE, FLASH_START
    time_diff = (pygame.time.get_ticks() - FLASH_START) / 1000
    if FLASH_MESSAGE is None or time_diff > FLASH_TIMER:
        return
    flash_message(DISPLAYSURF, FLASH_MESSAGE,
                  color=FLASH_COLOR, opacity=255 * (1 - time_diff / FLASH_TIMER))


def set_flash_message(text, color=GREEN, timer=5):
    global FLASH_MESSAGE, FLASH_TIMER, FLASH_START, FLASH_COLOR
    FLASH_MESSAGE, FLASH_TIMER, FLASH_START = text, timer, pygame.time.get_ticks()
    FLASH_COLOR = color


def update(authenticator, game_id):
    global has_initialized
    board_json = server_manager.get_board(authenticator=authenticator, game_id=game_id)
    print(board_json)
    if not has_initialized:
        has_initialized = True
        initialize_game(board_json)
    global action_manager
    action_manager.update(Player.instance(id=CURR_PLAYER).name)
    update_turn_player(board_json)
    update_players(board_json)
    update_decks(board_json)
    update_tokens(board_json)


def update_tokens(board_json):
    Token.update_all(board_json['bank']['tokens'])

def update_decks(board_json):
    GreenDeck.instance().update(board_json['decks'])
    BlueDeck.instance().update(board_json['decks'])
    RedDeck3.instance().update(board_json['decks'])
    YellowDeck.instance().update(board_json['decks'])
    RedDeck2.instance().update(board_json['decks'])
    RedDeck1.instance().update(board_json['decks'])


def update_turn_player(board_json):
    global CURR_PLAYER
    CURR_PLAYER = board_json['currentTurn']
    print("Current player is: " + str(Player.instance(id=CURR_PLAYER).name))


def update_players(board_json):
    global NUM_PLAYERS
    players = board_json['players']
    NUM_PLAYERS = len(players)
    for i in range(0, NUM_PLAYERS):
        player = Player.instance(id=i, name=players[i]['name'])
        player.update_player_inventory(players[i])


def display():
    # reset the display and re-display everything
    DISPLAYSURF.fill((0, 0, 0))
    display_sidebar()
    display_players()
    display_board()
    display_decks()
    display_tokens()
    display_nobles()

    show_flash_message()  # last so it's on top
    pygame.display.update()


def display_board():
    Board.instance().display(DISPLAYSURF)


def display_sidebar():
    # 0 = card, 1 = noble, 2 = reserve
    Sidebar.instance().display(DISPLAYSURF)


def display_decks():
    BlueDeck.instance().display(DISPLAYSURF)
    RedDeck3.instance().display(DISPLAYSURF)
    YellowDeck.instance().display(DISPLAYSURF)
    RedDeck2.instance().display(DISPLAYSURF)
    GreenDeck.instance().display(DISPLAYSURF)
    RedDeck1.instance().display(DISPLAYSURF)


def display_tokens():
    Token.display_all(DISPLAYSURF)


def display_nobles():
    Noble.display_all(DISPLAYSURF)


def display_players():
    for i in range(NUM_PLAYERS):
        highlight = i == CURR_PLAYER
        Player.instance(id=i).display(DISPLAYSURF, NUM_PLAYERS, highlight)


def get_clicked_object(pos):
    board = Board.instance()
    sidebar = Sidebar.instance()
    for i in range(NUM_PLAYERS):
        temp_player = Player.instance(id=i)
        if temp_player.is_clicked(pos, WIDTH, HEIGHT, NUM_PLAYERS):
            return temp_player
    if not board.is_clicked(pos):
        return None
    for deck in DECKS:
        card = deck.instance().get_clicked_card(pos)
        if card is not None:
            return card
    token = Token.get_clicked_token(pos)
    if token is not None:
        return token
    noble = Noble.get_clicked_noble(pos)
    if noble is not None:
        return noble
    return None


def get_user_card_selection(card :Card):
    """
    Allow user to choose whether to buy or reserve the card
    :param card:
    :return:
    """
    dim_screen(DISPLAYSURF)
    action = card.get_user_selection(DISPLAYSURF)
    global FLASH_MESSAGE, FLASH_TIMER, CURR_PLAYER, action_manager
    server_action_id = action_manager.get_card_action_id(card, Player.instance(id=CURR_PLAYER).name,
                                                         action)
    if server_action_id > -1:
        if action == Action.BUY:
            action_manager.perform_action(server_action_id)
            set_flash_message('Bought a card')
        elif action == Action.RESERVE:
            action_manager.perform_action(server_action_id)
            set_flash_message('Reserved a card')
        else:
            return
    else:
        set_flash_message('Invalid action', color=RED)


def perform_action(obj):
    if obj is None:
        return
    global CURR_PLAYER
    if isinstance(obj, Card):
        get_user_card_selection(obj)
    elif isinstance(obj, Token):
        # obj.take_token(Player.instance(id=CURR_PLAYER))
        # TODO: add token menu
        get_token_selection()
        set_flash_message('Opened token menu')
    elif isinstance(obj, Noble):
        obj.take_noble(Sidebar.instance(), Player.instance(id=CURR_PLAYER))
        set_flash_message('Took a noble')
    elif isinstance(obj, Player):
        Sidebar.instance().switch_player(obj)

class TokenMenu:
    """generates all the buttons, remembers which tokens user picked, checks if legal"""
    def __init__(self):
        self.menu = pygame.Surface((WIDTH, HEIGHT))
        self.menu.fill((0, 0, 0))
        self.menu.set_alpha(200)
        self.menu_rect = self.menu.get_rect()
        self.menu_rect.center = (WIDTH / 2, HEIGHT / 2)
        self.buttonlist :List[IndividualTokenSelection] = [] 
        self.confirm_button = Button(pygame.Rect(WIDTH/2,HEIGHT/2,90,55), "Confirm", self.check_enough_tokens)
        # todo: add +/- buttons for each token
        # add buy button
        
    def generate_buttons(self) -> List[Button]:
        # generate a list of buttons for the token menu        
        button_list = []
        for token in Token.flyweights.values():
            tokenSelection = IndividualTokenSelection(token,WIDTH/4,HEIGHT/2)
            tokenSelection.display()
            button_list.append(tokenSelection)
            #button_list.append(tokenSelection.incrementButton)
        return button_list

    def display(self):
        DISPLAYSURF.blit(self.menu, self.menu_rect)
    
    def check_enough_tokens(self) -> Dict[Token,int]:
        """ checks if the input corresponds to a valid token selection
        returns the token selection if valid, None if not valid """
        pass

    def get_user_selection(self):
        while True:
            buttons_generated: List[Button] = self.generate_buttons()
            self.display()

            for button in buttons_generated:
                button.display()
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    quit()

                elif event.type == MOUSEBUTTONDOWN:
                    clicked_position = pygame.mouse.get_pos()
                    # button is individual token selection class
                    if self.confirm_button.collidepoint(clicked_position):
                        return self.confirm_button.activation()
                    for button in self.buttonlist:
                        if button.incrementButton.rectangle.collidepoint(clicked_position):
                            button.incrementButton.activation()
                        elif button.decrementButton.rectangle.collidepoint(clicked_position):
                            button.decrementButton.activation()
            return None

def get_token_selection():
    """RETURNS WHAT TOKENS PLAYER CHOSE"""

    # draw the 7 buttons 
    TokenMenu().get_user_selection()
    pygame.display.update()
    # wait for user to click on a button

def check_toggle(mouse_pos):
    sidebar = Sidebar.instance()
    page_num = sidebar.is_clicked_toggle(mouse_pos)
    sidebar.toggle(page_num)


def check_toggle(mouse_pos):
    sidebar = Sidebar.instance()
    page_num = sidebar.is_clicked_toggle(mouse_pos)
    sidebar.toggle(page_num)


def play(authenticator, game_id):
    last_update = pygame.time.get_ticks()  # force update on first loop
    global action_manager
    action_manager = ActionManager(authenticator=authenticator, game_id=game_id)
    update(authenticator, game_id)
    while True:
        # update every 5 seconds on a separate thread
        if pygame.time.get_ticks() - last_update > 5000:
            last_update = pygame.time.get_ticks()
            # start a new thread
            with threading.Lock():
                threading.Thread(target=update, args=(authenticator, game_id)).start()
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    pygame.quit()
                    sys.exit()
                if event.key == K_m:
                    # minimize the window
                    # FIXME: Is there a better way to do this?
                    pygame.display.set_mode((1, 1))
                if event.key == K_f:
                    pygame.display.set_mode((WIDTH, HEIGHT), pygame.FULLSCREEN)
                if event.key == K_UP:
                    Sidebar.instance().scroll_sidebar(50)
                if event.key == K_DOWN:
                    Sidebar.instance().scroll_sidebar(-50)
            elif event.type == MOUSEBUTTONDOWN:
                if event.button == 4:
                    Sidebar.instance().scroll_sidebar(50)
                elif event.button == 5:
                    Sidebar.instance().scroll_sidebar(-50)
                else:
                    # check if it's the sidebar toggle
                    position = pygame.mouse.get_pos()
                    check_toggle(position)
                    obj = get_clicked_object(position)
                    perform_action(obj)
                    with threading.Lock():
                        threading.Thread(target=update, args=(authenticator, game_id)).start()

        display()
        pygame.display.update()
        FPSCLOCK.tick(FPS)
