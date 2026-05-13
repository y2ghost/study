class Amount:

    def __init__(self):
        # 私有属性
        self._amount = None

    @property
    def amount(self):
        return self._amount

    @amount.setter
    def amount(self, value):
        if isinstance(value, int) or isinstance(value, float):
            self._amount = value
        else:
            print(f'值必须是int或float')


if __name__ == '__main__':
    amt = Amount()
    print(f'当前的amount值 {amt.amount}')
    amt.amount = 'the'
    print(f'当前的amount值 {amt.amount}')
    amt.amount = 5.5
    print(f'当前的amount值 {amt.amount}')
