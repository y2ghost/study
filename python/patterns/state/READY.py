from state import Validity, State


class READY(State):
    def __init__(self, machine):
        super().__init__('READY', machine)

    def insert_credit_card(self):
        if not self.card_inserted:
            print("Validating your credit card.")

            self.card_inserted = True
            self.card_validity = Validity.UNKNOWN
            return self.states.VALIDATING

        elif self.card_validity == Validity.NO:
            print("*** Credit card rejected. ***")
            print("Remove your card.")
            return self

        else:
            print("Remove the credit card that's "
                  "already inserted.")
            return self

    def check_validity(self):
        if not self.card_inserted:
            print("First insert your credit card.")

        elif self.card_validity == Validity.YES:
            print("Remove the credit card that's "
                  "already inserted.")
        else:
            print("*** Credit card rejected. ***")
            print("Remove your card.")

        return self

    def take_ticket(self):
        if not self.card_inserted:
            print("First insert your credit card.")

        elif self.card_validity == Validity.UNKNOWN:
            print("Still checking your "
                  "credit card's validity.")

        elif self.card_validity == Validity.YES:
            print("Take the ticket that you've "
                  "already bought.")
            print("Remove your card.")

        else:
            print("*** Credit card rejected. ***")
            print("Remove your card.")

        return self

    def remove_credit_card(self):
        if self.card_inserted:
            print("You've removed your credit card.")

        else:
            print("No credit card inserted.")

        self.card_inserted = False
        self.card_validity = Validity.UNKNOWN
        return self

