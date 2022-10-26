import pygame

GREEN = (0, 255, 0)
RED = (255, 0, 0)
WHITE = (255, 255, 255)


def dim_screen(screen, color=(0, 0, 0), alpha=128):
    """
    Dim the screen with a color and alpha value
    """
    dim = pygame.Surface(screen.get_size())
    dim.fill(color)
    dim.set_alpha(alpha)
    screen.blit(dim, (0, 0))


def draw_selection_box(screen: pygame.Surface, width=0.5, height=0.5, color=(255, 255, 255), thickness=2):
    """
    Draw a selection box on the screen. Centered by default.
    :param screen: the screen to draw on
    :param width: the width of the box relative to the screen size
    :param height: the height of the box relative to the screen size
    :param color: the color of the box
    :param thickness: the thickness of the box
    :return: The rect of the selection box
    """
    x = screen.get_width() * (1 - width) / 2
    y = screen.get_height() * (1 - height) / 2
    width = screen.get_width() * width
    height = screen.get_height() * height
    rect = pygame.Rect(x, y, width, height)
    box = pygame.Surface((width, height))
    box.fill(color)
    screen.blit(box, (x, y))

    return rect


def button(text, width, height, color):
    """
    Create a green button with the given text.
    :param text: the text to display on the button
    :param width: the width of the button
    :param height: the height of the button
    :param color: the color of the button
    :return: the button
    """
    button = pygame.Surface((width, height))
    button.fill(color)
    write_to_rect(text, button)
    pygame.draw.rect(button, color, (0, 0, width, height), 5, border_radius=50)
    return button


def flash_message(screen, text, color=GREEN):
    """
    Display a message in a box of color for 2 seconds.
    :param color: the color of the box
    :param screen: the screen to display the message on
    :param text: the text to display
    :param: color: the color of the text
    """
    box = pygame.Surface((screen.get_width() / 4, screen.get_height() / 10))
    box.fill(color)
    write_to_rect(text, box)
    screen.blit(box, (screen.get_width() / 2 - box.get_width() / 2, 0))


def write_to_rect(text, surface):
    """
    Write text to a surface
    :param text: the text to write
    :param surface: the rect to write to
    :return: the text surface
    """
    font = pygame.font.SysFont('Arial', 20)
    text = font.render(text, True, (0, 0, 0))
    text_rect = text.get_rect()
    text_rect.center = (surface.get_width() / 2, surface.get_height() / 2)
    surface.blit(text, text_rect)
    return text
