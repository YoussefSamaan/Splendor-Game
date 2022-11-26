from enum import Enum


class Action(Enum):
    RESERVE = 'RESERVE'
    BUY = 'BUY'
    CANCEL = 'CANCEL'

    def __eq__(self, other):
        return self.value == other.value
