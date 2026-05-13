# 时间复杂度: O(N * log2(N))
def mergeSort(elements):
    size = len(elements)
    if size <= 1:
        return elements
    # 分治
    mid = size // 2
    left = elements[:mid]
    right = elements[mid:]
    mergeSort(left)
    mergeSort(right)
    # 合并
    a = 0
    b = 0
    c = 0
    while a < len(left) and b < len(right):
        if left[a] < right[b]:
            elements[c] = left[a]
            a = a + 1
        else:
            elements[c] = right[b]
            b = b + 1
        c = c + 1
    while a < len(left):
        elements[c] = left[a]
        a = a + 1
        c = c + 1
    while b < len(right):
        elements[c] = right[b]
        b = b + 1
        c = c + 1
    return elements

if '__main__' == __name__:
    scores = [44, 16, 83, 7, 67, 21, 34, 45, 10]
    mergeSort(scores)
    print(scores)

