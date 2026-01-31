class Amount:
    def __init__(self):
        # 私有属性
        self._amount = None

    def get_amount(self):
        return self._amount

    def set_amount(self, value):
        if isinstance(value, int) or isinstance(value, float):
            self._amount = value
        else:
            print(f'值必须是int或float')


if __name__ == '__main__':
    amt = Amount()
    print(f'当前的amount值 {amt.get_amount()}')
    amt.set_amount('the')
    print(f'当前的amount值 {amt.get_amount()}')
    amt.set_amount(5.5)
    print(f'当前的amount值 {amt.get_amount()}')
