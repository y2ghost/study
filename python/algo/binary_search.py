from bubble_sort import bubbleSort

# 时间复杂度: O(logN)
def binarySearch(elements, item):
    first = 0
    last = len(elements) - 1
    found = False
    while first <= last and not found:
        midpoint = (first + last) // 2
        if elements[midpoint] == item:
            found = True
        else:
            if item < elements[midpoint]:
                last = midpoint - 1
            else:
                first = midpoint + 1
    return found

if '__main__' == __name__:
    scores = [12, 33, 11, 99, 22, 55, 90]
    sortedScores = bubbleSort(scores)
    print(binarySearch(scores, 12))
    print(binarySearch(scores, 91))

