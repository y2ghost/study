from READY import READY
from VALIDATING import VALIDATING
from TICKET_SOLD import TICKET_SOLD
from SOLD_OUT import SOLD_OUT


class StatesBlock:
    _READY_state       = None
    _VALIDATING_state  = None
    _TICKET_SOLD_state = None
    _SOLD_OUT_state    = None
    
    @classmethod
    def initialize(cls, machine):
        cls._READY_state       = READY(machine)
        cls._VALIDATING_state  = VALIDATING(machine)
        cls._TICKET_SOLD_state = TICKET_SOLD(machine)
        cls._SOLD_OUT_state    = SOLD_OUT(machine)
        
        return cls._READY_state
        
    @classmethod
    @property
    def READY(cls):
        return cls._READY_state
        
    @classmethod
    @property
    def VALIDATING(cls):
        return cls._VALIDATING_state
        
    @classmethod
    @property
    def TICKET_SOLD(cls):
        return cls._TICKET_SOLD_state
        
    @classmethod
    @property
    def SOLD_OUT(cls):
        return cls._SOLD_OUT_state
