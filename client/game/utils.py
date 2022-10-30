import pygame

GREEN = (0, 255, 0)
RED = (255, 0, 0)
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
LIGHT_BLUE = (0, 255, 255)
LIGHT_GREY = (200, 200, 200)
# BACKGROUND_COLOR = (158, 58, 64)
BACKGROUND_COLOR = (113, 155, 158)
def dim_screen(screen, color=(0, 0, 0), alpha=128):
    """
    Dim the screen with a color and alpha value
    """
    dim = pygame.Surface(screen.get_size())
    dim.fill(color)
    dim.set_alpha(alpha)
    screen.blit(dim, (0, 0))


def get_selection_box(screen: pygame.Surface, width=0.5, height=0.5, color=BACKGROUND_COLOR):
    """
    Creates a selection box on the screen. Centered by default.
    :param screen: used for relative positioning
    :param width: the width of the box relative to the screen size
    :param height: the height of the box relative to the screen size
    :param color: the color of the box
    :return: The surface of the selection box, and the rect of the selection box
    """
    x = screen.get_width() * (1 - width) / 2
    y = screen.get_height() * (1 - height) / 2
    width = screen.get_width() * width
    height = screen.get_height() * height
    box = pygame.Surface((width, height))
    box.fill(color)
    rect = pygame.Rect(x, y, width, height)
    return box, rect


def button(text, width, height, color):
    """
    Create a green button with the given text.
    :param text: the text to display on the button
    :param width: the width of the button
    :param height: the height of the button
    :param color: the color of the button
    :return: the button
    """
    # create a transparent surface
    button = pygame.Surface((width, height), pygame.SRCALPHA)
    pygame.draw.rect(button, color, (0, 0, width, height), border_radius=10)
    write_on(button, text)
    return button


def flash_message(screen, text, color=GREEN, opacity=255):
    """
    Display a message
    :param color: the color of the box
    :param screen: the screen to display the message on
    :param text: the text to display
    :param: color: the color of the text
    :param opacity: the opacity of the box
    """
    box = pygame.Surface((screen.get_width() / 4, screen.get_height() / 10))
    box.set_alpha(opacity)
    box.fill(color)
    write_on(box, text)
    screen.blit(box, (screen.get_width() / 2 - box.get_width() / 2, 0))


def write_on(surface, text, color=BLACK, font='Arial', font_size=20):
    """
    Write text to a surface
    :param text: the text to write
    :param surface: the rect to write to
    :param color: the color of the text
    :param font: the font of the text
    :param font_size: the size of the font
    """
    font = pygame.font.SysFont(font, font_size)
    text = font.render(text, True, color)
    text_rect = text.get_rect()
    text_rect.center = (surface.get_width() / 2, surface.get_height() / 2)
    surface.blit(text, text_rect)
