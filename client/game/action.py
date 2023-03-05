from enum import Enum


class Action(Enum):
    RESERVE = 'RESERVE'
    BUY = 'BUY'
    CANCEL = 'CANCEL'
    TAKE_TOKENS = 'TAKE_TOKENS'
    RETURN_TOKENS = 'RETURN_TOKENS'

    def __eq__(self, other):
        return self.value == other.value
