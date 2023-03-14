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
from trade_route import * 
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
PERSISTENT_MESSAGE = None
NUM_PLAYERS = 4  # For now
CURR_PLAYER = 0
action_manager = None
has_initialized = False
cascade = False
TRADING_POST_ENABLED = True 
CITIES_ENABLED = False

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
                self.display()

        def decrementEvent():
            if self.amount > 0:
                self.amount -= 1
                self.display()
        X_SHIFT = 40
        Y_SHIFT = 60
        BUTTON_WIDTH = 90
        BUTTON_HEIGHT = 55
        green_rect = pygame.Rect(x_pos-X_SHIFT,y_pos+Y_SHIFT,BUTTON_WIDTH,BUTTON_HEIGHT)
        red_rect = pygame.Rect(x_pos+X_SHIFT,y_pos+Y_SHIFT,BUTTON_WIDTH,BUTTON_HEIGHT)
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
    
    initialize_players(board_json)
    if TRADING_POST_ENABLED:
        initialize_trade_routes(board_json)
    if CITIES_ENABLED:
        pass
        #initialize_cities(board_json)
    else:
        initialize_nobles(board_json)
    initialize_sidebar()
    print(board_json)

# Trade routes visual will be able to be accessed via a button
# Not yet implemented
def initialize_trade_routes(board_json):
    #trade_routes = board_json['tradeRoutes']
    TradeRoute.instance()


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
    ids = [noble['cardId'] for noble in board_json['nobleDeck']['nobles']  if noble is not None]
    Noble.initialize(ids)


def show_flash_message():
    global FLASH_TIMER, FLASH_MESSAGE, FLASH_START
    time_diff = (pygame.time.get_ticks() - FLASH_START) / 1000
    if FLASH_MESSAGE is None or time_diff > FLASH_TIMER:
        return
    flash_message(DISPLAYSURF, FLASH_MESSAGE,
                  color=FLASH_COLOR, opacity=255 * (1 - time_diff / FLASH_TIMER))

def show_persistent_message(color=GREEN):
    if PERSISTENT_MESSAGE is None:
        return flash_right_side(DISPLAYSURF, "", color=GREEN, opacity=0)

    flash_right_side(DISPLAYSURF, PERSISTENT_MESSAGE, color=color, opacity=255)


def set_flash_message(text, color=GREEN, timer=5):
    global FLASH_MESSAGE, FLASH_TIMER, FLASH_START, FLASH_COLOR
    FLASH_MESSAGE, FLASH_TIMER, FLASH_START = text, timer, pygame.time.get_ticks()
    FLASH_COLOR = color


def update(authenticator, game_id):
    global has_initialized
    board_json = server_manager.get_board(authenticator=authenticator, game_id=game_id)
    if not has_initialized:
        has_initialized = True
        initialize_game(board_json)
    global action_manager
    action_manager.update(Player.instance(id=CURR_PLAYER).name)
    # TODO: add cascading buy for cards]
    # if we need to cascade, we don't chance players
    check_cascade()
    update_turn_player(board_json)
    update_players(board_json)
    update_decks(board_json)
    update_tokens(board_json)
    
    if TRADING_POST_ENABLED:
        TradeRoute.instance().update(board_json)
    if CITIES_ENABLED:
        pass
        #update_cities(board_json)
    else:
        update_nobles(board_json)

def check_cascade():
    """Checks if we need to cascade a card purchase.
      If so, let the next card bought be bought for free"""
    global action_manager, cascade, PERSISTENT_MESSAGE

    if action_manager.has_unlocked_cascade(Player.instance(id=CURR_PLAYER).name):
      cascade = True
      PERSISTENT_MESSAGE = "You Unlocked a Cascade! Choose a card to buy for free!"
    else:
        cascade = False
        PERSISTENT_MESSAGE = None

def update_nobles(board_json):
    ids = [noble['cardId'] for noble in board_json['nobleDeck']['nobles'] if noble is not None]
    Noble.update_all(ids)

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


def display_everything(current_user):
    # reset the display and re-display everything
    DISPLAYSURF.fill((0, 0, 0))
    display_sidebar()
    display_players(current_user)
    display_board()
    display_decks()
    display_tokens()
    if CITIES_ENABLED:
        pass
        #display_cities()
    else:
        display_nobles()
    if TRADING_POST_ENABLED:
        display_trade_routes()

    show_flash_message()  # last so it's on top
    show_persistent_message()
    pygame.display.update()

def display_trade_routes():
    TradeRoute.instance().display(DISPLAYSURF)

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


def display_players(logged_in_player_username):
    for i in range(NUM_PLAYERS):
        highlight = i == CURR_PLAYER
        Player.instance(id=i).display(DISPLAYSURF, NUM_PLAYERS, logged_in_player_username, highlighted=highlight)


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
    if server_action_id == 0:
        return
    if server_action_id <= -1:
        set_flash_message('Invalid action', color=RED)
        return

    action_manager.perform_action(server_action_id)
    if action == Action.RESERVE:
        set_flash_message('Reserved a card')
    else:
        set_flash_message('Bought a card')
    action_manager.force_update(Player.instance(id=CURR_PLAYER).name)

def get_user_cascade_selection(card :Card):
    """
    Allow user to choose whether to get a card for free or not
    :param card:
    :return:
    """
    dim_screen(DISPLAYSURF)
    action = card.get_user_cascade_selection(DISPLAYSURF)
    print(action.value)
    global FLASH_MESSAGE, FLASH_TIMER, CURR_PLAYER, action_manager, cascade
    server_action_id = action_manager.get_card_action_id(card, Player.instance(id=CURR_PLAYER).name,
                                                         action)
    if server_action_id == 0:
        return
    if server_action_id <= -1:
        set_flash_message('Invalid action', color=RED)
        return
        
    action_manager.perform_action(server_action_id)
    set_flash_message('You got a free card!')
    global PERMANENT_MESSAGE
    PERMANENT_MESSAGE = None
    action_manager.force_update(Player.instance(id=CURR_PLAYER).name)


def perform_action(obj, user):
    if obj is None:
        return
    global CURR_PLAYER
    global action_manager
    # make sure it's the current user's turn, otherwise cannot take cards
    if user == Player.instance(id=CURR_PLAYER).name:
        action_manager.update(Player.instance(id=CURR_PLAYER).name)
        if isinstance(obj, Card):
            global cascade
            if cascade:
                get_user_cascade_selection(obj)
            else:
                get_user_card_selection(obj)
        elif isinstance(obj, Token):
            # opens token selection menu
            get_token_selection()
            
        elif isinstance(obj, Noble):
            pass
        #elif isinstance(obj, City):
            #pass
        # players shouldn't click on nobles
            # obj.take_noble(Sidebar.instance(), Player.instance(id=CURR_PLAYER))
            # set_flash_message('Took a noble')
        
        elif isinstance(obj, Player):
            Sidebar.instance().switch_player(obj)

    # When it's not the user's turn, still allow switching between sidebars
    elif isinstance(obj, Player):
        Sidebar.instance().switch_player(obj)


class TokenMenu:
    """generates all the buttons, remembers which tokens user picked, checks if legal"""
    def __init__(self):
        selection_box, selection_box_rect = get_selection_box(DISPLAYSURF)
        self.selection_box = selection_box
        self.selection_box_rect = selection_box_rect

        self.menu = pygame.Surface((WIDTH, HEIGHT))
        self.menu.fill((0, 0, 0))
        self.menu.set_alpha(200)
        self.menu_rect = self.menu.get_rect()
        self.menu_rect.center = (WIDTH / 2, HEIGHT / 2)

        self.token_selection_list :List[IndividualTokenSelection] = [] 
        # button for confirming token selection
        self.confirm_take_button = Button(pygame.Rect(WIDTH/2-100,HEIGHT*7/10,90,55), self.confirm_take_token, text="Take Token")
        self.confirm_return_button = Button(pygame.Rect(WIDTH/2+100,HEIGHT*7/10,90,55), self.confirm_return_token, text="Return Token")

        
    def generate_selection_and_buttons(self) -> Tuple[List[IndividualTokenSelection],List[Button]]:
        # generate a list of buttons for the token menu   
        self.token_selection_list = []     
        button_list = []
        for index,token in enumerate(Token.get_all_token_colors()):
            tokenSelection = IndividualTokenSelection(token,WIDTH/10+index*200,HEIGHT/2)
            tokenSelection.display()
            self.token_selection_list.append(tokenSelection)
            button_list.append(tokenSelection.incrementButton)
            button_list.append(tokenSelection.decrementButton)
        return self.token_selection_list,button_list

    def display(self):
        self.selection_box.blit(self.menu, self.menu_rect)
        
    
    def confirm_take_token(self) -> None:
        """ checks if the input corresponds to a valid token selection
        returns the token selection if valid, None if not valid """
        global FLASH_MESSAGE, FLASH_TIMER, CURR_PLAYER, action_manager

        valid_selection = True

        
        # Logic to validate tokens on client
        total_tokens = 0
        same_color_chosen = False
        user_selection: Dict[Token,int] = {}
        for token_selection in self.token_selection_list:
            
            current_token = token_selection.token
            current_count = token_selection.amount

            print(current_token)
            print(current_count)

            user_selection[current_token] = current_count
            if current_count == 2:
                same_color_chosen = True
            if current_count > 2:
                valid_selection = False
            total_tokens += current_count
        
        # Can only take 2 tokens total if taken from the same color
        if total_tokens > 2 and same_color_chosen:
            valid_selection = False
        
        # Can take up to 3 colors
        if total_tokens > 3:
            valid_selection = False

        # Return to the flow if invalid
        if not valid_selection:
            set_flash_message('Invalid selection', color=RED)
            return

        # Find the action id and perform it if it's valid
        take_token_action_id: int = action_manager.get_token_action_id(user_selection,Player.instance(id=CURR_PLAYER).name,Action.TAKE_TOKENS)

        # Return to the flow if invalid
        if take_token_action_id < 0:
            set_flash_message('Illegal selection', color=RED)
            return
        
        action_manager.perform_action(take_token_action_id)
        action_manager.force_update(Player.instance(id=CURR_PLAYER).name)
        return
    
    def confirm_return_token(self) -> None:
        """ checks if the input corresponds to a valid token selection for RETURNING
        returns the token selection if valid, None if not valid """
        global FLASH_MESSAGE, FLASH_TIMER, CURR_PLAYER, action_manager
        
        # Logic to validate tokens on client
        user_selection: Dict[Token,int] = {}
        for token_selection in self.token_selection_list:
            
            current_token = token_selection.token
            current_count = token_selection.amount

            user_selection[current_token] = current_count

        # Find the action id and perform it if it's valid
        take_token_action_id: int = action_manager.get_token_action_id(user_selection,Player.instance(id=CURR_PLAYER).name,Action.RETURN_TOKENS)

        # Return to the flow if invalid
        if take_token_action_id < 0:
            set_flash_message('Illegal selection', color=RED)
            return
        
        action_manager.perform_action(take_token_action_id)

        return

    def get_user_token_selection(self) -> Action:
            DISPLAYSURF.blit(self.selection_box, self.selection_box_rect)
            components_generated: Tuple[List[IndividualTokenSelection],List[Button]] = self.generate_selection_and_buttons()
            individual_token_list: List[IndividualTokenSelection] = components_generated[0]
            button_list: List[Button] = components_generated[1]
            self.display()

            self.confirm_take_button.display(DISPLAYSURF)
            self.confirm_return_button.display(DISPLAYSURF)

            write_on(DISPLAYSURF,self.confirm_take_button.text,center=self.confirm_take_button.rectangle.center)
            write_on(DISPLAYSURF,self.confirm_return_button.text,center=self.confirm_return_button.rectangle.center)
            pygame.display.update()
            for token_selection in button_list:
                pygame.draw.rect(self.selection_box,token_selection.color,token_selection.rectangle)
                #self.selection_box.blit(button.rectangle)
                #button.display()
            
            # Check the list of actions when we click the tokens
            action_manager.get_actions_json()

            while True:
                for event in pygame.event.get():
                    if event.type == pygame.KEYDOWN:
                        if event.key == pygame.K_ESCAPE:
                            return Action.CANCEL
                    elif event.type == pygame.QUIT:
                        pygame.quit()
                        quit()

                    elif event.type == MOUSEBUTTONDOWN:
                        clicked_position = pygame.mouse.get_pos()
                        # button is individual token selection class
                        if self.confirm_take_button.rectangle.collidepoint(clicked_position):
                            print("confirm take")
                            return self.confirm_take_button.activation()
                        if self.confirm_return_button.rectangle.collidepoint(clicked_position):
                            print("confirm return")
                            return self.confirm_return_button.activation()
                        for token_selection in individual_token_list:
                            if token_selection.incrementButton.rectangle.collidepoint(clicked_position):
                                print("increment")
                                token_selection.incrementButton.activation()
                            elif token_selection.decrementButton.rectangle.collidepoint(clicked_position):
                                print("decrement")
                                token_selection.decrementButton.activation()
                        pygame.display.update()

def get_token_selection():
    """RETURNS WHAT TOKENS PLAYER CHOSE"""

    # draw the 7 buttons 
    TokenMenu().get_user_token_selection()
    pygame.display.update()
    # wait for user to click on a button

def check_toggle(mouse_pos):
    sidebar = Sidebar.instance()
    page_num = sidebar.is_clicked_toggle(mouse_pos)
    sidebar.toggle(page_num)

def play(authenticator, game_id):
    last_update = pygame.time.get_ticks()  # force update on first loop
    global action_manager
    action_manager = ActionManager(authenticator=authenticator, game_id=game_id)
    update(authenticator, game_id)
    logged_in_user = authenticator.username
    while True:
        # update every 5 seconds on a separate thread
        if pygame.time.get_ticks() - last_update > 2000:
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
                    if TRADING_POST_ENABLED:
                        TradeRoute.instance().check_click(position,DISPLAYSURF)
                    obj = get_clicked_object(position)
                    perform_action(obj, logged_in_user)
                    with threading.Lock():
                        threading.Thread(target=update, args=(authenticator, game_id)).start()

        display_everything(logged_in_user)
        pygame.display.update()
        FPSCLOCK.tick(FPS)
