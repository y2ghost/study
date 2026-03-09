from state import Validity, State


class TICKET_SOLD(State):
    def __init__(self, machine):
        super().__init__('TICKET_SOLD', machine)

    def insert_credit_card(self):
        print("Take the ticket you've "
              "already bought.")

        self.card_inserted = True;
        self.card_validity = Validity.UNKNOWN;
        return self

    def check_validity(self):
        print("Take the ticket that you've "
              "already bought.")

        return self

    def take_ticket(self):
        print("Remove your credit card.")
        print("Enjoy the game!")
        self.count -= 1

        if self.count > 0:
            return self.states.READY
        else:
            return self.states.SOLD_OUT

    def remove_credit_card(self):
        print("First take your ticket.")

        return self

