import random
from state import Validity, State


class VALIDATING(State):
    def __init__(self, machine):
        super().__init__('VALIDATING', machine)

    def insert_credit_card(self):
        print("Still checking your "
              "credit card's validity.")

        return self

    def check_validity(self):
        if self.card_validity == Validity.UNKNOWN:
            self.card_validity = Validity.YES \
                if random.randint(0, 2) < 2 \
                else Validity.NO

        if self.card_validity == Validity.YES:
            print("Your credit card is validated. "
                  "Take your ticket.")

            return self.states.TICKET_SOLD;

        else:
            print("*** Credit card rejected. ***")
            print("Remove your card.")

            return self.states.READY;

    def take_ticket(self):
        if self.card_validity == Validity.UNKNOWN:
            print("Still checking your "
                  "credit card's validity.")

        elif self.card_validity == Validity.YES:
            print("Your card is already validated.")
            print("Take your ticket.")

        else:
            print("*** Credit card rejected. ***")
            print("Remove your card.")

        return self

    def remove_credit_card(self):
        if not self.card_inserted:
            print("No credit card inserted.")

        elif self.card_validity == Validity.NO:
            print("*** Credit card rejected. *** ")
            print("Remove your card.")

        else:
            print("You removed your credit card "
                  "before it was validated.")
            print("No sale.")

        self.card_inserted = False
        self.card_validity = Validity.UNKNOWN
        return self.states.READY

