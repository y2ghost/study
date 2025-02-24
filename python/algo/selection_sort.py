# 时间复杂度: O(N^2)
def selectionSort(elements):
    for slot in range(len(elements)-1, 0, -1):
        maxIndex = 0
        for location in range(1, slot + 1):
            if elements[location] > elements[maxIndex]:
                maxIndex = location
        elements[slot], elements[maxIndex] = elements[maxIndex], elements[slot]

if '__main__' == __name__:
    scores = [70, 15, 25, 19, 34, 44]
    selectionSort(scores)
    print(scores)

