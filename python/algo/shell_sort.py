# 时间复杂度: O(N)
def shellSort(elements):
    length = len(elements)
    distance = length // 2
    while distance > 0:
        for i in range(distance, length):
            temp = elements[i]
            j = i
            while j >= distance and elements[j - distance] > temp:
                elements[j] = elements[j - distance]
                j = j - distance
            elements[j] = temp
        distance = distance // 2
    return elements

if '__main__' == __name__:
    scores = [26, 17, 20, 11, 23, 21, 13, 18, 24, 14, 12, 22, 16, 15, 19, 25]
    shellSort(scores)
    print(scores)

