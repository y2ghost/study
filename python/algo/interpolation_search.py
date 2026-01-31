from bubble_sort import bubbleSort

# 时间复杂度: 最佳O(log(log N))，最差O(N)
def interpolationSearch(elements, item):
    start = 0
    end = len(elements) - 1
    found = False
    while start <= end and item >= elements[start] and item <= elements[end]:
        mid = start + int(((float(end - start)/(elements[end]-elements[start]))\
            * (item-elements[start])))
        if elements[mid] == item:
            return True
        if elements[mid] < item:
            start = mid + 1
    return found

if '__main__' == __name__:
    scores = [12, 33, 11, 99, 22, 55, 90]
    sortedScores = bubbleSort(scores)
    print(interpolationSearch(scores, 12))
    print(interpolationSearch(scores, 91))

