# 时间复杂度: O(N^2)
def bubbleSort(elements):
    lastElementIndex = len(elements) - 1
    for passNo in range(lastElementIndex,0,-1):
        for idx in range(passNo):
            if elements[idx] > elements[idx+1]:
                elements[idx], elements[idx+1] = elements[idx+1], elements[idx]
    return elements

if '__main__' == __name__:
    scores = [25, 21, 22, 24, 23, 27, 26]
    bubbleSort(scores);
    print(scores)

