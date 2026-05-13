# 线性搜索，时间复杂度: O(N)
def linearSearch(elements, item):
    index = 0
    found = False
    while index < len(elements) and found is False:
        if elements[index] == item:
            found = True
        else:
            index  = index + 1
    return found

if '__main__' == __name__:
    scores = [12, 33, 11, 99, 22, 55, 90]
    print(linearSearch(scores, 12))
    print(linearSearch(scores, 91))

