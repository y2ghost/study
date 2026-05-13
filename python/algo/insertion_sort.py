# 时间复杂度: 最好O(N), 最坏O(N^2)
def insertionSort(elements):
    for i in range(1, len(elements)):
        j = i - 1
        elementNext = elements[i]
        while (elements[j] > elementNext) and (j >= 0):
            elements[j+1] = elements[j]
            j = j - 1
        elements[j+1] = elementNext
    return elements

if '__main__' == __name__:
    scores = [25, 26, 22, 24, 27, 23, 21]
    insertionSort(scores)
    print(scores)
